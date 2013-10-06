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
package com.moss.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.Icon;

import com.moss.swing.util.ResizeUtil;

public class NormalizedImageIcon implements Icon {
	private final int width;
	private final int height;
	private final Image image;
	private final int widthPadding;
	private final int heightPadding;
	private final Dimension newIconSize;
	
    /**
     * Id used in loading images from MediaTracker.
     */
    private static int mediaTrackerID;

	
    protected final static Component component = new Component() {};
    protected final static MediaTracker tracker = new MediaTracker(component);

    transient int loadStatus = 0;
    
	public NormalizedImageIcon(Image icon, int width, int height) {
		newIconSize = normalize(icon, width, height);

		this.image =  icon.getScaledInstance(newIconSize.width, newIconSize.height, Image.SCALE_AREA_AVERAGING);
		
		this.width = width;
		this.height = height;
		
		widthPadding = (width - newIconSize.width)/2;
		heightPadding = (height - newIconSize.height)/2;
		
		loadImage(this.image);
	}
	
    /**
     * Returns an ID to use with the MediaTracker in loading an image.
     */
    private int getNextID() {
        synchronized(tracker) {
            return ++mediaTrackerID;
        }
    }

    /**
     * Loads the image, returning only when the image is loaded.
     * @param image the image
     */
	protected void loadImage(Image image) {
		synchronized(tracker) {
			int id = getNextID();

			tracker.addImage(image, id);
			try {
				tracker.waitForID(id, 0);
			} catch (InterruptedException e) {
				System.out.println("INTERRUPTED while loading Image");
			}
			loadStatus = tracker.statusID(id, false);
			tracker.removeImage(image, id);
		}
	}

	public int getIconHeight() {
		return height;
	}
	public int getIconWidth() {
		return width;
	}
	public void paintIcon(Component c, Graphics g, int x, int y) {
		MediaTracker t = new MediaTracker(c);
		t.addImage(image, 1);
		g.drawImage(
				image,
				x + widthPadding,
				y + heightPadding,
				null
				);
	}
	
	public Image getImage() {
		return image;
	}
	
	
	private static Dimension normalize(Image newImage, final int width, final int height){
		int origHeight = newImage.getHeight(null);
		int origWidth = newImage.getWidth(null);
		
		Dimension newIconSize = ResizeUtil.resizeWithinBounds(new Dimension(origWidth, origHeight), new Dimension(width, height));
		
//		System.out.println("Scaled from " + origWidth + "x" + origHeight + " to " + newIconSize.width + "x" + newIconSize.height);
		
		return newIconSize;
	}
	
}
