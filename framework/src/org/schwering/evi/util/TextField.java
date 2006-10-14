/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import org.schwering.evi.util.Document;

/**
 * A textfield that is mainly useful for logging purposes.<br />
 * <br />
 * The textfield has a <code>org.schwering.evi.util.Document</code> as 
 * document. The respective methods simply forward to the document.<br />
 * The created <code>JTextField</code> is initialized with 
 * <code>setEditable(true)</code> by default.<br />
 * <br />
 * This class encapsulates several often-used functionalities for colorful
 * textfields.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class TextField extends JTextField {
	private static final long serialVersionUID = 2865927208666493111L;
	
	/**
	 * Creates a new non-editable <code>TextPane</code> with an empty document.
	 */
	public TextField() {
		super();
		setDocument(new Document(this));
	}
	
	public void setText(String s) {
		removeText();
		append(s);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.awt.Color, boolean, boolean, boolean)
	 */
	public void append(String text, Color foreground, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).append(text, foreground, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.awt.Color, java.awt.Color, boolean, boolean, boolean)
	 */
	public void append(String text, Color foreground, Color background,
			boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).append(text, foreground, background, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.awt.Color, java.awt.Color)
	 */
	public void append(String text, Color foreground, Color background) {
		((Document)getDocument()).append(text, foreground, background);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.awt.Color)
	 */
	public void append(String text, Color foreground) {
		((Document)getDocument()).append(text, foreground);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, int, boolean, boolean, boolean)
	 */
	public void append(String text, int fontSize, boolean bold, boolean italic,
			boolean underline) {
		((Document)getDocument()).append(text, fontSize, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, int)
	 */
	public void append(String text, int fontSize) {
		((Document)getDocument()).append(text, fontSize);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, javax.swing.text.SimpleAttributeSet)
	 */
	public void append(String text, SimpleAttributeSet attr) {
		((Document)getDocument()).append(text, attr);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, boolean, boolean, boolean)
	 */
	public void append(String text, String fontFamily, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).append(text, fontFamily, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, int, boolean, boolean, boolean)
	 */
	public void append(String text, String fontFamily, int fontSize,
			boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).append(text, fontFamily, fontSize, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, int, java.awt.Color, boolean, boolean, boolean)
	 */
	public void append(String text, String fontFamily, int fontSize,
			Color foreground, boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).append(text, fontFamily, fontSize, foreground, bold, italic,
				underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, int, java.awt.Color, java.awt.Color, boolean, boolean, boolean)
	 */
	public void append(String text, String fontFamily, int fontSize,
			Color foreground, Color background, boolean bold, boolean italic,
			boolean underline) {
		((Document)getDocument()).append(text, fontFamily, fontSize, foreground, background, bold,
				italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, int, java.awt.Color, java.awt.Color)
	 */
	public void append(String text, String fontFamily, int fontSize,
			Color foreground, Color background) {
		((Document)getDocument()).append(text, fontFamily, fontSize, foreground, background);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, int, java.awt.Color)
	 */
	public void append(String text, String fontFamily, int fontSize,
			Color foreground) {
		((Document)getDocument()).append(text, fontFamily, fontSize, foreground);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String, int)
	 */
	public void append(String text, String fontFamily, int fontSize) {
		((Document)getDocument()).append(text, fontFamily, fontSize);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String, java.lang.String)
	 */
	public void append(String text, String fontFamily) {
		((Document)getDocument()).append(text, fontFamily);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#append(java.lang.String)
	 */
	public void append(String text) {
		((Document)getDocument()).append(text);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#getAttributes()
	 */
	public SimpleAttributeSet getAttributes() {
		return ((Document)getDocument()).getAttributes();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void insertString(int pos, String str, AttributeSet attr)
			throws BadLocationException {
		((Document)getDocument()).insertString(pos, str, attr);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.awt.Color, boolean, boolean, boolean)
	 */
	public void modifyAttributes(Color foreground, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).modifyAttributes(foreground, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.awt.Color, java.awt.Color, boolean, boolean, boolean)
	 */
	public void modifyAttributes(Color foreground, Color background,
			boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).modifyAttributes(foreground, background, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.awt.Color, java.awt.Color)
	 */
	public void modifyAttributes(Color foreground, Color background) {
		((Document)getDocument()).modifyAttributes(foreground, background);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.awt.Color)
	 */
	public void modifyAttributes(Color foreground) {
		((Document)getDocument()).modifyAttributes(foreground);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(int, boolean, boolean, boolean)
	 */
	public void modifyAttributes(int fontSize, boolean bold, boolean italic,
			boolean underline) {
		((Document)getDocument()).modifyAttributes(fontSize, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(int)
	 */
	public void modifyAttributes(int fontSize) {
		((Document)getDocument()).modifyAttributes(fontSize);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, boolean, boolean, boolean)
	 */
	public void modifyAttributes(String fontFamily, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).modifyAttributes(fontFamily, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, int, boolean, boolean, boolean)
	 */
	public void modifyAttributes(String fontFamily, int fontSize, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).modifyAttributes(fontFamily, fontSize, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, int, java.awt.Color, boolean, boolean, boolean)
	 */
	public void modifyAttributes(String fontFamily, int fontSize,
			Color foreground, boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).modifyAttributes(fontFamily, fontSize, foreground, bold, italic,
				underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, int, java.awt.Color, java.awt.Color, boolean, boolean, boolean)
	 */
	public void modifyAttributes(String fontFamily, int fontSize,
			Color foreground, Color background, boolean bold, boolean italic,
			boolean underline) {
		((Document)getDocument()).modifyAttributes(fontFamily, fontSize, foreground, background,
				bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, int, java.awt.Color, java.awt.Color)
	 */
	public void modifyAttributes(String fontFamily, int fontSize,
			Color foreground, Color background) {
		((Document)getDocument()).modifyAttributes(fontFamily, fontSize, foreground, background);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, int, java.awt.Color)
	 */
	public void modifyAttributes(String fontFamily, int fontSize,
			Color foreground) {
		((Document)getDocument()).modifyAttributes(fontFamily, fontSize, foreground);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String, int)
	 */
	public void modifyAttributes(String fontFamily, int fontSize) {
		((Document)getDocument()).modifyAttributes(fontFamily, fontSize);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#modifyAttributes(java.lang.String)
	 */
	public void modifyAttributes(String fontFamily) {
		((Document)getDocument()).modifyAttributes(fontFamily);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.awt.Color, boolean, boolean, boolean)
	 */
	public void prepend(String text, Color foreground, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).prepend(text, foreground, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.awt.Color, java.awt.Color, boolean, boolean, boolean)
	 */
	public void prepend(String text, Color foreground, Color background,
			boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).prepend(text, foreground, background, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.awt.Color, java.awt.Color)
	 */
	public void prepend(String text, Color foreground, Color background) {
		((Document)getDocument()).prepend(text, foreground, background);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.awt.Color)
	 */
	public void prepend(String text, Color foreground) {
		((Document)getDocument()).prepend(text, foreground);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, int, boolean, boolean, boolean)
	 */
	public void prepend(String text, int fontSize, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).prepend(text, fontSize, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, int)
	 */
	public void prepend(String text, int fontSize) {
		((Document)getDocument()).prepend(text, fontSize);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, javax.swing.text.SimpleAttributeSet)
	 */
	public void prepend(String text, SimpleAttributeSet attr) {
		((Document)getDocument()).prepend(text, attr);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, boolean, boolean, boolean)
	 */
	public void prepend(String text, String fontFamily, boolean bold,
			boolean italic, boolean underline) {
		((Document)getDocument()).prepend(text, fontFamily, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, int, boolean, boolean, boolean)
	 */
	public void prepend(String text, String fontFamily, int fontSize,
			boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).prepend(text, fontFamily, fontSize, bold, italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, int, java.awt.Color, boolean, boolean, boolean)
	 */
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground, boolean bold, boolean italic, boolean underline) {
		((Document)getDocument()).prepend(text, fontFamily, fontSize, foreground, bold, italic,
				underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, int, java.awt.Color, java.awt.Color, boolean, boolean, boolean)
	 */
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground, Color background, boolean bold, boolean italic,
			boolean underline) {
		((Document)getDocument()).prepend(text, fontFamily, fontSize, foreground, background, bold,
				italic, underline);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, int, java.awt.Color, java.awt.Color)
	 */
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground, Color background) {
		((Document)getDocument()).prepend(text, fontFamily, fontSize, foreground, background);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, int, java.awt.Color)
	 */
	public void prepend(String text, String fontFamily, int fontSize,
			Color foreground) {
		((Document)getDocument()).prepend(text, fontFamily, fontSize, foreground);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String, int)
	 */
	public void prepend(String text, String fontFamily, int fontSize) {
		((Document)getDocument()).prepend(text, fontFamily, fontSize);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String, java.lang.String)
	 */
	public void prepend(String text, String fontFamily) {
		((Document)getDocument()).prepend(text, fontFamily);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#prepend(java.lang.String)
	 */
	public void prepend(String text) {
		((Document)getDocument()).prepend(text);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#removeText()
	 */
	public void removeText() {
		((Document)getDocument()).removeText();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.util.Document#setAttributes(javax.swing.text.SimpleAttributeSet)
	 */
	public void setAttributes(SimpleAttributeSet attr) {
		((Document)getDocument()).setAttributes(attr);
	}
}
