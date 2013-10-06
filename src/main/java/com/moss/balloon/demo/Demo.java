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
package com.moss.balloon.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.moss.balloon.BalloonPanel;
import com.moss.swing.CrossPlatformScrollPane;
import com.moss.swing.birdviewer.BirdView;

public class Demo {
	public static void main(String[] args) {
		new Demo();
	}
	
	
	private Component balloonComponent;
	private DemoView view = new DemoView();
	private JFrame window;
	
	private BalloonPanel balloonPanel = new BalloonPanel();
	
	public Demo(){
		window = new JFrame();
		window.setSize(800,600);
		
		window.getContentPane().setLayout(new BorderLayout());
		balloonComponent = view.getFirstFieldTextTextField();
		view.getFirstFieldTextTextField().addFocusListener(new BalloonActionListener(view.getFirstFieldTextTextField()));
		view.getSecondFieldTextTextField().addFocusListener(new BalloonActionListener(view.getSecondFieldTextTextField()));
		view.getThirdFieldTextTextField().addFocusListener(new BalloonActionListener(view.getThirdFieldTextTextField()));
		window.addMouseListener(new MousePointListener());
		
		balloonPanel.setContent(view);
		
		balloonPanel.setMinimumSize(new Dimension(500, 500));
		balloonPanel.setPreferredSize(new Dimension(500, 500));
		
		balloonPanel.setDoubleBuffered(true);
		
		
		boolean showInScrollPane = true;
		
		JComponent content;
		JScrollPane scrollPane = null;
		
		if(showInScrollPane){
			scrollPane = new CrossPlatformScrollPane(balloonPanel);
			scrollPane.setBorder(null);
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			
			content = scrollPane;
		}else{
			content = balloonPanel;
		}
		
		
		
		
//		window.getContentPane().add(birdView, BorderLayout.WEST);
		window.getContentPane().add(content, BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		

		BirdView birdView = new BirdView();
		
		birdView.setSize(200, 200);
		birdView.setMinimumSize(new Dimension(200, 200));
		birdView.setLocation(0, 0);
		
		JFrame birdViewFrame = new JFrame("Bird View");
		birdViewFrame.getContentPane().add(birdView);
		birdViewFrame.setSize(200, 200);
		birdViewFrame.setVisible(true);
		
		birdView.setScrollpane(scrollPane);
		
//		Point firstTextLocation = Math2d.convertLocation(view.getFirstFieldTextTextField(), balloonPanel);
//		Point middleOfComponent = new Point((int)firstTextLocation.getX()+view.getFirstFieldTextTextField().getWidth()/2, (int)firstTextLocation.getY() + view.getFirstFieldTextTextField().getHeight()/2);
		
//		balloonPanel.showBalloon("A message on this component.", middleOfComponent, 135);
	}
	
	public void setFocus(Component component){
		System.out.println("Focusing on " + component);
		if(component==null)
			balloonPanel.hideBalloon();
		else
			balloonPanel.showBalloon("This is a " + component.getClass().getSimpleName(), component);
	}
	
	public class MousePointListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			System.out.println("Clicked at: "+e.getPoint());
			setFocus(new PointComponent(e.getPoint()));
		}
		public void mouseEntered(MouseEvent e) {};
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {};
		public void mouseReleased(MouseEvent e) {}
	}
	
	public class BalloonActionListener implements FocusListener{
		
		Component component;
		
		public BalloonActionListener(Component component) {
			super();
			this.component = component;
		}
		public void focusGained(FocusEvent e) {
			setFocus(this.component);
		}
		public void focusLost(FocusEvent e) {
			setFocus(null);
			
		}
//		public void actionPerformed(ActionEvent e) {
//			balloonComponent = this.component;
//			Point firstTextLocation = Math2d.convertLocation(balloonComponent, this);
//			Point middleOfComponent = new Point((int)firstTextLocation.getX()+balloonComponent.getWidth()/2, (int)firstTextLocation.getY() + balloonComponent.getHeight()/2);
//			balloonPanel = new BalloonPanel("New field selection", middleOfComponent);
//			balloonPanel.repaint();
//		}
	}
	
//	@Override
//	public void paint(Graphics g) {
//		super.paint(g);
//		Point firstTextLocation = Math2d.convertLocation(balloonComponent, this);
//		Point middleOfComponent = new Point((int)firstTextLocation.getX()+balloonComponent.getWidth()/2, (int)firstTextLocation.getY() + balloonComponent.getHeight()/2);
//		g.drawOval((int)middleOfComponent.getX() - 8/2, (int)middleOfComponent.getY() - 8/2, 8, 8);
//		
////		glassPane.removeAll();
//	}
}
