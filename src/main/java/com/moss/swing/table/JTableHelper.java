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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
 
 
public class JTableHelper
{
 
    private static final int DEFAULT_COLUMN_PADDING = 5;
 
    
    public static void sizeTable(JTable table){
    	int numColumns = table.getColumnCount();
    	for(int x=0;x<numColumns;x++){
        	TableColumn col = table.getColumnModel().getColumn(x);
        	TableCellRenderer cellRenderer = col.getCellRenderer();
        	int width= getMaxColumnWidth(table, x, true, 0);
//        	if(cellRenderer !=null){
//        		Component c = cellRenderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, x);
//        		if(c instanceof JTextComponent){
//        			JTextComponent textComp = (JTextComponent)c;
//        			
//        			String textLabel = textComp.getText();
//        			Font font = textComp.getFont();
//        			FontMetrics fm = textComp.getFontMetrics(font);
//        			width=SwingUtilities.computeStringWidth(fm, textLabel);
//        			
//        		}
//        	}else{
//        		try
//                {
//                    String headerText = (String)col.getHeaderValue();
//                    JLabel defaultLabel = new JLabel ( headerText );
// 
//                    Font font = defaultLabel.getFont();
//                    FontMetrics fontMetrics = table.getGraphics().getFontMetrics ( font );
// 
//                    width = SwingUtilities.computeStringWidth ( fontMetrics, headerText );
//                }
//                catch ( ClassCastException ce )
//                {
//                    // Can't work out the header column width..
//                    width = 0;
//                    System.out.println("Column " + x + " has no renderer");
//                }
//                
//        	}
        	col.setPreferredWidth(width + 20);
    	}
    	table.doLayout();
    }
    
    /*
     * @param JTable aTable, the JTable to autoresize the columns on
     * @param boolean includeColumnHeaderWidth, use the Column Header width as a minimum width
     * @returns The table width, just in case the caller wants it...
     */
 
    public static int autoResizeTable ( JTable aTable, boolean includeColumnHeaderWidth )
    {
        return ( autoResizeTable ( aTable, includeColumnHeaderWidth, DEFAULT_COLUMN_PADDING ) );
    }
 
 
    /*
     * @param JTable aTable, the JTable to autoresize the columns on
     * @param boolean includeColumnHeaderWidth, use the Column Header width as a minimum width
     * @param int columnPadding, how many extra pixels do you want on the end of each column
     * @returns The table width, just in case the caller wants it...
     */
    public static int autoResizeTable ( JTable aTable, boolean includeColumnHeaderWidth, int columnPadding )
    {
        int columnCount = aTable.getColumnCount();
        int tableWidth  = 0;
 
        Dimension cellSpacing = aTable.getIntercellSpacing();
 
        if ( columnCount > 0 )  // must have columns !
        {
            // STEP ONE : Work out the column widths
 
            int columnWidth[] = new int [ columnCount ];
 
            for ( int i=0; i<columnCount; i++ )
            {
                columnWidth[i] = getMaxColumnWidth ( aTable, i, true, columnPadding );
 
                tableWidth += columnWidth[i];
            }
 
            // account for cell spacing too
            tableWidth += ( ( columnCount - 1 ) * cellSpacing.width );
 
            // STEP TWO : Dynamically resize each column
 
            // try changing the size of the column names area
            JTableHeader tableHeader = aTable.getTableHeader();
 
            Dimension dim = tableHeader.getPreferredSize();
            dim.width = tableWidth;
            tableHeader.setPreferredSize ( dim );
 
            dim = aTable.getPreferredSize();
            dim.width = tableWidth;
            aTable.setPreferredSize ( dim );
 
            TableColumnModel tableColumnModel = aTable.getColumnModel();
            TableColumn tableColumn;
 
            for ( int i=0; i<columnCount; i++ )
            {
                // rowLabelTable
 
                tableColumn = tableColumnModel.getColumn ( i );
 
                tableColumn.setPreferredWidth ( columnWidth[i] );
            }
 
            aTable.doLayout();
        }
 
        return ( tableWidth );
    }
 
 
 
    /*
     * @param JTable aTable, the JTable to autoresize the columns on
     * @param int columnNo, the column number, starting at zero, to calculate the maximum width on
     * @param boolean includeColumnHeaderWidth, use the Column Header width as a minimum width
     * @param int columnPadding, how many extra pixels do you want on the end of each column
     * @returns The table width, just in case the caller wants it...
     */
 
    private static int getMaxColumnWidth ( JTable aTable, int columnNo,
                                           boolean includeColumnHeaderWidth,
                                           int columnPadding )
    {
        TableColumn column = aTable.getColumnModel().getColumn ( columnNo );
        Component comp = null;
        int maxWidth = 0;
 
        if ( includeColumnHeaderWidth )
        {
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if ( headerRenderer != null )
            {
                comp = headerRenderer.getTableCellRendererComponent ( aTable, column.getHeaderValue(), false, false, 0, columnNo );
                if ( comp instanceof JTextComponent )
                {
                    JTextComponent jtextComp = (JTextComponent)comp;
 
                    String text = jtextComp.getText();
                    Font font = jtextComp.getFont();
                    FontMetrics fontMetrics = jtextComp.getFontMetrics ( font );
 
                    maxWidth = SwingUtilities.computeStringWidth ( fontMetrics, text );
                }
                else
                {
                    maxWidth = comp.getPreferredSize().width;
                }
            }
            else
            {
                try
                {
                    String headerText = (String)column.getHeaderValue();
                    JLabel defaultLabel = new JLabel ( headerText );
 
                    Font font = defaultLabel.getFont();
                    FontMetrics fontMetrics = aTable.getGraphics().getFontMetrics ( font );
 
                    maxWidth = SwingUtilities.computeStringWidth ( fontMetrics, headerText );
                }
                catch ( ClassCastException ce )
                {
                    // Can't work out the header column width..
                    maxWidth = 0;
                }
            }
        }
 
        TableCellRenderer tableCellRenderer;
//         Component comp;
        int cellWidth   = 0;
 
        for (int i = 0; i < aTable.getRowCount(); i++)
        {
            tableCellRenderer = aTable.getCellRenderer ( i, columnNo );
 
            comp = tableCellRenderer.getTableCellRendererComponent ( aTable, aTable.getValueAt ( i, columnNo ), false, false, i, columnNo );
 
            if ( comp instanceof JTextComponent )
            {
                JTextComponent jtextComp = (JTextComponent)comp;
 
                String text = jtextComp.getText();
                Font font = jtextComp.getFont();
                FontMetrics fontMetrics = aTable.getGraphics().getFontMetrics ( font );
 
                int textWidth = SwingUtilities.computeStringWidth ( fontMetrics, text );
 
                maxWidth = Math.max ( maxWidth, textWidth );
            }
            else
            {
                cellWidth = comp.getPreferredSize().width;
 
                // maxWidth = Math.max ( headerWidth, cellWidth );
                maxWidth = Math.max ( maxWidth, cellWidth );
            }
        }
 
        return ( maxWidth + columnPadding );
    }
}