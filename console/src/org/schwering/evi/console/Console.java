/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JPanel;

import javax.swing.border.TitledBorder;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.util.RightClickMenu;

/**
 * The IModule class for the console module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class Console extends JPanel implements IModule, IPanel {
	private static final long serialVersionUID = 2735909201802891383L;
	
	private static Console instance = null;
	
	public static Console getInstance() {
		if (instance == null) {
			instance = new Console();
		}
		return instance;
	}

	private Console() {
		final PrintStream oldOut = System.out;
		final PrintStream oldErr = System.err;
		final DefaultStyledDocument doc = new DefaultStyledDocument();
		final JTextPane pane = new JTextPane(doc);
		final Color outColor = ConsoleConfiguration.getOutColor();
		final Color errColor = ConsoleConfiguration.getErrColor();
		final SimpleAttributeSet attrOut = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrOut, "Monospaced");
		StyleConstants.setForeground(attrOut, outColor);
		final SimpleAttributeSet attrErr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrErr, "Monospaced");
		StyleConstants.setForeground(attrErr, errColor);
		
		PrintStream newOut = new PrintStream(new OutputStream() {
			public void write(int i) throws IOException {
				oldOut.write(i);
				try {
					doc.insertString(doc.getLength(), 
							Character.toString((char)i), attrOut);
					pane.setCaretPosition(doc.getLength());
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		System.setOut(newOut);
		
		PrintStream newErr = new PrintStream(new OutputStream() {
			public void write(int i) throws IOException {
				oldErr.write(i);
				try {
					doc.insertString(doc.getLength(), 
							Character.toString((char)i), attrErr);
					pane.setCaretPosition(doc.getLength());
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		System.setErr(newErr);
		
		pane.setEditable(false);
		RightClickMenu.addRightClickMenu(pane);
		
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(Messages.getString("Console.Console.CONSOLE_BORDER_TITLE"))); //$NON-NLS-1$
		add(new JScrollPane(pane));
	}

	public Icon getIcon() {
		return null;
	}

	public Component getPanelInstance() {
		return this;
	}

	public String getTitle() {
		return Messages.getString("Console.CONSOLE_TITLE"); //$NON-NLS-1$
	}

	public void dispose() {
		instance = null;
	}

}
