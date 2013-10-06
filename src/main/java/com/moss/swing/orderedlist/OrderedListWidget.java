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
package com.moss.swing.orderedlist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.swtdesigner.SwingResourceManager;

public class OrderedListWidget extends OrderedListWidgetView {
	public OrderedListWidget() {
		getOrderedList().setCellRenderer(new OrderedListItemRenderer());
		
		ArrowListener arrowListener = new ArrowListener();
		super.getUpButton().addActionListener(arrowListener);
		super.getDnButton().addActionListener(arrowListener);
		
		DragListener dragListener = new DragListener(super.getOrderedList());
		super.getOrderedList().addMouseListener(dragListener);
		super.getOrderedList().getSelectionModel().addListSelectionListener(dragListener);
	}
		
	public OrderedListModel getModel() {
		return (OrderedListModel)getOrderedList().getModel();
	}
	
	public void setModel(OrderedListModel model) {
		getOrderedList().setModel(model);
	}
	
	public void setArrowsVisible(boolean showArrows) {
		getArrowPanel().setVisible(showArrows);
		validate();
	}
	
	public List getItems() {
		OrderedListModel model = (OrderedListModel)getOrderedList().getModel();
		
		List list = new ArrayList();
		for (int i=0; i<model.size(); i++) {
			list.add((OrderedListItem)model.get(i));
		}
		return list;
	}
	
	public class OrderedListModel extends DefaultListModel {
		public OrderedListModel() {}
		
		public OrderedListModel(OrderedListItem[] items) {
			for (int i=0; i<items.length; i++) {
				addElement(items[i]);
			}
		}
	}
	
	public class OrderedListItem {
		private String name;
		private Icon icon;
		private boolean selected = false;
		
		public OrderedListItem(String name, Icon icon) {
			this.name = name;
			this.icon = icon;
		}
		
		public Icon getIcon() {
			return icon;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public Object clone() {
			OrderedListItem item = new OrderedListItem(name, icon);
			item.selected = false;
			return item;
		}
	}
	
	public class OrderedListItemRenderer extends JLabel implements ListCellRenderer {
	     public OrderedListItemRenderer() {
	         setOpaque(true);
	     }
	     public Component getListCellRendererComponent(
	         JList list,
	         Object value,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus)
	     {
	    	 	OrderedListItem item = (OrderedListItem)value;
	    	 
	        setText(item.getName());
	        setIcon(item.getIcon());
	        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
	        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
	        
	        if (item.isSelected()) {
	        		setBorder(new LineBorder(Color.black));
	        }
	        else setBorder(new EmptyBorder(1,1,1,1));
	        
	        return this;
	     }
	 }
	
	public class DragListener extends MouseAdapter implements ListSelectionListener {
		private boolean mousePressed = false;
		private int indexDragged = -1;
		private JList list;
		
		public DragListener(JList list) {
			this.list = list;
		}
		
		public void mousePressed(MouseEvent e) {
			mousePressed = true;
			indexDragged = list.getSelectedIndex();
			
			OrderedListItem item = (OrderedListItem)list.getSelectedValue();
			item.setSelected(true);
			list.repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			mousePressed = false;
			indexDragged = -1;
			
			OrderedListItem item = (OrderedListItem)list.getSelectedValue();
			if (item != null) item.setSelected(false);
			list.repaint();
		}

		public void valueChanged(ListSelectionEvent e) {
			if (indexDragged == -1) {
				indexDragged = list.getSelectedIndex();
				return;
			}
			
			OrderedListModel model = (OrderedListModel)list.getModel();
			OrderedListItem[] oldOrder = (OrderedListItem[])getItems().toArray(new OrderedListItem[0]);
	
			int newIndex = list.getSelectedIndex();
			if (mousePressed && indexDragged != newIndex) {
				swapRows(indexDragged, newIndex);
				indexDragged = newIndex;
				
				OrderedListItem[] newOrder = (OrderedListItem[])getItems().toArray(new OrderedListItem[0]);
				firePropertyChange("listOrder", oldOrder, newOrder);
			}
		}

		public void swapRows(int a, int b) {
			DefaultListModel model = (DefaultListModel)list.getModel();
			
			Object rowA = model.get(a);
			model.set(a, model.get(b));
			model.set(b, rowA);
			list.setSelectedIndex(b);
		}

		public int getRowIndexAt(Point p) {
			JList list = getOrderedList();
			DefaultListModel model = (DefaultListModel)list.getModel();
			
			int currentIndex = -1;
			for (int i=0; i< model.size(); i++) {
				Rectangle r = list.getCellBounds(i, i);
				
				if (r.contains(p)) {
					currentIndex = i;
					break;
				}
			}
			return currentIndex;
		}
	}
	
	public class ArrowListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JList list = getOrderedList();
			DefaultListModel model = (DefaultListModel)list.getModel();
			
			int selectedIndex = list.getSelectedIndex();
			int direction = 0;
			
			if (list.getSelectedIndex() == -1) return;
			
			JButton button = (JButton)e.getSource();
			if (button == getUpButton() && selectedIndex > 0) {
				direction = -1;
			}
			else if (button == getDnButton() && selectedIndex < (model.getSize() - 1)) {
				direction = 1;
			}
			else return;
			
			OrderedListItem[] oldOrder = (OrderedListItem[])getItems().toArray(new OrderedListItem[0]);
				
			Object tmp = model.get(selectedIndex);
			model.set(selectedIndex, model.get(selectedIndex + direction));
			model.set(selectedIndex + direction, tmp);
			list.setSelectedIndex(selectedIndex + direction);
			
			OrderedListItem[] newOrder = (OrderedListItem[])getItems().toArray(new OrderedListItem[0]);
			firePropertyChange("listOrder", oldOrder, newOrder);
		}
	}
	
	public static void main(String[] args) {
		Icon companyIcon = SwingResourceManager.getIcon(OrderedListWidget.class, "company.png");
		Icon regionIcon = SwingResourceManager.getIcon(OrderedListWidget.class, "region.png");
		Icon divisionIcon = SwingResourceManager.getIcon(OrderedListWidget.class, "division.png");
		
		OrderedListWidget w = new OrderedListWidget();
		//w.setArrowsVisible(false);
		
		OrderedListItem[] items = new OrderedListItem[] {
			w.new OrderedListItem("Company", companyIcon),
			w.new OrderedListItem("Region", regionIcon),
			w.new OrderedListItem("Division", divisionIcon)
		};
		OrderedListModel model = w.new OrderedListModel(items);
		w.setModel(model);
		
		w.addPropertyChangeListener("listOrder", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("order changed");
			}
		});
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(w);
		frame.setSize(300,300);
		frame.setVisible(true);
	}
}
