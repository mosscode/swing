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
package com.moss.balloon.math;

import java.awt.Point;


public class Ellipse2d {
	private Point center;
	private double width;
	private double height;
	
	public Ellipse2d(Point center, double width, double height) {
		this.center = center;
		this.width = width;
		this.height = height;
	}
	
	public Ellipse2d(double centerX, double centerY, double width, double height) {
		this.center = new Point((int)centerX, (int)centerY);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @see: Calculates the intersecting point on this ellipse based on a line
	 * extending from it's center with the given angle.
	 * 
	 * @param: a double value representing an angle in degrees.
	 * 
	 * @return: a point on this ellipse at an angle specified by degree.
	 *  
	 **/
	public Point getIntersection(double degree) {
		double W = width/2;
		double H = height/2;
		double x1 = center.getX() + W*Math.cos(Math.toRadians(degree));
		double y1 = center.getY() + H*Math.sin(Math.toRadians(degree));
		
		return new Point((int)x1, (int)y1);
	}

	/**
	 * @see; Takes an x value that exists on the ellipse and returns a pair of 
	 * y-coordinates corresponding to the values on the ellipse.
	 * 
	 * @param: a double representing the x-axis that intersects this ellipse.
	 * 
	 * @return: a pair of Y coordinates on the specified X axis, 
	 * of null if this ellipse doesn't intersect that axis.
	 *  
	 **/
	public Point[] getYIntercepts(double x) throws Exception {
		double h = center.getX();
		double k = center.getY();
		
		double H = height/2.0;
		double W = width/2.0;
		
		double a = 1;
		double b = -2*k;
		double c = k*k - (H*H*W*W - H*H*(x*x-2*h*x+h*h))/(W*W);
	
		double[] ySolutions = Math2d.quadratic(a, b, c);
		Point solutionPoint1 = new Point((int)x, (int)ySolutions[0]);
		Point solutionPoint2 = new Point((int)x, (int)ySolutions[1]);
		return new Point[] {solutionPoint1, solutionPoint2};
	}
	
	public Point[] getXIntercepts(double y) throws Exception {
		double h = center.getX();
		double k = center.getY();
		
		double H = height/2.0;
		double W = width/2.0;
		
		double a = 1;
		double b = -2*h;
//		double c = h*h - (W*W)/(H*H)*(y-k)*(y-k) - W*W;
		double c = h*h - (W*W*H*H - W*W*(y*y-2*k*y+k*k))/(H*H);
		
		double[] xSolutions = Math2d.quadratic(a, b, c);
		Point solutionPoint1 = new Point((int)xSolutions[0], (int)y);
		Point solutionPoint2 = new Point((int)xSolutions[1], (int)y);
		return new Point[] {solutionPoint1, solutionPoint2};
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	

	
}
