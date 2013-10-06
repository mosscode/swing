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

import javax.swing.JPanel;

public class FadingDialogPanel extends BufferedImagePanel {
	
	private double finalImageOpacity = 1.0;
	
	private boolean isDoneFading = true;
	
//	private double finalWidth;
//	private double finalHeight;
	
	private FadeStatus fadeStatus = FadeStatus.FADE_OFF; 
	
	public enum FadeStatus { 
		FADE_OUT, 
		FADE_IN, 
		FADE_OFF;
	}
	
	public FadingDialogPanel(JPanel content) {
		super(content);

//		this.finalWidth = content.getWidth();
//		this.finalHeight = content.getHeight();
//		
		setTransform(new Transform(0, 1));
		this.totalAnimationTime = 2000;
		content.setVisible(true);
		setOpaque(false);
		content.setOpaque(false);
		
//		setDialogSize(finalSize);

	}
	
//	public FadingDialogPanel(JPanel content, Dimension finalSize, Point finalLocation) {
//		this(content, finalSize);
////		this.finalDialogLocation = finalLocation;
//
//	}
	
	public enum ExpansionStatus {
		EXPANDING,
		SHRINKING, 
		OFF;
	}

	@Override
	public boolean isDoneAnimating() {
		return isDoneFading;
	}
	
	@Override
	public void advanceFrame() {
		super.advanceFrame();
		
		double imageAlpha = getTransform().alpha();
		
		if(this.fadeStatus == FadeStatus.FADE_OUT) {
			if(imageAlpha < this.finalImageOpacity) {

				imageAlpha += frameAdvancePercentage*this.finalImageOpacity;
				if(imageAlpha > this.finalImageOpacity) 
					imageAlpha = this.finalImageOpacity;
			}

			if(imageAlpha >= this.finalImageOpacity) {
				this.isDoneFading=true;
			}
			
		} else if(this.fadeStatus == FadeStatus.FADE_IN) {
			if(imageAlpha > 0) {

				imageAlpha -= frameAdvancePercentage*this.finalImageOpacity;
				if(imageAlpha < 0) 
					imageAlpha = 0;
			}

			if(imageAlpha <= 0) {
				this.isDoneFading=true;
			}
		}
		
		setTransform(getTransform().withAlpha(imageAlpha));
		super.repaint();
	}
	
	public void fadeIn() {
		takeSnapshot();
		showSnapshot();
		this.isDoneFading = false;
		setTransform(new Transform(0, 1));
//		this.imageScale = 1;
//		this.imageAlpha = 0;
		this.fadeStatus = FadeStatus.FADE_IN;
		animate(null);
		repaint();
	}
	
	public void fadeOut() {
		showContent();
//		content.setVisible(true);
		this.isDoneFading = false;
		setTransform(new Transform(1, 1));
//		this.imageScale = 1;
//		this.imageAlpha = 1;
		this.fadeStatus = FadeStatus.FADE_OUT;
		animate(null);
		repaint();
	}
	
//	public void setDialogSize(int width, int height) {
//		this.finalWidth = width;
//		this.finalHeight = height;
//	}
//	
//	public void setDialogSize(Dimension size) {
//		this.finalWidth = size.getWidth();
//		this.finalHeight = size.getHeight();
//		
//		super.finalContentWidth = (int)finalWidth;
//		super.finalContentHeight = (int)finalHeight;
//	}
}
