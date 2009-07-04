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
package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.skin.AbstractComponent;

public class ComponentBlueModernActiveLine extends AbstractComponent {

	private final int shadowview = 3;
	private final Color foregroundColor;

	public ComponentBlueModernActiveLine(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	@Override
	protected void drawInternal(Graphics2D g2d, Dimension2D dimensionToUse) {
		final int x = (int) (dimensionToUse.getWidth() - getPreferredWidth(g2d)) / 2;
		final ShadowShape shadowShape = new ShadowShape(getPreferredWidth(g2d),
				dimensionToUse.getHeight() - shadowview, 3);
		g2d.translate(shadowview, shadowview);
		shadowShape.draw(g2d);
		g2d.translate(-shadowview, -shadowview);

		g2d.setColor(foregroundColor);
		g2d.fillRect(x, 0, (int) getPreferredWidth(g2d), (int) (dimensionToUse.getHeight() - shadowview));
	}

	@Override
	public double getPreferredHeight(Graphics2D g2d) {
		return 0;
	}

	@Override
	public double getPreferredWidth(Graphics2D g2d) {
		return 10;
	}

}