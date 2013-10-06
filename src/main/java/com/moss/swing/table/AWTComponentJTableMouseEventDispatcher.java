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
package com.moss.swing.table;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

public class AWTComponentJTableMouseEventDispatcher implements MouseListener, MouseMotionListener {
	private Component component;
	private JTable table;
	public AWTComponentJTableMouseEventDispatcher(JTable table) {
		this.table = table;
		table.addMouseListener(this);
		table.addMouseMotionListener(this);
	}
	
	private Component getComponent(MouseEvent e){
		TableColumnModel columnModel = table.getColumnModel();
		int column = columnModel.getColumnIndexAtX(e.getX());
		int row    = e.getY() / table.getRowHeight();
		Object value;
		JButton button;
		MouseEvent buttonEvent;
		
		if(row >= table.getRowCount() || row < 0 ||
				column >= table.getColumnCount() || column < 0)
			return null;
		
		value = table.getValueAt(row, column);
		
		if(!(value instanceof Component))
			return null;
		else return (Component)value;
	}
	private void fireMouseExited(MouseEvent e, Component c){
		e = new MouseEvent(c, MouseEvent.MOUSE_EXITED, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
		if(c!=null){
			MouseEvent buttonEvent =
				(MouseEvent)SwingUtilities.convertMouseEvent(table, e, c);
			c.dispatchEvent(buttonEvent);
			
			// This is necessary so that when a button is pressed and released
			// it gets rendered properly.  Otherwise, the button may still appear
			// pressed down when it has been released.
			table.repaint();
		}
	}
	

	
	public void mouseDragged(MouseEvent e) {
		doComponentThing(e);
	}
	private void doComponentThing(MouseEvent e){
		Component eventComponent = getComponent(e);
		if(component != eventComponent){
			//mouse moved from component to component
			if(component != null){
				//fire mouse exited on component
				this.fireMouseEvent(MouseEvent.MOUSE_EXITED, e, component);
			}
			if(eventComponent != null){
				//fire mouse entered on component
				this.fireMouseEvent(MouseEvent.MOUSE_ENTERED, e, eventComponent);
			}
		}
		forwardEvent(e);
		component = eventComponent;
	}
	public void mouseMoved(MouseEvent e) {
		doComponentThing(e);
	}
	
	private void forwardEvent(MouseEvent e) {
		Component c = getComponent(e);
		if(c!=null){
			MouseEvent buttonEvent =
				(MouseEvent)SwingUtilities.convertMouseEvent(table, e, c);
			c.dispatchEvent(buttonEvent);
			
			// This is necessary so that when a button is pressed and released
			// it gets rendered properly.  Otherwise, the button may still appear
			// pressed down when it has been released.
			System.out.print("Repaint...");
			table.repaint();
			System.out.println("Done");
		}
	}
	private void fireMouseEntered(MouseEvent e, Component c){
		e = new MouseEvent(c, MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
		if(c!=null){
			MouseEvent buttonEvent =
				(MouseEvent)SwingUtilities.convertMouseEvent(table, e, c);
			c.dispatchEvent(buttonEvent);
			
			// This is necessary so that when a button is pressed and released
			// it gets rendered properly.  Otherwise, the button may still appear
			// pressed down when it has been released.
			System.out.print("Repaint...");
			table.repaint();
			System.out.println("Done");
		}
	}
	private void fireMouseEvent(int eventType, MouseEvent origEvent, Component c){
		if(c!=null){
			MouseEvent newEvent = new MouseEvent(c, eventType, origEvent.getWhen(), origEvent.getModifiers(), origEvent.getX(), origEvent.getY(), origEvent.getClickCount(), origEvent.isPopupTrigger());
			c.dispatchEvent(newEvent);
			// This is necessary so that when a button is pressed and released
			// it gets rendered properly.  Otherwise, the button may still appear
			// pressed down when it has been released.
			table.repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	
	public void mouseEntered(MouseEvent e) {
		doComponentThing(e);
	}
	
	public void mouseExited(MouseEvent e) {
		doComponentThing(e);
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
	}
} 