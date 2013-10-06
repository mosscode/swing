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
package com.moss.balloon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.moss.balloon.math.Math2d;

public class BalloonPanel extends JPanel {
	
	private BalloonCaption2d balloon;
	private Point location = new Point(200,100);
	private String caption;
	private Point directionPoint;
	private Point edgeOfComponent;
	private Component content;
	private double windowWidth=0.0;
	private double windowHeight=0.0;
	
	private BalloonOverlay balloonOverlay = new BalloonOverlay();
	
	public BalloonPanel(){
		super(null);
		setOpaque(false);
	}
	
	public void setContent(Component component){
		removeAll();
		this.content = component;
		
		int width = getWidth();
		int height = getHeight();
		balloonOverlay.setSize(width, height);
		content.setSize(width, height);
		
		super.setLayout(null);
		super.add(balloonOverlay);
		super.add(component);
		
	}
	
	
	
	@Override
	public Component add(Component comp) {
		throw new RuntimeException("You should not set this!");
	}
	
	@Override
	public Component add(Component comp, int index) {
		throw new RuntimeException("You should not set this!");
	}
	
	@Override
	public void add(Component comp, Object constraints) {
		throw new RuntimeException("You should not set this!");
	}
	
	@Override
	public void add(Component comp, Object constraints, int index) {
		throw new RuntimeException("You should not set this!");
	}
	@Override
	public void setLayout(LayoutManager mgr) {
		if(mgr!=null)
			throw new RuntimeException("You should not set this!");
	}
	
	private void showBalloon(String caption, Point centerOfComponent, double windowWidth, double windowHeight) {
		this.windowWidth=windowWidth;
		this.windowHeight=windowHeight;
		this.balloon = new BalloonCaption2d(caption, centerOfComponent, focus.getWidth(), focus.getHeight(), windowWidth, windowHeight);
		this.caption = caption;
		startAnimation();
	}
	
	private Component focus;
	
	public void hideBalloon(){
		System.out.println("Hiding Balloon");
//		Thread.dumpStack();
		balloon = null;
		focus = null;
		repaint();
	}
	
	private ChangeListener changeListener = new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
			showBalloonAtLocation(false);
		}
	};
	
	/**
	 * Moves an existing balloon to the current focus location.
	 * For use when the focus may have moved relative to where it
	 * was when the ballon was last shown or relocated.
	 */
	private void showBalloonAtLocation(boolean create){
		if(focus==null) return;
		Component balloonComponent = focus;
		
		Point convertedComponentLocation = Math2d.convertLocation(balloonComponent, this);
//		this.edgeOfComponent = new Point((int)convertedComponentLocation.getX()+balloonComponent.getWidth()/2, (int)convertedComponentLocation.getY());
		Point centerOfComponent = new Point((int)(convertedComponentLocation.x + balloonComponent.getWidth()/2.0), (int)(convertedComponentLocation.y + balloonComponent.getHeight()/2.0));
		this.edgeOfComponent = Math2d.findRectangleIntersection(centerOfComponent, balloonComponent.getWidth(), balloonComponent.getHeight(), -45);
//		this.directionPoint = new Point(this.getWidth() - edgeOfComponent.x, this.getHeight() - edgeOfComponent.y);
//		double tailAngle = Math.toDegrees(Math.atan((directionPoint.y - edgeOfComponent.y)/(directionPoint.x - edgeOfComponent.x)));
		
		if(create) {
			System.out.println("creating with width: "+this.getWidth() + " height: "+this.getHeight());
			showBalloon(caption, centerOfComponent, this.getWidth(), this.getHeight());
		}
		else {
			System.out.println("relocating with width: "+this.getWidth() + " height: "+this.getHeight());
			balloon.relocateBalloon(centerOfComponent, focus.getWidth(), focus.getHeight(), this.getWidth(), this.getHeight());
//			balloon.translateCenter(new Point(getWidth()/2, getHeight()/2), new Point(0,0));
//			System.out.println("Angle between balloon center and 0,0: "+Math2d.calculateAngleInDegrees(new Point(getWidth()/2, getHeight()/2), new Point(0,0)));
//			balloon.translateCenter(new Point((int)edgeOfComponent.getX(), (int)(convertedComponentLocation.getY() + balloonComponent.getHeight()/2)), new Point(0,0));
		}
		repaint();
	}
	private List<JScrollPane> monitoredScrollPanes = new LinkedList<JScrollPane>();
	
	private void registerListeners(){
		Component next = focus;
		while(next!=null){
			if(next instanceof JScrollPane){
				JScrollPane sp = (JScrollPane) next;
				if(!monitoredScrollPanes.contains(sp)){
					sp.getViewport().addChangeListener(changeListener);
					monitoredScrollPanes.add(sp);
				}
			}
			next = next.getParent();
		}
	}
	private void deRegisterListeners(){
		for(JScrollPane sp : monitoredScrollPanes){
			sp.getViewport().removeChangeListener(changeListener);
		}
		monitoredScrollPanes.clear();
	}
	public void showBalloon(String caption, Component balloonComponent){
		if(
				this.caption!=null 
				&& 
				this.caption.equals(caption) 
				&& 
				this.focus!=null 
				&& 
				(this.focus==balloonComponent)){
			System.out.println("Ignoring this because it's already showing");
			return; // we're already showing this
		}
		else{
			System.out.println("Showing new balloon");
//			Thread.currentThread().dumpStack();
		}
		//		if(balloonComponent == focus) {
		//			this.focus = balloonComponent;
		//			this.caption = caption;
		//			registerListeners();
		//
		//			Point convertedComponentLocation = Math2d.convertLocation(balloonComponent, this);
		//			this.middleOfComponent = new Point((int)convertedComponentLocation.getX()+balloonComponent.getWidth()/2, (int)convertedComponentLocation.getY() + balloonComponent.getHeight()/2);
		//			this.directionPoint = new Point(this.getWidth() - middleOfComponent.x, this.getHeight() - middleOfComponent.y);
		//			double tailAngle = Math.toDegrees(Math.atan((directionPoint.y - middleOfComponent.y)/(directionPoint.x - middleOfComponent.x)));
		//			
		//			balloon.relocateBalloon(middleOfComponent);
		//			this.caption = caption;
		//			this.location = location;
		//			startAnimation();
		//			
		//		} else {
		if(balloonComponent != focus) 
			deRegisterListeners();

		this.focus = balloonComponent;
		this.caption = caption;
		registerListeners();

//		Point convertedComponentLocation = Math2d.convertLocation(balloonComponent, this);
//		this.middleOfComponent = new Point((int)convertedComponentLocation.getX()+balloonComponent.getWidth()/2, (int)convertedComponentLocation.getY() + balloonComponent.getHeight()/2);
//		this.directionPoint = new Point(this.getWidth() - middleOfComponent.x, this.getHeight() - middleOfComponent.y);
//		double tailAngle = Math.toDegrees(Math.atan((directionPoint.y - middleOfComponent.y)/(directionPoint.x - middleOfComponent.x)));
//
//		showBalloon(caption, middleOfComponent, 135);
		//		}
		
		
		showBalloonAtLocation(true);
		
		invalidate();
		repaint();
		getParent().validate();
		getParent().repaint();
		
	}
	
	Rectangle lastBounds;
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		Rectangle newBounds = new Rectangle(x, y, width, height);
		boolean resized = lastBounds==null || !newBounds.equals(getBounds());
		super.setBounds(x, y, width, height);
		if(resized && focus!=null){
//			showBalloon(caption, focus);
			showBalloonAtLocation(false);
		}
		lastBounds = newBounds;
		
		balloonOverlay.setSize(width, height);
		if(content!=null)
			content.setSize(width, height);
		repaint();
	}
	
	private boolean equals(Rectangle a, Rectangle b){
		return a.x == b.x && a.y == b.y && a.width == b.width && a.height == b.height;
	}
	
	private void timerAction() {
		
	}
	
	class BalloonAnimation implements ActionListener {
		final Timer timer ;
		public BalloonAnimation() {
			timer = new Timer(3, this);
		}
		public void actionPerformed(ActionEvent e) {
			if((balloon==null || balloon.isDoneGrowing())) {
				if(timer != null) stop();
			} else {
				balloon.advanceFrame();
				repaint();
			}
		}
		
		public void start(){
			timer.start();
		}
		
		public void stop(){
			timer.stop();
		}
	}
	
	private void startAnimation() {
		
		new BalloonAnimation().start();
		
	}

	class BalloonOverlay extends Component{
		public BalloonOverlay() {
			setOpaque(false);
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);	
			//		balloon.draw(g, location);

			if(balloon!=null) balloon.draw(g);
			//			g.setColor(Color.green);
			//		g.drawLine(middleOfComponent.x, middleOfComponent.y, directionPoint.x, directionPoint.y);
		}
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
}
