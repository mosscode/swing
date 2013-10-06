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
package com.moss.swing.propertyeditor;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PropertyPanel extends JPanel {
	private List<PHolder> properties = new LinkedList<PHolder>();
	
	private static class PHolder implements UIPropertyListener {
		final UIProperty property;
		final JLabel errorLabel = new JLabel();
		public PHolder(UIProperty property) {
			this.property = property;
			errorLabel.setForeground(Color.RED);
			errorLabel.setVisible(false);
			
//			errorLabel.setText(property.toString());
//			errorLabel.setVisible(true);
//			
			property.addPropertyListener(this);
			updateErrorState();
		}
		
		private void updateErrorState(){
			if(property.errorMessage()==null){
				errorLabel.setText("");
				errorLabel.setVisible(false);
			}else{
				System.out.println(property.name() + ": " + property.errorMessage());
				errorLabel.setText("<-" + property.errorMessage());
				errorLabel.setVisible(true);
			}
			
			errorLabel.invalidate();
			Container p = errorLabel.getParent();
			if(p!=null){
				p.validate();
				p.repaint();
			}
		}
		
		public void errorStateChanged(UIProperty property) {
			if(this.property!=property)
				throw new IllegalStateException();
			updateErrorState();
		}
		
	}
	
	public PropertyPanel() {
		setLayout(new GridBagLayout());
	}
	
	public void add(UIProperty ... properties){
		add(Arrays.asList(properties));
	}

	public void add(List<UIProperty> properties){
		if(properties!=null){
			for(UIProperty next : properties){
				this.properties.add(new PHolder(next));
			}
			relayout();
		}
	}
	
	public void setProperties(List<UIProperty> properties){
		if(properties!=null){
			this.properties.clear();
			for(UIProperty next : properties){
				this.properties.add(new PHolder(next));
			}
			relayout();
		}
	}
	
	public void clearAllProperties(){
		this.properties.clear();
		relayout();
	}
	
	public boolean hasErrors(){
		boolean hasErrors = false;

		for(PHolder next : properties){
			if(next.property.errorMessage()!=null){
				hasErrors = true;
			}
		}
		
		return hasErrors;
	}
	
	private void relayout(){
		
		removeAll();
		
		GridBagConstraints labelC = new GridBagConstraints();
		labelC.fill=GridBagConstraints.HORIZONTAL;
		labelC.insets.left=5;
		labelC.insets.right=5;
		labelC.insets.top=5;
		labelC.weightx=0.0;
		labelC.weighty=0.0;
		labelC.gridx=0;
		labelC.gridy=0;
		
		GridBagConstraints valueC = new GridBagConstraints();
		valueC.fill=GridBagConstraints.BOTH;
		valueC.insets.left=5;
		valueC.insets.right=5;
		valueC.insets.top=5;
		valueC.weightx=1.0;
		valueC.weighty=0.0;
		valueC.gridx=1;
		valueC.gridy=0;
		
		GridBagConstraints errorC = new GridBagConstraints();
		errorC.fill=GridBagConstraints.HORIZONTAL;
		errorC.insets.left=0;
		errorC.insets.right=5;
		errorC.insets.top=5;
		errorC.weightx=0.0;
		errorC.weighty=0.0;
		errorC.gridx=2;
		errorC.gridy=0;
		errorC.gridwidth=1;
		errorC.gridheight=1;
		
		for(PHolder next : properties){
			labelC.gridy++;
			valueC.gridy++;
			errorC.gridy++;
			add(new JLabel(next.property.name() + ":", JLabel.RIGHT), labelC);
			add(next.property.editor(), valueC);
			add(next.errorLabel, errorC);
		}
		
		valueC.weighty=1.0;
		valueC.gridy++;
		JPanel spacer = new JPanel();
		add(spacer, valueC);
		
		invalidate();
		getParent().validate();
		repaint();
	}
}
