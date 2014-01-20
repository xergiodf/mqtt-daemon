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

import java.text.DecimalFormat;

/**
 * @noimplement
 */
public interface DaemonConstant {

	public static final String PAYLOAD_FIELD_SEPARATOR = "#";

	public static final DecimalFormat RESULT_FORMATER = new DecimalFormat(
			"0.00");

}
