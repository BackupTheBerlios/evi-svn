/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JTextField;

import org.schwering.evi.util.RightClickMenu;
import org.schwering.evi.util.IteratorList;

/**
 * The input field with history.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class InputField extends JTextField implements ActionListener, KeyListener {
	protected Vector listeners = new Vector();
	protected IteratorList history = new IteratorList(50);
	
	public InputField() {
		addActionListener(this);
		addKeyListener(this);
		RightClickMenu.addRightClickMenu(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		String s = getText();
		setText("");
		fireInputFired(s);
		history.append(s);
		history.moveBehind();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			String current = getText();
			if (current != null && current.length() > 0 && history.isBehind()) {
				history.append(current);
			}
			if (history.previous() != null) {
				setText((String)history.current());
			} else {
				setText("");
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (history.next() != null) {
				setText((String)history.current());
			} else {
				setText("");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent arg0) {
	}
	
	protected void fireInputFired(String text) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IInputListener)listeners.get(i)).inputFired(text);
		}
	}
	
	public void addListener(IInputListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IInputListener listener) {
		listeners.remove(listener);
	}
}
