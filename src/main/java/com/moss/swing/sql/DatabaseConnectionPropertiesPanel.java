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
package com.moss.swing.sql;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DatabaseConnectionPropertiesPanel extends JPanel {

    private JButton button;
    private JTextField textField_1;
    private JPasswordField passwordField;
    private JTextField textField;
    public DatabaseConnectionPropertiesPanel() {
        super();
        setLayout(new GridBagLayout());

        final JLabel label = new JLabel();
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 0;
        add(label, gridBagConstraints);
        label.setText("JDBC Url");

        textField = new JTextField();
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_1.weightx = 1;
        gridBagConstraints_1.gridy = 0;
        gridBagConstraints_1.gridx = 1;
        add(textField, gridBagConstraints_1);
        textField.setText("jdbc:jtds:sqlserver://localhost/CHECKMAX");

        final JLabel label_1 = new JLabel();
        final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
        gridBagConstraints_2.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_2.gridy = 1;
        gridBagConstraints_2.gridx = 0;
        add(label_1, gridBagConstraints_2);
        label_1.setText("Username");

        textField_1 = new JTextField();
        final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
        gridBagConstraints_5.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_5.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_5.weightx = 1;
        gridBagConstraints_5.gridy = 1;
        gridBagConstraints_5.gridx = 1;
        add(textField_1, gridBagConstraints_5);

        final JLabel label_2 = new JLabel();
        final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
        gridBagConstraints_3.gridy = 2;
        gridBagConstraints_3.gridx = 0;
        add(label_2, gridBagConstraints_3);
        label_2.setText("Password");

        passwordField = new JPasswordField();
        final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
        gridBagConstraints_4.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_4.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_4.weightx = 1;
        gridBagConstraints_4.gridy = 2;
        gridBagConstraints_4.gridx = 1;
        add(passwordField, gridBagConstraints_4);

        button = new JButton();
        final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
        gridBagConstraints_6.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints_6.anchor = GridBagConstraints.NORTHEAST;
        gridBagConstraints_6.weighty = 1;
        gridBagConstraints_6.gridy = 3;
        gridBagConstraints_6.gridx = 1;
        add(button, gridBagConstraints_6);
        button.setText("Connect");
        //
    }
	public JButton getButtonConnect() {
		return button;
	}
	public JTextField getTextFieldDBUrl() {
		return textField;
	}
	public JTextField getTextFieldUsername() {
		return textField_1;
	}
	public JPasswordField getPasswordField() {
		return passwordField;
	}
}
