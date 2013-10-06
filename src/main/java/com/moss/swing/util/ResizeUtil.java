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
package com.moss.swing.util;

import java.awt.Dimension;

import com.moss.swing.util.shape.AspectRatio;


public class ResizeUtil {
	public static Dimension resizeWithinBounds(Dimension orig, Dimension bounds){
		AspectRatio aspect = new AspectRatio(orig);
		AspectRatio boundsAspect = new AspectRatio(bounds);
		
		if(aspect.isNarrowerThan(boundsAspect)){
			return matchToBoundaryWidth(aspect, bounds);
		}else{
			return matchToBoundaryHeight(aspect, bounds);
		}
//
//		/*
//		 * orig
//		 * |---------------------|
//		 * |      orig           |
//		 * |---------------------|
//		 * 
//		 * bounds
//		 * |------------|
//		 * |            |
//		 * |            |
//		 * |   bounds   |
//		 * |            |
//		 * |            |
//		 * |------------|
//		 */
//		if(aspect.is(BoxShape.WIDE) && boundsAspect.is(BoxShape.TALL)){
//			
//			return new Dimension(bounds.width, aspect.findHeight(bounds.width));
//			
//		}
//		
//		/*
//		 * |---------------------|
//		 * |      orig           |
//		 * |---------------------|
//		 * 
//		 * 
//		 * 
//		 * |---------------------|
//		 * |      bounds         |
//		 * |---------------------|
//		 * 
//		 */
//		else if(aspect.type == BoxShape.WIDE && boundsAspect.is(BoxShape.WIDE)){
//			
//			if(aspect.isNarrowerThan(boundsAspect)){
//				return matchToBoundaryWidth(aspect, bounds);
//			}else{
//				return matchToBoundaryHeight(aspect, bounds);
//			}
//		}else if(aspect.type == BoxShape.TALL && boundsAspect.is(BoxShape.WIDE)){
//			return new Dimension(aspect.findWidth(bounds.height), bounds.height);
//		}else if(aspect.type == BoxShape.TALL && boundsAspect.is(BoxShape.TALL)){
//			return new Dimension(aspect.findWidth(bounds.height), bounds.height);
//		}else if(aspect.is(BoxShape.SQUARE) && boundsAspect.is(BoxShape.TALL)){
//			return new Dimension(aspect.findWidth(bounds.height), bounds.height);
//		}else if(aspect.is(BoxShape.SQUARE) && boundsAspect.is(BoxShape.WIDE)){
//			return new Dimension(bounds.width, aspect.findHeight(bounds.width));
//		}else if(aspect.is(BoxShape.SQUARE) && boundsAspect.is(BoxShape.SQUARE)){
//			return new Dimension(bounds.width, aspect.findHeight(bounds.width));
//		}
//		else throw new RuntimeException("IMPOSSIBLE! (unable to figure out how to resize " + aspect.type + "(" + orig + ") within " + boundsAspect.type + "(" + bounds + ")");
	}
	
	private static Dimension matchToBoundaryHeight(AspectRatio aspect, Dimension bounds){
		return new Dimension(aspect.findWidth(bounds.height), bounds.height);
	}
	private static Dimension matchToBoundaryWidth(AspectRatio aspect, Dimension bounds){
		return new Dimension(bounds.width, aspect.findHeight(bounds.width));
	}
	enum DIR {
		HEIGHT, WIDTH
	}
	
}
