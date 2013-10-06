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

import java.math.BigInteger;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;

public class CJNumeric extends JFormattedTextField {
	String mood;
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	
    static protected MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
            formatter.setPlaceholderCharacter('_');
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
	public CJNumeric(){
		super(getNumericFormatter());
		
	}
	
	private static JFormattedTextField.AbstractFormatter getNumericFormatter(){
		return new AbstractFormatter(){
			
			
			protected DocumentFilter getDocumentFilter() {
				return new DocumentFilter(){

					public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
						System.out.println(string);
						char[] chars = string.toCharArray();
						for (int i = 0; i < chars.length; i++) {
							char c = chars[i];
							if(!Character.isDigit(c)){
								
								throw new BadLocationException("Not numeric", i);
							}
						}
					}

					public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
						System.out.println("remove " + offset + " " + length);
						super.remove(fb, offset, length);
					}

					public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
						//System.out.println(text);
						char[] chars = text.toCharArray();
						for (int i = 0; i < chars.length; i++) {
							char c = chars[i];
							if(!Character.isDigit(c)){
								
								throw new BadLocationException("Not numeric", i);
							}
						}
						super.replace(fb, offset, length, text, attrs);
					}
					
				};
			}

			public Object stringToValue(String text) throws ParseException {
						try {
							return new BigInteger(text);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							throw new ParseException("Error",0);
						}
			}

			public String valueToString(Object value) throws ParseException {
				if(value==null)throw new 	ParseException("Error", 0);
				return ((BigInteger)value).toString();
			}
			
		};
	}
}
