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
 * Revision $Revision: 6710 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graph.ANode;
import net.sourceforge.plantuml.graph.ANodeImpl;
import net.sourceforge.plantuml.graph.Board;
import net.sourceforge.plantuml.graph.BoardExplorer;
import net.sourceforge.plantuml.graph.Graph2;
import net.sourceforge.plantuml.graph.Heap;
import net.sourceforge.plantuml.graph.Zoda1;
import net.sourceforge.plantuml.graph.Zoda2;
import net.sourceforge.plantuml.png.PngSplitter;

public final class CucaDiagramPngMaker2 {

	private final CucaDiagram diagram;

	public CucaDiagramPngMaker2(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	public void createPng(OutputStream os) throws IOException {
		final Zoda2 zoda2 = new Zoda2();

		for (Link link : diagram.getLinks()) {
			final String s = link.getEntity1().getCode() + "->" + link.getEntity2().getCode();
			System.err.println("CucaDiagramPngMaker2:: " + s);
			final int diffHeight = link.getLength() - 1;
			System.err.println("CucaDiagramPngMaker2:: " + s + " " + diffHeight);
			zoda2.addLink(s, diffHeight, link);
		}
		for (Entity ent : diagram.entities().values()) {
			ANode n = zoda2.getNode(ent.getCode());
			if (n == null) {
				n = zoda2.createAloneNode(ent.getCode());
			}
			((ANodeImpl) n).setUserData(ent);
		}

		final List<Graph2> graphs = getGraphs2(zoda2.getHeaps());

		final Dimension2D totalDim = getTotalDimension(graphs);
		final EmptyImageBuilder im = new EmptyImageBuilder(totalDim.getWidth(), totalDim.getHeight(), Color.WHITE);

		double x = 0;

		final Graphics2D g2d = im.getGraphics2D();

		for (Graph2 g : graphs) {
			g2d.setTransform(new AffineTransform());
			g2d.translate(x, 0);
			g.draw(g2d);
			x += g.getDimension().getWidth();
		}

		ImageIO.write(im.getBufferedImage(), "png", os);
	}

	private Dimension2D getTotalDimension(List<Graph2> graphs) {
		double width = 0;
		double height = 0;
		for (Graph2 g : graphs) {
			width += g.getDimension().getWidth();
			height = Math.max(height, g.getDimension().getHeight());
		}
		return new Dimension2DDouble(width, height);

	}

	private List<Graph2> getGraphs2(Collection<Heap> heaps) {
		final List<Graph2> result = new ArrayList<Graph2>();
		for (Heap h : heaps) {
			h.computeRows();
			Board board = new Board(h.getNodes(), h.getLinks());

			final BoardExplorer boardExplorer = new BoardExplorer(board);
			final long start = System.currentTimeMillis();
			for (int i = 0; i < 400; i++) {
				final boolean finished = boardExplorer.onePass();
				if (finished) {
					break;
				}
				if (i % 100 == 0) {
					Log.info("" + i + " boardExplorer.getBestCost()=" + boardExplorer.getBestCost() + " "
							+ boardExplorer.collectionSize());
				}
			}
			Log.info("################# DURATION = " + (System.currentTimeMillis() - start));
			board = boardExplorer.getBestBoard();

			result.add(new Graph2(board));
		}
		return result;
	}

	private void createPngOld(OutputStream os) throws IOException {
		final Zoda1 zoda1 = new Zoda1();
		if (diagram.getLinks().size() == 0) {
			return;
		}
		for (Link link : diagram.getLinks()) {
			final String s = link.getEntity1().getCode() + "->" + link.getEntity2().getCode();
			// System.err.println("CucaDiagramPngMaker2:: " + s);
			zoda1.addLink(s);
		}
		for (Entity ent : diagram.entities().values()) {
			final ANodeImpl n = zoda1.getExistingNode(ent.getCode());
			n.setUserData(ent);
		}
		zoda1.computeRows();
		final Board board = new Board(zoda1.getNodes(), zoda1.getLinks());
		// final BufferedImage im = new Graph2(board).createBufferedImage();
		// ImageIO.write(im, "png", os);

	}

	public List<File> createPng(File pngFile) throws IOException {
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(pngFile));
			createPng(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}

		return new PngSplitter(pngFile, diagram.getHorizontalPages(), diagram.getVerticalPages(),
				diagram.getMetadata(), 96).getFiles();
	}
}
