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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.moulis.mqtt.daemon.util.DaemonUtil;

public final class DaemonConfigLoader {

	private static final Logger LOG = Logger.getLogger(DaemonConfigLoader.class
			.getName());

	public DaemonConfigLoader() {
		// RAF
	}

	public synchronized final void loadDaemonConfiguration(String[] programArgs) {
		if (programArgs == null || programArgs.length != 1
				|| programArgs[0] == null) {
			LOG.info("No configuration file given. Loading default settings...");
			setBrokerValue(DaemonDefaultConfigValues.BROKER_URI_DEFAULT_VALUE,
					DaemonConfig.CONFIG);
			setDaemonIdValue(DaemonDefaultConfigValues.DAEMON_ID_DEFAULT_VALUE,
					DaemonConfig.CONFIG);
			setDaemonLookUpTopicValue(
					DaemonDefaultConfigValues.DAEMON_LOOKUP_TOPIC_DEFAULT_VALUE,
					DaemonConfig.CONFIG);
		} else {
			String configurationFilePath = programArgs[0];
			LOG.info("Loading daemon configurations from file :\""
					+ configurationFilePath + "\"...");
			loadDaemonConfigurationFromPropertiesFile(configurationFilePath);
		}
	}

	private final void loadDaemonConfigurationFromPropertiesFile(
			String configurationFilePath) {

		Properties properties = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(configurationFilePath);
			properties.load(input);

			loadBrokerFromProperties(properties);
			loadDaemonIdFromProperties(properties);
			loadDaemonLookUpTopicFromProperties(properties);

		} catch (IOException e) {
			LOG.error("Unable to load configuration from file \""
					+ configurationFilePath + "\"", e);
			loadDaemonConfiguration(null);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.debug("Unable to close input stream \"" + input + "\"",
							e);
				}
			}
		}

	}

	private void loadBrokerFromProperties(Properties properties) {
		DaemonConfig config = DaemonConfig.CONFIG;
		String broker = properties
				.getProperty(DaemonPropertiesKeys.BROKER_URI_PROPERTY_KEY);
		if (DaemonUtil.isNullOrEmpty(broker)) {
			setBrokerValue(DaemonDefaultConfigValues.BROKER_URI_DEFAULT_VALUE,
					config);
		} else {
			setBrokerValue(broker, config);
		}
	}

	private void loadDaemonIdFromProperties(Properties properties) {
		DaemonConfig config = DaemonConfig.CONFIG;
		String daemonId = properties
				.getProperty(DaemonPropertiesKeys.DAEMON_ID_PROPERTY_KEY);
		if (DaemonUtil.isNullOrEmpty(daemonId)) {
			setDaemonIdValue(DaemonDefaultConfigValues.DAEMON_ID_DEFAULT_VALUE,
					config);
		} else {
			setDaemonIdValue(daemonId, config);
		}
	}

	private void loadDaemonLookUpTopicFromProperties(Properties properties) {
		DaemonConfig config = DaemonConfig.CONFIG;
		String daemonLookUpTopic = properties
				.getProperty(DaemonPropertiesKeys.DAEMON_LOOKUP_TOPIC_PROPERTY_KEY);
		if (DaemonUtil.isNullOrEmpty(daemonLookUpTopic)) {
			setDaemonLookUpTopicValue(
					DaemonDefaultConfigValues.DAEMON_LOOKUP_TOPIC_DEFAULT_VALUE,
					config);
		} else {
			setDaemonLookUpTopicValue(daemonLookUpTopic, config);
		}
	}

	private void setBrokerValue(String broker, DaemonConfig config) {
		config.setBrokerUri(broker);
		LOG.debug("Broker URI set to \"" + broker + "\"");
	}

	private void setDaemonIdValue(String daemonId, DaemonConfig config) {
		config.setDaemonId(daemonId);
		LOG.debug("Daemon ID set to \"" + daemonId + "\"");
	}

	private void setDaemonLookUpTopicValue(String daemonLookUpTopic,
			DaemonConfig config) {
		config.setDaemonLookUpTopic(daemonLookUpTopic);
		LOG.debug("Daemon LookUp ID set to \"" + daemonLookUpTopic + "\"");
	}

}
