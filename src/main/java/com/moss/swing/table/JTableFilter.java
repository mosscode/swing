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
package com.moss.swing.table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class JTableFilter implements TableModel, TableModelListener{
	TableModel model;
//	TreeMap map;
	String filter;
	LinkedList list = new LinkedList();
	public JTableFilter(TableModel model){
		this.model=model;
		model.addTableModelListener(this);
		setFilter("");
	}

	public void setFilter(String filter){
		System.out.println("Set Filter");
		this.filter=filter;
		list=new LinkedList();
		boolean match=false;
		filter = filter.trim().toUpperCase();
		int numRows = model.getRowCount();
		int numCols = model.getColumnCount();
		for(int x=0;x<numRows;x++){// for each row in model
			match=false;
			for(int y=0;y<numCols;y++){//for each cell in row
				Object o = model.getValueAt(x,y);
				if(o!=null){
					String tempCell = o.toString();
					tempCell = tempCell.toUpperCase();
					//System.out.println(tempCell);
					if(-1 != tempCell.indexOf(filter) | filter.equals("")){
						match=true;
					}

				}
			}
			if(match){
				list.add(new Integer(x));
			}

		}
		fireDataChanged();
	}

	public void fireDataChanged(){
		for(Iterator i = listeners.iterator();i.hasNext();){
			TableModelListener l = (TableModelListener)i.next();
			l.tableChanged(new TableModelEvent(this));
		}

	}
	public void fireDataChanged(TableModelEvent e){
		for(Iterator i = listeners.iterator();i.hasNext();){
			TableModelListener l = (TableModelListener)i.next();
			l.tableChanged(e);
		}

	}

	public void tableChanged(TableModelEvent e){
		System.out.println("table data changed");
		setFilter(filter);
		fireDataChanged();
	}

	ArrayList listeners = new ArrayList();
	public void addTableModelListener(TableModelListener l){
		if(l!=null)listeners.add(l);
		//model.addTableModelListener(l);
	}

	public Class getColumnClass(int columnIndex){
		return model.getColumnClass(columnIndex);
	}

	public int getColumnCount(){
		return model.getColumnCount();
	}

	public String getColumnName(int columnIndex){
		return model.getColumnName(columnIndex);
	}

	public int getRowCount(){
		return list.size();
	}

	public int translate(int filterIndex){
		return ((Integer)list.get(filterIndex)).intValue();
	}
	public Object getValueAt(int rowIndex, int columnIndex){
		return model.getValueAt(translate(rowIndex), columnIndex);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex){
		return model.isCellEditable(rowIndex, columnIndex);
	}

	public void removeTableModelListener(TableModelListener l){
		listeners.remove(l);
		//model.removeTableModelListener(l);
	}

	public void setValueAt(Object value, int row, int col){
		model.setValueAt(value,translate(row),col);
	}
}
