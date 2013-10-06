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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JPanel {
    private JButton button;
    private JPanel panel_1;
    private JTextArea textAreaConsoleOutput;
    private JScrollPane scrollPane;
    private JPanel panel;
    PipedInputStream piOut;
    PipedInputStream piErr;
    PipedOutputStream poOut;
    PipedOutputStream poErr;

    public Console() throws IOException {
        piOut = new PipedInputStream();
        poOut = new PipedOutputStream(piOut);
        System.setOut(new PrintStream(poOut, true));

        piErr = new PipedInputStream();
        poErr = new PipedOutputStream(piErr);
        System.setErr(new PrintStream(poErr, true));

        setLayout(new BorderLayout());
        setVisible(true);
        add(getPanel(), BorderLayout.CENTER);

        new ReaderThread(piErr, Color.BLUE).start();
        new ReaderThread(piOut, Color.GREEN).start();
    }
    private  synchronized void printText(JTextArea textArea, String text, Color c){
        textArea.append(text);
    }
    class ReaderThread extends Thread {
        PipedInputStream pi;
        Color color;
        ReaderThread(PipedInputStream pi, Color c) {
            this.pi = pi;
            color=c;
        }
      
        public void run() {
            final byte[] buf = new byte[1024];
            try {
                while (true) {
                    final int len = pi.read(buf);
                    if (len == -1) {
                        break;
                    }
                	printText(textAreaConsoleOutput, new String(buf, 0, len), color);
                	textAreaConsoleOutput.setCaretPosition(textAreaConsoleOutput.getDocument().getLength());

                }
            } catch (IOException e) {
            }
        }
    }
	protected JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(getScrollPane(), BorderLayout.CENTER);
			panel.add(getPanel_1(), BorderLayout.SOUTH);
		}
		return panel;
	}
	protected JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextAreaConsoleOutput());
		}
		return scrollPane;
	}
	protected JTextArea getTextAreaConsoleOutput() {
		if (textAreaConsoleOutput == null) {
			textAreaConsoleOutput = new JTextArea();
		}
		return textAreaConsoleOutput;
	}
	protected JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.add(getButton());
		}
		return panel_1;
	}
	
	private void clearScreen(){
		this.textAreaConsoleOutput.setText("");
	}
	protected JButton getButton() {
		if (button == null) {
			button = new JButton();
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clearScreen();
				}
			});
			button.setText("Clear");
		}
		return button;
	}
}
 