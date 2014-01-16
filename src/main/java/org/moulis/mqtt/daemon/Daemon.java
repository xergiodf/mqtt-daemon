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
package org.moulis.mqtt.daemon;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.moulis.mqtt.daemon.client.DaemonCallback;
import org.moulis.mqtt.daemon.client.DaemonClient;
import org.moulis.mqtt.daemon.config.DaemonParams;

public final class Daemon {

	private static final Logger LOG = Logger.getLogger(Daemon.class.getName());

	public static void main(String[] args) {
		try {
			LOG.debug("Creating daemon client with BROKER=\""
					+ DaemonParams.BROKER_URI + "\" and ID=\""
					+ DaemonParams.DAEMON_ID + "\"");
			DaemonClient client = null;
			
			try {
				client = new DaemonClient();
				client.setCallback(new DaemonCallback());
				LOG.debug("Daemon created!");
				LOG.debug("Connecting to broker...");
				client.connect();
				LOG.debug("Connected!");
				LOG.debug("Subscribing to TOPIC=\""
						+ DaemonParams.DAEMON_SUB_TOPIC + "\"");
				client.subscribe(DaemonParams.DAEMON_SUB_TOPIC);
				LOG.debug("Subscribed!");
			} finally {
//				if (client != null && client.isConnected()) {
//					client.disconnect();
//				}
			}

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}
