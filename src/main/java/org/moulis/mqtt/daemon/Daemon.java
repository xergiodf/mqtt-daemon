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
import org.moulis.mqtt.daemon.config.DaemonConfig;
import org.moulis.mqtt.daemon.config.DaemonConfigLoader;

public final class Daemon {

	private static final Logger LOG = Logger.getLogger(Daemon.class.getName());

	public static void main(String[] args) {
		try {
			// Load settings first :
			DaemonConfigLoader daemonConfigLoader = new DaemonConfigLoader();
			daemonConfigLoader.loadDaemonConfiguration(args);

			// Launch daemon :
			String daemonId = DaemonConfig.CONFIG.getDaemonId();
			String daemonLookupTopic = DaemonConfig.CONFIG
					.getDaemonLookUpTopic();
			String brokerUri = DaemonConfig.CONFIG.getBrokerUri();

			LOG.debug("Creating daemon client with BROKER=\"" + brokerUri
					+ "\" and ID=\"" + daemonId + "\"");
			DaemonClient client = new DaemonClient();
			client.setCallback(new DaemonCallback());
			LOG.debug("Daemon created!");
			LOG.debug("Connecting to broker...");
			client.connect();
			LOG.debug("Connected!");
			LOG.debug("Subscribing to TOPIC=\"" + daemonLookupTopic + "\"");
			client.subscribe(daemonLookupTopic);
			LOG.debug("Subscribed!");

		} catch (MqttException e) {
			LOG.error("Init Error", e);
		}
	}
}
