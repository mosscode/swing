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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.prefs.Preferences;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class JTableSaver{
	JTable theTable;
	String identifier;
	TreeMap columns = new TreeMap();
	public JTableSaver(String identifier, JTable t){
		System.out.println("Using identifier" + identifier);
		try{
			theTable=t;
			this.identifier = identifier;
			Preferences root = Preferences.userRoot();
			Preferences tableNode = root.node(identifier);

			TableColumnModel colModel = theTable.getColumnModel();
			int numCols = theTable.getColumnCount();
			for(int x=0;x<numCols;x++){
				TableColumn col = colModel.getColumn(x);
				String header = col.getHeaderValue().toString();
				Preferences columnNode = tableNode.node(header);
				int width = columnNode.getInt("Size", -1);
				int position = columnNode.getInt("Position", -1);
				if(width != -1){

					System.out.println("setting " + header + width);
					col.setPreferredWidth(width);

				}else{
					col.sizeWidthToFit();
				}
				if(position != -1){

					columns.put(new Integer(position), col);
				}
			}
			for(Iterator i = columns.entrySet().iterator();i.hasNext();){
				Map.Entry e = (Map.Entry)i.next();
				int oldpos = ((Integer)e.getKey()).intValue();
				TableColumn col = ((TableColumn)e.getValue());
				//System.out.println("Move " + curpos + " to " + oldpos);
				//colModel.moveColumn(curpos, oldpos);
				int x=0;
				for(Enumeration renamedEnum = colModel.getColumns();renamedEnum.hasMoreElements();x++){
					TableColumn c = (TableColumn)renamedEnum.nextElement();
					if(c==col){
						colModel.moveColumn(x, oldpos);
					}
				}
			}

			//int firstRow = tableNode.getInt("FirstVisibleRow", -1);
			//if(firstRow != -1) theTable.
		}catch(Exception err){err.printStackTrace();}
	}

	public void save(){
		try{
			TableColumnModel colModel = theTable.getColumnModel();
			int numCols = theTable.getColumnCount();
			Preferences root = Preferences.userRoot();
			Preferences tableNode = root.node(identifier);

			for(int x=0;x<numCols;x++){
				TableColumn col = colModel.getColumn(x);
				String header = col.getHeaderValue().toString();
				Preferences columnNode = tableNode.node(header);
				int width = col.getWidth();
				System.out.println("values: " + header + width);
				columnNode.put("Size" , Integer.toString(width));
				columnNode.put("Position", Integer.toString(x));
			}

		}catch(Exception err){err.printStackTrace();}
	}

}
