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
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * A component which allows the user to swap rows between
 * two table models.
 */
public class MultiSelectWidget extends JComponent {
    private JTable table;
    private MultiSelectWidgetModel model;

    private Frame window;
    private MultiSelectWidgetEditDialog dialog;
    private TableCellRenderer renderer = new DefaultTableCellRenderer();
    
    private void createDialog(){
    	if(dialog!=null) return;
    	if(window==null){
    		Component c = this;
    		while(true){
    			if(c instanceof Frame){
    				window = (Frame) c;
    				break;
    			}
    		
    			if(c==null) break;
    		
    			c = c.getParent();
    		}
    	}
    	
    	dialog = new MultiSelectWidgetEditDialog(window);
    	dialog.getTableOptions().setDefaultRenderer(TableColumn.class, renderer);
    	dialog.getTableSelections().setDefaultRenderer(TableColumn.class, renderer);
    }
    
    private void setup() {
    	
		createDialog();
	
    setLayout(new BorderLayout());

    final JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);

    final JScrollPane scrollPane = new JScrollPane();
    panel.add(scrollPane, BorderLayout.CENTER);

    table = new JTable();
    scrollPane.setViewportView(table);

    final JPanel panel_1 = new JPanel();
    panel.add(panel_1, BorderLayout.SOUTH);

    final JButton button = new JButton();
    button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            openEditDialog();
        }
    });
    panel_1.add(button);
    button.setText("Edit This List");
            
    dialog.getButtonAddOne().addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            addOneRow();
        }
    });
    
    dialog.getButtonRemoveOne().addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            removeOneRow();
        }
    });
    
    dialog.getButtonAddAll().addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            addAllRows();
        }
    });

    dialog.getButtonRemoveAll().addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            removeAllRows();
        }
    });

    dialog.getButtonDone().addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent evt){
            dialog.hide();
        }
    });
    }
	    
    public MultiSelectWidget(){
    	setup();
    }
    
    JButton editButton = new JButton();
    public MultiSelectWidget(TableCellRenderer renderer){

    	this.renderer = renderer;

    	createDialog();

    	setLayout(new BorderLayout());

    	final JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());
    	add(panel, BorderLayout.CENTER);

    	final JScrollPane scrollPane = new JScrollPane();
    	panel.add(scrollPane, BorderLayout.CENTER);

    	table = new JTable();
    	scrollPane.setViewportView(table);

    	final JPanel panel_1 = new JPanel();
    	panel.add(panel_1, BorderLayout.SOUTH);

    	
    	editButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			openEditDialog();
    		}
    	});
    	panel_1.add(editButton);
    	editButton.setText("Edit This List");

    	dialog.getButtonAddOne().addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent evt){
    			addOneRow();
    		}
    	});

    	dialog.getButtonRemoveOne().addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent evt){
    			removeOneRow();
    		}
    	});

    	dialog.getButtonAddAll().addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent evt){
    			addAllRows();
    		}
    	});

    	dialog.getButtonRemoveAll().addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent evt){
    			removeAllRows();
    		}
    	});

    	dialog.getButtonDone().addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent evt){
    			dialog.hide();
    		}
    	});
    }
    
    public MultiSelectWidget(JFrame window) {
        this.window = window;
        setup();
    }
    
    public void addDoneAction(ActionListener listener) {
    	dialog.getButtonDone().addActionListener(listener);
    }
    
    public void setModel(MultiSelectWidgetModel model){
        this.model = model;
        this.getTableSelections().setModel(model.getSelectionsModel());
        dialog.getTableOptions().setModel(model.getOptionsModel());
        dialog.getTableSelections().setModel(model.getSelectionsModel());
        
//      Ask to be notified of selection changes.
        dialog.getTableOptions().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
             public void valueChanged(ListSelectionEvent e) {
                 //Ignore extra messages.
                 if (e.getValueIsAdjusting()) return;

                 ListSelectionModel lsm =
                     (ListSelectionModel)e.getSource();
                 if (lsm.isSelectionEmpty()) {
                     //no rows are selected
                 } else {
                     int selectedRow = lsm.getMinSelectionIndex();
                     //selectedRow is selected
                     optionsRowSelected(selectedRow);
                 }
             }
         });

//      Ask to be notified of selection changes.
        dialog.getTableSelections().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
             public void valueChanged(ListSelectionEvent e) {
                 //Ignore extra messages.
                 if (e.getValueIsAdjusting()) return;

                 ListSelectionModel lsm =
                     (ListSelectionModel)e.getSource();
                 if (lsm.isSelectionEmpty()) {
                     //no rows are selected
                 } else {
                     int selectedRow = lsm.getMinSelectionIndex();
                     //selectedRow is selected
                     System.out.println("Selections table row selected");
                     selectionsRowSelected(selectedRow);
                 }
             }
         });
    }
    
    private int selectionsRow=-1, optionsRow=-1;
    private void selectionsRowSelected(int row){
        this.selectionsRow = row;
        if(selectionsRow>=0){
            dialog.getButtonRemoveOne().setEnabled(true);
        }else{
            dialog.getButtonRemoveOne().setEnabled(false);
        }
    }
    
    private void optionsRowSelected(int row){
        this.optionsRow = row;
        if(optionsRow >=0){
            dialog.getButtonAddOne().setEnabled(true);
        }else{
            dialog.getButtonAddOne().setEnabled(false);
        }
    }
    
    private void setTransferControlsEnabled(boolean enabled){
        dialog.getButtonAddAll().setEnabled(enabled);
        dialog.getButtonAddOne().setEnabled(enabled);
        dialog.getButtonRemoveAll().setEnabled(enabled);
        dialog.getButtonRemoveOne().setEnabled(enabled);
    }
    
    private void removeOneRow(){
        if(selectionsRow>=0){
            System.out.println("Removing row " + selectionsRow);
            model.removeRow(selectionsRow);
        }
    }
    
    private void addOneRow(){
        if(optionsRow >= 0){
            System.out.println("Add row " + optionsRow);
            model.addRow(optionsRow);
        }
    }
    private void addAllRows(){
        int numRows = dialog.getTableOptions().getRowCount();
        for(int rowIndex=0;rowIndex<numRows;rowIndex++){
            model.addRow(0);
        }
    }

    private void removeAllRows(){
        int numRows = dialog.getTableSelections().getRowCount();
        for(int rowIndex=0;rowIndex<numRows;rowIndex++){
            model.removeRow(0);
        }
    }

    private void openEditDialog(){
        dialog.setSize(400,400);
        dialog.show();
    }
    
    public static void main(String[] args){
        JFrame window = new JFrame("Test");
        MultiSelectWidget widget = new MultiSelectWidget(window);
        widget.setModel(new DefaultMultiSelectWidgetModel());
        window.getContentPane().add(widget);
        window.setSize(300, 600);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private JTable getTableSelections() {
        return table;
    }

	public TableCellRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(TableCellRenderer renderer) {
		this.renderer = renderer;
		getTableSelections().setDefaultRenderer(TableColumn.class, renderer);
	}

	public JButton getEditButton() {
		return editButton;
	}

	public void setEditButton(JButton editButton) {
		this.editButton = editButton;
	}

}
