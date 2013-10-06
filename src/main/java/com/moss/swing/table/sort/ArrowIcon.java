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
import javax.swing.UIManager;


public class ArrowIcon implements Icon {
    public static final int UP    = 0;         // direction
    public static final int DOWN  = 1;

    private static final int DEFAULT_SIZE = 11;

    private Color edge1;
    private Color edge2;
    private Color fill;
    private int size;
    private int direction;

    /**
    * This constructor calls init to init properties for this icon.
    *
    * @param int direction - the direction of the arrow that will be painted on the icon.
    * @param boolean isRaisedView, isPressedView - indicate if the icon is pressed or raised
    *               used to get color for edges from UIManager.
    */
    public ArrowIcon(int direction, boolean isRaisedView, boolean isPressedView) {
        if (isRaisedView) {
            if (isPressedView) {
                init( UIManager.getColor("controlLtHighlight"),
                    UIManager.getColor("controlDkShadow"),
                    UIManager.getColor("controlShadow"),
                    DEFAULT_SIZE, direction);
            } else {
                init( UIManager.getColor("controlHighlight"),
                    UIManager.getColor("controlShadow"),
                    //UIManager.getColor("control"),
                    new Color(12,50,116),
                    DEFAULT_SIZE, direction);
            }
        } else {
            if (isPressedView) {
                init( UIManager.getColor("controlDkShadow"),
                    UIManager.getColor("controlLtHighlight"),
                    UIManager.getColor("controlShadow"),
                    DEFAULT_SIZE, direction);
            } else {
                init( UIManager.getColor("controlShadow"),
                    UIManager.getColor("controlHighlight"),
                    //UIManager.getColor("control"),
                    new Color(12,50,116),
                    DEFAULT_SIZE, direction);
            }
        }
    }

    /**
    * This constructor calls init to initialize properties for this icon.
    *
    * @param Color edge1,edge2 - color for Icon's edges.
    * @param Color fill - fill color for Icon.
    * @param int size - the size for this icon.
    * @param int direction - the direction of the arrow that will be painted on the icon.
    */
    public ArrowIcon(Color edge1, Color edge2, Color fill,
                    int size, int direction) {
        init(edge1, edge2, fill, size, direction);
    }


    /**
    * Draw the icon at the specified location.
    *
    * @param Component c - the Component may be used to get properties useful for painting,
    *       e.g. the foreground or background color.
    * @param Graphics g - the graphic handler to paint on this icon.
    * @param int x,y - the position to draw this icon
    */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        switch (direction) {
            case DOWN: drawDownArrow(g, x, y); break;
            case   UP: drawUpArrow(g, x, y);   break;
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


    /**
    * This method init properties for this icon.
    *
    * @param Color edge1,edge2 - color for Icon's edges.
    * @param Color fill - fill color for Icon.
    * @param int size - the size for this icon.
    * @param int direction - the direction of the arrow that will be painted on the icon.
    */
    private void init(Color edge1, Color edge2, Color fill,
                    int size, int direction) {
        this.edge1 = edge1;
        this.edge2 = edge2;
        this.fill = fill;
        this.size = size;
        this.direction = direction;
    }

    /**
    * This method draw a down arrow on the passed in graphics at the specified position.
    *
    * @param Graphics g - where the arrow will be drawn on.
    * @param int xo,yo - the position on the Graphics to draw this arrow.
    */
    private void drawDownArrow(Graphics g, int xo, int yo) {
        g.setColor(edge1);
        g.drawLine(xo, yo,   xo+size-1, yo);
        g.drawLine(xo, yo+1, xo+size-3, yo+1);
        g.setColor(edge2);
        g.drawLine(xo+size-2, yo+1, xo+size-1, yo+1);
        int x = xo+1;
        int y = yo+2;
        int dx = size-6;
        while (y+1 < yo+size) {
        g.setColor(edge1);
        g.drawLine(x, y,   x+1, y);
        g.drawLine(x, y+1, x+1, y+1);
        if (0 < dx) {
            g.setColor(fill);
            g.drawLine(x+2, y,   x+1+dx, y);
            g.drawLine(x+2, y+1, x+1+dx, y+1);
        }
        g.setColor(edge2);
        g.drawLine(x+dx+2, y,   x+dx+3, y);
        g.drawLine(x+dx+2, y+1, x+dx+3, y+1);
        x += 1;
        y += 2;
        dx -= 2;
        }
        g.setColor(edge1);
        g.drawLine(xo+(size/2), yo+size-1, xo+(size/2), yo+size-1);
    }

    /**
    * This method draw an up arrow on the passed in graphics at the specified position.
    *
    * @param Graphics g - where the arrow will be drawn on.
    * @param int xo,yo - the position on the Graphics to draw this arrow.
    */
    private void drawUpArrow(Graphics g, int xo, int yo) {
        g.setColor(edge1);
        int x = xo+(size/2);
        g.drawLine(x, yo, x, yo);
        x--;
        int y = yo+1;
        int dx = 0;
        while (y+3 < yo+size) {
        g.setColor(edge1);
        g.drawLine(x, y,   x+1, y);
        g.drawLine(x, y+1, x+1, y+1);
        if (0 < dx) {
            g.setColor(fill);
            g.drawLine(x+2, y,   x+1+dx, y);
            g.drawLine(x+2, y+1, x+1+dx, y+1);
        }
        g.setColor(edge2);
        g.drawLine(x+dx+2, y,   x+dx+3, y);
        g.drawLine(x+dx+2, y+1, x+dx+3, y+1);
        x -= 1;
        y += 2;
        dx += 2;
        }
        g.setColor(edge1);
        g.drawLine(xo, yo+size-3,   xo+1, yo+size-3);
        g.setColor(edge2);
        g.drawLine(xo+2, yo+size-2, xo+size-1, yo+size-2);
        g.drawLine(xo, yo+size-1, xo+size, yo+size-1);
    }
}