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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.moss.effectpanels.ExpandingDialogPanel;

public class ExpandingDialogPanelDemo {
	
	private static ExpandingDialogPanel expandingDialogPanel;
	
	private static JButton fadeOutButton;
	private static JButton fadeInButton;
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setLayout(new BorderLayout());
		
		window.getContentPane().setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		
		fadeOutButton = new JButton("Expand!");
		fadeOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				expandAction();
			}
		});
		controlPanel.add(fadeOutButton);

		fadeInButton = new JButton("Contract!");
		fadeInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				contractAction();
			}
		});
		controlPanel.add(fadeInButton);
	
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		
		DemoPanel demoPanel = new DemoPanel();
		expandingDialogPanel = new ExpandingDialogPanel(demoPanel, new Dimension(400,200), new Point(100,100));
//		contentPanel.add(freezePanel, BorderLayout.CENTER);
		
		mainPanel.add(expandingDialogPanel, BorderLayout.CENTER);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		window.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		window.setSize(500,500);
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	private static void expandAction() {
		expandingDialogPanel.expand();
	}
	
	private static void contractAction() {
		expandingDialogPanel.shrink();
	}
}