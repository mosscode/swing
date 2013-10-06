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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class DialogablePanel extends JPanel {
	public enum ExitMode {
		EXIT_ON_CLOSE(WindowConstants.EXIT_ON_CLOSE),
		DO_NOTHING_ON_CLOSE(WindowConstants.DO_NOTHING_ON_CLOSE),
		DISPOSE_ON_CLOSE(WindowConstants.DISPOSE_ON_CLOSE),
		HIDE_ON_CLOSE(WindowConstants.HIDE_ON_CLOSE)
		;
		
		public final int value;

		private ExitMode(int value) {
			this.value = value;
		}
		
	}
	
	private ExitMode exitMode = ExitMode.DISPOSE_ON_CLOSE;
	private boolean modal = false;
	private JDialog dialog;
	private String title;
	
	protected DialogablePanel(ExitMode exitMode) {
		super();
		this.exitMode = exitMode;
	}
	
	protected DialogablePanel(ExitMode exitMode, boolean modal) {
		super();
		this.exitMode = exitMode;
		this.modal = modal;
	}

	public ExitMode getExitMode() {
		return exitMode;
	}
	
	public void dispose(){
		if(dialog==null) return;
		else dialog.dispose();
	}
	
	private boolean packWhenCreated = false;
	
	public void pack(){
		if(dialog==null){
			packWhenCreated = true;
		} else {
			dialog.pack();
		}
	}
	
	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public void setExitMode(ExitMode exitMode) {
		this.exitMode = exitMode;
	}
	
	private void initDialog(){
		dialog.getContentPane().add(this);
		dialog.setDefaultCloseOperation(exitMode.value);
		if(packWhenCreated)
			dialog.pack();
	}
	
	public JDialog makeDialogFor(Component c){
		Window w = SwingUtilities.windowForComponent(c);
		System.out.println("w: "+w);
		if(w == null){
			throw new RuntimeException("The component heirarchy for the passed component does not end at a window:" + c);
		}else if(w instanceof Frame){
			return makeDialogFor((Frame)w);
		}else if(w instanceof Dialog){
			return makeDialogFor((Dialog)w);
		}else{
			throw new RuntimeException("Unsupported window type:" + w.getClass().getName());
		}
	}
	public JDialog makeDialogFor(Frame parent){
		dialog = new JDialog(parent, title, modal);
		initDialog();
		return dialog;
	}
	
	public JDialog makeDialogFor(Dialog parent){
		dialog = new JDialog(parent, title, modal);
		initDialog();
		return dialog;
	}
}
