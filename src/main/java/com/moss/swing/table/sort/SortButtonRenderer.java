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
package com.moss.swing.table.sort;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SortButtonRenderer extends DefaultTableCellRenderer{
    public static final int NONE = 0;
    public static final int DOWN = 1;
    public static final int UP   = 2;

    protected static final Color DEFAULT_BACKGROUND = new Color(153,188,207);//Color.LIGHT_GRAY;
    protected static final Color DEFAULT_FOREGROUND = new Color(12,50,116);
    protected static final Font DEFAULT_FONT=new Font("SansSerif", Font.BOLD, 12);
    protected Color background;
    protected Color foreground;
    protected Font font;

    int pushedColumn;
    Hashtable states;

	public SortButtonRenderer()
	{
	    super();
	    states = new Hashtable();
    }

    public void clearStates(){
        states.clear();
    }

    public void setBackground(Color background){
        this.background=background;
    }

    public void setForeground(Color foreground){
        this.foreground=foreground;
    }
    public void setFont(Font font){
        this.font=font;
    }

//return the value in use
    public Color getBackground(){
        return this.background==null?this.DEFAULT_BACKGROUND:this.background;
    }

    public Color getForeground(){
        return this.foreground==null?this.DEFAULT_FOREGROUND:this.foreground;
    }

    public Font getFont(){
        return this.font==null?this.DEFAULT_FONT:this.font;
    }
  /**
  * This method set the new sort state for selected column.
  *
  * @param int col - the selected column
  */
  public void setSelectedColumn(int col) {
    if (col < 0) return;
    Integer value = null;
    Object obj = states.get(new Integer(col));
    if (obj == null || ((Integer)obj).intValue() == UP) {
        value = new Integer(DOWN);
    } else {
        value = new Integer(UP);
    }
    states.clear();
    states.put(new Integer(col), value);
  }

  /**
  * This method is used to get the sort state of a column.
  *
  * @param int col - the column to get the sort state of
  * @return int - the sort state
  */
  public int getState(int col) {
    int retValue;
    Object obj = states.get(new Integer(col));
    if (obj == null) {
        retValue = NONE;
    } else {
        if (((Integer)obj).intValue() == DOWN) {
            retValue = DOWN;
        } else {
            retValue = UP;
        }
    }
    return retValue;
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column)
  {

    JButton button = new JButton();
    button.setMargin(new Insets(0,0,0,0));
    button.setHorizontalTextPosition(LEFT);
    button.setBackground(this.background==null?this.DEFAULT_BACKGROUND:this.background);
    button.setForeground(this.foreground==null?this.DEFAULT_FOREGROUND:this.foreground);
    button.setFont(this.font==null?this.DEFAULT_FONT:this.font);

    Object obj = states.get(new Integer(column));
    if (obj != null) {
        if (((Integer)obj).intValue() == DOWN) {
            button.setIcon(new ArrowIcon(ArrowIcon.DOWN, false, false));
            button.setPressedIcon(new ArrowIcon(ArrowIcon.DOWN, false, true));
        } else {
            button.setIcon(new ArrowIcon(ArrowIcon.UP, false, false));
            button.setPressedIcon(new ArrowIcon(ArrowIcon.UP, false, true));
        }
    }else{
        button.setIcon(null);
    }

    button.setText((value ==null) ? "" : value.toString());
    return button;
  }
}