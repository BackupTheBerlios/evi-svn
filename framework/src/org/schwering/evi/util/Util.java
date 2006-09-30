/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.gui.main.MainFrame;

/**
 * Provides some GUI utilities.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class Util {
	/**
	 * Returns the installed look and feels.
	 * @return The installed look and feels.
	 */
	public static String[] getLookAndFeels() {
		UIManager.LookAndFeelInfo[] lafi = UIManager.getInstalledLookAndFeels();
		if (lafi == null) {
			return null;
		}
		String[] lafs = new String[lafi.length];
		for (int i = 0; i < lafi.length; i++) {
			lafs[i] = lafi[i].getClassName();
		}
		return lafs;
	}
	
	/**
	 * Returns the active look and feel.
	 * @return The active look and feel.
	 */
	public static String getLookAndFeel() {
		return UIManager.getLookAndFeel().getClass().getName();
	}
	
	/**
	 * Sets the native look and feel.
	 */
	public static void setNativeLookAndFeel() throws LookAndFeelException {
	    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	}
	
	/**
	 * Changes the look and feel.
	 * @param laf The class name of the new look and feel.
	 */
	public static void setLookAndFeel(String laf) throws LookAndFeelException {
		try {
		    UIManager.setLookAndFeel(laf);
		    SwingUtilities.updateComponentTreeUI(MainFrame.getInstance()); 
		} catch (Exception exc) {
			throw new LookAndFeelException(exc);
		}
	}
	
	/**
	 * Puts the stack trace into a string.
	 * @param exc The exception whose stack trace is meant.
	 * @return A string containing a human-readable stack trace.
	 */
	public static String exceptionToString(Throwable exc) {
		if (exc == null) {
			return "(no exception given)"; //$NON-NLS-1$
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exc.printStackTrace(pw);
		return sw.toString();
	}
	
	/**
	 * Centers a component.
	 * @param c The component which is to be centered.
	 */
	public static void centerComponent(Component c) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension mySize = c.getSize();
		if (mySize.height > screenSize.height) {
			mySize.height = screenSize.height;
		}
		if (mySize.width > screenSize.width) {
			mySize.width = screenSize.width;
		}
		int x = (screenSize.width - mySize.width)/2;
		int y = (screenSize.height - mySize.height)/2;
		c.setLocation(x, y); 
	}
	
	/**
	 * Encodes a font into a string with the format described in 
	 * {@link java.awt.Font#decode(java.lang.String)}. 
	 * An example would be: Arial-BOLD-12
	 * @param f The font.
	 * @return A string representation like Arial-ITALIC-14.
	 */
	public static String encodeFont(Font f) {
		if (f == null) {
			return ""; //$NON-NLS-1$
		}
		String fontname = f.getFamily();
		int size = f.getSize();
		String style = encodeFontStyle(f.getStyle());
		
		char delim;
		if (fontname.indexOf('-') != -1) {
			delim = ' ';
		} else {
			delim = '-';
		}
		
		return fontname + delim + style + delim + size;
	}
	
	/**
	 * Returns a string representation of the font style. <br>
	 * The result is one of the following:
	 * <ul>
	 * <li> PLAIN </li>
	 * <li> BOLD </li>
	 * <li> ITALIC </li>
	 * <li> BOLDITALIC </li>
	 * </ul>
	 * @param style The style in <code>java.awt.Font</code>.
	 * @return PLAIN, BOLD, ITALIC or BOLDITALIC.
	 */
	public static String encodeFontStyle(int style) {
		if (style == Font.PLAIN) {
			return "PLAIN"; //$NON-NLS-1$
		} else if ((style & Font.BOLD) != 0 && (style & Font.ITALIC) != 0) {
			return "BOLDITALIC"; //$NON-NLS-1$
		} else if ((style & Font.BOLD) != 0) {
			return "BOLD"; //$NON-NLS-1$
		} else if ((style & Font.ITALIC) != 0) {
			return "ITALIC"; //$NON-NLS-1$
		} else {
			return "PLAIN"; //$NON-NLS-1$
		}
	}
	
	/**
	 * Creates a <code>Font</code> from some human-readable information.
	 * @param fontname The name of the font.
	 * @param size The point size of the font in pixels
	 * @param style The style. This should contain one or more of the words
	 * "plain", "italic", "bold". For example "bold" is a valid style, "italic"
	 * is valid too and "bold italic" or "italic bold" mean the conjunctions of
	 * them. Any invalid style results in "plain".
	 * @return A Font object representing the meant font.
	 * @see java.awt.Font#decode(java.lang.String)
	 */
	public static Font decodeFont(String fontname, String size, String style) {
		if (fontname == null) {
			return Font.decode(null);
		}
		
		if (size == null) {
			size = ""; //$NON-NLS-1$
		}
		
		if (style == null) {
			style = "PLAIN"; //$NON-NLS-1$
		} 
		
		char delim;
		if (fontname.indexOf('-') != -1) {
			delim = ' ';
		} else {
			delim = '-';
		}
		
		style = style.toUpperCase();
		if (style.indexOf("ITALIC") != -1 && style.indexOf("BOLD") != -1) { //$NON-NLS-1$ //$NON-NLS-2$
			style = "BOLDITALIC"; //$NON-NLS-1$
		} else if (style.indexOf("ITALIC") != -1) { //$NON-NLS-1$
			style = "ITALIC"; //$NON-NLS-1$
		} else if (style.indexOf("BOLD") != -1) { //$NON-NLS-1$
			style = "BOLD"; //$NON-NLS-1$
		}
		
		String s = fontname + delim + style + delim + size;
		return Font.decode(s);
	}
	
	/**
	 * Asks whether the user wants to exit and does the respective.
	 */		
	public static void askToExit() {
		if (MainConfiguration.PROPS.getBoolean("gui.asktoexit")) { //$NON-NLS-1$
			int i = JOptionPane.showConfirmDialog(null, Messages.getString("Util.19"), //$NON-NLS-1$
					Messages.getString("Util.20"), JOptionPane.YES_NO_OPTION); //$NON-NLS-2$
			if (i != JOptionPane.YES_OPTION) {
				return;
			}
		}
		if (MainFrame.getInstance() != null) {
			MainFrame.getInstance().dispose();
		}
		System.exit(0);
	}
}