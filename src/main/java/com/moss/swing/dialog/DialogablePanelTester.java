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
package com.moss.swing.dialog;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DialogablePanelTester {
	public static void main(String[] args) {
		
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(new JPanel(), BorderLayout.CENTER);
		mainFrame.setSize(400,400);
		mainFrame.setVisible(true);
		
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new BorderLayout());
		testPanel.add(new javax.swing.JButton("test"));
//		JDialog channelDialog = new JDialog();
//		channelDialog.setLayout(new BorderLayout());
//		channelDialog.add(view, BorderLayout.CENTER);
//		channelDialog.setLocationRelativeTo(window);
//		channelDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		channelDialog.setVisible(true);
		
		boolean create = PanelShowerDialog.show(mainFrame, testPanel, "OK");
		if(!create)
			System.exit(1);
		
		
		
	}
}
