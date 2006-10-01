/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

/**
 * A color selector that displays the color and a button that links to a 
 * JColorChooser.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class ColorSelector extends JPanel {
	private static final long serialVersionUID = -8521372820516646412L;
	
	private Color color;
	private String title;
	private JButton label = new JButton("    "); //$NON-NLS-1$
	
	/**
	 * Initializes with default values. Use setColor and setTitle.
	 */
	public ColorSelector() {
		this(null, null);
	}
	
	/**
	 * Initializes with default title.
	 * @param color The first selected color.
	 */
	public ColorSelector(Color color) {
		this(color, null);
	}
	
	/**
	 * Initializes with specified color and title.
	 * @param c The first selected color.
	 * @param t The title of the JColorChooser window.
	 */
	public ColorSelector(Color c, String t) {
		setColor(c);
		setTitle(t);
		
		label.setBorderPainted(false);
		label.setEnabled(false);
		
		final JButton choose = new JButton(Messages.getString("ColorSelector.CHOOSE_COLOR")); //$NON-NLS-1$
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String t = (title != null) ? title : Messages.getString("ColorSelector.DEFAULT_TITLE"); //$NON-NLS-1$
				Color c = JColorChooser.showDialog(choose, t, color);
				if (c != null) {
					setColor(c);
				}
			}
		});
		
		setLayout(new BorderLayout());
		add(new JPanel(), BorderLayout.NORTH);
		add(choose, BorderLayout.EAST);
		add(new JPanel(), BorderLayout.SOUTH);
		add(label, BorderLayout.WEST);
	}
	
	/**
	 * Sets the selected color.
	 * @param c The new color.
	 */
	public void setColor(Color c) {
		color = c;
		label.setBackground(c);
	}
	
	/**
	 * Returns the selected color.<br/>
	 * This might be <code>null</code> if the color was set to <code>null</code>
	 * or if it wasn't set neither in a constructor nor with setColor.
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the new title of the <code>JColorChooser</code>.
	 * @param title The new title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the new title of the <code>JColorChooser</code>.
	 * @return The new title of the <code>JColorChooser</code>.
	 */
	public String getTitle() {
		return title;
	}
}
