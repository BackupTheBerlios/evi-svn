/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * 
 * Default Document for TextField and TextPane.
 * @see TextField
 * @see TextPane
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class Document extends DefaultStyledDocument {
	private static final long serialVersionUID = -1595401247628807791L;
	
	private SimpleAttributeSet attr = new SimpleAttributeSet();
	private JTextComponent owner;
	private int maxSize = -1;
	private int newSize = -1;
	
	/**
	 * Creates a new non-editable <code>TextPane</code> with an empty document.
	 */
	public Document(JTextComponent owner) {
		super();
		this.owner = owner;
	}
	
	public void setLimit(int maxSize, int newSize) {
		this.maxSize = maxSize;
		this.newSize = newSize;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public synchronized void insertString(int pos, String str, 
			AttributeSet attr) throws BadLocationException {
		super.insertString(pos, str, ((attr != null) ? attr : this.attr));
	}
	
	/**
	 * Sets the new attribute set.
	 * @param attr The new attributes. 
	 * @throws IllegalArgumentException If <code>attr == null</code>.
	 */
	public void setAttributes(SimpleAttributeSet attr) {
		if (attr == null) {
			throw new IllegalArgumentException("attr == null");
		} else {
			this.attr = attr;
		}
	}
	
	/**
	 * Returns the attributes.
	 * @return The attributes (never <code>null</code>).
	 */
	public SimpleAttributeSet getAttributes() {
		return attr;
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily) {
		modifyAttributes(fontFamily, -1, null, null);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(int fontSize) {
		modifyAttributes(null, fontSize, null, null);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(Color foreground) {
		modifyAttributes(foreground, null);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(Color foreground, Color background) {
		modifyAttributes(null, -1, foreground, background);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, int fontSize) {
		modifyAttributes(fontFamily, fontSize, null, null);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, int fontSize, 
			Color foreground) {
		modifyAttributes(fontFamily, fontSize, foreground, null);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, int fontSize, 
			Color foreground, Color background) {
		if (fontFamily != null)
			StyleConstants.setFontFamily(attr, fontFamily);
		if (fontSize != -1)
			StyleConstants.setFontSize(attr, fontSize);
		if (foreground != null)
			StyleConstants.setForeground(attr, foreground);
		if (background != null)
			StyleConstants.setBackground(attr, background);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, boolean bold, 
			boolean italic, boolean underline) {
		modifyAttributes(fontFamily, -1, null, null, 
				bold, italic, underline);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(int fontSize, boolean bold, 
			boolean italic, boolean underline) {
		modifyAttributes(null, fontSize, null, null, 
				bold, italic, underline);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(Color foreground, boolean bold, 
			boolean italic, boolean underline) {
		modifyAttributes(foreground, null, bold, italic, underline);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(Color foreground, Color background, 
			boolean bold, boolean italic, boolean underline) {
		modifyAttributes(foreground, background, bold, italic, underline);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, int fontSize, 
			boolean bold, boolean italic, boolean underline) {
		modifyAttributes(fontFamily, fontSize, null, null, 
				bold, italic, underline);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, int fontSize, 
			Color foreground, boolean bold, boolean italic, boolean underline) {
		modifyAttributes(fontFamily, fontSize, foreground, null, 
				bold, italic, underline);
	}
	
	/**
	 * Modifies the attributes.
	 */
	public void modifyAttributes(String fontFamily, int fontSize, 
			Color foreground, Color background, boolean bold, 
			boolean italic, boolean underline) {
		if (fontFamily != null)
			StyleConstants.setFontFamily(attr, fontFamily);
		if (fontSize != -1)
			StyleConstants.setFontSize(attr, fontSize);
		if (foreground != null)
			StyleConstants.setForeground(attr, foreground);
		if (background != null)
			StyleConstants.setBackground(attr, background);
		StyleConstants.setBold(attr, bold);
		StyleConstants.setItalic(attr, italic);
		StyleConstants.setUnderline(attr, underline);
	}
	
	public void append(String text) {
		append(text, attr);
	}
	
	public void append(String text, String fontFamily) {
		append(text, fontFamily, -1);
	}
	
	public void append(String text, int fontSize) {
		append(text, null, fontSize);
	}
	
	public void append(String text, Color foreground) {
		append(text, foreground, null);
	}
	
	public void append(String text, Color foreground, Color background) {
		append(text, null, -1, foreground, background);
	}
	
	public void append(String text, String fontFamily, int fontSize) {
		append(text, fontFamily, fontSize, null);
	}
	
	public void append(String text, String fontFamily, int fontSize,
			Color foreground) {
		append(text, fontFamily, fontSize, foreground, null);
	}
	
	public void append(String text, String fontFamily, int fontSize,
			Color foreground, Color background) {
		SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
		if (fontFamily != null)
			StyleConstants.setFontFamily(newAttr, fontFamily);
		if (fontSize != -1)
			StyleConstants.setFontSize(newAttr, fontSize);
		if (foreground != null)
			StyleConstants.setForeground(newAttr, foreground);
		if (background != null)
			StyleConstants.setForeground(newAttr, background);
		append(text, newAttr);
	}
	
	public void append(String text, String fontFamily,
			boolean bold, boolean italic, boolean underline) {
		append(text, fontFamily, -1, bold, italic, underline);
	}
	
	public void append(String text, int fontSize,
			boolean bold, boolean italic, boolean underline) {
		append(text, null, fontSize, bold, italic, underline);
	}
	
	public void append(String text, Color foreground,
			boolean bold, boolean italic, boolean underline) {
		append(text, foreground, null, bold, italic, underline);
	}
	
	public void append(String text, Color foreground, Color background, 
			boolean bold, boolean italic, boolean underline) {
		append(text, null, -1, foreground, background, bold, italic, underline);
	}
	
	public void append(String text, String fontFamily, int fontSize,
			boolean bold, boolean italic, boolean underline) {
		append(text, fontFamily, fontSize, null, bold, italic, underline);
	}
	
	public void append(String text, String fontFamily, int fontSize,
			Color foreground, boolean bold, boolean italic, boolean underline) {
		append(text, fontFamily, fontSize, foreground, null, 
				bold, italic, underline);
	}
	
	public void append(String text, String fontFamily, int fontSize,
			Color foreground, Color background, 
			boolean bold, boolean italic, boolean underline) {
		SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
		if (fontFamily != null)
			StyleConstants.setFontFamily(newAttr, fontFamily);
		if (fontSize != -1)
			StyleConstants.setFontSize(newAttr, fontSize);
		if (foreground != null)
			StyleConstants.setForeground(newAttr, foreground);
		if (background != null)
			StyleConstants.setBackground(newAttr, background);
		StyleConstants.setBold(newAttr, bold);
		StyleConstants.setItalic(newAttr, italic);
		StyleConstants.setUnderline(newAttr, underline);
		append(text, newAttr);
	}
	
	/**
	 * Appends <code>text</code> with the attributes <code>attr</code>.<br />
	 * @param text The string that should be appended.
	 * @param attr The layout.
	 */
	public void append(String text, SimpleAttributeSet attr) {
		try {
			insertString(getLength(), text, attr);
			if (maxSize > 0 && newSize > 0 && maxSize > newSize && getLength() > maxSize) {
				remove(0, getLength() - newSize);
			}
			owner.setCaretPosition(getLength());
		} catch (Exception exc) {
			ExceptionDialog.show(exc);
		}
	}
	
	public void prepend(String text) {
		prepend(text, attr);
	}
	
	public void prepend(String text, String fontFamily) {
		prepend(text, fontFamily, -1);
	}
	
	public void prepend(String text, int fontSize) {
		prepend(text, null, fontSize);
	}
	
	public void prepend(String text, Color foreground) {
		prepend(text, foreground, null);
	}
	
	public void prepend(String text, Color foreground, Color background) {
		prepend(text, null, -1, foreground, background);
	}
	
	public void prepend(String text, String fontFamily, int fontSize) {
		prepend(text, fontFamily, fontSize, null);
	}
	
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground) {
		prepend(text, fontFamily, fontSize, foreground, null);
	}
	
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground, Color background) {
		SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
		if (fontFamily != null)
			StyleConstants.setFontFamily(newAttr, fontFamily);
		if (fontSize != -1)
			StyleConstants.setFontSize(newAttr, fontSize);
		if (foreground != null)
			StyleConstants.setForeground(newAttr, foreground);
		if (background != null)
			StyleConstants.setBackground(newAttr, background);
		prepend(text, newAttr);
	}
	
	public void prepend(String text, String fontFamily,
			boolean bold, boolean italic, boolean underline) {
		prepend(text, fontFamily, -1, bold, italic, underline);
	}
	
	public void prepend(String text, int fontSize,
			boolean bold, boolean italic, boolean underline) {
		prepend(text, null, fontSize, bold, italic, underline);
	}
	
	public void prepend(String text, Color foreground,
			boolean bold, boolean italic, boolean underline) {
		prepend(text, foreground, null, bold, italic, underline);
	}
	
	public void prepend(String text, Color foreground, Color background, 
			boolean bold, boolean italic, boolean underline) {
		prepend(text, null, -1, foreground, background, bold, italic, underline);
	}
	
	public void prepend(String text, String fontFamily, int fontSize,
			boolean bold, boolean italic, boolean underline) {
		prepend(text, fontFamily, fontSize, null, bold, italic, underline);
	}
	
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground, boolean bold, boolean italic, boolean underline) {
		prepend(text, fontFamily, fontSize, foreground, null, 
				bold, italic, underline);
	}
	
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground, Color background, 
			boolean bold, boolean italic, boolean underline) {
		SimpleAttributeSet newAttr = new SimpleAttributeSet(attr);
		if (fontFamily != null)
			StyleConstants.setFontFamily(newAttr, fontFamily);
		if (fontSize != -1)
			StyleConstants.setFontSize(newAttr, fontSize);
		if (foreground != null)
			StyleConstants.setForeground(newAttr, foreground);
		if (background != null)
			StyleConstants.setBackground(newAttr, background);
		StyleConstants.setBold(newAttr, bold);
		StyleConstants.setItalic(newAttr, italic);
		StyleConstants.setUnderline(newAttr, underline);
		prepend(text, newAttr);
	}
	
	/**
	 * Sets the text <code>text</code> with the attributes <code>attr</code>.<br />
	 * @param text The string that should be prepended.
	 * @param attr The layout.
	 */
	public void prepend(String text, SimpleAttributeSet attr) {
		try {
			insertString(0, text, attr);
			if (maxSize > 0 && newSize > 0 && maxSize > newSize && getLength() > maxSize) {
				remove(newSize, getLength() - newSize);
			}
			owner.setCaretPosition(0);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Removes all text from the textfield.
	 */
	public void removeText() {
		try {
			remove(0, getLength());
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
