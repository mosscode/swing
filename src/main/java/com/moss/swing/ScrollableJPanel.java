/**
 * Copyright (C) 2013, Moss Computing Inc.
 *
 * This file is part of swing.
 *
 * swing is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * swing is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with swing; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package com.moss.swing;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class ScrollableJPanel extends JPanel implements Scrollable {
	private boolean scrollableTracksViewportHeight = true;
	private boolean scrollableTracksViewportWidth = true;
	private int scrollableUnitIncrement = 20;
	private int scrollableBlockIncrement = 100;
	
	public ScrollableJPanel(boolean scrollableTracksViewportHeight,
			boolean scrollableTracksViewportWidth) {
		super();
		this.scrollableTracksViewportHeight = scrollableTracksViewportHeight;
		this.scrollableTracksViewportWidth = scrollableTracksViewportWidth;
	}

	public ScrollableJPanel() {
		super();
	}

	public ScrollableJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public ScrollableJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public ScrollableJPanel(LayoutManager layout) {
		super(layout);
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public void setScrollableBlockIncrement(int scrollableBlockIncrement) {
		this.scrollableBlockIncrement = scrollableBlockIncrement;
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return scrollableBlockIncrement;
	}
	public void setScrollableTracksViewportHeight(
			boolean scrollableTracksViewportHeight) {
		this.scrollableTracksViewportHeight = scrollableTracksViewportHeight;
	}
	public boolean getScrollableTracksViewportHeight() {
		return scrollableTracksViewportHeight;
	}
	public void setScrollableTracksViewportWidth(
			boolean scrollableTracksViewportWidth) {
		this.scrollableTracksViewportWidth = scrollableTracksViewportWidth;
	}
	public boolean getScrollableTracksViewportWidth() {
		return scrollableTracksViewportWidth;
	}
	public void setScrollableUnitIncrement(int scrollableUnitIncrement) {
		this.scrollableUnitIncrement = scrollableUnitIncrement;
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return scrollableUnitIncrement;
	}
	
}
