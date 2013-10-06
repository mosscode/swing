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
package com.moss.swing.birdviewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BirdView extends JPanel {
	private JScrollPane scrollpane;
	private double scale = .25;
	private Color shadeColor = new Color(23,23,23, 20);
	private BufferedImage image;
	private double minSizeRatio =.25;
	
	public BirdView() {
		addHierarchyBoundsListener(new HierarchyBoundsListener(){
			public void ancestorMoved(HierarchyEvent e) {
			}
			public void ancestorResized(HierarchyEvent e) {
			}
		});
	}
	
	
	private void recalibrateScale(){
		Dimension mySize = getSize();
		Dimension viewportSize = scrollpane.getViewport().getViewSize();
		
		if(mySize.height>0 && mySize.width>0 && viewportSize.height>0 && viewportSize.width>0){
			double myAspectRatio = ((double)mySize.height)/((double)mySize.width);
			double viewportAspectRatio = ((double)viewportSize.height)/((double)viewportSize.width);
			
			if(myAspectRatio>viewportAspectRatio){
				//I AM TALLER AND THINNER PROPORTIONALLY
				//  (have to fit the viewport's short height into mine)
				scale = ((double)mySize.width)/((double)viewportSize.width);
			}else{
				//I AM SHORTER AND FATTER PROPORTIONALLY
				//  (have to fit the viewport's tall height into mine
				scale = ((double)mySize.height)/((double)viewportSize.height);
			}
			
			Component view = scrollpane.getViewport().getView();
			Dimension newSize = scale(viewportSize);
		}
		
	}
	
	public void setScrollpane(JScrollPane scrollpane) {
		this.scrollpane = scrollpane;
		
		scrollpane.getViewport().addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});
		
		recalibrateScale();
		


		new Thread() {
			private boolean isTrulyVisible(){
				boolean visible = BirdView.this.isVisible();
				Container c = BirdView.this.getParent();
				while(c!=null && visible){
					visible = c.isVisible();
					c = c.getParent();
				}
				return visible;
				
			}
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				
				while (isTrulyVisible()) {
//					System.out.println("Refreshing Birdview " + getSize());
					SwingUtilities.invokeLater(new RerenderRunnable());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();

		addMouseMotionListener(new MouseMotionAdapter() {
			// int dragLocation = -1;
			Point lastLocation;

			@Override
			public void mouseDragged(MouseEvent e) {
				if (lastLocation == null)
					lastLocation = e.getPoint();
				int deltaY = lastLocation.y - e.getY();
				int deltaX = lastLocation.x - e.getX();

				moveViewport(-unscale(deltaX), -unscale(deltaY));

				lastLocation = e.getPoint();
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				lastLocation = e.getPoint();
			}

		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				centerOnPoint(unscale(e.getPoint()));
			}

		});
	}
	
	class RerenderRunnable implements Runnable {
		public void run() {
			try {
				render();
				repaint();
			} catch (Exception e) {
//				throw new RuntimeException(e);
//				e.printStackTrace();
//				 e.printStackTrace();
			}			
		}
	}
	
	public void centerOnPoint(Point p){
		
		// ESTABLISH THE MOVE
		
		Point startPoint = BirdView.this.scrollpane.getViewport().getViewPosition();
		Point endPoint = fromCenterToCorner(p);
		
		// KEEP THE RECTANGLE WITHIN THE BOUNDS OF THE VIEW
		Dimension bounds = scrollpane.getViewport().getViewSize();
		Dimension portSize = scrollpane.getViewport().getExtentSize();
		
		keepBoxInBox(bounds, portSize, endPoint);
		
		// DO THE SLIDE
		new Slider().slide(new Slider.Positioner(){
			public void setPosition(Point position) {
				BirdView.this.scrollpane.getViewport().setViewPosition(position);
				BirdView.this.repaint();
			}
		}, startPoint, endPoint);
	}
	
	public void keepBoxInBox(Dimension bounds, Dimension inner, Point position){
		if(position.x<0) position.x = 0;
		if((position.x + inner.width)>bounds.width)position.x = (bounds.width - inner.width);
		
		if(position.y<0) position.y = 0;
		if((position.y + inner.height)>bounds.height)position.y = (bounds.height - inner.height);
	}
	
	
	private Point fromCornerToCenter(Point p){
		Dimension size = scrollpane.getViewport().getExtentSize();
		return new Point(p.x + (size.width/2), p.y + (size.height/2));
	}
	
	private Point fromCenterToCorner(Point p){
		Dimension size = scrollpane.getViewport().getExtentSize();
		return new Point(p.x - (size.width/2), p.y - (size.height/2));
	}
	
	private Point getViewCenterPosition(){
		return fromCornerToCenter(scrollpane.getViewport().getViewPosition());

	}
	
	private void setViewCenterPosition(Point p){
		scrollpane.getViewport().setViewPosition(fromCenterToCorner(p));
	}
	
	static class Slider implements ActionListener {
		
		public static interface Positioner {
			void setPosition(Point position);
		}
		
		private javax.swing.Timer timer = new javax.swing.Timer(5, this);
		private long start;
		private long duration = 250;
		private Point startPoint;
		private Point endPoint;
		private AxisSlider xSlide;
		private AxisSlider ySlide;
		private Positioner positioner;
		
		public void slide(Positioner positioner, Point startPoint, Point endPoint){
			this.startPoint = startPoint;
			this.endPoint = endPoint;
			this.positioner = positioner;
			
			xSlide = new AxisSlider(endPoint.x - startPoint.x);
			ySlide = new AxisSlider(endPoint.y - startPoint.y);
			
			start = System.currentTimeMillis();

			timer.start();
		}

		public void actionPerformed(ActionEvent e) {
			long now = System.currentTimeMillis();
			long elapsed = now - start;
			double percentComplete = (double)elapsed/(double)duration;
			
			positioner.setPosition(new Point(
					startPoint.x + xSlide.slide(percentComplete),
					startPoint.y + ySlide.slide(percentComplete)
					));
			
//			repaint();
			if(elapsed >= duration){
				timer.stop();
			}
		}
	}
	
	static class AxisSlider {
		private int translateMax;
		
		public AxisSlider(int translateMax) {
			super();
			this.translateMax = translateMax;
		}

		int slide(double percentComplete){
			return (int)(percentComplete * translateMax);
		}
	}
	
	private Dimension scale(Dimension size) {

		return new Dimension((int) (size.width * scale),
				(int) (size.height * scale));
	}
	private Dimension scale(Dimension size, double scale) {

		return new Dimension((int) (size.width * scale),
				(int) (size.height * scale));
	}

	private Point scale(Point point) {
		return new Point((int) (point.x * scale), (int) (point.y * scale));
	}

	private int unscale(int size) {
		return (int) (size * (1 / scale));
	}

	private Point unscale(Point point) {
		return new Point(unscale(point.x), unscale(point.y));
	}

	public void moveViewport(int dx, int dy) {
		Point oldPos = scrollpane.getViewport().getViewPosition();
		Point pos = new Point((int) (oldPos.getX() + dx),
				(int) (oldPos.getY() + dy));
		
		// KEEP THE RECTANGLE WITHIN THE BOUNDS OF THE VIEW
		Dimension bounds = scrollpane.getViewport().getViewSize();
		Dimension portSize = scrollpane.getViewport().getExtentSize();
		
		keepBoxInBox(bounds, portSize, pos);

		scrollpane.getViewport().setViewPosition(pos);
		repaint();
	}

	private void render() {
//		System.out.println("Rendering Birdview Snapshot");
		
		Component view = scrollpane.getViewport().getView();
//		System.out.println("View Size:" + view.getSize());
//		System.out.println("View Preferred Size:" + view.getPreferredSize());
		
		Dimension size = scale(view.getPreferredSize());
//		System.out.println("Scaled Size: " + size);
		
		BufferedImage bImage = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics2D gi = bImage.createGraphics();
		gi.scale(scale, scale);
		gi.setBackground(Color.white);
		view.paintAll(gi);
//		SwingUtilities.paintComponent(gi, view, new JPanel(), view.getBounds());

		image = bImage;
		
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//	}
//	@Override
//	public void paint(Graphics g) {
//		super.paint(g);
		
		recalibrateScale();
		
		if (image != null) {
			g.drawImage(image, 0, 0, null);

			Dimension viewSize = scale(scrollpane.getViewport().getSize());

			Point location = scale(scrollpane.getViewport().getViewPosition());
			g.drawRect(location.x, location.y, (int) viewSize.getWidth(),
					(int) viewSize.getHeight());
			
			if(shadeColor!=null){
				Dimension mySize = getSize();
				g.setColor(shadeColor);
				g.fillRect(0, 0, mySize.width, location.y);//above
				g.fillRect(0, location.y + viewSize.height, mySize.width, mySize.height);//below
				g.fillRect(0, location.y, location.x, viewSize.height);//left
				g.fillRect(location.x + viewSize.width, location.y, mySize.width, viewSize.height);//right
			}
		}
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		Dimension min = getMinimumSize();
		if(size.height<min.height)
			size.height = min.height;
		if(size.width<min.width)
			size.width = min.width;
		return size;
	}
	
	@Override
	public Dimension getMinimumSize() {
		Dimension size = scale(scrollpane.getViewport().getExtentSize(), minSizeRatio);
		System.out.println("Min: " + size);
		return size;
	}
}
