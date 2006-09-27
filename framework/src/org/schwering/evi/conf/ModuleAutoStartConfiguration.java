/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.schwering.evi.util.ExceptionDialog;

/**
 * Administers the module auto start configuration file. Each entry must be 
 * an id of a module. Each of these modules is started automagically when the 
 * client starts.<br>
 * The format of the file is quite simple:
 * <pre>
 * my.package.Foobar
 * # this is a comment and thus the following module:
 * # my.package.IRCModule
 * # will not be startet
 * this.module.will.be.Started with this string as argument
 * </pre>
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public abstract class ModuleAutoStartConfiguration {
	/**
	 * The auto start list.<br>
	 * It is tried to set it to <code>CONFIG_DIR/autostart</code>.
	 */
	public static final File MODULE_AUTO_START_CONFIG_FILE;
	
	private static final String MODULES_CONFIG_FILE_NAME = "autostart";
	
	static {
		File autoStartFile = new File(MainConfiguration.CONFIG_DIR, 
				MODULES_CONFIG_FILE_NAME);
		if (!autoStartFile.isFile()) {
			try {
				autoStartFile.createNewFile();
			} catch (Exception exc) {
				ExceptionDialog.show(exc);
			}
		}
		MODULE_AUTO_START_CONFIG_FILE = autoStartFile;
	}
	
	private static Vector idList = new Vector();
	private static Vector argList = new Vector();
	
	/**
	 * No instances allowed/useful.
	 */
	private ModuleAutoStartConfiguration() {
	}
	
	/**
	 * Loads the ids of modules that are startet automatically. If an 
	 * exception is thrown, this method shows an <code>ExceptionDialog</code>. 
	 * This method also adds a shutdownhook which invokes <code>store()</code>.
	 */
	public static void load() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(
					MODULE_AUTO_START_CONFIG_FILE));
			for (String s; (s = br.readLine()) != null; ) {
				s = s.trim();
				if (s.length() > 0 && s.charAt(0) != '#') {
					String[] arr = s.split(" ", 2);
					String id = arr[0];
					String arg = (arr.length > 1) ? arr[1].trim() : "";
					try {
						addModule(id, arg);
					} catch (Exception e) {
						ExceptionDialog.show("One module-id is not valid "+
								"in file "+ MODULE_AUTO_START_CONFIG_FILE, e);
					}
				}
			}
		} catch (Exception exc) {
			ExceptionDialog.show("Autostarting modules failed", exc);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception exc) {
				}
			}
		}
		
		addShutdownHook();
	}
	
	/**
	 * Adds a shutdown hook which invokes <code>store()</code>.
	 */
	private static void addShutdownHook() {
		Runtime r = Runtime.getRuntime();
		r.addShutdownHook(new Thread() {
			public void run() {
				store();
			}
		});
	}
	
	/**
	 * Adds a shutdown hook which invokes <code>store()</code>.
	 */
	public static void store() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(MODULE_AUTO_START_CONFIG_FILE));
			String[] ids = getIds();
			String[] args = getArgs();
			for (int i = 0; i < ids.length && i < args.length; i++) {
				pw.print(ids[i]);
				if (args != null) {
					pw.print(" ");
					pw.print(args[i]);
				}
				pw.println();
			}
		} catch (Exception exc) {
			ExceptionDialog.show("Saving module-autostart-config failed", exc);
		} finally {
			if (pw != null) {
				try {
					pw.close();
				} catch (Exception exc) {
				}
			}
		}
	}
	
	/**
	 * Returns all listed ids.
	 * @return All listed ids.
	 */
	public static String[] getIds() {
		String[] ids = new String[idList.size()];
		ids = (String[])idList.toArray(ids);
		return ids;
	}
	
	/**
	 * Returns all listed arguments.
	 * @return All listed arguments.
	 */
	public static String[] getArgs() {
		String[] args = new String[argList.size()];
		args = (String[])argList.toArray(args);
		return args;
	}
	
	/**
	 * Adds another id.
	 * @param id The module's id.
	 * @param arg The argument.
	 */
	public static void addModule(String id, String arg) {
		idList.add(id.trim());
		argList.add((arg != null) ? arg.trim() : "");
	}
	
	/**
	 * Updates the argument at the <code>i</code>th index.
	 * @param i The index of the module that's to be changed.
	 * @param arg The new argument.
	 */
	public static void updateArg(int i, String arg) {
		if (arg == null) {
			arg = "";
		}
		argList.set(i, arg);
	}
	
	/**
	 * Removes a module.
	 * @param id The module's id.
	 * @param arg The argument.
	 */
	public static void remove(String id, String arg) {
		idList.remove(id);
		argList.remove(arg);
	}
	
	/**
	 * Clears the whole list.
	 */
	public static void removeAll() {
		idList.removeAllElements();
		argList.removeAllElements();
	}
}