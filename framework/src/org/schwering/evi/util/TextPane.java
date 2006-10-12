/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * A textfield that is mainly useful for logging purposes.<br />
 * <br />
 * The textfield has a <code>DefaultStyledDocument</code> as document and 
 * makes use of <code>SimpleAttributeSet</code>. If you do not work with 
 * them, this class is worthless for you.<br />
 * The created <code>JTextPane</code> is initialized with 
 * <code>setAutoscrolls(true)</code> and <code>setEditable(false)</code>.<br />
 * <br />
 * This class encapsulates several often-used functionalities.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class TextPane extends JTextPane {
	private static final long serialVersionUID = 2865927208666493111L;
	
	private DefaultStyledDocument doc = new DefaultStyledDocument();
	private SimpleAttributeSet attr = new SimpleAttributeSet();
	
	/**
	 * Creates a new non-editable <code>TextPane</code> with an empty document.
	 */
	public TextPane() {
		super();
		setDocument(doc);
		setAutoscrolls(true);
		setEditable(false);
	}
	
	/**
	 * Sets the new document, which <b>must</b> be an instance of 
	 * <code>DefaultStyledDocument</code>.
	 * @param doc The new <code>DefaultStyledDocument</code>.
	 * @throws IllegalArgumentException If <code>doc</code> is not an instance 
	 * of <code>DefaultStyledDocument</code>. 
	 */
	public void setDocument(Document doc) {
		if (!(doc instanceof DefaultStyledDocument)) {
			throw new IllegalArgumentException("doc not instanceof DefaultStyledDocument!");
		} else {
			this.doc = (DefaultStyledDocument)doc;
			super.setDocument(doc);
		}
	}
	
	/**
	 * Returns the <code>DefaultStyledDocument</code>.
	 */
	public Document getDocument() {
		return doc;
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
			StyleConstants.setForeground(attr, background);
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
			StyleConstants.setForeground(attr, background);
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
			StyleConstants.setForeground(newAttr, background);
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
			doc.insertString(doc.getLength(), text, attr);
			setCaretPosition(doc.getLength());
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
			StyleConstants.setForeground(newAttr, background);
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
			StyleConstants.setForeground(newAttr, background);
		StyleConstants.setBold(newAttr, bold);
		StyleConstants.setItalic(newAttr, italic);
		StyleConstants.setUnderline(newAttr, underline);
		prepend(text, newAttr);
	}
	
	/**
	 * prepends <code>text</code> with the attributes <code>attr</code>.<br />
	 * @param text The string that should be prepended.
	 * @param attr The layout.
	 */
	public void prepend(String text, SimpleAttributeSet attr) {
		try {
			doc.insertString(0, text, attr);
			setCaretPosition(0);
		} catch (Exception exc) {
			ExceptionDialog.show(exc);
		}
	}
}
