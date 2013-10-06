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
package com.moss.balloon.demo;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DemoView extends JPanel{

	private JTextArea textArea_1;
	private JTextArea textArea;
	private JTextField thirdFieldTextTextField;
	private JTextField secondFieldTextTextField;
	private JTextField firstFieldTextTextField;
	
	public DemoView(){
		super();
		setLayout(new GridBagLayout());

		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
		gridBagConstraints_9.weighty = .5;
		gridBagConstraints_9.weightx = 1;
		gridBagConstraints_9.fill = GridBagConstraints.BOTH;
		gridBagConstraints_9.gridx = 0;
		gridBagConstraints_9.gridy = 1;
		gridBagConstraints_9.insets = new Insets(0, 0, 0, 0);
		add(panel, gridBagConstraints_9);

		final JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		gridBagConstraints_1.weighty = 1;
		gridBagConstraints_1.weightx = 1;
		gridBagConstraints_1.fill = GridBagConstraints.BOTH;
		gridBagConstraints_1.gridy = 0;
		gridBagConstraints_1.gridx = 0;
		gridBagConstraints_1.insets = new Insets(5, 5, 5, 5);
		panel.add(bottomPanel, gridBagConstraints_1);

		final JLabel firstFieldLabel = new JLabel();
		firstFieldLabel.setText("First Field:");
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.weightx = 0;
		gridBagConstraints_2.gridx = 0;
		gridBagConstraints_2.gridy = 0;
		gridBagConstraints_2.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(firstFieldLabel, gridBagConstraints_2);

		firstFieldTextTextField = new JTextField();
		firstFieldTextTextField.setText("First Field Text");
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridx = 1;
		gridBagConstraints_3.gridy = 0;
		gridBagConstraints_3.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(firstFieldTextTextField, gridBagConstraints_3);

		final JLabel secondFieldLabel = new JLabel();
		secondFieldLabel.setText("Second Field:");
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.gridx = 0;
		gridBagConstraints_4.gridy = 1;
		gridBagConstraints_4.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(secondFieldLabel, gridBagConstraints_4);

		secondFieldTextTextField = new JTextField();
		secondFieldTextTextField.setText("Second Field Text");
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.weighty = 1;
		gridBagConstraints_6.weightx = 1;
		gridBagConstraints_6.fill = GridBagConstraints.BOTH;
		gridBagConstraints_6.gridx = 1;
		gridBagConstraints_6.gridy = 1;
		gridBagConstraints_6.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(secondFieldTextTextField, gridBagConstraints_6);

		final JLabel thirdFieldLabel = new JLabel();
		thirdFieldLabel.setText("Third Field:");
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.fill = GridBagConstraints.BOTH;
		gridBagConstraints_7.weighty = 1;
		gridBagConstraints_7.weightx = 1;
		gridBagConstraints_7.gridx = 0;
		gridBagConstraints_7.gridy = 3;
		gridBagConstraints_7.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(thirdFieldLabel, gridBagConstraints_7);

		thirdFieldTextTextField = new JTextField();
		thirdFieldTextTextField.setText("Third Field Text");
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.fill = GridBagConstraints.BOTH;
		gridBagConstraints_5.weightx = 1;
		gridBagConstraints_5.weighty = 1;
		gridBagConstraints_5.gridx = 1;
		gridBagConstraints_5.gridy = 3;
		gridBagConstraints_5.gridwidth = 2;
		gridBagConstraints_5.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(thirdFieldTextTextField, gridBagConstraints_5);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		bottomPanel.add(panel_1, gridBagConstraints);

		final JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		final JPanel topPanel = new JPanel();
		final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
		gridBagConstraints_8.fill = GridBagConstraints.BOTH;
		gridBagConstraints_8.weighty = 1;
		gridBagConstraints_8.weightx = 1;
		gridBagConstraints_8.gridx = 0;
		gridBagConstraints_8.gridy = 0;
		gridBagConstraints_8.insets = new Insets(0, 0, 0, 0);
		add(topPanel, gridBagConstraints_8);
		topPanel.setLayout(new BorderLayout());

		final JScrollPane scrollPane_1 = new JScrollPane();
		topPanel.add(scrollPane_1);

		textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
	}
	

	public JTextField getFirstFieldTextTextField() {
		return firstFieldTextTextField;
	}
	public JTextField getSecondFieldTextTextField() {
		return secondFieldTextTextField;
	}
	public JTextField getThirdFieldTextTextField() {
		return thirdFieldTextTextField;
	}
		
}
