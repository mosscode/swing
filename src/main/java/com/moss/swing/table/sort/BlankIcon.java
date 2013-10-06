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
package com.moss.swing.table.sort;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;


public class BlankIcon implements Icon {
    private static final int DEFAULT_SIZE = 11;

    private Color fillColor;
    private int size;

    /**
    * This constructor constructs an Icon with no fillcolor and default size.
    */
    public BlankIcon() {
        this(null, DEFAULT_SIZE);
    }

    /**
    * This constructor constructs an Icon with given fillcolor and given size.
    *
    * @param Color color - the fill color for this icon.
    * @param int size - the size of the icon.
    */
    public BlankIcon(Color color, int size) {
        fillColor = color;
        this.size = size;
    }

    /**
    * Draw the icon at the specified location.
    *
    * @param Component c - the Component may be used to get properties useful for painting,
    *       e.g. the foreground or background color.
    * @param Graphics g - the graphic handler to paint on this icon, not used here.
    * @param int x,y - the position to draw this icon
    */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (fillColor != null) {
            g.setColor(fillColor);
            g.drawRect(x, y, size-1, size-1);
        }
    }

    /**
    * This method get the icon's fixed width.
    *
    * @return int - the icon's fixed width.
    */
    public int getIconWidth() {
        return size;
    }

    /**
    * This method get the icon's fixed height .
    *
    * @return int - the icon's fixed height .
    */
    public int getIconHeight() {
        return size;
    }
}