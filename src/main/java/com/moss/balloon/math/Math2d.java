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

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;


public class Math2d {
	public static Point convertRealCoordinatesToScreenCoordinates(Point p, int windowHeight) {
		return new Point((int)p.getX(), (int)(windowHeight - p.getY()) );
	}
	
	public static Point convertScreenCoordinateToRealCoordinate(Point p, int windowHeight) {
		return new Point((int)p.getX(), (int)(windowHeight - p.getY()) );
	}
	
	public static double[] quadratic(double a, double b, double c) throws NoRealSolutionException {
		if(b*b - 4*a*c < 0.0) return null;
		double discriminant = b*b - 4*a*c;
		
		if(discriminant < 0.0) throw new NoRealSolutionException("No solution was found for "+a+"t^2 + "+b+"t + "+c+" = 0");
		
		double sqrtPartOfEquation = Math.sqrt(discriminant);
		double solution1 = (-b + sqrtPartOfEquation)/(2*a);
		double solution2 = (-b - sqrtPartOfEquation)/(2*a);
		return new double[] {solution1,solution2};
	}
	
	public static Point getCenter(double width, double height) {
		return new Point((int)(width/2.0), (int)(height/2.0));
	}
	
	public static Point getCenter(Point origin, double width, double height) {
		double dx = origin.x + width/2.0;
		double dy = origin.y + height/2.0;
		return new Point((int)dx, (int)dy);
	}
	
    public static Point convertLocation(Component source, Component destination) {
    	Point relativePoint = source.getLocation();
    	for(Component p = source.getParent(); p != null; p = p.getParent()) {
    		relativePoint.translate(p.getX(), p.getY());
    		if(p == destination) break;
        }
        return relativePoint;
    }
    
    /**
     * Returns the angle (in degrees) between the specific origin and the destination point.
     * @param origin
     * @param destination
     * @return
     */
    public static double calculateAngleInDegrees(Point origin, Point destination) {
    	double dX = destination.x - origin.x;
    	double dY = destination.y - origin.y;
    	double angle = Math.atan2(dY,dX);
    	return Math.toDegrees(angle);
    }
    
    /**
     * Returns the point intersection from the center of a rectangle at the specified angle.
     * @param center
     * @param bounds
     * @param angle
     * @return
     */
    public static Point findRectangleIntersection(Point center, double width, double height, double angle) {
    	
    	double normalizedAngle = angle % 360;
    	double lineDistanceToIntersection = 0.0;
    	
    	double rightX = center.x + width/2.0;
    	double upperY = center.y + height/2.0;
    	double leftX = center.x - width/2.0;
    	double lowerY= center.y - height/2.0;
    	    	
    	double cosAngle = Math.cos(Math.toRadians(normalizedAngle));
    	double sinAngle = Math.sin(Math.toRadians(normalizedAngle));
    	
    	double xInt = 0.0;
    	double yInt = 0.0;
    	
    	// QUAD 1
    	if((normalizedAngle >=0.0 && normalizedAngle < 90.0) || (normalizedAngle <= -270 && normalizedAngle > -360)){
    		double distanceToRightIntersectionX = (rightX - center.x)/cosAngle;
    		double distanceToUpperIntersectionY = (upperY - center.y)/sinAngle;
    		if(distanceToRightIntersectionX <= distanceToUpperIntersectionY ) {
    			lineDistanceToIntersection = distanceToRightIntersectionX;
    	    	xInt = rightX; 
    	    	yInt = center.y + lineDistanceToIntersection*Math.abs(sinAngle);
    		} else {
    			lineDistanceToIntersection = distanceToUpperIntersectionY;
    	    	xInt = center.x + lineDistanceToIntersection*Math.abs(cosAngle);
    	    	yInt = upperY;
    		}
    	} 
    	// QUAD 2
    	else if((normalizedAngle >= 90.0 && normalizedAngle < 180.0) || (normalizedAngle <= -180 && normalizedAngle > -270)) {
    		double distanceToLeftIntersectionX = (center.x - leftX)/Math.abs(cosAngle);
    		double distanceToUpperIntersectionY = (upperY - center.y)/sinAngle;
    		if(distanceToLeftIntersectionX <= distanceToUpperIntersectionY ) {
    			lineDistanceToIntersection = distanceToLeftIntersectionX;
    	    	xInt = leftX;
    	    	yInt = center.y + lineDistanceToIntersection*Math.abs(sinAngle);
    		} else {
    			lineDistanceToIntersection = distanceToUpperIntersectionY;
    	    	xInt = center.x - lineDistanceToIntersection*Math.abs(cosAngle);
    	    	yInt = upperY;
    		}
    	} 
    	// QUAD 3
    	else if((normalizedAngle >= 180.0 && normalizedAngle < 235.0) || (normalizedAngle <= -90 && normalizedAngle > -180)) {
    		double distanceToRightIntersectionX = (center.x - leftX)/Math.abs(cosAngle);
    		double distanceToLowerIntersectionY = (center.y - lowerY)/Math.abs(sinAngle);
    		if(distanceToRightIntersectionX <= distanceToLowerIntersectionY ) {
    			lineDistanceToIntersection = distanceToRightIntersectionX;
    	    	xInt = leftX;
    	    	yInt = center.y - lineDistanceToIntersection*Math.abs(sinAngle);
    		} else {
    			lineDistanceToIntersection = distanceToLowerIntersectionY;
    	    	xInt = center.x - lineDistanceToIntersection*Math.abs(cosAngle);
    	    	yInt = lowerY;
    		}	

    	}
    	// QUAD 4
    	else if((normalizedAngle >=235.0 && normalizedAngle < 360.0) || (normalizedAngle <= 0 && normalizedAngle > -90)) {
//    	else {
    		double distanceToLeftIntersectionX = (rightX - center.x)/cosAngle;
    		double distanceToLowerIntersectionY = (center.y - lowerY)/Math.abs(sinAngle);
    		if(distanceToLeftIntersectionX <= distanceToLowerIntersectionY ) {
    			lineDistanceToIntersection = distanceToLeftIntersectionX;
    	    	xInt = rightX;
    	    	yInt = center.y - lineDistanceToIntersection*Math.abs(sinAngle);
    		} else {
    			lineDistanceToIntersection = distanceToLowerIntersectionY;
    	    	xInt = center.x + lineDistanceToIntersection*Math.abs(cosAngle);
    	    	yInt = lowerY;
    		}	

    	}
    	
    	Point intersection = new Point((int)xInt, (int)yInt);
    	return intersection;
    }
    
    public static boolean intersects(Point p, Rectangle bounds) {
    	if( (p.x >= bounds.x) && (p.x <= bounds.x + bounds.getWidth()
    			&& (p.y >= bounds.y) && (p.y <= bounds.y + bounds.getHeight()) )) {
    		return true;
    	} else return false;
    }
}
