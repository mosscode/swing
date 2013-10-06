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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RangeWidget extends JPanel {
	public enum Mode{RANGE, VALUE}
	public enum DisplayMode{EDITING, DISPLAYING}
	
	private EditableLabelField low = new EditableLabelField();
	private EditableLabelField hi = new EditableLabelField();
	private EditableLabelField value = new EditableLabelField();
	private JLabel unitsLabel = new JLabel();
	
	private CommandLink furtherRestrictLink = new CommandLink(new AbstractAction("Further Restrict"){
		public void actionPerformed(ActionEvent e) {
			setDisplayMode(DisplayMode.EDITING);
			setMode(Mode.RANGE);
		}
	});
	private CommandLink defineLink = new CommandLink(new AbstractAction("Set Value"){
		public void actionPerformed(ActionEvent e) {
			setDisplayMode(DisplayMode.EDITING);
			setMode(Mode.VALUE);
		}
	});
	private CommandLink restrictLink = new CommandLink(new AbstractAction("Broaden to Range Restriction"){
		public void actionPerformed(ActionEvent e) {
			setDisplayMode(DisplayMode.EDITING);
			setMode(Mode.RANGE);
		}
	});
	private CommandLink cancelLink = new CommandLink(new AbstractAction("Undo Changes"){
		public void actionPerformed(ActionEvent e) {
			setDisplayMode(DisplayMode.DISPLAYING);
			setMode(Mode.RANGE);
		}
	});
	
	private Mode mode;
	private DisplayMode displayMode;
	private boolean editable = true ;
	
	public RangeWidget(){
		cancelLink.setVisible(false);
		setMode(Mode.RANGE);
	}
	
	public void setUnitsLabel(String text){
		unitsLabel.setText(text);
		refresh();
	}
	
	public String getUnitsLabel(){
		return unitsLabel.getText();
	}
	
	public void setDisplayMode(DisplayMode displayMode){
		this.displayMode = displayMode;
		refresh();
	}
	
	private void updateFieldState(){
		boolean fieldsAreEditable = (displayMode == DisplayMode.EDITING);
		low.setEditable(fieldsAreEditable & editable);
		hi.setEditable(fieldsAreEditable & editable);
		value.setEditable(fieldsAreEditable & editable);
		cancelLink.setVisible(fieldsAreEditable & editable);
		restrictLink.setVisible(!fieldsAreEditable & editable);
		defineLink.setVisible(!fieldsAreEditable & editable);
		furtherRestrictLink.setVisible(!fieldsAreEditable & editable);
	}
	
	public final boolean isEditable() {
		return editable;
	}

	public final void setEditable(boolean editable) {
		this.editable = editable;
		refresh();
	}

	public final void setMode(Mode mode) {
		if(this.mode==mode)return;
		this.mode = mode;
		refresh();
	}
	private void refresh(){
		removeAll();
		
		updateFieldState();
		
		setLayout(new GridBagLayout());

		switch(mode){
			case RANGE:{
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.insets.right=5;
				
				// fields
				add(new JLabel("Between"), c);
				c.gridx++;
				add(low, c);
				c.gridx++;
				add(new JLabel("and"), c);
				c.gridx++;
				add(hi, c);
				if(!unitsLabel.getText().equals("")){
					c.gridx++;
					add(unitsLabel, c);
				}

				// links
				c.gridx++;
				add(defineLink, c);
				c.gridx++;
				add(furtherRestrictLink, c);
				c.gridx++;
				add(cancelLink, c);
				
				//spacer
				c.gridx++;
				c.weightx=1;
				c.insets.right=0;
				add(new JPanel(), c);
				break;
			}
			case VALUE:{
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = 0;
				c.insets.right=5;
	
				add(value, c);
				if(!unitsLabel.getText().equals("")){
					c.gridx++;
					add(unitsLabel, c);
				}
				
				// links
				c.gridx++;
				add(restrictLink, c);
				c.gridx++;
				add(cancelLink, c);

				//spacer
				c.gridx++;
				c.weightx=1;
				c.insets.right=0;
				add(new JPanel(), c);
				break;
			}
		}
//		invalidate();
//		if(getParent()!=null){
//			getParent().validate();
//			repaint();
//		}
		revalidate();
	}

	public void setValueText(String text){
		value.setText(text);
	}

	public String getValueText(){
		return value.getText();
	}

	public String getLowText(){
		return low.getText();
	}

	public void setLowText(String text){
		low.setText(text);
	}
	public String getHiText(){
		return hi.getText();
	}

	public void setHiText(String text){
		hi.setText(text);
	}

	public Mode getMode() {
		return mode;
	}

	public DisplayMode getDisplayMode() {
		return displayMode;
	}


}
