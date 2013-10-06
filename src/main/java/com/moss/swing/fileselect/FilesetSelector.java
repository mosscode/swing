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
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class FilesetSelector extends JPanel {
	private FilesetSelectorView view = new FilesetSelectorView();
	private TreeSet selectedFiles = new TreeSet();
	
	public FilesetSelector(){
		setLayout(new BorderLayout());
		add(view, BorderLayout.CENTER);
		
		glueToView();
	}
	
	JFileChooser fc = new JFileChooser();
	private void addFiles(){
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(filter);
		fc.showOpenDialog(view);
		File[] selection = fc.getSelectedFiles();
		if(selection!=null){
			selectedFiles.addAll(Arrays.asList(selection));
			refreshList();
		}
		
	}
	
	FileFilter filter;
	
	public void setFileFilter(FileFilter filter){
		this.filter = filter;
	}
	
	private void refreshList(){
		DefaultListModel model = new DefaultListModel();
		for (Iterator i = selectedFiles.iterator(); i.hasNext();) {
			File f = (File) i.next();
			model.addElement(f);
		}
		view.getList().setModel(model);
	}
	private void addRecursively(){
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setFileFilter(filter);
		int result = fc.showOpenDialog(view);
		if(result!=JFileChooser.APPROVE_OPTION) return;
		File parentDir = fc.getSelectedFile();
		
		if(parentDir!=null && parentDir.exists() && parentDir.isDirectory()){
			ArrayList directoriesToScan = new ArrayList();
			directoriesToScan.add(parentDir);
			while(directoriesToScan.size()>0){
				File dir = (File)directoriesToScan.get(0);
				File[] children = dir.listFiles();
				for (int i = 0; i < children.length; i++) {
					File file = children[i];
					if(file.isDirectory()) directoriesToScan.add(file);
					if(file.isFile() && filter.accept(file)){
						selectedFiles.add(file);
					}
				}
				directoriesToScan.remove(dir);
			}
		}
		refreshList();
	}
	
	private void removeFiles(){
		Object[] selections = view.getList().getSelectedValues();
		selectedFiles.removeAll(Arrays.asList(selections));
		refreshList();

	}
	
	public File[] getSelectedFiles(){
		return (File[]) selectedFiles.toArray(new File[0]);
	}
	
	public String serialize(){
		return fc.getCurrentDirectory().getAbsolutePath();
	}
	public void deSerialize(String data){
		fc.setCurrentDirectory(new File(data));
	}
	
//	 GLUE CODE
	private Action addRecursivelyAction = new AbstractAction("Add From Directory"){
		public void actionPerformed(ActionEvent e){
			addRecursively();
		};
	};
	private Action addAction = new AbstractAction("Add File(s)"){
		public void actionPerformed(ActionEvent e) {
			addFiles();
		}};
	private Action removeAction = new AbstractAction("Remove File(s)"){
			public void actionPerformed(ActionEvent e) {
				removeFiles();
			}};
	private void glueToView(){
		view.getButtonAddFiles().setAction(addAction);
		view.getButtonRemove().setAction(removeAction);
		view.getButtonAddDirectories().setAction(addRecursivelyAction);
	}
	
}