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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ThrobberPanel extends JPanel {
	
	Image throbberImage;
//	JLabel imageLabel;
	JPanel content;
	double imageScale = 1.0;
	double imageAlpha = .3;
	boolean imageIsShowing = false;
	int width;
	int height;
	double lastFrameTick = 0;
	double millisSinceLastFrame = 0;
	private Timer timer;

	public ThrobberPanel(JPanel content) {
		super();
		this.setOpaque(false);
		this.content = content;
		this.setLayout(null);
		
		this.throbberImage = getImage("/sun-spin-throbber.gif");
		timer = new Timer(100, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		add(content);
		hideThrobber();
	}
	
	public Image getImage(String imageName) {
		return (new ImageIcon(getClass().getResource(imageName))).getImage();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		/**
		 * Paint the image 
		 */
		if(imageIsShowing && throbberImage != null) {
			Graphics2D graphics = (Graphics2D)g;

			double locX = getWidth()/2 - imageScale * throbberImage.getWidth(this) / 2;
			double locY = getHeight()/2 - imageScale * throbberImage.getHeight(this) / 2; 
			
			graphics.translate(locX, locY);
			graphics.scale(imageScale, imageScale);
			
			int type = AlphaComposite.SRC_OVER; 
			AlphaComposite composite = AlphaComposite.getInstance(type, (float)imageAlpha);
			graphics.setComposite(composite);
			graphics.drawImage(throbberImage, 0, 0, null);
		} 
	}
	
	@Override
	public void reshape(int x, int y, int w, int h) {
		super.reshape(x, y, w, h);
		content.reshape(x, y, w, h);
	}
	
	public void showThrobber(){
		imageIsShowing = true;
		timer.start();
		setVisible(true);
		repaint();
	} 
	
	public void hideThrobber() {
		imageIsShowing = false;
		timer.stop();
		repaint();
	}
	
	public boolean isImageIsShowing() {
		return imageIsShowing;
	}

	public void setImageIsShowing(boolean imageIsShowing) {
		this.imageIsShowing = imageIsShowing;
	}
}
