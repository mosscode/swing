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
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PanelShowerDialog extends JDialog {
	private static class CancelAction extends AbstractAction {
		public CancelAction() {
			super("Cancel");
		}
		
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	public static boolean show(Component f, JPanel panel, String nonCancelActionName){
		List<Action> theActions = new LinkedList<Action>();
		theActions.add(new CancelAction());
		theActions.add(new AbstractAction(nonCancelActionName){
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		PanelShowerDialog dialog;
		
		Window w = null;
		if( f.getParent() != null) {
			w = SwingUtilities.windowForComponent(f);
		} else {
			w = (Window)f;
		}
		System.out.println("w: "+w);
		if(w instanceof Frame || w instanceof JFrame){
			System.out.println("w instanceof Frame");
			dialog = new PanelShowerDialog((Frame)w, panel, theActions);
		}else{
			System.out.println("else w ! instanceof Frame");
			dialog = new PanelShowerDialog((Dialog)w, panel, theActions);
		}
		
		dialog.pack();
		dialog.show();
		return dialog.waitForSelection()!=null;
	}
	public static Action show(Component f, JPanel panel, Action...actions){
		List<Action> theActions = new LinkedList<Action>();
		theActions.add(new CancelAction());
		theActions.addAll(Arrays.asList(actions));
		
		PanelShowerDialog dialog;
		Window w = SwingUtilities.windowForComponent(f);
		if(w instanceof Frame){
			dialog = new PanelShowerDialog((Frame)w, panel, theActions);
		}else{
			dialog = new PanelShowerDialog((Dialog)w, panel, theActions);
		}
		
		dialog.pack();
		dialog.show();
		return dialog.waitForSelection();
	}
	private JPanel panel;
	private List<Action> actions;
	private Action selection;
	
	public PanelShowerDialog(Frame f, JPanel panel, Action...actions) throws HeadlessException {
		this(f, panel, actions!=null?Arrays.asList(actions):new LinkedList<Action>());
	}
	public PanelShowerDialog(Dialog f, JPanel panel, Action...actions) throws HeadlessException {
		this(f, panel, actions!=null?Arrays.asList(actions):new LinkedList<Action>());
	}
		
	public PanelShowerDialog(Frame parent, JPanel panel, List<Action> actions) throws HeadlessException {
		super(parent);
		init(panel, actions);
	}
	
	public PanelShowerDialog(Dialog parent, JPanel panel, List<Action> actions) throws HeadlessException {
		super(parent);
		init(panel, actions);
	}
	
	private void init(JPanel panel, List<Action> actions)throws HeadlessException {
		this.actions = actions;
		this.panel = panel;
		
		JPanel buttonsPanel = new JPanel();
		for (final Action action : actions) {
			JButton button = new JButton(action);
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					selection = action;
					setVisible(false);
					synchronized(hideLock){
						hideLock.notifyAll();
					}
				}
			});
			buttonsPanel.add(button);
			
		}
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
	}	
	
	private Object hideLock = new Object();
	public Action waitForSelection(){
		synchronized(hideLock){
			try {
				hideLock.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		if(selection instanceof CancelAction)
			selection=null;
		return selection;
	}
}
