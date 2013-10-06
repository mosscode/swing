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
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Redirects System.out and System.err to a scrolling text component
 */

public class JConsole extends JComponent {
        PipedInputStream piOut;
        PipedInputStream piErr;
        PipedOutputStream poOut;
        PipedOutputStream poErr;
        JTextArea textArea = new JTextArea();
        PrintStream origOut;
        public JConsole() throws IOException {
          origOut = System.out;
            // Set up System.out
            piOut = new PipedInputStream();
            poOut = new PipedOutputStream(piOut);
            System.setOut(new PrintStream(poOut, true));

            // Set up System.err
            piErr = new PipedInputStream();
            poErr = new PipedOutputStream(piErr);
            System.setErr(new PrintStream(poErr, true));

            // Add a scrolling text area
            textArea.setEditable(false);
            textArea.setRows(20);
            textArea.setColumns(50);
            //getContentPane().
            setLayout(new BorderLayout());
            add(new JScrollPane(textArea), BorderLayout.CENTER);
            //pack();
            setVisible(true);

            // Create reader threads
            new ReaderThread(piOut).start();
            new ReaderThread(piErr).start();
        }

        class ReaderThread extends Thread {
            PipedInputStream pi;

            ReaderThread(PipedInputStream pi) {
                this.pi = pi;
            }

            public void run() {
                final byte[] buf = new byte[1024];
                try {
                    while (true) {
                        final int len = pi.read(buf);
                        if (len == -1) {
                            break;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                              String text = new String(buf, 0, len);
                                textArea.append(text);
                                origOut.print(text);
                                // Make sure the last line is always visible
                                textArea.setCaretPosition(textArea.getDocument().getLength());

                                // Keep the text area down to a certain character size
                                /*int idealSize = 1000;
                                int maxExcess = 500;
                                int excess = textArea.getDocument().getLength() - idealSize;
                                if (excess >= maxExcess) {
                                    textArea.replaceRange("", 0, excess);
                                }
                  */
                            }
                        });
                    }
                } catch (IOException e) {
                }
            }
        }
    }
