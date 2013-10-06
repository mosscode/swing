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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * A label which displays a line of dots which automatically grows to fill the size of this component.
 */
public class ElipsesLine extends JLabel {
	String text="";
	Graphics graphics;
	public ElipsesLine(){
		super("...");
	}
	
	public ElipsesLine(String text){
		super(text);
		this.text = text;
	}
	private FontMetrics fontMetrics;

	protected void paintComponent(Graphics g) {
		Font font = super.getFont();
		fontMetrics = g.getFontMetrics(font);
		graphics = g;
		super.paintComponent(g);
		recalcLabelText();
	}

	public void setSize(Dimension d) {
		super.setSize(d);
		recalcLabelText();
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		recalcLabelText();
	}
	
	public void setText(String text){
		this.text = text;
		recalcLabelText();
		super.setText(text);
	}
	private void recalcLabelText(){
		if(fontMetrics==null)return;
		
		Rectangle2D bounds = fontMetrics.getStringBounds(text, graphics);
		int labelWidth = (int) bounds.getWidth();
		
		int charWidth = fontMetrics.charWidth('.');
		int availableSpace = super.getWidth() - labelWidth;
		int numElipsesChars = availableSpace/charWidth;
//		if(numElipsesChars>3)numElipsesChars -=3;
		
		if (numElipsesChars<0) {
			numElipsesChars = 0;
		}
		
		char[] elipsesChars = new char[numElipsesChars];
		for (int x = 0; x < elipsesChars.length; x++) {
			elipsesChars[x] = '.';
		}
		
		super.setText(text + new String(elipsesChars));
		
	}
	
	public static void main(String[] args){
		JFrame window = new JFrame();
		Container panel = window.getContentPane();
		panel.setLayout(new BorderLayout());
		ElipsesLine line = new ElipsesLine();
		line.setText("HotDogs");
		panel.add(line, BorderLayout.CENTER);
//		panel.add(new JLabel(" Total "), BorderLayout.WEST);
		panel.add(new JLabel(" $234.23 "), BorderLayout.EAST);
		window.setVisible(true);
	}
}
