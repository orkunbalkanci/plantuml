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
package net.sourceforge.plantuml.activitydiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Command;
import net.sourceforge.plantuml.CommandControl;
import net.sourceforge.plantuml.CommandMultilinesTitle;
import net.sourceforge.plantuml.CommandTitle;
import net.sourceforge.plantuml.PSystemFactory;
import net.sourceforge.plantuml.activitydiagram.command.CommandLinkActivity;
import net.sourceforge.plantuml.activitydiagram.command.CommandLinkLongActivity;
import net.sourceforge.plantuml.activitydiagram.command.CommandPartition;

public class ActivityDiagramFactory implements PSystemFactory {

	private ActivityDiagram system;

	private List<Command> cmds;

	public ActivityDiagramFactory() {
		reset();
	}

	public List<Command> create(List<String> lines) {
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.OK) {
				return Arrays.asList(cmd);
			} else if (result == CommandControl.OK_PARTIAL) {
				return Collections.emptyList();
			}
		}
		return null;
	}

	public ActivityDiagram getSystem(String source) {
		system.setSource(source);
		return system;
	}

	public void reset() {
		system = new ActivityDiagram();
		cmds = new ArrayList<Command>();

		cmds.add(new CommandLinkActivity(system));
		cmds.add(new CommandPartition(system));
		cmds.add(new CommandLinkLongActivity(system));
		
		cmds.add(new CommandTitle(system));
		cmds.add(new CommandMultilinesTitle(system));
	}
}