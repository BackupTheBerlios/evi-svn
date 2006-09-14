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

public class Console extends JPanel implements IModule, IPanel {
	
	public Console() {
		final PrintStream oldOut = System.out;
		final PrintStream oldErr = System.err;
		final SimpleAttributeSet attr = new SimpleAttributeSet();
		final DefaultStyledDocument doc = new DefaultStyledDocument();
		StyleConstants.setFontFamily(attr, "Monospaced");
		
		PrintStream newOut = new PrintStream(new OutputStream() {
			public void write(int i) throws IOException {
				oldOut.write(i);
				try {
					StyleConstants.setForeground(attr, Color.BLACK);
					doc.insertString(doc.getLength(), 
							Character.toString((char)i), attr);
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
					StyleConstants.setForeground(attr, Color.RED);
					doc.insertString(doc.getLength(), 
							Character.toString((char)i), attr);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		System.setErr(newErr);
		
		JTextPane pane = new JTextPane(doc);
		pane.setEditable(false);
		RightClickMenu.addRightClickMenu(pane);
		
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("Console"));
		add(new JScrollPane(pane));
	}

	public Icon getIcon() {
		return null;
	}

	public Component getPanelInstance() {
		return this;
	}

	public String getTitle() {
		return "Console";
	}

	public void dispose() {
	}

}
