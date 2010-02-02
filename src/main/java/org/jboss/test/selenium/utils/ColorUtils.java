/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/

package org.jboss.test.selenium.utils;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

/**
 * Provides Color manipulations and functionality.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class ColorUtils {
	/**
	 * <p>
	 * Converts a string representation of color to integer.
	 * </p>
	 * 
	 * <p>
	 * Works with two formats:
	 * </p>
	 * 
	 * <ul>
	 * <li><code>#09FE4A</code> - <b>hexadecimal</b></li>
	 * <li><code>rgb(132, 5, 18)</code> - <b>decimal</b></li>
	 * </ul>
	 * 
	 * @param colorValue
	 *            string represented in one of two formats
	 * @return integer value of color derived from string representation
	 */
	public static int convertToInteger(String colorValue) {
		Validate.notNull(colorValue);

		int result = 0;

		if (colorValue.charAt(0) == '#') {
			result = Integer.parseInt(colorValue.substring(1), 16);
		} else {
			Matcher matcher = Pattern.compile("(\\d+)").matcher(colorValue);
			for (int i = 1; i <= 3; i++) {
				if (!matcher.find()) {
					throw new IllegalArgumentException(colorValue);
				}

				if (i != 1) {
					result <<= 8;
				}

				result |= (0xFF & Short.parseShort(matcher.group(1)));
			}
		}

		return result;
	}

	/**
	 * <p>
	 * Converts a string representation of color to AWT Color object.
	 * </p>
	 * 
	 * <p>
	 * Works with two formats:
	 * </p>
	 * 
	 * <ul>
	 * <li><code>#09FE4A</code> - <b>hexadecimal</b></li>
	 * <li><code>rgb(132, 5, 18)</code> - <b>decimal</b></li>
	 * </ul>
	 * 
	 * @param colorValue
	 *            string represented in one of two formats
	 * @return AWT's Color value representation of string-represented
	 *         colorValue; if colorValue is null, returns null
	 */
	public static Color convertToAWTColor(String colorValue) {
		if (colorValue == null) {
			return null;
		}
		int convertedValue = convertToInteger(colorValue);
		return new Color(convertedValue);
	}
}
