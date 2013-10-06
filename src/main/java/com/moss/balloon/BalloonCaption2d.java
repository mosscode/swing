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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import com.moss.balloon.math.Ellipse2d;
import com.moss.balloon.math.Math2d;

public class BalloonCaption2d {
	private Ellipse2d ellipse;
	private JLabel captionLabel;
	private String caption = "Test Message";
	
	// tailAngle counter-clockwise from 0 degrees from center of component that's showing this balloon.
	private double tailAngleFromCenterOfComponent = -45;
	private double tailLength = 30;
	private Point tipLocation;
	private Point centerOfComponent;
	private double componentWidth;
	private double componentHeight;
	private Font font;
	
	private double growingWidth=0.0;
	private double growingHeight=0.0;
	private double growingTailLength = 0.0;
	private double finalTailLength = tailLength;
	private double ovalWidth = 0.0;
	private double ovalHeight = 0.0;
	private double growingFontSize = 0.0;
	private double fontSize=15;
	
	private double totalGrowthTimeInMillis = 150;
	private double lastFrameAdvanceTime=0.0;
	private double totalFrameAdvanceTime=0.0;
	
	private double windowWidth = 0.0;
	private double windowHeight = 0.0;
	private Rectangle2D fontBounds;
	
	public BalloonCaption2d(String caption, Point centerOfComponent, double componentWidth, double componentHeight, double windowWidth, double windowHeight) {
		this(caption, centerOfComponent, componentWidth, componentHeight, null, windowWidth, windowHeight);
	}
	public BalloonCaption2d(String caption, Point centerOfComponent,double componentWidth, double componentHeight, Font font, double windowWidth, double windowHeight) {
		this.centerOfComponent = centerOfComponent;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.captionLabel = new JLabel(caption);
		this.caption = caption;
		this.tipLocation = Math2d.findRectangleIntersection(centerOfComponent, componentWidth, componentHeight, tailAngleFromCenterOfComponent);
		this.componentWidth = componentWidth;
		this.componentHeight = componentHeight;
		
		this.font = font;
		if(font==null){			
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font[] fonts = ge.getAllFonts();
			
			// ATTEMPT TO FIND THE FIRST SANS-SERIF FONT LISTED
			for(int i=0; i<fonts.length; i++) {
				if(fonts[i].getFamily().equals("SansSerif")) {
					this.font = fonts[i];
				}
			}
			
			if(this.font == null) throw new NullPointerException("No default sans-serif fonts!");
			
			if(this.font == null) { 
				GraphicsEnvironment gEnv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
				this.font = gEnv.getAllFonts()[0].deriveFont(14f);
				if(this.font==null)
					throw new NullPointerException("No fonts!");
			}
		}
		ovalWidth = 200;
		ovalHeight = 100;
		createBalloon(tipLocation, ovalWidth, ovalHeight, tailLength);
	}
	
	public void advanceFrame() {
		double changedMillis = 0.0;
		double percentComplete = totalFrameAdvanceTime/totalGrowthTimeInMillis;

		if(lastFrameAdvanceTime > 0) {
			changedMillis = System.currentTimeMillis() - lastFrameAdvanceTime;
		}

		if(growingHeight < ovalHeight) {
			growingHeight = (ovalHeight*percentComplete);
			if(growingHeight > ovalHeight) 
				ellipse.setHeight(ovalHeight);
			else
				ellipse.setHeight(growingHeight);
		}

		if(growingWidth < ovalWidth) {
			growingWidth = (ovalWidth*percentComplete);
			if(growingWidth > ovalWidth) 
				ellipse.setWidth(ovalWidth);
			else
				ellipse.setWidth(growingWidth);
		}
		if(growingFontSize < fontSize) {
			growingFontSize = (fontSize*percentComplete);
			GraphicsEnvironment gEnv = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
			if(growingFontSize > fontSize) 
				this.font = font.deriveFont((float)fontSize);
			else
				this.font = font.deriveFont((float)growingFontSize);
		}

		if(growingTailLength < finalTailLength) {
			growingTailLength = (finalTailLength*percentComplete);
			if(growingTailLength > finalTailLength) 
				tailLength = finalTailLength;
			else 
				tailLength = growingTailLength;
		}
			lastFrameAdvanceTime = System.currentTimeMillis();
			totalFrameAdvanceTime+=changedMillis;
//			System.out.println("isDoneGrowing: "+isDoneGrowing());
	}
	
	public boolean isDoneGrowing() {
		return ((growingHeight >= ovalHeight) && (growingWidth >= ovalWidth)
				&& (growingFontSize >= fontSize) && (growingTailLength >= finalTailLength));
	}
	
	public void createBalloon(Point tailTip, double ovalWidth, double ovalHeight, double tailLength) {	

		// Translate the center out by the length and angle of the tail
		double centerX = tailTip.getX() + (tailLength)*Math.cos(Math.toRadians(tailAngleFromCenterOfComponent));
		double centerY = tailTip.getY() + (tailLength)*Math.sin(Math.toRadians(tailAngleFromCenterOfComponent));

		// Translate the center from the tail to the oval's intersection with the tail
		centerX = centerX + (ovalWidth/2.0)*Math.cos(Math.toRadians(tailAngleFromCenterOfComponent));
		centerY = centerY + (ovalHeight/2.0)*Math.sin(Math.toRadians(tailAngleFromCenterOfComponent));		

		Point ellipseCenter = new Point((int)centerX, (int)centerY);
		this.ellipse = new Ellipse2d(ellipseCenter, ovalWidth, ovalHeight);
		tipLocation = tailTip;

		// Check to see if balloon fits on-screen
		if(windowWidth > 0.0 && ellipseCenter.x + ovalWidth/2.0 > windowWidth) {
			// rotate away from window bounds
			rotateBalloon(-90);
		} else if(windowWidth > 0.0 && ellipseCenter.x - ovalWidth/2.0 < 0) {
			rotateBalloon(90);
		}
	}
	
	private void rotateBalloon(double rotateAngle) {
		this.tailAngleFromCenterOfComponent += rotateAngle;
		this.tipLocation = Math2d.findRectangleIntersection(centerOfComponent, componentWidth, componentHeight, tailAngleFromCenterOfComponent);
		// Translate the center out by the length and angle of the tail
		double centerX = tipLocation.getX() + (tailLength)*Math.cos(Math.toRadians(tailAngleFromCenterOfComponent));
		double centerY = tipLocation.getY() + (tailLength)*Math.sin(Math.toRadians(tailAngleFromCenterOfComponent));

		// Translate the center from the tail to the oval's intersection with the tail
		centerX = centerX + (ovalWidth/2.0)*Math.cos(Math.toRadians(tailAngleFromCenterOfComponent));
		centerY = centerY + (ovalHeight/2.0)*Math.sin(Math.toRadians(tailAngleFromCenterOfComponent));		

		Point ellipseCenter = new Point((int)centerX, (int)centerY);

		this.ellipse = new Ellipse2d(ellipseCenter, ovalWidth, ovalHeight);
	}
	
	public void relocateBalloon(Point centerOfComponent, double componentWidth, double componentHeight, double windowWidth, double windowHeight){
		this.centerOfComponent = centerOfComponent;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
		Point newIntersection = Math2d.findRectangleIntersection(centerOfComponent, componentWidth, componentHeight, tailAngleFromCenterOfComponent); 
		int dX = tipLocation.x - newIntersection.x;
		int dY = tipLocation.y - newIntersection.y;
		
		Point oldCenter = ellipse.getCenter();
		Point newCenter = new Point((int)oldCenter.x + dX, (int)oldCenter.y + dY);
		
		// Apply the Changes
		this.ellipse.setCenter(newCenter);
		this.tipLocation = newIntersection;
	}
	
	public void draw(Graphics g) {
		g.setFont(font);
		fontBounds = font.getStringBounds(caption, ((Graphics2D)g).getFontRenderContext());
		this.ovalWidth = fontBounds.getWidth() + 50;
		this.ovalHeight = fontBounds.getHeight() + 50;
		
		createBalloon(tipLocation, ovalWidth, ovalHeight, tailLength);
		advanceFrame();
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		((Graphics2D)g).setStroke(new java.awt.BasicStroke(2));
		g.setColor(Color.WHITE);
		fillOval(ellipse.getCenter(),(int)ellipse.getWidth(),(int)ellipse.getHeight(), g);
		g.setColor(Color.BLACK);
		drawOval(ellipse.getCenter(), (int)ellipse.getWidth(),(int)ellipse.getHeight(), g);
		
		drawBalloonTail(tailLength, tailAngleFromCenterOfComponent, g);
		
		drawString(caption, (int)fontBounds.getWidth(), (int)fontBounds.getHeight(), ellipse.getCenter(), g);	
		
		
		/**
		 * Testing
		 */
//		g.setColor(Color.green);
//		g.drawLine(tipLocation.x, tipLocation.y, ellipse.getCenter().x, ellipse.getCenter().y);
//		double tipToCenterAngle = Math2d.calculateAngleInDegrees(tipLocation, ellipse.getCenter());
//		double centerToTipAngle = Math2d.calculateAngleInDegrees(ellipse.getCenter(), tipLocation);
//		System.out.println("tipToCenterAngle: "+tipToCenterAngle);
//		System.out.println("centerToTipAngle: "+centerToTipAngle);
//		
//		g.setColor(Color.red);
//		Point intersect = ellipse.getIntersection(centerToTipAngle);
//		g.drawLine(intersect.x, intersect.y, ellipse.getCenter().x, ellipse.getCenter().y);
//		
//		g.setColor(Color.cyan);
//		int newX = (int)(tipLocation.x+tailLength*Math.cos(Math.toRadians(tipToCenterAngle)));
//		int newY = (int)(tipLocation.y+tailLength*Math.sin(Math.toRadians(tipToCenterAngle)));
//		Point intersect2 = new Point(newX, newY);
//		g.drawLine(tipLocation.x,  tipLocation.y, intersect2.x, intersect2.y);
//		double centerToIntersectionAngle = Math2d.calculateAngleInDegrees(ellipse.getCenter(), intersect2);
// 		System.out.println("tipToIntersectionAngle: "+centerToIntersectionAngle);
// 		
// 		g.setColor(Color.red);
// 		Point tipToOvalIntersection = ellipse.getIntersection(centerToIntersectionAngle);
// 		double xPart = ellipse.getWidth()/2.0*Math.cos(Math.toRadians(centerToIntersectionAngle));
// 		double yPart = ellipse.getHeight()/2.0*Math.sin(Math.toRadians(centerToIntersectionAngle));
// 		double distance = Math.sqrt(xPart*xPart + yPart*yPart);
// 		double xNew = ellipse.getCenter().x + distance*Math.cos(Math.toRadians(centerToIntersectionAngle));
// 		double yNew = ellipse.getCenter().y + distance*Math.sin(Math.toRadians(centerToIntersectionAngle));
// 		Point newIntersection = new Point((int)xNew, (int)yNew);
// 		g.drawOval(newIntersection.x-4, newIntersection.y-4, 8, 8);	
		
//		g.setColor(Color.red);
//		g.drawOval(centerOfComponent.x - 4, centerOfComponent.y - 4, 8,8);
//		g.drawOval(tipLocation.x - 4, tipLocation.y - 4, 8, 8);
	}
	
	private void drawString(String caption, int fontWidth, int fontHeight, Point center, Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString(caption, (int)(center.getX() - fontWidth/2), (int)(center.getLocation().getY() + fontHeight/2));
	}
	
	private void drawOval(Point center, int width, int height, Graphics g) {
		g.drawOval((int)center.getX()-width/2, (int)center.getY()-height/2, width, height);
	}
	
	private void fillOval(Point center, int width, int height, Graphics g) {
		g.fillOval((int)center.getX()-width/2, (int)center.getY()-height/2, width, height);
	}
	
	private void drawBalloonTail(double tailLength, double tailAngleFromTailTip, Graphics g) {
				
		double tailAngleFromCenterOfBalloon = tailAngleFromTailTip - 180;
		
		Point leftBase = ellipse.getIntersection(tailAngleFromCenterOfBalloon+7);
		Point rightBase = ellipse.getIntersection(tailAngleFromCenterOfBalloon-7);

		Point tailEnd = tipLocation;
		
		int[] polyXPoints = {(int)leftBase.getX(), (int)rightBase.getX(), (int)tailEnd.getX()};
		int[] polyYPoints = {(int)leftBase.getY(), (int)rightBase.getY(), (int)tailEnd.getY()};
		Polygon polygon = new Polygon(polyXPoints, polyYPoints, 3);
		g.setColor(Color.white);
		g.fillPolygon(polygon);
		((Graphics2D)g).setStroke(new java.awt.BasicStroke(4));
		g.drawLine((int)leftBase.getX(), (int)leftBase.getY(), (int)rightBase.getX(), (int)rightBase.getY());
		((Graphics2D)g).setStroke(new java.awt.BasicStroke(2));

		g.setColor(Color.black);
		g.drawLine((int)tailEnd.getX(), (int)tailEnd.getY(), (int)leftBase.getX(), (int)leftBase.getY());
		g.drawLine((int)tailEnd.getX(), (int)tailEnd.getY(), (int)rightBase.getX(), (int)rightBase.getY());
	}
		
	public Ellipse2d getEllipse() {
		return ellipse;
	}
	public void setEllipse(Ellipse2d ellipse) {
		this.ellipse = ellipse;
	}
	public JLabel getCaptionLabel() {
		return captionLabel;
	}
	public void setCaptionLabel(JLabel captionLabel) {
		this.captionLabel = captionLabel;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public double getTailAngleFromCenterOfComponent() {
		return tailAngleFromCenterOfComponent;
	}

	public void setTailAngleFromCenterOfComponent(double tailAngle) {
		this.tailAngleFromCenterOfComponent = tailAngle;
	}
}
