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
package com.moss.effectpanels.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.moss.swing.test.TestFrame;

public class JPanelPainter {
	static JLabel c;
	public static void main(String[] args) throws Exception {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		c = new JLabel("Hello World") {
		};
		c.setBorder(BorderFactory.createLineBorder(Color.red));
		
		panel.add(c);
//		c.validate();
//		c.doLayout();
//		panel.doLayout();
//		c.setBounds(0,0,100,100);
//		c.setLocation(0,0);
//		c.setSize(100,100);
//		System.out.println("c's bounds: "+c.getBounds());
		panel.add(new JTextField("Hi"), BorderLayout.SOUTH);
		panel.setBackground(Color.PINK);
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		System.out.println(c.getBounds());
//		TestFrame tempFrame = new TestFrame(panel);
//		tempFrame.setVisible(false);
		System.out.println("JLabel is showing after adding it's panel to JFrame: "+c.isShowing());
		panel.setDoubleBuffered(false);
		System.out.println("panel rootPane: "+panel.getRootPane());
		Image image = paint(panel, 300, 300);
		TestFrame testFrame = new TestFrame(new JLabel(new ImageIcon(image)));

	}
	
	static Image paint(Component panel, int width, int height){
		double scale = 1;
		
		// Create something to draw on
	    BufferedImage i = new BufferedImage((int)(width*scale), (int)(height*scale), BufferedImage.TYPE_INT_ARGB_PRE);
	    Graphics2D g = i.createGraphics();
	    
		g.scale(scale, scale);
	    // Prepare the panel for drawing
		panel.setSize(width, height);
		panel.doLayout();
	
		System.out.println("c.getBounds: "+c.getBounds());
		System.out.println("c.getSize: "+c.getSize());
		System.out.println("c.getLocation: "+c.getLocation());
		// Draw it
		panel.validate();
		c.validate();
		c.setVisible(true);
		c.show();
		System.out.println("JLabel is showing: "+c.isShowing());
		
		
		panel.paint(g);
//		panel.paintAll(g);
//		return g.getDeviceConfiguration().createCompatibleImage(width, height);
		return i;
	}
}
