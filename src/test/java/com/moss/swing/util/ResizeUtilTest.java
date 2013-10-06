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

import com.moss.swing.util.ResizeUtil;

import junit.framework.TestCase;

public class ResizeUtilTest extends TestCase {
	public void testIt() throws Exception {
		{
			Dimension d = ResizeUtil.resizeWithinBounds(new Dimension(10, 20), new Dimension(20, 10));
			assertEquals(10, d.height);
			assertEquals(5, d.width);
		}
		{
//			java.awt.Dimension[width=48,height=48] within java.awt.Dimension[width=100,height=100]
			Dimension d = ResizeUtil.resizeWithinBounds(new Dimension(48, 48), new Dimension(100, 100));
			assertEquals(100, d.height);
			assertEquals(100, d.width);
		}
//		100x67 to 100x45
		{
			Dimension d = ResizeUtil.resizeWithinBounds(new Dimension(100, 67), new Dimension(100, 45));
			
			assertTrue("The image should be narrower", d.width<100);
			assertEquals(45, d.height);
		}
		//33x50, 100x100
		{
			Dimension d = ResizeUtil.resizeWithinBounds(new Dimension(33, 50), new Dimension(100, 100));
			
			assertEquals(100, d.height);
			assertEquals(66, d.width);
		}
		
	}
}
