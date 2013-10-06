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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.JFrame;

public class RunningText extends Canvas
implements Runnable, Serializable
{
	
	protected String text;
	protected String fontName;
	protected Color fgColor;
	protected Color bgColor;
	protected int fontStyle;
	protected int fontSize;
	protected boolean shadowOn;
//	protected int height;
//	protected int width;
	protected int sleepingTime;
	protected String _fontStyle;
	protected transient Thread jkThread;
	protected int x;
	protected int rlength;
	protected int rheight;
	protected Image image;
	protected Graphics graphics;
	protected Font f;
	
	private boolean stopping = false;
	
	public RunningText(String s, String s1, Color color, Color color1, int i, int j, boolean flag,
	                   int k, int l, int i1)
	{
		text = s;
		fontName = s1;
		fgColor = color;
		bgColor = color1;
		fontStyle = i;
		fontSize = j;
		shadowOn = flag;
		
		sleepingTime = i1;
		f = new Font(s1, i, j);
	}
	
	public RunningText()
	{
		this("Hello World", "TimesRoman", Color.gray, Color.red, 1, 28, true, 100, 300, 100);
		_fontStyle = "Normal";
		jkThread = new Thread(this);
		jkThread.start();
	}
	
	public String getText()
	{
		return text;
	}
	
	public String getFontName()
	{
		return fontName;
	}
	
	public Color getFgColor()
	{
		return fgColor;
	}
	
	public Color getBgColor()
	{
		return bgColor;
	}
	
	public String getFontStyle()
	{
		return _fontStyle;
	}
	
	public int getFontSize()
	{
		return fontSize;
	}
	
	public boolean getShadowOn()
	{
		return shadowOn;
	}
	
	
	
	public int getDelay()
	{
		return sleepingTime;
	}
	
	public void setText(String s)
	{
		text = s;
	}
	
	public void setFontName(String s)
	{
		fontName = s;
		f = new Font(s, fontStyle, fontSize);
		super.setFont(f);
	}
	
	public void setFgColor(Color color)
	{
		fgColor = color;
	}
	
	public void setBgColor(Color color)
	{
		bgColor = color;
	}
	
	public void setFontStyle(String s)
	{
		_fontStyle = s;
		if(_fontStyle == "Normal")
		{
			f = new Font(fontName, 0, fontSize);
		} else
			if(_fontStyle == "Bold")
			{
				f = new Font(fontName, 1, fontSize);
			} else
				if(_fontStyle == "Italic")
				{
					f = new Font(fontName, 2, fontSize);
				}
		super.setFont(f);
	}
	
	public void setFontSize(int i)
	{
		fontSize = i;
		f = new Font(fontName, fontStyle, i);
		super.setFont(f);
	}
	
	public void setShadowOn(boolean flag)
	{
		shadowOn = flag;
	}
	
	
	
	public void setDelay(int i)
	{
		sleepingTime = i;
	}
	
	private Color parseColorString(String s)
	{
		if(s.length() == 6)
		{
			int i = Integer.valueOf(s.substring(0, 2), 16).intValue();
			int j = Integer.valueOf(s.substring(2, 4), 16).intValue();
			int k = Integer.valueOf(s.substring(4, 6), 16).intValue();
			return new Color(i, j, k);
		} else
		{
			return Color.lightGray;
		}
	}
	
	private int parseFontStyle(String s)
	{
		int i = Integer.valueOf(s).intValue();
		if(i == 1 || i == 2 || i == 3)
		{
			return i;
		} else
		{
			return 1;
		}
	}
	
	public void update(Graphics g)
	{
		if(graphics == null || image.getWidth(null) != getWidth() || image.getHeight(null)!=getHeight())
		{
			image = createImage(getWidth(), getHeight());
			graphics = image.getGraphics();
		}
		paintText(graphics);
		g.drawImage(image, 0, 0, null);
	}
	
	public void paint(Graphics g)
	{
		update(g);
	}
	
	public void paintText(Graphics g)
	{
		int i = (getHeight() + rheight) / 2;
		g.setColor(bgColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(f);
		FontMetrics fontmetrics = g.getFontMetrics();
		rlength = fontmetrics.stringWidth(text);
		rheight = fontmetrics.getHeight();
		if(shadowOn)
		{
			g.setColor(fgColor.darker().darker());
			g.drawString(text, x + 2, i + 2);
		}
		g.setColor(fgColor);
		g.drawString(text, x, i);
	}
	
	
	public synchronized void start()
	{
		if(jkThread == null)
		{
			jkThread = new Thread(this);
			jkThread.start();
		}
	}
	
	public synchronized void stop()
	{
		if(jkThread != null)
		{
			stopping = true;
		}
		jkThread = null;
	}
	
	protected synchronized void calculate()
	{
		x = x - 5;
		if(x < -rlength)
		{
			x = getWidth();
		}
	}
	
	public void run()
	{
		while(jkThread != null)
		{
			if(stopping) return;
			
			try
			{
				Thread.sleep(sleepingTime);
			}
			catch(InterruptedException _ex) { }
			calculate();
			repaint();
		}
	}
	
	private void sizeToFit()
	{
		System.out.println("sizeToFit()");
//		Dimension dimension = getPreferredSize();
//		setSize(dimension.width, dimension.height);
		java.awt.Container container = getParent();
		if(container != null)
		{
			container.invalidate();
			container.doLayout();
		}
	}
	public static void main(String[] args){
		JFrame window = new JFrame("Running Text");
		window.getContentPane().add(new RunningText());
		window.setSize(400,400);
		window.setVisible(true);
		
	}
}