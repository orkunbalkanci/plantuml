/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;

public class CommandHeader extends SingleLineCommand<UmlDiagram> {

	public CommandHeader(UmlDiagram diagram) {
		super(diagram, "(?i)^(?:(left|right|center)?\\s*)header(?:\\s*:\\s*|\\s+)(.*[\\p{L}0-9_.].*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final String align = arg.get(0);
		if (align != null) {
			getSystem().setHeaderAlignement(HorizontalAlignement.valueOf(align.toUpperCase()));
		}
		getSystem().setHeader(StringUtils.getWithNewlines(arg.get(1)));
		return CommandExecutionResult.ok();
	}

}
