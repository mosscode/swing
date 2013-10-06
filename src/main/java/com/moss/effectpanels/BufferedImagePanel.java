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
package com.moss.effectpanels;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BufferedImagePanel extends AnimatedPanel {
	
	private float blurX = 1.0f;
	private float blurY = 1.0f;
	private float blurZ = 1.0f;
	
	public static class Transform {
		private double scale=1.0;
		private double alpha=1.0;
		
		public Transform() {
		}
		public Transform(double scale) {
			super();
			this.scale = scale;
		}

		public Transform(double alpha, double scale) {
			super();
			this.alpha = alpha;
			this.scale = scale;
		}
		
		public double scale() {
			return scale;
		}
		public double alpha() {
			return alpha;
		}
		
		public Transform withAlpha(double alpha){
			return new Transform(alpha, scale);
		}
	}
	
	enum LayoutMode{FULL, RESTRICTED}
	
	private LayoutMode layoutMode = LayoutMode.FULL;
	private BufferedImage bImage;
	private Component content;
	
	private int finalContentWidth=-1;
	private int finalContentHeight=-1;
	
	private Transform transform = new Transform();
	private boolean imageIsShowing = true;

	public BufferedImagePanel(Component content) {
		this.content = content;
		
		this.setOpaque(false);
		setLayout(null);
		content.setSize(finalContentWidth, finalContentHeight);
		add(content);
	}
	
	public void setContentSize(int width, int height){
		layoutMode = LayoutMode.RESTRICTED;
		finalContentWidth = width;
		finalContentHeight = height;
		layoutContent();
	}
	
	public void showContent(){
			imageIsShowing = false;
			layoutContent();
			content.setVisible(true);
	}
	public void showSnapshot(){
			imageIsShowing = true;
			content.setVisible(false);
	}
	
	private void layoutContent(){
//		System.out.println("Width = " + getWidth() + ", content width = " + finalContentWidth);
		switch(layoutMode){
		case FULL:
			content.setBounds(getBounds());
			break;
		case RESTRICTED:
			content.setBounds(getWidth()/2 - finalContentWidth/2, getHeight()/2-finalContentHeight/2, finalContentWidth, finalContentHeight);
			break;
		}
	}
	
	public void advanceFrame() {

	}
	
	public boolean isDoneAnimating() {
		return false;
	}
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		/**
		 * Paint the image 
		 */
		if(imageIsShowing && bImage!=null) {
			
			Graphics2D graphics = (Graphics2D)g;

			/**
			 * TODO ? 
			 * USE RESCALEOP TO FADE IMAGE 
			 * ADD OTHER FILTER POSSIBILITIES
			 */
			
			double locX = getWidth()/2 - transform.scale * bImage.getWidth() / 2;
			double locY = getHeight()/2 - transform.scale * bImage.getHeight() / 2; 
			
			graphics.translate(locX, locY);
			graphics.scale(transform.scale, transform.scale);
			
			int type = AlphaComposite.SRC_OVER; 
			AlphaComposite composite = AlphaComposite.getInstance(type, (float)transform.alpha);
			graphics.setComposite(composite);
			graphics.drawImage(bImage, null, 0, 0);
		} 
		
	}
	
	public void takeSnapshot(){
		int width; 
		int height;
		
		switch(layoutMode){
		case FULL:
			width = getWidth();
			height = getHeight();
			break;
		case RESTRICTED:
			width = finalContentWidth;
			height = finalContentHeight;
			break;
		default:
			throw new RuntimeException("Unsupported layout mode: " + layoutMode);
		}
		
		
		if(width>0 & height>0){
//			System.out.println("Taking component snapshot on " + content);
//			Thread.dumpStack();
			// Create something to draw on
			boolean wasVisible = content.isVisible();
			content.setVisible(true);
			content.setSize(width, height);
			content.doLayout();
			content.validate();
			bImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
			Graphics2D g = bImage.createGraphics();
			content.paint(g);
			
			content.setVisible(wasVisible);
			
//			System.out.println("Done taking component snapshot");
		}else{
//			System.out.println("Too small to take snapshot.");
		}
	}
	
	@Override
	public void reshape(int x, int y, int w, int h) {
		super.reshape(x, y, w, h);
		layoutContent();
	}
	
	public Transform getTransform(){
		return transform;
	}
	
	public void setTransform(Transform t){
		this.transform = t;
	}
	
	public boolean snapshotIsShowing() {
		return imageIsShowing;
	}
}