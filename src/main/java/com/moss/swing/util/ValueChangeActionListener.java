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

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 */
public class ValueChangeActionListener implements ActionListener, FocusListener{
	public void focusGained(FocusEvent e) {
		doChangeCheck();
	}
	public void focusLost(FocusEvent e) {
		doChangeCheck();
	}
	
	boolean haveReportedChange=false;
	private synchronized void doChangeCheck(){
		if(helper.hasChanged() && !haveReportedChange){
			Iterator i = listeners.iterator();
			while(i.hasNext()){
				ActionListener listener = (ActionListener)i.next();
				listener.actionPerformed(new ActionEvent(this, 1, "ComponentValueChanged"));
			}
			//haveReportedChange = true;
		}
	}
	private Container container;
	private ValueChangeHelper helper;
	public void setContainer(Container c, ValueChangeHelper helper){
		this.container = c;
		this.helper = helper;
		listenToContainer(container);
		
	}
	
	private void listenToContainer(Container parent){
		Component[] children = parent.getComponents();
		for(int x=0;x<children.length;x++){
			Component comp = children[x];
			comp.addFocusListener(this);
			if(comp instanceof Container){
				Container cont = (Container)comp;
				listenToContainer(cont);
			}
		}
	}
	
	private ArrayList listeners = new ArrayList();
	public void addActionListener(ActionListener l){
		listeners.add(l);
	}
	public void removeActionListener(ActionListener l){
		listeners.remove(l);
	}
	public void actionPerformed(ActionEvent e){
		doChangeCheck();
	}
}
 