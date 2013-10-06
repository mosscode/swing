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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextEditor extends JPanel {
	
	private JPanel toolbarHolderPanel;
	private JPanel editorHolderPanel;
	private JPanel menuBarHolder;
	private JTextArea textArea;
	
	public void setText(String text){
		textArea.setText(text);
	}
	
	public String getText(){
		return textArea.getText();
	}
	boolean includeMenuBar;
	
	public TextEditor(boolean includeMenuBar) {
		this.includeMenuBar = includeMenuBar;
		setLayout(new BorderLayout());
		 
		editorHolderPanel = new JPanel();
		editorHolderPanel.setLayout(new GridBagLayout());
		add(editorHolderPanel, BorderLayout.CENTER);

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(panel, BorderLayout.NORTH);
		
		menuBarHolder = new JPanel();
		panel.add(menuBarHolder, BorderLayout.NORTH);
		menuBarHolder.setLayout(new BorderLayout());

		toolbarHolderPanel = new JPanel();
		panel.add(toolbarHolderPanel, BorderLayout.SOUTH);
		toolbarHolderPanel.setLayout(new BorderLayout());
		
		
		textArea = new JTextArea();
		editorHolderPanel.add(new JScrollPane(textArea), new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0));
	}
}
