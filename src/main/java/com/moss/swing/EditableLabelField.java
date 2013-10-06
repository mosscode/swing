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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditableLabelField extends JPanel {
	
	private JTextField editableView;
	private JLabel staticView;
	private Boolean editable;
	private Font font;
	private boolean showEditLink = false;
	private JComponent view;
	
	private Action editAction = new AbstractAction("edit"){
		public void actionPerformed(ActionEvent e) {
			enableEdits();
		}
	};
	
	private Action cancelAction = new AbstractAction("undo"){
		public void actionPerformed(ActionEvent e) {
			cancelEdits();
		}
	};
	
	private CommandLink editLink = new CommandLink(editAction);
	
	public EditableLabelField() {
		this("");
	}
	
	public EditableLabelField(String text){
		editableView = new JTextField(text);
		staticView = new JLabel(text);
		
		//needed because the JPanel() constructor calls setFont()
		editableView.setFont(font);
		staticView.setFont(font);
		
		setEditable(false);
	}
	private void enableEdits(){
		setEditable(true);
		editLink.setAction(cancelAction);
	}
	private void cancelEdits(){
		editableView.setText(staticView.getText());
		editLink.setAction(editAction);
		setEditable(false);
	}
	
	public String getText(){
		if(editable){
			return editableView.getText();
		}else{
			return staticView.getText();
		}
	}
	
	public void setFont(Font font){
		this.font = font;
		
		//needed because the JPanel() constructor calls setFont()
		if(staticView!=null) staticView.setFont(font);
		if(editableView!=null) editableView.setFont(font);
	}
	
	public Font getFont(){
		return font;
	}
	
	public void setText(String text){
		if(editable){
			editableView.setText(text);
		}else{
			staticView.setText(text);
		}
	}
	
	public boolean isEditable(){
		return editable;
	}
	
	public void setEditable(boolean editable){
		if(this.editable!=null && this.editable==editable) return;
		this.editable = editable;
		if(editable){
			editableView.setText(staticView.getText());
			setView(editableView);
			editLink.setAction(cancelAction);
		}else{
			staticView.setText(editableView.getText());
			setView(staticView);
			editLink.setAction(editAction);
		}
	}
	
	private void setView(JComponent view){
		this.view = view;
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		add(view, c);
		if(showEditLink){
			c.insets.left=5;
			c.weightx=0;
			c.weighty=0;
			c.gridx++;
			add(editLink, c);
		}
		invalidate();
	}

	public final boolean isShowEditLink() {
		return showEditLink;
	}

	public final void setShowEditLink(boolean showEditLink) {
		if(showEditLink==this.showEditLink) return;
		this.showEditLink = showEditLink;
		
		if(view!=null) setView(view);
		if(getParent()!=null) getParent().validate();
	}
	
}
