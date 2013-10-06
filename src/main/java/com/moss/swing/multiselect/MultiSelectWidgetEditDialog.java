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
package com.moss.swing.multiselect;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

class MultiSelectWidgetEditDialog extends JDialog{
    private JButton button_5;
    private JButton button_3;
    private JButton button_2;
    private JButton button_1;
    private JButton button;
    private JTable table_1;
    private JTable table;
    java.awt.Frame frame;
    
    public MultiSelectWidgetEditDialog(java.awt.Frame frame) {
        super(frame, true);
        System.out.println("frame: "+frame);
        this.frame = frame;
        final BorderLayout borderLayout = new BorderLayout();
        getContentPane().setLayout(borderLayout);
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        getContentPane().add(panel);
        setTitle("Edit List");
        borderLayout.setHgap(5);
        borderLayout.setVgap(5);
        
        final JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(.5);
        final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
        gridBagConstraints_4.weighty = 1;
        gridBagConstraints_4.weightx = 1;
        gridBagConstraints_4.fill = GridBagConstraints.BOTH;
        gridBagConstraints_4.gridx = 0;
        gridBagConstraints_4.gridy = 0;
        gridBagConstraints_4.insets = new Insets(5, 5, 0, 5);
        panel.add(splitPane, gridBagConstraints_4);

        final JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        splitPane.setRightComponent(panel_1);

        final JScrollPane scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1);

        table_1 = new JTable();
        scrollPane_1.setViewportView(table_1);

        final JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout());
        splitPane.setLeftComponent(panel_2);

        final JPanel panel_3 = new JPanel();
        panel_3.setLayout(new BorderLayout());
        panel_2.add(panel_3);

        final JScrollPane scrollPane = new JScrollPane();
        panel_3.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        final JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridBagLayout());
        panel_2.add(controlsPanel, BorderLayout.EAST);

        button = new JButton();
        final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
        gridBagConstraints_3.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_3.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_3.weightx = 1;
        controlsPanel.add(button, gridBagConstraints_3);
        button.setText("<<");

        button_1 = new JButton();
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridx = 0;
        controlsPanel.add(button_1, gridBagConstraints);
        button_1.setText("<");

        button_2 = new JButton();
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_1.weightx = 1;
        gridBagConstraints_1.gridy = 2;
        gridBagConstraints_1.gridx = 0;
        controlsPanel.add(button_2, gridBagConstraints_1);
        button_2.setText(">");

        button_3 = new JButton();
        final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
        gridBagConstraints_2.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_2.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints_2.weightx = 1;
        gridBagConstraints_2.gridy = 3;
        gridBagConstraints_2.gridx = 0;
        controlsPanel.add(button_3, gridBagConstraints_2);
        button_3.setText(">>");

        final JPanel panel_4 = new JPanel();
        panel_4.setLayout(new GridBagLayout());
        final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
        gridBagConstraints_5.fill = GridBagConstraints.BOTH;
        gridBagConstraints_5.weightx = 1;
        gridBagConstraints_5.gridx = 0;
        gridBagConstraints_5.gridy = 1;
        gridBagConstraints_5.insets = new Insets(0, 0, 0, 0);
        panel.add(panel_4, gridBagConstraints_5);

        final JPanel panel_5 = new JPanel();
        final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
        gridBagConstraints_8.weightx = 1;
        gridBagConstraints_8.gridy = 0;
        gridBagConstraints_8.gridx = 0;
        panel_4.add(panel_5, gridBagConstraints_8);

        button_5 = new JButton();
        final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
        gridBagConstraints_7.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints_7.gridy = 0;
        gridBagConstraints_7.gridx = 2;
        panel_4.add(button_5, gridBagConstraints_7);
        button_5.setText("Done");
        
        centerLocation();
    }
	private void centerLocation() {
		int mainFrameX = frame.getLocation().x;
		int mainFrameY = frame.getLocation().y;
		int centerLocMainFrameX = mainFrameX + frame.getWidth() / 2;
		int centerLocMainFrameY = mainFrameY + frame.getHeight() / 2;
		
		int centerThisFrameX = this.getWidth()/2;
		int centerThisFrameY = this.getHeight()/2;
		
		setLocation(centerLocMainFrameX - centerThisFrameX, centerLocMainFrameY - centerThisFrameY);
	}
    public JTable getTableSelections() {
        return table_1;
    }
    public JTable getTableOptions() {
        return table;
    }
    public JButton getButtonRemoveAll() {
        return button;
    }
    public JButton getButtonRemoveOne() {
        return button_1;
    }
    public JButton getButtonAddOne() {
        return button_2;
    }
    public JButton getButtonAddAll() {
        return button_3;
    }
    public JButton getButtonDone() {
        return button_5;
    }
    
}