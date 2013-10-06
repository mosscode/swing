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
package com.moss.swing.orderedlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.swtdesigner.SwingResourceManager;

public class OrderedListWidgetView extends JPanel {
	private JPanel arrowPanel;
	protected JButton dnButton;
	protected JButton upButton;
	protected JList orderedList;
	public OrderedListWidgetView() {
		super();
		setSize(419, 313);
		setLayout(new BorderLayout());

		final JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		orderedList = new JList();
		scrollPane.setViewportView(orderedList);

		arrowPanel = new JPanel();
		arrowPanel.setLayout(new GridBagLayout());
		add(arrowPanel, BorderLayout.WEST);

		CompoundBorder buttonBorder = BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5,5,5,5)); 
		
		upButton = new JButton();
		upButton.setIcon(SwingResourceManager.getIcon(OrderedListWidgetView.class, "arrowup.png"));
		upButton.setBorder(buttonBorder);
		Border b = upButton.getBorder();
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		arrowPanel.add(upButton, gridBagConstraints);

		dnButton = new JButton();
		dnButton.setIcon(SwingResourceManager.getIcon(OrderedListWidgetView.class, "arrowdown.png"));
		dnButton.setBorder(buttonBorder);
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints_1.gridy = 1;
		gridBagConstraints_1.gridx = 0;
		arrowPanel.add(dnButton, gridBagConstraints_1);
	}
	
	public JButton getUpButton() {
		return upButton;
	}
	public JButton getDnButton() {
		return dnButton;
	}
	public JList getOrderedList() {
		return orderedList;
	}
	public JPanel getArrowPanel() {
		return arrowPanel;
	}
}
