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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class FadingPanel extends AnimatedPanel {
	
	public double currentOpacityLevel;
	public double finalOpacityLevel = .8;
	public boolean fadeOut = true;
	
	public Color fadeColor = Color.black;
	
	private JPanel content;
	
	public boolean isDoneFading = false;
	
	private Runnable finalFadeOutAction;
	private Runnable finalFadeInAction;
	
	private FadeStatus fadeStatus = FadeStatus.FADE_OFF;
	public FadeStatus getFadeStatus() {
		return fadeStatus;
	}

	public void setFadeStatus(FadeStatus fadeStatus) {
		this.fadeStatus = fadeStatus;
	}
	
	public FadingPanel(JPanel content, Runnable finalFadeOutAction, Runnable finalFadeInAction) {
		this(content);
		this.finalFadeOutAction = finalFadeOutAction;
		this.finalFadeInAction = finalFadeInAction;
	}
	
	public FadingPanel(JPanel content) {
		setOpaque(false);
		super.totalAnimationTime = 500;
		this.content = content;
		setLayout(new BorderLayout());
		add(content, BorderLayout.CENTER);
		fadeIn();
	}
	
	public enum FadeStatus { 
		FADE_OUT, 
		FADE_IN, 
		FADE_OFF;
	}

	public boolean isDoneAnimating() {
		return isDoneFading;
	}
	
	public void advanceFrame() {

		if(this.fadeStatus == FadeStatus.FADE_OUT) {
			
			if(this.currentOpacityLevel < this.finalOpacityLevel) {

				this.currentOpacityLevel += frameAdvancePercentage*this.finalOpacityLevel;
				if(this.currentOpacityLevel > this.finalOpacityLevel) 
					this.currentOpacityLevel = this.finalOpacityLevel;
			}

			if(this.currentOpacityLevel >= this.finalOpacityLevel) {
				this.isDoneFading=true;
			}
			
		} else if(this.fadeStatus == FadeStatus.FADE_IN) {
			if(this.currentOpacityLevel > 0) {

				this.currentOpacityLevel -= frameAdvancePercentage*this.finalOpacityLevel;
				if(this.currentOpacityLevel < 0) 
					this.currentOpacityLevel = 0;
			}

			if(this.currentOpacityLevel <= 0) {
				this.isDoneFading=true;
			}
		}
		
		super.repaint();
	}
	
	public void fadeIn() {
		this.lastFrameTime = 0;
		this.currentOpacityLevel = finalOpacityLevel;
		this.isDoneFading = false;
		this.fadeStatus = FadeStatus.FADE_IN;
		animate(finalFadeInAction);
	}
	
	public void fadeOut() {
		this.lastFrameTime = 0;
		this.currentOpacityLevel = 0;
		this.isDoneFading= false;
		this.fadeStatus = FadeStatus.FADE_OUT;
		animate(finalFadeOutAction);
	}
	
	@Override
	public void paint(Graphics g) {

		super.paint(g);
		try {
			// Graphics2D g = (Graphics2D)this.getRootPane().getGraphics();
			Graphics2D graphics = (Graphics2D)g;
			AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)currentOpacityLevel);
			graphics.setComposite(alphaComposite);
			graphics.setColor(fadeColor);
			graphics.fillRect(0, 0, this.getWidth(), this.getHeight());		

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}