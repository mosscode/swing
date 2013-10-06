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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ValueChangeHelper{
	private static Log log = LogFactory.getLog(ValueChangeHelper.class);
	
	Container rootContainer;
	ArrayList ignoredComponents = new ArrayList();
	Map componentValues = new Hashtable();
	ArrayList containers = new ArrayList();
	public ValueChangeHelper(Container rootContainer){
		setContainer(rootContainer);
	}
	
	
	public void setContainer(Container container){
		if(container == null) throw new NullPointerException("Root Container cannot be null");
		rootContainer = container;
		componentValues=new Hashtable();
		containers = new ArrayList();
		rememberCurrentValues();
	}
	
	/**
	 * This determines if, for our purposes, we treat this component as a end-of the line, 
	 * or as a container of other components (needed since some "components" such as JTextComponents
	 * are also subclasses of Container
	 * 
	 * @param c
	 * @return
	 */
	
	private boolean treatAsComponent(Component c){
		
		
		if(c instanceof JTextComponent)return true;
		if(c instanceof JComboBox)return true;
		if(c instanceof JCheckBox)return true;
		
		if(reflectiveIsInstanceOf(c, "pv.jfcx.JPVEdit")) return true;
		
		return false;
	}


	private boolean reflectiveIsInstanceOf(Component c, String className) {
		boolean result;
		try {
			Class<?> clazz = Class.forName(className);
			if(c.getClass().isAssignableFrom(clazz))result = true;
			else result = false;
		} catch (ClassNotFoundException e) {
			result = false;
		}
		return result;
	}
	
	public void ignoreAll(Component c){
		if(c instanceof Container){
			Container container = (Container)c;
			Component[] components = container.getComponents();
			for (int x = 0; x < components.length; x++) {
				Component component = components[x];
				ignoreAll(component);
			}
		}
		ignore(c);
	}
	
	public void ignore(Component c){
		ignoredComponents.add(c);
	}
	
	public void rememberCurrentValues(){
		rememberContainer(rootContainer);
	}
	
	
	
	private void rememberComponentValues(Component comp){
		if(comp instanceof JTextComponent){
			JTextComponent tComp = (JTextComponent)comp;
			componentValues.put(tComp, tComp.getText());
			log.debug("Remembering value " + tComp.getText());
			return;
		}
		if(reflectiveIsInstanceOf(comp, "pv.jfcx.JPVEdit")){
			Object value = getJpveditValue(comp);
			if(value!=null)componentValues.put(comp, value);
			log.debug("Remembering value:" + value);
			return;
		}
		if(comp instanceof JComboBox){
			JComboBox cBox = (JComboBox)comp;
			Object value = cBox.getSelectedItem();
			if(value!=null)componentValues.put(cBox, value);
			log.debug("Remembering value: " + cBox.getSelectedItem());
			return;
		}
		if(comp instanceof JCheckBox){
			JCheckBox cbox = (JCheckBox)comp;
			Boolean value = new Boolean(cbox.isSelected());
			componentValues.put(cbox, value);
			log.debug("Remembering value:" + value);
			return;
		}
		log.debug("Don't understand component type " + comp.getClass().getName());
	}


	private Object getJpveditValue(Component comp) {
		Object value;
		try {
			Class<?> clazz = Class.forName("pv.jfcx.JPVEdit");
			value = clazz.getMethod("getValue").invoke(comp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return value;
	}
	
	
	private void rememberContainer(Container parent){
		Component[] children = parent.getComponents();
		for(int x=0;x<children.length;x++){
			Component comp = children[x];
			log.debug(comp.getClass().toString());
			if(treatAsComponent(comp)){
				rememberComponentValues(comp);
			}else if(comp instanceof Container){
				rememberContainer((Container)comp);
				containers.add(comp);
			}else{
				log.debug("We don't understand components of type " + comp.getClass().getName());
			}
			
		}
	}
	
	private boolean isIgnored(Component c){
		if(ignoredComponents.indexOf(c)>-1) {
			log.debug("Component " + c + " is ignored");
			return true;
		}
		else {
			// recursively ascend through the container heirarchy to make sure that our container is not ignored
			
			for(Container container = c.getParent();container !=null;container=container.getParent()){
				if(ignoredComponents.indexOf(container)>-1){
					log.debug("Containter is ignored");
					return true;
				}
			}
			return false;
		}
	}
	
	private boolean componentHasChanged(Component comp){
		if(isIgnored(comp)){
			log.debug("component was ignored: " + comp.getClass().getName());
			return false;
		}
		log.debug("hasChanged() called on " + comp.getClass().getName());
		if(comp instanceof JTextComponent){
			JTextComponent tComp = (JTextComponent)comp;
			Object origValue = componentValues.get(tComp);
			Object newValue = tComp.getText();

			if(!objectUnchanged(tComp, origValue, newValue))return true;
			
		}
		if(reflectiveIsInstanceOf(comp, "pv.jfcx.JPVEdit")){
			Object newValue = getJpveditValue(comp);
			Object origValue = componentValues.get(comp);
			if(!objectUnchanged(comp, origValue, newValue))return true;
		}
		if(comp instanceof JComboBox){
			JComboBox cBox = (JComboBox)comp;
			if(!objectUnchanged(cBox, componentValues.get(cBox),cBox.getSelectedItem()))return true;
		}
		if(comp instanceof JCheckBox){
			JCheckBox cBox = (JCheckBox)comp;
			if(!objectUnchanged(cBox, componentValues.get(cBox),new Boolean(cBox.isSelected())))return true;
		}
		return false;
	}
	
	private boolean hasChanged(Container parent){
		Component[] children = parent.getComponents();
		for(int x=0;x<children.length;x++){
			Component comp = children[x];
			
			if(treatAsComponent(comp)){
				if(componentHasChanged(comp))return true;
			}else if(comp instanceof Component){
				if(hasChanged((Container)comp))return true;
			}
		}
		return false;
	}
	
	private boolean objectUnchanged(Component key, Object origValue, Object newValue){
		boolean unchanged= (origValue == null && newValue==null) //the orig value & components current value are both null (and therefore unchanged)
		||
		(
				((origValue != null) && (newValue != null)) //both values are non null (tests for one null & one non-null - i.e. a change)
				&&
				origValue.equals(newValue) //the orig value equal()s the new value
		)
		&& !isIgnored(key)
		;
		if(!unchanged)log.info("Object of type " + key.getClass().getName() + " was changed, Old values:" + origValue + " new values:" + newValue);
		return unchanged;
	}
	public boolean hasChanged(){
//		System.out.println("Checking for changes");
		if(rootContainer==null) throw new IllegalArgumentException("Root container has not been set");
		return hasChanged(rootContainer);
	}
	
} 