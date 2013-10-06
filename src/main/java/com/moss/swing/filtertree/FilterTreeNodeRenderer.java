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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;

public class FilterTreeNodeRenderer implements TreeCellRenderer {
	private JPanel panel = new JPanel();
	private JCheckBox checkBox = new JCheckBox();
	private JLabel label = new JLabel();
	private Color selectionBorderColor, selectionForeground, selectionBackground,
		textForeground, textBackground;

	public FilterTreeNodeRenderer() {
		panel.setLayout(new BorderLayout());
		panel.add(checkBox, BorderLayout.WEST);
		panel.add(label, BorderLayout.CENTER);
		panel.setOpaque(false);
		checkBox.setOpaque(false);
		label.setOpaque(false);
		
		selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
	    selectionForeground = UIManager.getColor("Tree.selectionForeground");
	    selectionBackground = UIManager.getColor("Tree.selectionBackground");
	    textForeground = UIManager.getColor("Tree.textForeground");
	    textBackground = UIManager.getColor("Tree.textBackground");
	}
	
	protected JPanel getRenderer() {
		return panel;
	}
	
	protected JCheckBox getCheckBox() {
		return checkBox;
	}
	
	protected JLabel getLabel() {
		return label;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,
		boolean selected, boolean expanded, boolean leaf, int row,
		boolean hasFocus)
	{
		if (selected) {
			label.setOpaque(true);
			label.setForeground(selectionForeground);
			label.setBackground(selectionBackground);
		}
		//else if (checkBox.isSelected()) {}
		else label.setOpaque(false);
		
		FilterTreeNode node = ((FilterTreeNode)value);
		checkBox.setSelected(node.isSelected());
		label.setIcon(node.getIcon());
		label.setText(node.getText());
		return panel;
	}
}
