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
package org.moulis.mqtt.daemon.client;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.moulis.mqtt.daemon.config.DaemonConfig;
import org.moulis.mqtt.daemon.payload.PayloadHandler;
import org.moulis.mqtt.daemon.util.DaemonUtil;

public final class DaemonCallback implements MqttCallback {

	private static final Logger LOG = Logger.getLogger(DaemonCallback.class
			.getName());

	/**
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
	 */
	public void connectionLost(Throwable cause) {
		LOG.debug("Connection to BROKER lost. Reason :\n ", cause);
	}

	/**
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String,
	 *      org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		if (message != null) {
			if (DaemonConfig.CONFIG.getDaemonLookUpTopic().equals(topic)) {
				byte[] payload = message.getPayload();
				if (payload != null && payload.length > 0) {
					String payloadContent = new String(payload, "UTF-8");
					if (!DaemonUtil.isNullOrEmpty(payloadContent)) {
						LOG.info("Handle payload \"" + payloadContent + "\"");
						PayloadHandler payloadHandler = new PayloadHandler(
								payloadContent);
						payloadHandler.start();
					} else {
						LOG.error("Received an empty payload... Ignored!");
					}
				} else {
					LOG.error("Received an empty payload... Ignored!");
				}
			} else {
				LOG.debug("\"" + topic + "\" is not a valid topic... Ignored!");
			}

		} else {
			LOG.error("Received an empty message... Ignored!");
		}
	}

	/**
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		// RAF
	}

}
