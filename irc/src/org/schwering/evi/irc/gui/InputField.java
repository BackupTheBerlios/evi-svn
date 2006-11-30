/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.text.Document;

import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.RightClickMenu;
import org.schwering.evi.util.IteratorList;
import org.schwering.evi.util.TextField;
import org.schwering.irc.lib.IRCConstants;

/**
 * The input field with history.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class InputField extends TextField implements ActionListener, KeyListener {
	private static final long serialVersionUID = 3914942143557323290L;
	protected Vector listeners = new Vector();
	protected IteratorList history = new IteratorList(50);
	
	public InputField() {
		this(null);
	}
	
	public InputField(Profile p) {
		addActionListener(this);
		addKeyListener(this);
		RightClickMenu.addRightClickMenu(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		String s = getText();
		if (s != null && s.length() > 0) {
			setText("");
			fireInputFired(s);
			history.append(s);
			history.moveBehind();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			String current = getText();
			if (current != null && current.length() > 0 && history.isBehind()) {
				history.append(current);
			}
			if (history.previous() != null) {
				setText((String)history.current());
			} else {
				setText("");
			}
		} else if (key == KeyEvent.VK_DOWN) {
			if (history.next() != null) {
				setText((String)history.current());
			} else {
				setText("");
			}
		} else if (key == KeyEvent.VK_RIGHT) {
			tryNickCompletion();
		} else if (key == KeyEvent.VK_B 
				&& e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
			insertText(String.valueOf(IRCConstants.BOLD_INDICATOR));
		} else if (key == KeyEvent.VK_U 
				&& e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
			insertText(String.valueOf(IRCConstants.UNDERLINE_INDICATOR));
		} else if (key == KeyEvent.VK_R 
				&& e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
			insertText(String.valueOf(IRCConstants.COLOR_REVERSE_INDICATOR));
		} else if (key == KeyEvent.VK_K 
				&& e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
			insertText(String.valueOf(IRCConstants.COLOR_INDICATOR));
		} else if (key == KeyEvent.VK_L 
				&& e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
			insertText(String.valueOf(IRCConstants.COLOR_END_INDICATOR));
		}
	}
	
	/**
	 * Tries to complete a already typed-in phrase to a nickname.
	 */
	private void tryNickCompletion() {
		int pos = getCaretPosition();
		
		if (pos == 0)
			return;
		if (charAt(pos - 1) == ' ')
			return;
		
		int c = charAt(pos);
		int prevSpace = previousIndexOf(pos-1, ' ');
		if (prevSpace == -1)
			prevSpace = 0;
		
		if (c == ' ' || c == '\n') {
			String s = getText(prevSpace, pos - prevSpace);
			System.out.println("Completing '"+ s +"'");
			try {
				getDocument().insertString(pos, "TABCOMPL", null);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the next index of c, seen from the current caret position.
	 * @param c The searched character.
	 * @return The index or -1.
	 */
	public int nextIndexOf(int c) {
		return nextIndexOf(getCaretPosition(), c);
	}
	
	/**
	 * Returns the next index of c.
	 * @param pos The position.
	 * @param c The searched character.
	 * @return The index or -1.
	 */
	public int nextIndexOf(int pos, int c) {
		Document doc = getDocument();
		for (int i = pos; i < doc.getLength(); i++) {
			if (charAt(i) == c) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the previous index of c, seen from the current caret position.
	 * @param c The searched character.
	 * @return The index or -1.
	 */
	public int previousIndexOf(int c) {
		return previousIndexOf(getCaretPosition(), c);
	}
	
	/**
	 * Returns the previous index of c.
	 * @param pos The position.
	 * @param c The searched character.
	 * @return The index or -1.
	 */
	public int previousIndexOf(int pos, int c) {
		for (int i = pos; i >= 0; i--) {
			if (charAt(i) == c) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the character at a position or -1.
	 * @param pos The index of the searched character.
	 * @return The character value or -1.
	 */
	public int charAt(int pos) {
		try {
			return getDocument().getText(pos, 1).charAt(0);
		} catch (Exception exc) {
			return -1;
		}
	}
	
	/**
	 * Inserts text after the caret.
	 */
	public void insertText(String s) {
		try {
			getDocument().insertString(getCaretPosition(), s, null);
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
	
	/**
	 * Forwards to Document.getText but returns <code>null</code> instead of 
	 * throwing exceptions.
	 */
	public String getText(int pos, int len) {
		try {
			return getDocument().getText(pos, len);
		} catch (Exception exc) {
			return null;
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
