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
package org.moulis.mqtt.daemon.config;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * @noimplement
 */
public interface DaemonDefaultConfigValues {

	public static final String BROKER_URI_DEFAULT_VALUE = "tcp://m2m.eclipse.org:1883";

	public static final String DAEMON_ID_DEFAULT_VALUE = MqttClient
			.generateClientId();

	public static final String DAEMON_LOOKUP_TOPIC_DEFAULT_VALUE = "org/moulis/calctools";

}
