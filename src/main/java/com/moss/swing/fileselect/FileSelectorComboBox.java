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
package com.moss.swing.fileselect;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class FileSelectorComboBox extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String SELECT_JARSIGNER_COMMAND_OPTION = "Select ...";
	private JComboBox comboBox = new JComboBox();
	private ArrayList jarsignerCommands = new ArrayList();
	private JFileChooser jarsignerCommandFileChooser = new JFileChooser();
	
	public FileSelectorComboBox() {
		setLayout(new BorderLayout());
		add(comboBox, BorderLayout.CENTER);
		comboBox.addItem(SELECT_JARSIGNER_COMMAND_OPTION);
		comboBox.setSelectedIndex(-1);
		comboBox.setAction(new AbstractAction(){public void actionPerformed(ActionEvent e) {
			if(valueIsAdjusting) return; 
			String selection = (String) comboBox.getSelectedItem();
			if(selection==null) return;
			if(selection.equals(SELECT_JARSIGNER_COMMAND_OPTION)){
				findJarsignerExecutable();
			}else{
				fireActions();
			}
		}});
	}
	
	private ArrayList actions = new ArrayList(3);
	
	public void addAction(Action a){
		actions.add(a);
	}
	boolean valueIsAdjusting=false;
	public void clear(){
		valueIsAdjusting=true;
		jarsignerCommands.clear();
		comboBox.removeAllItems();
		comboBox.addItem(SELECT_JARSIGNER_COMMAND_OPTION);
		comboBox.setSelectedIndex(-1);
		valueIsAdjusting=false;
	}
	private void fireActions(){
		for (Iterator i = actions.iterator(); i.hasNext();) {
			Action action = (Action) i.next();
			action.actionPerformed(new ActionEvent(this, 1, "File Selected"));
		}
	}
	
	private FileFilter filter = new FileFilter(){

		public boolean accept(File f) {
			if(f.isFile() || f.isDirectory()) return true;
			return false;
		}

		public String getDescription() {
			return "All Files";
		}};
	
	public void setFileFilter(FileFilter filter){
		this.filter = filter;
	}
	
	private void findJarsignerExecutable(){
		
		jarsignerCommandFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jarsignerCommandFileChooser.setFileFilter(filter);
		jarsignerCommandFileChooser.showOpenDialog(this);
		jarsignerCommandFileChooser.setMultiSelectionEnabled(false);
		
		File selection = jarsignerCommandFileChooser.getSelectedFile();
		if(selection!=null && selection.exists()){
			String commandPath = selection.getAbsolutePath();
			
			if(!jarsignerCommands.contains(commandPath)){
				jarsignerCommands.add(commandPath);
				comboBox.addItem(commandPath);
			}
			comboBox.setSelectedItem(commandPath);
			fireActions();
		}else{
			comboBox.setSelectedIndex(-1);
		}
	}
	
	public File getSelectedFile(){
		String path = (String) comboBox.getSelectedItem();
		if(path==null) return null;
		
		File file = new File(path);
		return file;
	}
	
	public String serialize(){
		String data = "";
		String separator = System.getProperty("path.separator");
		for (Iterator i = jarsignerCommands.iterator(); i.hasNext();) {
			String path = (String) i.next();
			if(data.length()>0) data += separator;
			data +=path;
		}
		return data;
	}
	
	public void deSerialize(String data){
		String separator = System.getProperty("path.separator");
		String[] paths = data.split(separator);
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if(!path.equals("")){
				jarsignerCommands.add(path);
				comboBox.addItem(path);
			}
		}
	}
}
