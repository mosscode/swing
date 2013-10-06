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

package com.moss.swing.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ResultSetTableModel implements TableModel {
	private ResultSet data;
	private List listeners = new ArrayList();
	
	private int numRows = -1;
	private boolean returnRealColumnClasses = false;
	
	public ResultSetTableModel(ResultSet data) throws SQLException {
		this(data, false);
	}
	
	/**
	 * Counts the number of rows for you.
	 * @param data
	 * @throws SQLException
	 */
	public ResultSetTableModel(ResultSet data, boolean returnRealColumnClasses) throws SQLException {
		this(data, countRows(data));
		this.returnRealColumnClasses = returnRealColumnClasses;
	}
	
	/**
	 * This expects you to count the number of rows yourself
	 * @param data
	 * @param numRows
	 */
	public ResultSetTableModel(ResultSet data, int numRows){
		this.data = data;
		this.numRows = numRows;
	}
	
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	public Class getColumnClass(int columnIndex) {
		try {
			if(returnRealColumnClasses){
				return Class.forName(data.getMetaData().getColumnClassName(columnIndex + 1));
			}else{
				return String.class;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getColumnCount() {
		try{
			return data.getMetaData().getColumnCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getColumnName(int columnIndex) {
		try{
			return data.getMetaData().getColumnLabel(columnIndex +1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR!";
	}

	public int getRowCount() {
		return numRows;
	}
	
	
	public static int countRows(ResultSet rs) throws SQLException{
		boolean result = rs.last();
		if(!result) return 0;
		else return rs.getRow();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		try {
			int currentRow = data.getRow();
			data.absolute(rowIndex+1);
			
			if(data.getRow() != rowIndex +1){
				throw new IllegalStateException("Cannot scroll to row " + (rowIndex + 1) + " (current row was " + currentRow + ", and now we're at row " + data.getRow() + ")");
			}
			
			Object value = data.getObject(columnIndex+1);
			if(value==null){
				return "";
			}
			return value;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
	
}
 