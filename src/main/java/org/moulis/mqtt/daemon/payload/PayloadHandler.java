/**
 * Copyright (C) 2014  Marius MOULIS <moulis.marius@gmail.com>
 * 
 * This file is part of Daemon.
 *
 *   Daemon is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Daemon is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Daemon.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.moulis.mqtt.daemon.payload;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.moulis.mqtt.daemon.config.DaemonConfig;
import org.moulis.mqtt.daemon.config.DaemonConstant;
import org.moulis.mqtt.daemon.exception.DaemonException;

public final class PayloadHandler extends Thread {

	private static final Logger LOG = Logger.getLogger(PayloadHandler.class
			.getName());

	private final String payload;

	public PayloadHandler(String payload) {
		this.payload = payload;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			String[] payloadContent = payload
					.split(DaemonConstant.PAYLOAD_FIELD_SEPARATOR);
			if (payloadContent != null && payloadContent.length == 4) {
				String answerTopic = DaemonConfig.CONFIG.getDaemonLookUpTopic()
						+ "/" + payloadContent[0];
				String result = calculateResult(payloadContent);
				sendAnswer(answerTopic, result);
			} else {
				throw new DaemonException("Malformed payload");
			}
		} catch (Exception e) {
			LOG.error("Unable to process payload \"" + payload
					+ "\". Reason : \n\n", e);
		}
	}

	private void sendAnswer(String answerTopic, String result)
			throws MqttException, MqttSecurityException,
			MqttPersistenceException {
		MqttClient answerClient = null;
		try {
			LOG.debug("Sending answer \"" + result + "\" into TOPIC=\""
					+ answerTopic + "\"");
			answerClient = new MqttClient(DaemonConfig.CONFIG.getBrokerUri(),
					MqttClient.generateClientId());
			answerClient.connect();
			answerClient.publish(answerTopic,
					new MqttMessage(result.getBytes(Charset.forName("UTF-8"))));
			LOG.debug("Answer sent!");
		} finally {
			if (answerClient != null && answerClient.isConnected()) {
				answerClient.disconnect();
			}
		}
	}

	private static String calculateResult(String[] payloadContent)
			throws DaemonException {
		double x = Double.valueOf(payloadContent[1]);
		double y = Double.valueOf(payloadContent[2]);
		double result = 0.0d;
		String operator = payloadContent[3];
		if ("+".equals(operator)) {
			result = x + y;
		} else if ("-".equals(operator)) {
			result = x - y;
		} else if ("/".equals(operator)) {
			if (y == 0) {
				if (x > 0) {
					result = Double.POSITIVE_INFINITY;
				} else if (x < 0) {
					result = Double.NEGATIVE_INFINITY;
				} else {
					result = Double.NaN;
				}
			} else {
				result = x / y;
			}
		} else if ("*".equals(operator)) {
			result = x * y;
		} else {
			throw new DaemonException(
					"Unrecognized operator, ignoring payload...");
		}
		return DaemonConstant.RESULT_FORMATER.format(result);
	}

	public String getPayload() {
		return payload;
	}

}
