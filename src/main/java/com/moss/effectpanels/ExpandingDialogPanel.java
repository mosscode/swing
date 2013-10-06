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

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPanel;

public class ExpandingDialogPanel extends BufferedImagePanel {
	
	
	private ExpansionStatus expansionStatus = ExpansionStatus.OFF;
	private double finalImageScale = 1.0;
	private boolean isDoneGrowing = true;
	
	private double finalWidth;
	private double finalHeight;
	
	private Runnable finalExpandAction = null;
	private Runnable finalShrinkAction = null;
	
	public ExpandingDialogPanel(JPanel content, Dimension finalSize, Runnable finalExpandAction, Runnable finalShrinkAction) {
		this(content, finalSize);
		this.finalExpandAction = finalExpandAction;
		this.finalShrinkAction = finalShrinkAction;
	}
	
	public ExpandingDialogPanel(JPanel content, Dimension finalSize) {
		super(content);

		this.finalWidth = finalSize.getWidth();
		this.finalHeight = finalSize.getHeight();
		
		this.totalAnimationTime = 250;
		content.setVisible(true);
		setOpaque(false);
//		content.setOpaque(false);
		
		setDialogSize(finalSize);
//		turnOff();

	}
	
	public ExpandingDialogPanel(JPanel content, Dimension finalSize, Point finalLocation) {
		this(content, finalSize);
	}
	
	public enum ExpansionStatus {
		EXPANDING,
		SHRINKING, 
		OFF;
	}

	@Override
	public boolean isDoneAnimating() {
		return isDoneGrowing;
	}
	
	@Override
	public void advanceFrame() {
		double imageScale = getTransform().scale();
		if(this.expansionStatus == ExpansionStatus.EXPANDING) {
			showSnapshot();
			if(imageScale < this.finalImageScale) {
				imageScale += frameAdvancePercentage*this.finalImageScale;
				if(imageScale > this.finalImageScale) 
					imageScale = this.finalImageScale;
			}

			if(imageScale >= this.finalImageScale) {
				this.isDoneGrowing=true;
				showContent();
			}
			
		} else if(this.expansionStatus == ExpansionStatus.SHRINKING) {

			showSnapshot();
			if(imageScale > 0) {

				imageScale -= frameAdvancePercentage*this.finalImageScale;
				if(imageScale < 0) 
					imageScale = 0;
			}

			if(imageScale <= 0) {
				this.expansionStatus = ExpansionStatus.OFF;
				this.isDoneGrowing=true;
			}
		}
		setTransform(new Transform(imageScale));
	}
	
	public void expand() {
		takeSnapshot();
		showSnapshot();
		this.isDoneGrowing = false;
		setTransform(new Transform(0));
		this.expansionStatus = ExpansionStatus.EXPANDING;
		animate(finalExpandAction);
		repaint();
	}
	
	public void turnOff() {
		showContent();
		isDoneGrowing=false;
		setTransform(new Transform(0));
		this.expansionStatus = ExpansionStatus.SHRINKING;
		animate(null);
		repaint();
	}
	
	public void shrink() {
		takeSnapshot();
		showContent();
		this.isDoneGrowing = false;
		setTransform(new Transform(1));
		this.expansionStatus = ExpansionStatus.SHRINKING;
		animate(finalShrinkAction);
		repaint();
	}
	
	public void setDialogSize(int width, int height) {
		this.finalWidth = width;
		this.finalHeight = height;
		setContentSize((int)finalWidth, (int)finalHeight);
	}
	
	public void setDialogSize(Dimension size) {
		setDialogSize(size.width, size.height);
	}
	
	public ExpansionStatus expansionStatus() {
		return expansionStatus;
	}

}
