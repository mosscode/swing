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

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.moss.swing.test.TestFrame;

public class IconChooser extends JPanel{
	public static void main(String[] args) {
		new TestFrame(new IconChooser());
	}
	
	private final JButton button = new JButton();
	private final JFileChooser fc;
	private final List<ActionListener> listeners = new LinkedList<ActionListener>();
	private final List<IconSelectedEvent> selectionEvents = new LinkedList<IconSelectedEvent>();
	
	private File imagePath;
	
	private Icon defaultIcon;
	
	int scaledWidth = 100;
	int scaledHeight = 100;

	public IconChooser() {
		this(new ImageIcon(IconChooser.class.getResource("/lock.png")), new JFileChooser());
	}
	public IconChooser(Icon icon, JFileChooser fc) {
		this.defaultIcon = icon;
		button.setIcon(icon);
		this.fc = fc;
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showDialog();
			}
		});
		
		setLayout(new BorderLayout());
		add(button);
	}
	
	public void setDefaultIcon(Icon icon){
		this.defaultIcon = icon;
		if(imagePath==null){
			button.setIcon(defaultIcon);
		}
	}
	
	
	public Icon getDefaultIcon() {
		return defaultIcon;
	}
	
	public IconChooser(Icon icon, IconSelectedEvent ... iconSelectedEvent) {
		this(icon, new JFileChooser());
		for(IconSelectedEvent e : iconSelectedEvent) {
			this.selectionEvents.add(e);
		}
	}
	
	private void showDialog(){
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Image Files";
			}
			
			@Override
			public boolean accept(File f) {
				String name = f.getName().toLowerCase();
				return 
					f.isDirectory() 
					|| 
					name.endsWith(".jpg")
					||
					name.endsWith(".png")
					||
					name.endsWith(".gif");
			}
		});
		
		int result = fc.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION){
			File selection = fc.getSelectedFile();
			
			ImageIcon icon = new ImageIcon(selection.getAbsolutePath());
			button.setIcon(icon);
			button.invalidate();
			validate();
			repaint();
			
			this.imagePath = selection;
			
			fireSelectionChanged(selection);
		}
	}
	
	public Image getSelectedImage() {
		ImageIcon icon = new ImageIcon(imagePath.getAbsolutePath());
		return icon.getImage();
	}
	
	private void fireSelectionChanged(File selection){
		ActionEvent event = new ActionEvent(this, 1, "");
		for(ActionListener listener : listeners){
			listener.actionPerformed(event);
		}
		for(IconSelectedEvent e : selectionEvents) {
			e.iconSelected(selection);
		}
	}
	
	public File getImagePath() {
		return imagePath;
	}
	
	public void addListener(ActionListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(ActionListener listener){
		listeners.remove(listener);
	}
}
