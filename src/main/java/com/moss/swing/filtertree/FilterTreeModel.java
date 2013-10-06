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

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FilterTreeModel extends DefaultTreeModel {
	public static final String 
		SELECT_ALL = "selectall",
		SELECT_NONE = "selectnone",
		MINIMIZE_ALL = "minimizeall",
		MAXIMIZE_ALL = "maximizeall";
	
	public FilterTreeModel (TreeNode root) {
		super(root);
	}
	
	public void valueForPathChanged(TreePath path, Object newValue) {
		FilterTreeNode oldNode = (FilterTreeNode)path.getLastPathComponent();
		FilterTreeNode newNode = (FilterTreeNode)newValue;
		// set all checkboxes of nodes beneath this one to the same value
		setChildrenSelected(oldNode, newNode.isSelected());
		updateParents(oldNode);
		
		fireTreeNodesChanged(this, getPathToRoot(oldNode.getParent()), null, null);
	}
	
	protected void setChildrenSelected(FilterTreeNode node, boolean selected) {
		node.setSelected(selected);
		for (int i=0; i<node.getChildCount(); i++) {
			FilterTreeNode nextNode = (FilterTreeNode)node.getChildAt(i);
			setChildrenSelected(nextNode, selected);
		}
	}
	
	protected void updateParents(FilterTreeNode node) {
		if (node.isRoot()) return;
		
		boolean allSiblingsSelected = true;
		for (int i=0; i<node.getParent().getChildCount(); i++) {
			FilterTreeNode sibling = (FilterTreeNode)node.getParent().getChildAt(i);
			
			if (!sibling.isSelected()) {
				allSiblingsSelected = false;
				break;
			}
		}
		
		((FilterTreeNode)node.getParent()).setSelected(allSiblingsSelected); 
		updateParents((FilterTreeNode)node.getParent());
	}
	
	public void fireAllNodesChanged() {
		fireTreeNodesChanged(this, getPathToRoot(root), null, null);
		fireTreeStructureChanged(this, getPathToRoot(root), null, null);
	}
	
	public void setNodes(String operation) {
		FilterTreeNode root = (FilterTreeNode)getRoot();
		setNodes(root, operation);
		fireTreeNodesChanged(this, getPathToRoot(root), null, null);
	}
	
	protected void setNodes(FilterTreeNode node, String operation) {
		if (SELECT_ALL.equals(operation)) {
			node.setSelected(true);
		}
		else if (SELECT_NONE.equals(operation)) {
			node.setSelected(false);
		}
		
		//else if (MAXIMIZE_ALL.equals(operation)) 
		//else if (MINIMIZE_ALL.equals(operation))
		
		Enumeration children = node.children();
		while (children.hasMoreElements()) {
			FilterTreeNode child = (FilterTreeNode)children.nextElement();
			setNodes(child, operation);
		}
	}
	
	public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
    
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
	
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } 
        else if (parent.getPath().length > 1){
            tree.collapsePath(parent);
        }
    }
}
