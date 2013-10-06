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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class LockDisplayPanel extends JPanel {

	private Image lockImage;
	
	private double imageScale = .6;

	public double imageFadeComposite = 1;
	
	public boolean isLockShowing = false;
	
	public LockDisplayPanel(JPanel content) {
		
		setOpaque(false);
		setLayout(new BorderLayout());
		this.lockImage = getImage("/lock.png");

		add(content, BorderLayout.CENTER);
	}
	
//	@Override
//	public void reshape(int x, int y, int w, int h) {
//		super.reshape(x, y, getWidth(), getHeight());
//	}

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		if(isLockShowing && lockImage != null) {
			Graphics2D graphics = (Graphics2D)g.create(0, 0, getWidth(), getHeight());
			AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)imageFadeComposite);

			double locX = getWidth() - imageScale*lockImage.getWidth(this)-10;
			double locY = 10;
			graphics.setComposite(alphaComposite);
			graphics.translate(locX, locY);
			graphics.scale(imageScale, imageScale);
			graphics.drawImage(lockImage, 0, 0, this);
			
		}
		
	}
	
	public Image getImage(String imageName) {
		return (new ImageIcon(getClass().getResource(imageName))).getImage();
	}
	
	public void showLock() {
		this.isLockShowing = true;
	}
	
	public void hideLock() {
		this.isLockShowing = false;
	}
}