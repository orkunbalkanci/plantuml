/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques (for Atos Origin).
 *
 */
package net.sourceforge.plantuml.code;

class Base81 {

	//private static final String cars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_:;@.,[]{}()=$#*!^";
	private static final String cars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_:;@.,/+|~()=$#*!^";

	static char digitToChar(int digit) {
		if (digit < 0 || digit >= 81) {
			throw new IllegalArgumentException("d=" + digit);
		}
		return cars.charAt(digit);
	}

	static int charToDigit(char c) {
		final int idx = cars.indexOf(c, 0);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		return idx;
	}

	static public int decode(String s) {
		if (s.length() != 3) {
			throw new IllegalArgumentException();
		}
		final int v1 = charToDigit(s.charAt(0));
		final int v2 = charToDigit(s.charAt(1));
		final int v3 = charToDigit(s.charAt(2));
		return ((v1 * 81) + v2) * 81 + v3;
	}

	static public String code(final int x) {
		if (x < 0 || x >= 81 * 81 * 81) {
			throw new IllegalArgumentException("x=" + x);
		}
		final int v1 = x / 81 / 81;
		int r = x - v1 * 81 * 81;
		final int v2 = r / 81;
		r = r - v2 * 81;
		final int v3 = r;

		final StringBuilder sb = new StringBuilder();
		sb.append(digitToChar(v1));
		sb.append(digitToChar(v2));
		sb.append(digitToChar(v3));
		return sb.toString();
	}

}