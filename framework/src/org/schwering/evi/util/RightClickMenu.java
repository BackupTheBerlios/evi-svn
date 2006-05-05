package org.schwering.evi.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.text.JTextComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Represents a standard right click menu for textcomponents. It provides the 
 * common options for copying, pasting, cutting and selecting text.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class RightClickMenu extends JPopupMenu {
	private static final long serialVersionUID = 6669520773465427749L;
	
	private JTextComponent text;
	private JMenuItem menuItemCopy = new JMenuItem("Copy");
	private JMenuItem menuItemCut = new JMenuItem("Cut");
	private JMenuItem menuItemPaste = new JMenuItem("Paste");
	private JMenuItem menuItemSelectAll = new JMenuItem("Select All");
	
	/**
	 * Creates and adds a new right click menu. It has the standard options 
	 * "copy", "paste", "cut", "select all".
	 * @param textComponent The textcomponent whose text is dealt with and to 
	 * which the menu is added.
	 */
	private RightClickMenu(JTextComponent textComponent) {
		super();
		text = textComponent;
		
		menuItemCopy.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				text.copy(); 
			} 
		} );
		add(menuItemCopy);

		menuItemCut.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				text.cut(); 
			} 
		} );
		add(menuItemCut);
		
		menuItemPaste.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				text.paste(); 
			} 
		} );
		add(menuItemPaste);
		
		addSeparator();
		
		menuItemSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.selectAll();
			}
		} );
		add(menuItemSelectAll);
	}
	
	/**
	 * Calls super.show() and enables/disables cut and paste.
	 */
	public void show(Component invoker, int x, int y) {
		menuItemCut.setEnabled(text.isEditable());
		menuItemPaste.setEnabled(text.isEditable());
		super.show(invoker, x, y);
	}
	
	/**
	 * Adds a right click listener.
	 */
	private void addRightClickListener() {
		text.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) { 
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1 
						&& e.getButton() == MouseEvent.BUTTON3) {
					show(text, e.getX(), e.getY());
				}
			}
			public void mouseReleased(MouseEvent e) {
			}
		} );
	}
	
	/**
	 * Adds a right click menu to the text component. The menu provides 
	 * copy, paste, cut, select all.
	 * @param textComponent The text component, e.g. a JTextField.
	 */
	public static void addRightClickMenu(JTextComponent textComponent) {
		RightClickMenu menu = new RightClickMenu(textComponent);
		menu.addRightClickListener();
		textComponent.add(menu);
	}
}
