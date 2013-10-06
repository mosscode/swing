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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BalloonTestPanel extends JPanel {
	private JTextField textField_5;
	private JTextField textField_4;
	private JTextField textField_3;
	private JTextField textField_2;
	private JTextField textField_1;
	private Component selectedComponent;
	private Point selectedPoint;
	private static JTextField textField;
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setLayout(new BorderLayout());
		
		BalloonTestPanel balloonPanel = new BalloonTestPanel();
		window.add(balloonPanel, BorderLayout.CENTER);
		window.setSize(600, 400);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel glass = (JPanel)window.getGlassPane();
		glass.setLayout(new BorderLayout());
		glass.setVisible(true);
		glass.add(new JPanel() {

			@Override
			public void paint(Graphics g) {
				g.setColor(Color.black);
				g.drawLine(0, 0, 300, 300);
			}
		}, BorderLayout.CENTER);
	}
	
	public BalloonTestPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		add(panel);

		final JLabel field1Label = new JLabel();
		field1Label.setText("Field 1");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(11, 5, 11, 0);
		panel.add(field1Label, gridBagConstraints);

		textField = new JTextField();
		textField.setText("Text Field 1");
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.gridx = 1;
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.insets = new Insets(5, 5, 5, 0);
		panel.add(textField, gridBagConstraints_1);
		textField.addMouseListener(new ClickyMouseListener());
		textField.addActionListener(new RepaintActionLister());

		final JLabel field2Label_1 = new JLabel();
		field2Label_1.setText("Field 2");
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.gridx = 2;
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.insets = new Insets(11, 5, 11, 0);
		panel.add(field2Label_1, gridBagConstraints_2);

		textField_1 = new JTextField();
		textField_1.setText("Text Field 2");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridx = 3;
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.insets = new Insets(5, 5, 5, 0);
		panel.add(textField_1, gridBagConstraints_3);
		textField_1.addMouseListener(new ClickyMouseListener());
		textField_1.addActionListener(new RepaintActionLister());
		
		final JLabel field3Label = new JLabel();
		field3Label.setText("Field 3");
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.gridx = 0;
		gridBagConstraints_4.gridy = 1;
		gridBagConstraints_4.insets = new Insets(11, 5, 11, 0);
		panel.add(field3Label, gridBagConstraints_4);

		textField_2 = new JTextField();
		textField_2.setText("Text Field 4");
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.gridx = 1;
		gridBagConstraints_5.gridy = 1;
		gridBagConstraints_5.insets = new Insets(5, 5, 5, 0);
		panel.add(textField_2, gridBagConstraints_5);
		textField_2.addMouseListener(new ClickyMouseListener());
		textField_2.addActionListener(new RepaintActionLister());
		
		final JLabel field2Label = new JLabel();
		field2Label.setText("Field 4");
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.gridx = 2;
		gridBagConstraints_6.gridy = 1;
		gridBagConstraints_6.insets = new Insets(11, 5, 11, 0);
		panel.add(field2Label, gridBagConstraints_6);

		textField_3 = new JTextField();
		textField_3.setText("Text Field 3");
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.gridx = 3;
		gridBagConstraints_7.gridy = 1;
		gridBagConstraints_7.insets = new Insets(5, 5, 5, 0);
		panel.add(textField_3, gridBagConstraints_7);
		textField_3.addMouseListener(new ClickyMouseListener());
		textField_3.addActionListener(new RepaintActionLister());
		
		final JLabel field5Label = new JLabel();
		field5Label.setText("Field 5");
		final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
		gridBagConstraints_8.gridx = 0;
		gridBagConstraints_8.gridy = 2;
		gridBagConstraints_8.insets = new Insets(11, 5, 11, 0);
		panel.add(field5Label, gridBagConstraints_8);

		textField_4 = new JTextField();
		textField_4.setText("Text Field 5");
		final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
		gridBagConstraints_9.gridx = 1;
		gridBagConstraints_9.gridy = 2;
		gridBagConstraints_9.insets = new Insets(5, 5, 5, 0);
		panel.add(textField_4, gridBagConstraints_9);
		textField_4.addMouseListener(new ClickyMouseListener());
		textField_4.addActionListener(new RepaintActionLister());
		
		final JLabel field6Label = new JLabel();
		field6Label.setText("Field 6");
		final GridBagConstraints gridBagConstraints_10 = new GridBagConstraints();
		gridBagConstraints_10.gridx = 2;
		gridBagConstraints_10.gridy = 2;
		gridBagConstraints_10.insets = new Insets(11, 5, 11, 0);
		panel.add(field6Label, gridBagConstraints_10);

		textField_5 = new JTextField();
		textField_5.setText("Text Field 6");
		final GridBagConstraints gridBagConstraints_11 = new GridBagConstraints();
		gridBagConstraints_11.gridx = 3;
		gridBagConstraints_11.gridy = 2;
		gridBagConstraints_11.insets = new Insets(5, 5, 5, 5);
		panel.add(textField_5, gridBagConstraints_11);
		textField_5.addMouseListener(new ClickyMouseListener());
		textField_5.addActionListener(new RepaintActionLister());
		
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("Clicked: "+"("
						+e.getPoint().getX()+", "
						+e.getPoint().getY()+")");
				selectedPoint = e.getPoint();
				repaint();

			}
			public void mouseEntered(MouseEvent e) {
				
			}
			public void mouseExited(MouseEvent e) {
				
			}
			public void mousePressed(MouseEvent e) {
				
			}
			public void mouseReleased(MouseEvent e) {
				
			}
		});
	}
	
	private class RepaintActionLister implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}
	
	private class ClickyMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			System.out.println("Mounse clicked");
			System.out.println(((JTextField)e.getComponent()).getText());
			selectedComponent = e.getComponent();
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Component[] components = this.getComponents();

		System.out.println("painting");
		if(selectedComponent != null) {
			System.out.println("selectedComponent.getLocation: "+selectedComponent.getLocation());
		}
		
		if(selectedPoint != null) paintBalloon("This is a test!", selectedPoint, g);
		if(selectedPoint != null)  getBalloonInterceptPoints(1, selectedPoint, 200, 100, g);

	}
	
	public void paintBalloon(String message, Point centerPoint, Graphics g) {
		int ovalWidth = 200;
		int ovalHeight = 100;
		Point adjustedCirclePoint = new Point((int)(centerPoint.getX()-ovalWidth/2), (int)(centerPoint.getY()-ovalHeight/2));
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		((Graphics2D)g).setStroke(new java.awt.BasicStroke(2));
		
		g.setColor(Color.WHITE);
		g.fillOval(((int)adjustedCirclePoint.getX()), ((int)adjustedCirclePoint.getY()), ovalWidth, ovalHeight);
		g.setColor(Color.BLACK);
		g.drawOval(((int)adjustedCirclePoint.getX()), ((int)adjustedCirclePoint.getY()), ovalWidth, ovalHeight);
		
		g.drawString(message, (int)centerPoint.getX() - ovalWidth/4, (int)centerPoint.getY());
		
		// TESTING
//		getBalloonInterceptPoints(1.0, centerPoint, (double)ovalWidth, (double)ovalHeight, g);
		drawBalloonTail(30, 0, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 45, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 90, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 135, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 180, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 225, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 270, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 315, centerPoint, ovalWidth, ovalHeight, g);
		drawBalloonTail(5, 360, centerPoint, ovalWidth, ovalHeight, g);
		
		// PAINT BALLOON TAIL
		
	}
	/*
	 * Given the slope of a line that passes through an oval's center with specified width and height, 
	 * this returns the two points where that line will intercept the oval.
	 */
	private Point[] getBalloonInterceptPoints(double slope, Point center, double width, double height, Graphics g) {
		
		Point centerInRealCoordinates = convertScreenCoordinateToRealCoordinate(center);
//		Point centerInRealCoordinates = center;

		// line equation: y=mx+b, where m = slope, b = y-intercept
		// solving for b ...
		double b =centerInRealCoordinates.getY() - slope*centerInRealCoordinates.getX();
		double m = slope;
		// we now have a complete equation for our line.
		
		// ellipse equation: x^2/width + y^2/height = 1
		// substituting y for mx+b so we can solve for x in this equation, and rearranging, we get ...
		// ((width*m^2+height)*x^2 + 2*width*b*m*x + (width*b^2 - width*height) = 0
		// we can solve for x by applying the quadratic equation, which is: x=(-b +/- sqrt(b^2-4*ac))/2a
		// where a=width*m^2+height, b=2*width*b*m*x, c=width*b^2 - width*height
		
		double W = width/2;
		double H = height/2;
		double h = centerInRealCoordinates.getX();
		double k = centerInRealCoordinates.getY();
		
//		double aQ = (width*m*m + height);
//		double bQ = 2*width*b*m;
//		double cQ = width*b*b - width*height;
		
//		double aQ = height+width*m*m;
//		double bQ = 2*width*b*m;
//		double cQ = width*b*b-width*height;
		
//		double aQ = H*H+W*W*m*m;
//		double bQ = 2*W*W*m*b - 2*H*H*h - 2*H*H*k*m;
//		double cQ = H*H*h*h + W*W*b*b - 2*H*H*k*b + W*W*k*k - W*W*H*H;
		
		
		double aQ = H*H + W*W*m*m;
		double bQ = W*W*(2*m*b - 2*m*k) - 2*H*H*h;
		double cQ = H*H*h*h + W*W*(b*b+k*k) - W*W*H*H;
		
		double sqrtPartOfEquation = Math.sqrt(bQ*bQ - 4*aQ*cQ);
		double x1 = (-bQ + sqrtPartOfEquation)/(2*aQ);
		double x2 = (-bQ - sqrtPartOfEquation)/(2*aQ);
		
		double y1 = m*x1 + b;
		double y2 = m*x2 + b;
		
		
		// TESTING y=mx+b line
		double startX = 0;
		double endX = getWidth();
//		double newX = centerInRealCoordinates.getX()+50;
		double startY = m*startX+b;
		double endY = m*endX+b;
		
		Point startPoint = convertRealCoordinatesToScreenCoordinates(new Point((int)startX, (int)startY));
		Point endPoint = convertRealCoordinatesToScreenCoordinates(new Point((int)endX, (int)endY));
		g.setColor(Color.blue);
		g.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)endPoint.getX(), (int)endPoint.getY());
		////////////////////////////////////////////////////////////////
		
		// TESTING 2
		double testY = m*(h + W*Math.cos(1));
		double testX = (k+b*Math.sin(1))/m;
		Point testScreenPoint = convertRealCoordinatesToScreenCoordinates(new Point((int)testX, (int)testY));
		g.drawOval((int)(testScreenPoint.getX()-50/2), (int)(testScreenPoint.getY()-50/2),50, 50);
		////////////////////////////////////////////////////////////////
		
		// TESTING 3
		// x = x0 + at
	    // y = y0 + bt
		double t = -(H*H*h + W*W*k - W*W*H*H) / (H*H*W + W*W*H);
		double testX2 = h + W*t;
		double testY2 = k + H*t;
		g.setColor(Color.black);
		Point testScreenPoint2 = convertRealCoordinatesToScreenCoordinates(new Point((int)testX, (int)testY));
		g.drawOval((int)(testScreenPoint2.getX()-50/2), (int)(testScreenPoint2.getY()-50/2),50, 50);
		
		////////////////////////////////////////////////////////////////

		
//		
		Point point1 = convertRealCoordinatesToScreenCoordinates(new Point((int)x1, (int)y1));
		Point point2 = convertRealCoordinatesToScreenCoordinates(new Point((int)x2, (int)y2));
//		Point point1 = new Point((int)x1, (int)y1);
//		Point point2 = new Point((int)x2, (int)y2);
		
		g.setColor(Color.GREEN);
		g.drawLine((int)center.getX(), (int)center.getY(),(int)point1.getX(), (int)point1.getY());
		g.setColor(Color.RED);
		g.drawLine((int)center.getX(), (int)center.getY(),(int)point2.getX(), (int)point2.getY());
		
		drawCoordinatePoint("intercept 1", new Point((int)point1.getX(),(int)point1.getY()), g);
		drawCoordinatePoint("intercept 2", new Point((int)point2.getX(),(int)point2.getY()), g);
		
		Point[] points =  {new Point((int)x1,(int)y1), new Point((int)x2,(int)y2)};
		return points;
	}
	
	private void drawBalloonTail(int tailLength, double degrees, Point centerPoint, int width, int height, Graphics g) {
		g.setColor(Color.WHITE);
		Point realCoordinateCenter = convertScreenCoordinateToRealCoordinate(centerPoint);
		System.out.println("screen x,y: "+centerPoint.getX() + ", "+centerPoint.getY());
		System.out.println("real x,y: "+realCoordinateCenter.getX() + ", " + realCoordinateCenter.getY());
		int W = width/2;
		int H = height/2;
		int x1 = (int)(realCoordinateCenter.getX() + W*Math.cos(Math.toRadians(degrees)));
		int y1 = (int)(realCoordinateCenter.getY() + H*Math.sin(Math.toRadians(degrees)));
		
		Point screenCoordinatePoint = convertRealCoordinatesToScreenCoordinates(new Point(x1,y1));
		g.drawOval((int)(screenCoordinatePoint.getX() - 20/2), (int)(screenCoordinatePoint.getY() - 20/2), 20, 20);
		g.setColor(Color.RED);
		g.drawLine((int)screenCoordinatePoint.getX(), (int)screenCoordinatePoint.getY(), (int)screenCoordinatePoint.getX(), (int)screenCoordinatePoint.getY());
		
		// STARTING NEW METHOD: 
		
//		Point bottomOvalPoint = convertRealCoordinatesToScreenCoordinates(new Point((int)realCoordinateCenter.getX(), (int)realCoordinateCenter.getY()-height/2));
//		g.drawOval((int)(bottomOvalPoint.getX() - 20/2), (int)(bottomOvalPoint.getY() - 20/2), 20, 20);
//		Point tailTipScreenCoordinates = convertRealCoordinatesToScreenCoordinates(new Point((int)realCoordinateCenter.getX(), (int)realCoordinateCenter.getY()-height/2-tailLength));
//		Point tailEndLeftScreenCoordinates = convertRealCoordinatesToScreenCoordinates(new Point((int)realCoordinateCenter.getX()-5, (int)realCoordinateCenter.getY()-height/2+1));
//		Point tailEndRightScreenCoordinates = convertRealCoordinatesToScreenCoordinates(new Point((int)realCoordinateCenter.getX()+5, (int)realCoordinateCenter.getY()-height/2+1));
//		
//		// DRAW TAIL BODY
//		g.setColor(Color.white);
//		Polygon tailBody = new Polygon(new int[] {(int)tailTipScreenCoordinates.getX(), (int)tailEndLeftScreenCoordinates.getX(), (int)tailEndRightScreenCoordinates.getX()}, 
//				new int[] {(int)tailTipScreenCoordinates.getY(), (int)tailEndLeftScreenCoordinates.getY(), (int)tailEndRightScreenCoordinates.getY()}, 3);
//		g.fillPolygon(tailBody);
//		g.setColor(Color.black);
//		g.drawLine((int)tailTipScreenCoordinates.getX(), (int)tailTipScreenCoordinates.getY(), (int)tailEndLeftScreenCoordinates.getX(), (int)tailEndLeftScreenCoordinates.getY());
//		g.drawLine((int)tailTipScreenCoordinates.getX(), (int)tailTipScreenCoordinates.getY(), (int)tailEndRightScreenCoordinates.getX(), (int)tailEndRightScreenCoordinates.getY());
		
		//////////////////////////////////////
		
		// YET ANOTHER METHOD: 
//		Point bottomOvalPoint = convertRealCoordinatesToScreenCoordinates(new Point((int)realCoordinateCenter.getX(), (int)realCoordinateCenter.getY()-height/2));
		Point bottomOvalPoint = new Point((int)realCoordinateCenter.getX(), (int)realCoordinateCenter.getY()-height/2);
		double leftX = bottomOvalPoint.getX() - 10;
		double leftY = getYValueOnEllipse(leftX, realCoordinateCenter, width, height);
		
		double rightX = bottomOvalPoint.getX() - 5;
		double rightY = getYValueOnEllipse(rightX, realCoordinateCenter, width, height);
		
		Point leftPointScreenCoordinates = convertRealCoordinatesToScreenCoordinates(new Point((int)leftX, (int)leftY));
		Point rightPointScreenCoordinates = convertRealCoordinatesToScreenCoordinates(new Point((int)rightX, (int)rightY));
		
		g.setColor(Color.yellow);
		g.drawOval((int)leftPointScreenCoordinates.getX() - 20/2, (int)rightPointScreenCoordinates.getY() - 20/2, 20, 20);
		g.drawOval((int)rightPointScreenCoordinates.getX() - 20/2, (int)leftPointScreenCoordinates.getY() - 20/2, 20, 20);
		
		
		/////////////////////////////////////////////////////////

		
		Polygon tail = new Polygon();
	}
	
	private double getYValueOnEllipse(double x, Point centerPoint, double width, double height) {
		double h = centerPoint.getX();
		double k = centerPoint.getY();
		
		
		double a = height*height;
		double b = height*height*k;
		double c = height*height*k*k + width*width*(x*x - 2*h*x + h*h)-1;
		
		double[] ySolutions = quadratic(a, b, c);
		if(ySolutions[0] < ySolutions[1]) return ySolutions[0];
		else return ySolutions[1];
	}
	
	private double[] quadratic(double a, double b, double c) {
		double sqrtPartOfEquation = Math.sqrt(b*b - 4*a*c);
		double solution1 = (-b + sqrtPartOfEquation)/(2*a);
		double solution2 = (-b - sqrtPartOfEquation)/(2*a);
		return new double[] {solution1,solution2};
	}
	
	private Point convertRealCoordinatesToScreenCoordinates(Point p) {
		return new Point((int)p.getX(), (int)(getGraphicsConfiguration().getDevice().getDisplayMode().getHeight() - p.getY()) );
	}
	
	private Point convertScreenCoordinateToRealCoordinate(Point p) {
		return new Point((int)p.getX(), (int)(getGraphicsConfiguration().getDevice().getDisplayMode().getHeight() - p.getY()) );
	}
	
	private void drawCoordinatePoint(String pointDescription, Point p, Graphics g) {
		g.setColor(Color.RED);
		g.drawString(pointDescription+": ("+p.getX()+", "+p.getY()+")", (int)p.getX(), (int)p.getY());
	}
	
	public void paintErrorBalloon(Graphics g, Component anchor) {

		
		g.setColor(Color.red);
		g.drawString("origin: (0,0)", 0, 0);
		g.drawOval(0, 0, 75, 75);
		System.out.println("window location: ("+getLocation().getX()+", "+getLocation().getY()+")");
		g.drawString("window location: ("+getX()+", "+getY()+")", getX(), getY());
		Point relativePoint = convertLocation(anchor, this);
//		g.drawString("anchor location: "+"("+(int)anchor.getLocation().getX()+", "+(int)anchor.getLocation().getY()+")",
//				(int)anchor.getLocation().getX(), (int)anchor.getLocation().getY());
		g.drawString("anchor location: "+"("+(int)relativePoint.getX()+", "+(int)relativePoint.getY()+")",
				(int)relativePoint.getX(), (int)relativePoint.getY());
		
		g.setColor(Color.blue);
//
//		Point relativePoint = anchor.getLocation();
//
//		g.drawOval((int)relativePoint.getX(), (int)relativePoint.getY(), 200, 100);
//		g.fillOval((int)relativePoint.getX(), (int)relativePoint.getY(), 200, 100);
//		g.setColor(Color.green);
//		g.drawString(((JTextField)anchor).getText(), (int)relativePoint.getX()+50, (int)relativePoint.getY()+50);
		getRadialDirection(relativePoint, g);
	}
	public JTextField getTextField1() {
		return textField;
	}
	public JTextField getTextField2() {
		return textField_1;
	}
	public JTextField getTextField3() {
		return textField_2;
	}
	public JTextField getTextField4() {
		return textField_3;
	}
	public JTextField getTextField5() {
		return textField_4;
	}
	public JTextField getTextField6() {
		return textField_5;
	}
	
	private void getRadialDirection(Point point, Graphics g) {
		int startX = 0;
		int startY = 0;
		if(Math.abs((int)point.getX() -this.getWidth()) > Math.abs(this.getWidth() - (int)point.getX())) {
			startX = Math.abs((int)point.getX() -this.getWidth());
		} else {
			startX = Math.abs(this.getWidth() - (int)point.getX());
		}	
		
		if(Math.abs((int)point.getY() - this.getHeight()) > Math.abs(this.getHeight() - (int)point.getY())) {
			startY = Math.abs((int)point.getY() - this.getHeight());
		} else {
			startY = Math.abs(this.getHeight() - (int)point.getY());
		}

//		drawTextLine(startX,startY, (int)point.getX(), (int)point.getY(), g);
		drawTextLine((int)point.getX(), (int)point.getY(), startX, startY, g);
	}
	
	private void drawTextLine(int x1, int y1, int x2, int y2, Graphics g) {
		g.setColor(Color.red);
		g.drawString("start: "+"("+x1+", "+y1+")", x1, y1);
		g.drawString("finish: " +"("+x2+", "+y2+")", x2, y2);
		g.setColor(Color.green);
		g.drawLine(x1, y1, x2, y2);
	}
	
	Point center(Rectangle rect) {
		return new Point((int)rect.getCenterX(), (int)rect.getCenterY());
	}
	
	private Point getCenterOfComponent(Component component) {
		double x = component.getWidth()/2 + component.getX();
		double y = component.getY() - component.getHeight()/2;
		return new Point((int)x,(int)y);
	}
	
    public Point convertLocation(Component source, Component destination) {
        Point relativePoint = new Point(source.getX(),source.getY());
    	for(Component p = source.getParent(); p != null; p = p.getParent()) {
    		relativePoint.translate(p.getX(), p.getY());
    		if(p == destination) break;
        }
        return relativePoint;
    }
    
	
}
