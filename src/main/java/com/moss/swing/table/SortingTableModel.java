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

import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * 
 */
public class SortingTableModel extends AbstractTableModel implements TableModelListener , DecoratingTableModel{
	
	public DecoratingTableModel getDecoratedModel() {
		return null;
	}
	public TableModel getUndecoratedModel() {
		return model;
	}
	
	public int translateDecoratedRow(int row) {
		return 0;
	}
	
	public int translateRootDecoratedRow(int row) {
		return 0;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return model.isCellEditable(rowIndex, columnIndex);
	}

	private TableModel model;
	private int indexes[];
	private Vector sortingColumns = new Vector();
	private Vector sortingOrders = new Vector();
	private boolean bIgnoreCase = false;
	
	
	public SortingTableModel() {
		model = new DefaultTableModel();
		model.addTableModelListener(this);
		this.initializeIndex();
	}
	
	public SortingTableModel(TableModel model) {
		if (model != null) {
			this.model = model;
			model.addTableModelListener(this);
		} else {
			model = new DefaultTableModel();
			model.addTableModelListener(this);
		}
		this.initializeIndex();
	}
	
	public TableModel getTableModel() {
		return model;
	}
	
	
	
	public void setIgnoreCase(boolean bIgnoreCase) {
		this.bIgnoreCase = bIgnoreCase;
	}
	
	public int getTranslatedRowIndex(int rowIndex) throws IllegalStateException {
		if (checkModel() == false) {
			throw new IllegalStateException("Table model inconsistent.");
		}
		return indexes[rowIndex];
	}
	
	public void setValueAt(Object aValue, int row, int column) {
		if (checkModel() == false) {
			return;
		}
		model.setValueAt(aValue, indexes[row], column);
	}
	
	public int getRowCount() {
		return model.getRowCount();
	}
	
	public int getColumnCount() {
		return model.getColumnCount();
	}
	
	public String getColumnName(int column) {
		return model.getColumnName(column);
	}
	
	public Class getColumnClass(int column) {
		return model.getColumnClass(column);
	}
	
	
	public int compareRowsByColumn(int row1, int row2, int column) {
		Object value1 = model.getValueAt(row1, column);
		Object value2 = model.getValueAt(row2, column);
		int result = 0;
		if (value1 == null) { //null value is always less
			return -1;
		}
		if (value2 == null) { //null value is always less
			return 1;
		}
		if (value1 instanceof Comparable && value2 instanceof Comparable) {
			if (bIgnoreCase && value1 instanceof String && value2 instanceof String) {
				result = (value1.toString().toUpperCase()).compareTo(value2.toString().toUpperCase());
			} else {
				if (value1.toString().equals("")) {
					return -1;
				}
				if (value2.toString().equals("")) {
					return 1;
				}
				result = ( (Comparable) value1).compareTo( (Comparable) value2);
			}
		} else {
			result = value1.toString().compareTo(value2.toString());
		}
		if (result < 0) {
			return -1;
		}
		if (result > 0) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * this method uses compareRowsByColumn method to copmare values of cells
	 * in the given two rows and whose column index is in sortingColumns
	 * and sorting order is in sortingOrders.
	 *
	 * @param int row1, row2 - row indexes for cells to be compared.
	 * @return int - comparison result of the values in two rows.
	 */
	public int compare(int row1, int row2) {
		for (int i = 0; i < sortingColumns.size(); i++) {
			Integer column = (Integer) sortingColumns.elementAt(i);
			boolean ascending = ( (Boolean) sortingOrders.elementAt(i)).booleanValue();
			int result = compareRowsByColumn(row1, row2, column.intValue());
			if (result != 0) {
				return ascending ? result : -result;
			}
		}
		return 0;
	}
	
	public void initializeIndex() {
		int rowCount = model.getRowCount();
		indexes = new int[rowCount];
		for (int i = 0; i < rowCount; i++) {
			indexes[i] = i;
		}
	}
	
	public boolean checkModel() {
		if (
					indexes.length != 
						model.getRowCount()
						) {
			System.out.println("index is not synchronized");
			return false;
		}
		return true;
	}
	
	public void sort() {
		if (checkModel() == false) {
			return;
		}
		if (indexes.length == 0) {
			return;
		}
		mergeSort(indexes, 0, indexes.length - 1);
	}
	
	/**
	 * This is a general merge sort algorithm uses compare method to compare values
	 * in cells.
	 *
	 * @param int[] a - the array to be sorted.
	 * @param low, high - the part of the array to be sorted from index low to index high.
	 */
	void mergeSort(int a[], int low, int high) {
		if (low == high) {
			return;
		}
		int length = high - low + 1;
		int pivot = (low + high) / 2;
		mergeSort(a, low, pivot);
		mergeSort(a, pivot + 1, high);
		int working[] = new int[length];
		for (int i = 0; i < length; i++) {
			working[i] = a[low + i];
		}
		int m1 = 0;
		int m2 = pivot - low + 1;
		for (int i = 0; i < length; i++) {
			if (m2 <= high - low) {
				if (m1 <= pivot - low) {
					if (compare(working[m1], working[m2]) > 0) {
						a[i + low] = working[m2++];
					} else {
						a[i + low] = working[m1++];
					}
				} else {
					a[i + low] = working[m2++];
				}
			} else {
				a[i + low] = working[m1++];
			}
		}
	}
	
	public Object getValueAt(int row, int column) {
		if (checkModel() == false) {
			return null;
		}
		return model.getValueAt(indexes[row], column);
	}
	
	public void sortByColumn(int column, boolean ascending) {
		System.out.println("Sorting by column");
		sortingColumns.removeAllElements();
		sortingOrders.removeAllElements();
		sortingColumns.addElement(new Integer(column));
		sortingOrders.addElement(new Boolean(ascending));
		sort();
		this.fireTableChanged(new TableModelEvent(this));
	}
	
	public void sortByColumn(int column) {
		sortByColumn(column, true);
	}
	
	public void sortByColumns(Vector columns, Vector orders) {
		if (columns.size() != orders.size()) {
			return;
		}
		
		sortingColumns = columns;
		sortingOrders = orders;
		
		sort();
		this.fireTableChanged(new TableModelEvent(this));
	}
	
	public void sortByColumns(Vector columns) {
		Vector orders = new Vector(columns.size());
		for (int i = 0; i < orders.size(); i++) {
			orders.add(new Boolean(true));
		}
		sortByColumns(columns, orders);
	}
	
	/**
	 * Implementation of the TableModelListener interface, init row mapping array
	 * if there is row added/deleted.
	 *
	 * @param TableModelEvent e - an event indicate that table model has changed.
	 */
	public void tableChanged(TableModelEvent e) {
		//Debug.println("tableChanged");
		initializeIndex();
		fireTableChanged(e);
	}
	/**
	 * @return Returns the model.
	 */
	public TableModel getModel() {
		return model;
	}
	/**
	 * @param model The model to set.
	 */
	public void setModel(TableModel model) {
		this.model = model;
		model.addTableModelListener(this);
	}
}