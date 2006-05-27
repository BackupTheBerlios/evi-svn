package org.schwering.evi.conf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import org.schwering.evi.util.ExceptionDialog;

/**
 * Administers the module configuration file. Modules listet in this file are 
 * loaded. Each URL must point to a module's URL file.<br>
 * The format of the file is quite simple:
 * <pre>
 * http://www.test.de/module.jar
 * file:/home/chs/foobar.jar
 * # This is a comment
 * # And this class will be loaded directly by the default classloader: 
 * # (And thus must be in the classpath when starting the application)
 * org.schwering.evi.modules.BlaModule
 * </pre><br>
 * Because internally a <code>HashSet</code> is used, no URL is stored twice.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public abstract class ModuleConfiguration {
	/**
	 * The module list.<br>
	 * It is tried to set it to <code>CONFIG_DIR/modules</code>.
	 */
	public static final File MODULES_CONFIG_FILE;
	
	private static final String MODULES_CONFIG_FILE_NAME = "modules";
	
	static {
		File moduleFile = new File(MainConfiguration.CONFIG_DIR, 
				MODULES_CONFIG_FILE_NAME);
		if (!moduleFile.isFile()) {
			try {
				moduleFile.createNewFile();
			} catch (Exception exc) {
				ExceptionDialog.show(exc);
			}
		}
		MODULES_CONFIG_FILE = moduleFile;
	}
	
	private static HashSet list = new HashSet();
	
	/**
	 * No instances allowed/useful.
	 */
	private ModuleConfiguration() {
	}
	
	/**
	 * Loads the URLs. If an exception is thrown, this method shows an 
	 * <code>ExceptionDialog</code>. This method also adds a shutdownhook
	 * which invokes <code>store()</code>.
	 */
	public static void load() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(MODULES_CONFIG_FILE));
			for (String s; (s = br.readLine()) != null; ) {
				s = s.trim();
				if (s.length() > 0 && s.charAt(0) != '#') {
					try {
						if (s.toLowerCase().endsWith(".jar")) {
							addURL(s);
						} else {
							addClassName(s);
						}
					} catch (Exception e) {
						ExceptionDialog.show("The following URL or classname "+
								"is not valid in "+ MODULES_CONFIG_FILE_NAME +
								":\n"+ s, e);
					}
				}
			}
		} catch (Exception exc) {
			ExceptionDialog.show("Loading modules failed", exc);
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
	 * Stores the URL list to the file.
	 */
	public static void store() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(MODULES_CONFIG_FILE));
			URL[] urls = getURLs();
			for (int i = 0; i < urls.length; i++) {
				pw.println(urls[i].toString());
			}
			String[] classNames = getClassNames();
			for (int i = 0; i < classNames.length; i++) {
				pw.println(classNames[i].toString());
			}
		} catch (Exception exc) {
			ExceptionDialog.show("Saving modules-config failed", exc);
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
	 * Returns all listed URLs.
	 * @return All listed URLs.
	 */
	public static URL[] getURLs() {
		Object[] arr = list.toArray();
		URL[] urls = new URL[arr.length];
		int j = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null && arr[i] instanceof URL) {
				urls[j++] = (URL)arr[i];
			}
		}
		URL[] newUrls = new URL[j];
		System.arraycopy(urls, 0, newUrls, 0, j);
		return newUrls;
	}
	
	/**
	 * Returns all listed classes.
	 * @return All listed classes.
	 */
	public static String[] getClassNames() {
		Object[] arr = list.toArray();
		String[] classNames = new String[arr.length];
		int j = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null && arr[i] instanceof String) {
				classNames[j++] = (String)arr[i];
			}
		}
		String[] newClassNames = new String[j];
		System.arraycopy(classNames, 0, newClassNames, 0, j);
		return newClassNames;
	}
	
	/**
	 * Adds another URL.
	 * @param s The String representation of the URL.
	 * @throws MalformedURLException If the URL is malformed.
	 */
	public static void addURL(String s) throws MalformedURLException {
		if (s != null) {
			URL url = new URL(s); 
			list.add(url);
		}
	}

	/**
	 * Adds another URL.
	 * @param url The URL.
	 */
	public static void addURL(URL url) {
		if (url != null) {
			list.add(url);
		}
	}

	/**
	 * Adds another class.
	 * @param s The classname of the class.
	 */
	public static void addClassName(String s) {
		if (s != null) {
			list.add(s);
		}
	}
	
	/**
	 * Removes a module.
	 * @param o The object must be an instance of <code>URL</code> or 
	 * <code>String</code>.
	 */
	public static void remove(Object o) {
		list.remove(o);
	}
}