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
package com.moss.balloon.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.moss.balloon.math.Math2d;

public class RectIntersectTestPanel extends JPanel {
	
	Rectangle rect;
	static Point mousePoint;
	
	public RectIntersectTestPanel() {
		rect = new Rectangle(100, 50);
		rect.setLocation(new Point(100,100));
		
		addMouseListener(new ClickyMouseListener());
	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setSize(400,200);
		window.getContentPane().setLayout(new BorderLayout());
		
		RectIntersectTestPanel rectPanel = new RectIntersectTestPanel();
		window.getContentPane().add(rectPanel, BorderLayout.CENTER);
		
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public class ClickyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			setMousePoint(e.getPoint());
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	private void setMousePoint(Point source) {
		mousePoint = source;
		System.out.println("mouse clicked at: "+mousePoint);
		repaint();
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.drawString("("+rect.getLocation().x+", "+rect.getLocation().y+")", (int)rect.getLocation().x, (int)rect.getLocation().y);
		g.drawString("("+(rect.getLocation().x+rect.getWidth())+", "+(rect.getLocation().y+rect.getHeight())+")", (int)(rect.getLocation().x+rect.getWidth()), (int)(rect.getLocation().y+rect.getHeight()));
		g.drawRect(rect.getLocation().x, rect.getLocation().y, (int)rect.getWidth(), (int)rect.getHeight());
		if(mousePoint!= null) drawRectangleIntersectionIndicators(g);
		else System.out.println("mousePoint is null");
	}
	
	private void drawRectangleIntersectionIndicators(Graphics g) {
		Point rectCenter = Math2d.getCenter(rect.getLocation(), rect.getWidth(), rect.getHeight());
		double angleBetweenRectCenterAndMousePoint = Math2d.calculateAngleInDegrees(rectCenter, mousePoint);
		System.out.println("Angle between rectangle center and mousePoint: "+angleBetweenRectCenterAndMousePoint);
		Point rectIntersect  = Math2d.findRectangleIntersection(rectCenter, rect.getWidth(), rect.getHeight(), angleBetweenRectCenterAndMousePoint);
//		Point realCenter = Math2d.convertScreenCoordinateToRealCoordinate(rectCenter, this.getHeight());
//		Point intersect  = Math2d.findRectangleIntersection(realCenter, rect.getWidth(), rect.getHeight(), Math.abs(angleBetweenRectCenterAndMousePoint));
//		Point rectIntersect = Math2d.convertRealCoordinatesToScreenCoordinates(intersect, this.getHeight());
		g.setColor(Color.green);
		g.drawLine(rectCenter.x, rectCenter.y, mousePoint.x, mousePoint.y);
		g.setColor(Color.red);
		g.drawOval(rectIntersect.x-2, rectIntersect.y-2, 4, 4);
		
	}
}
