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
package com.moss.swing.filtertree;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;

public class FilterTreeNodeEditor extends AbstractCellEditor implements TreeCellEditor {
	private FilterTreeNodeRenderer renderer;
	private JTree tree;
	
	public FilterTreeNodeEditor(JTree tree) {
		this.tree = tree;
		
		renderer = new FilterTreeNodeRenderer();
		renderer.getCheckBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (stopCellEditing())fireEditingStopped();
			}
		});
	}
	
	public Object getCellEditorValue() {
		boolean isSelected = renderer.getCheckBox().isSelected();
		String text = renderer.getLabel().getText();
		FilterTreeNode checkBoxNode = new FilterTreeNode(text, isSelected, new ImageIcon());
		return checkBoxNode;
	}
	
	public Component getTreeCellEditorComponent(JTree tree, Object value,
		boolean selected, boolean expanded, boolean leaf, int row) 
	{
		Component editor = renderer.getTreeCellRendererComponent(tree, value,
			true, expanded, leaf, row, true);
		return editor;
	}
	
	public boolean shouldSelectCell(EventObject event) {
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent)event;
			Component c = tree.getComponentAt(mouseEvent.getPoint());
			//System.out.println("foo");
		}
		Object source = event.getSource();
		return (!(source instanceof JCheckBox));
	}
}

