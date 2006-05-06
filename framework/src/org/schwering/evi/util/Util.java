package org.schwering.evi.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

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
			return "(no exception given)";
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
	 * Asks whether the user wants to exit and does the respective.
	 */		
	public static void askToExit() {
		if (MainConfiguration.getBoolean("gui.asktoexit")) {
			int i = JOptionPane.showConfirmDialog(null, "Do you really "+
			"want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
			if (i != JOptionPane.YES_OPTION) {
				return;
			}
		}
		MainFrame.getInstance().dispose();
		System.exit(0);
	}
}