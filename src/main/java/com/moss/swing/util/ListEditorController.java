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
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 * This class assists in managing a basic UI paradigm
 */
public class ListEditorController extends AbstractListModel{
	private ArrayList editors = new ArrayList();
	ArrayList newEditors = new ArrayList();
	JButton saveButton, cancelButton, addButton, deleteButton;
	JList editorList;
	JPanel editorDisplayPanel;
	Editor editorInFocus;
	EditorFactory editorFactory;
	
	public ListEditorController(JButton saveButton, JButton cancelButton, JButton addButton, JButton deleteButton,
			JList editorList, JPanel editorDisplayPanel,
			EditorFactory editorFactory){
		
		editors.addAll(Arrays.asList(editorFactory.getEditors()));
		this.saveButton=saveButton;
		this.cancelButton=cancelButton;
		this.addButton=addButton;
		this.deleteButton=deleteButton;
		this.editorList=editorList;
		this.editorDisplayPanel=editorDisplayPanel;
		this.editorFactory=editorFactory;
		
		editorList.setModel(this);
		editorList.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				listSelectionEventHappened();
			}});
		saveButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				saveButtonPressed();
			}});
		cancelButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				cancelButtonPressed();
			}});
		addButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				addButtonPressed();
			}});

		updateButtonStates();
	}
	/**
	 * TODO: Fixme - this is supposed to make it easy to handle 'unsaved changes' dialogs, etc, 
	 * but there appears to be no way to easily distinguish a 'don't save' response from a 'cancel' response.
	 */
	public boolean promptForUnsavedChanges(){
		return removeFocus();
	}
	
	private void addButtonPressed(){
		Editor e = editorFactory.newEditor();
		newEditors.add(e);
		editors.add(e);
		int index = editors.indexOf(e);
		if(index<0)System.out.println("There is a problem, added a new editor but the index was " + index);
		super.fireContentsChanged(this, index, index);
		if(removeFocus()){
			setFocus(e);
		}
	}
	
	private void saveButtonPressed(){
		if(editorInFocus==null)return;
		if(editorCanBeSaved(editorInFocus)){
			int index = editors.indexOf(editorInFocus);
			Editor newEditor = editorFactory.saveEditor(editorInFocus);
			if(newEditor==null) return;
			editorInFocus = newEditor;
			editors.set(index, editorInFocus);
			setFocus(editorInFocus);
		}
	}
	
	private void discardUnsavedChanges(){
		if(editorInFocus==null)return;
		int index = editors.indexOf(editorInFocus);
		if(isNewEditor(editorInFocus)){
			// If the existing editor is "new", remove it from the list if the user chose not to save it
			editors.remove(index);
			super.fireIntervalRemoved(this, index, index);
			editorInFocus=null;
			showNothing();
		}else{
			// somehow we need to revert to the saved version here
			editorInFocus = editorFactory.revertEditor(editorInFocus);
			setFocus(editorInFocus);
			editors.set(index, editorInFocus);
			super.fireContentsChanged(this, index, index);
			//showEditor();
			//editorList.setSelectedIndex(index);
		}
		updateButtonStates();
		
	}
	private void cancelButtonPressed(){
		discardUnsavedChanges();
	}
	private void listSelectionEventHappened(){
		int index = editorList.getSelectedIndex();
		if(index>=0){
			Editor newlySelectedEditor = (Editor)editors.get(index);
			
			if(newlySelectedEditor==editorInFocus){
				/*
				 * Do nothing - this editor already has our focus
				 */
			}else{
				changeFocus(newlySelectedEditor);
			}
		}
	}
	private boolean isNewEditor(Editor e){
		return this.newEditors.indexOf(e)>=0;
	}
	
	private boolean editorCanBeSaved(Editor e){
		String[] errors = e.validateInput();
		if(errors!=null && errors.length>0){
			String message = "<html>Please correct the following errors before continuing:<blockquote>";
			for (int x = 0; x < errors.length; x++) {
				message += errors[x] + "<br>";
			}
			message += "</blockquote></html>";
			JOptionPane.showMessageDialog(editorDisplayPanel, message);
			return false;
		}
		return true;
	}
	
	private boolean removeFocus(){
		if(!isNewEditor(editorInFocus) && (editorInFocus==null || !editorInFocus.isDirty()))return true;
		int index = editors.indexOf(editorInFocus);
		if(!editorCanBeSaved(editorInFocus)){
			editorList.setSelectedIndex(index);
			return false;
		}
		int response = JOptionPane.showConfirmDialog(editorDisplayPanel, "Save Changes?");
		switch(response){
		
		case JOptionPane.YES_OPTION:
			Editor savedEditor = editorFactory.saveEditor(editorInFocus);

			if (savedEditor == null) { // editor could not be saved
				if (index >= 0) editorList.setSelectedIndex(index);
				return false;
			}
			
			editorInFocus = savedEditor; 
			editors.set(index, editorInFocus);
			super.fireContentsChanged(this, index, index);
			break;
			
		case JOptionPane.NO_OPTION:
			if(isNewEditor(editorInFocus)){
				// If the existing editor is "new", remove it from the list if the user chose not to save it
				editors.remove(index);
				super.fireIntervalRemoved(this, index, index);
			}else{
				// somehow we need to revert to the saved version here
				editorInFocus = editorFactory.revertEditor(editorInFocus);
				editors.set(index, editorInFocus);
				super.fireContentsChanged(this, index, index);
				//showEditor();
				//editorList.setSelectedIndex(index);
			}
			break;
			
		case JOptionPane.CANCEL_OPTION:
			// Dont save and move the list selection back to our focused object
			
			if(index>=0)editorList.setSelectedIndex(index);
			return false;
		}
		return true;
	}
	
	private void changeFocus(Editor newFocus){
		// First we save the existing editor, if necessary
		if(removeFocus()){
			setFocus(newFocus);
		}

	}
	
	private void setFocus(Editor newFocus){
		// Display the new editor
		this.editorInFocus = newFocus;
		showEditor();
		int index = editors.indexOf(newFocus);
		editorList.setSelectedIndex(index);
		editorInFocus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateButtonStates();
			}});
		updateButtonStates();
	}
	
	private void updateButtonStates(){
		if(editorInFocus==null || !editorInFocus.isDirty()){
			addButton.setEnabled(true);
			cancelButton.setEnabled(false);
			saveButton.setEnabled(false);
		}else{
			addButton.setEnabled(false);
			cancelButton.setEnabled(true);
			saveButton.setEnabled(true);
			
		}
	}
	
	private void showNothing(){
		editorDisplayPanel.removeAll();
		editorDisplayPanel.doLayout();
		editorDisplayPanel.invalidate();
		editorDisplayPanel.validate();
		editorDisplayPanel.repaint();
	}
	
	private void showEditor(){
		editorDisplayPanel.removeAll();
		editorDisplayPanel.setLayout(new BorderLayout());
		editorDisplayPanel.add(editorInFocus.getView(), BorderLayout.CENTER);
		editorDisplayPanel.doLayout();
		editorDisplayPanel.invalidate();
		editorDisplayPanel.validate();
		editorDisplayPanel.repaint();
		System.out.println("Displayed Editor!!!");
	}
	
	/*
	 * FOR ListModel INTERFACE
	 */
	public Object getElementAt(int index) {
		System.out.println("Getting index " + index);
		return ((Editor)editors.get(index)).getLabel();
	}

	public int getSize() {
		return editors.size();
	}
}
