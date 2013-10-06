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
package com.moss.balloon.math;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class EllipseTestPanel extends JPanel {
	
	private Ellipse2d ellipseA = new Ellipse2d(0.0,0.0,200.0,100.0);
	
	public EllipseTestPanel() {
		super();
		setLayout(new BorderLayout());
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Point centerScreen = Math2d.getCenter(getWidth(), getHeight());
		g.setColor(Color.pink);
		Ellipse2d centerEllipse = new Ellipse2d(centerScreen, 500.0, 500.0);
		drawOval(centerEllipse, g);
//		try {
//			Point[] yInts = centerEllipse.getYIntercepts(centerEllipse.getCenter().getX() - 40);
//			g.setColor(Color.red);
//			drawOval((int)yInts[0].getX(), (int)yInts[0].getY(), 20, 20, g);
//			drawOval((int)yInts[1].getX(), (int)yInts[1].getY(), 20, 20, g);
//		} catch(Exception ex) {
//			ex.printStackTrace();
//		}
		
		testXDrawingIntercepts(centerEllipse, g);
		testYDrawingIntercepts(centerEllipse, g);
	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.add(new EllipseTestPanel());
		window.setSize(800,600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	private void drawOval(int x, int y, int width, int height, Graphics g) {
		g.drawOval(x - width/2, y - height/2, width, height);
	}
	
	private void drawOval(Ellipse2d ellipse, Graphics g) {
		int width = (int)ellipse.getWidth();
		int height = (int)ellipse.getHeight();
		g.drawOval((int)ellipse.getCenter().getX()-width/2, (int)ellipse.getCenter().getY()-height/2, width, height);
	}
	
	private void testXDrawingIntercepts(Ellipse2d ellipse, Graphics g) {
		try {
			g.setColor(Color.RED);
			Point[] xInts = ellipse.getXIntercepts(ellipse.getCenter().getY() - 40);
			drawOval((int)xInts[0].getX(), (int)xInts[0].getY(), 20, 20, g);
			drawOval((int)xInts[1].getX(), (int)xInts[1].getY(), 20, 20, g);
			
			Point[] xInts2 = ellipse.getXIntercepts(ellipse.getCenter().getY() - 80);
			drawOval((int)xInts2[0].getX(), (int)xInts2[0].getY(), 20, 20, g);
			drawOval((int)xInts2[1].getX(), (int)xInts2[1].getY(), 20, 20, g);
			
			Point[] xInts3 = ellipse.getXIntercepts(ellipse.getCenter().getY() - 120);
			drawOval((int)xInts3[0].getX(), (int)xInts3[0].getY(), 20, 20, g);
			drawOval((int)xInts3[1].getX(), (int)xInts3[1].getY(), 20, 20, g);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void testYDrawingIntercepts(Ellipse2d ellipse, Graphics g) {
		try {
			g.setColor(Color.green);
			Point[] yInts = ellipse.getYIntercepts(ellipse.getCenter().getX() - 40);
			drawOval((int)yInts[0].getX(), (int)yInts[0].getY(), 20, 20, g);
			drawOval((int)yInts[1].getX(), (int)yInts[1].getY(), 20, 20, g);
			
			Point[] xInts2 = ellipse.getYIntercepts(ellipse.getCenter().getX() - 80);
			drawOval((int)xInts2[0].getX(), (int)xInts2[0].getY(), 20, 20, g);
			drawOval((int)xInts2[1].getX(), (int)xInts2[1].getY(), 20, 20, g);
			
			Point[] xInts3 = ellipse.getYIntercepts(ellipse.getCenter().getX() - 120);
			drawOval((int)xInts3[0].getX(), (int)xInts3[0].getY(), 20, 20, g);
			drawOval((int)xInts3[1].getX(), (int)xInts3[1].getY(), 20, 20, g);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
