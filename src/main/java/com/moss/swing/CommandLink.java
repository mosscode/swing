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
package com.moss.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JLabel;

public class CommandLink extends JLabel {
	private Action action;
	private List<ActionListener> listeners = new LinkedList<ActionListener>();
	
	public CommandLink(Action action){
		this();
		setAction(action);
	}
	public CommandLink(String text){
		this();
		setText(text);
	}
	public CommandLink() {
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				fireMouseClicked();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				highlight();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				normal();
			}
		});
		setText("");
		
		Map map = getFont().getAttributes();
	    map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	    map.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
	    Font newFont = new Font(map);
	    
	    super.setFont(newFont);
	    
	    setForeground(Color.blue);
	}
	
	
	public void setAction(Action action){
		if(this.action!=null){
			listeners.remove(this.action);
		}
		this.action = action;
		addActionListener(action);
		setText((String) action.getValue(Action.NAME));
	}
	
	public void addActionListener(ActionListener action){
		listeners.add(action);
	}
	
	public void removeActionListener(ActionListener action){
		listeners.remove(action);
	}
	
	private void fireMouseClicked(){
		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(this, 1, ""));
		}
	}
	
	private String text;
	
	public void setText(String text){
		this.text = text;
		super.setText("<html><body><u>" + text + "</u></body></html>");
		
	}
	
	@Override
	public String getText() {
		return text;
	}
	private void highlight(){
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	private void normal(){
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}
}
