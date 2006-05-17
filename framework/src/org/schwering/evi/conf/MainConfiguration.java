package org.schwering.evi.conf;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.Util;

/**
 * Administers the main configuration file.<br />
 * The format of the file is quite simple:
 * <pre>
 * key=value
 * # This is a comment
 * </pre>
 * Here's a list of EVI configuration keys:
 * <ul>
 * <li> gui.asktoexit (boolean) </li>
 * <li> gui.lookandfeel (String) </li>
 * <li> gui.size (Dimension) </li>
 * <li> gui.topleft (Point) </li>
 * <li> gui.tabs.placement (int) </li>
 * <li> app.sayhello (boolean) </li>
 * <li> app.lang (String) </li>
 * <li> app.modulelist (String [in URL format], default 
 * {@link org.schwering.evi.gui.conf.ModuleConfigurationPanel#MODULE_LIST_URL})
 * </li>
 * <li> font.primary (Font) </li>
 * <li> font.secondary (Font) </li>
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public abstract class MainConfiguration {
	/**
	 * The program's config dir.<br />
	 * It is tried to set it to <code>${user.home}/.evi</code>.
	 */
	public static final File CONFIG_DIR;
	
	/**
	 * The main configuration file.<br />
	 * It is tried to set it to <code>CONFIG_DIR/modules</code>.
	 */
	public static final File MAIN_CONFIG_FILE;
	
	private static final String CONFIG_FOLDER_NAME = ".evi";
	private static final String MAIN_CONFIG_FILE_NAME = "conf";
	
	static {
		String home = System.getProperty("user.home", "");
		if (home.charAt(home.length()-1) != File.separatorChar) {
			home += File.separatorChar;
		}
		home += CONFIG_FOLDER_NAME;
		
		File configDir = new File(home);
		if (!configDir.exists()) {
			configDir.mkdir();
		}
		if (configDir.isDirectory() && configDir.canRead()) {
			CONFIG_DIR = configDir;
		} else {
			CONFIG_DIR = new File(CONFIG_FOLDER_NAME);
		}
		
		File confFile = new File(CONFIG_DIR, MAIN_CONFIG_FILE_NAME);
		if (!confFile.isFile()) {
			try {
				confFile.createNewFile();
			} catch (Exception exc) {
				ExceptionDialog.show(exc);
			}
		}
		MAIN_CONFIG_FILE = confFile;
	}
	
	private static Properties props = new Properties();
	
	/**
	 * No instances allowed/useful.
	 */
	private MainConfiguration() {
	}
	
	/**
	 * Loads the URLs. If an exception is thrown, this method shows an 
	 * <code>ExceptionDialog</code>. This method also adds a shutdownhook
	 * which invokes <code>store()</code>.
	 */
	public static void load() {
		Properties p = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(MAIN_CONFIG_FILE);
			p.load(fis);
		} catch (Exception exc) {
			ExceptionDialog.show("Loading configuration failed", exc);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception exc) {
				}
			}
		}

		props = p;
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
	 * Stores the properties.
	 */
	public static void store() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(MAIN_CONFIG_FILE);
			props.store(fos, "MAIN_CONFIG_FILE "+ MAIN_CONFIG_FILE.toString());
		} catch (Exception exc) {
			ExceptionDialog.show("Saving configuration failed", exc);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception exc) {
				}
			}
		}
	}
	
	/**
	 * Returns all keys in an array.
	 * @return All configuration keys.
	 */
	public static String[] getKeys() {
		Enumeration keys = props.keys();
		String[] arr = new String[props.size()];
		for (int i = 0; i < arr.length && keys.hasMoreElements(); i++) {
			arr[i] = (String)keys.nextElement();
		}
		return arr;
	}
	
	/**
	 * Grabs a <code>String</code> out of the configuration.<br />
	 * Default value is <code>""</code>.
	 * @param key The key.
	 * @return The String.
	 */
	public static String getString(String key) {
		return getString(key, "");
	}
	
	/**
	 * Grabs a <code>String</code> out of the configuration.<br />
	 * If the requested value does not exist, a new property with the key and 
	 * the default value is set.
	 * @param key The key.
	 * @param def The default value.
	 * @return The String.
	 */
	public static String getString(String key, String def) {
		String ret = props.getProperty(key);
		if (ret != null) {
			return ret.trim();
		} else {
			setString(key, def);
			return def;
		}
	}
	
	/**
	 * Sets a new property.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setString(String key, String value) {
		props.setProperty(key, value);
	}
	
	/**
	 * Grabs a <code>int</code> out of the configuration.<br />
	 * The default value is 0.
	 * @param key The key.
	 * @return The int.
	 */
	public static int getInt(String key) {
		return getInt(key, 0);
	}
	
	/**
	 * Grabs a <code>int</code> out of the configuration.
	 * @param key The key.
	 * @param def The default.
	 * @return The int.
	 */
	public static int getInt(String key, int def) {
		try {
			return Integer.parseInt(getString(key, String.valueOf(def)));
		} catch (Exception exc) {
			return def;
		}
	}
	
	/**
	 * Sets a new property.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setInt(String key, int value) {
		props.setProperty(key, String.valueOf(value));
	}

	/**
	 * Grabs a <code>double</code> out of the configuration.<br />
	 * The default is 0.0.
	 * @param key The key.
	 * @return The double.
	 */
	public static double getDouble(String key) {
		return getDouble(key, 0.0);
	}
	
	/**
	 * Grabs a <code>double</code> out of the configuration.
	 * @param key The key.
	 * @param def The default value.
	 * @return The double.
	 */
	public static double getDouble(String key, double def) {
		try {
			return Double.parseDouble(getString(key, String.valueOf(def)));
		} catch (Exception exc) {
			return def;
		}
	}

	/**
	 * Sets a new property.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setDouble(String key, double value) {
		props.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Grabs a <code>boolean</code> out of the configuration.<br />
	 * The default is true.
	 * @param key The key.
	 * @return The boolean.
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, true);
	}
	
	/**
	 * Grabs a <code>boolean</code> out of the configuration.
	 * @param key The key.
	 * @param def The default value.
	 * @return The boolean.
	 */
	public static boolean getBoolean(String key, boolean def) {
		try {
			String s = getString(key, String.valueOf(def));
			return Boolean.valueOf(s).booleanValue();
		} catch (Exception exc) {
			return def;
		}
	}
	
	/**
	 * Sets a new property.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setBoolean(String key, boolean value) {
		props.setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Grabs a point with format <code>(&lt;x&gt;|&lt;y&gt;)</code>.<br />
	 * The default is (0|0).
	 * @param key The key.
	 * @return A new point.
	 */
	public static Point getPoint(String key) {
		return getPoint(key, new Point(0,0));
	}
	
	/**
	 * Grabs a point with format <code>(&lt;x&gt;|&lt;y&gt;)</code>.
	 * @param key The key.
	 * @param def The default point.
	 * @return A new point.
	 */
	public static Point getPoint(String key, Point def) {
		try {
			String s = getString(key);
			s = s.substring(1, s.length()-1).trim();
			String[] arr = s.split("\\|");
			arr[0] = arr[0].trim();
			arr[1] = arr[1].trim();
			int x = Integer.parseInt(arr[0]);
			int y = Integer.parseInt(arr[1]);
			return new Point(x, y);
		} catch (Exception exc) {
			return def;
		}
	}
	
	/**
	 * Sets a new point value.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setPoint(String key, Point value) {
		int x = (int)value.getX();
		int y = (int)value.getY();
		props.setProperty(key, "("+ x +"|"+ y +")");
	}
	
	/**
	 * Grabs a dimension with format <code>&lt;width&gt;x&lt;height&gt;</code>.
	 * <br />
	 * The default is 0x0 which is probably not very useful.
	 * @param key The key.
	 * @return A new point.
	 */
	public static Dimension getDimension(String key) {
		return getDimension(key, new Dimension(0, 0));
	}
	
	/**
	 * Grabs a dimension with format <code>&lt;width&gt;x&lt;height&gt;</code>.
	 * @param key The key.
	 * @param def The default dimension.
	 * @return A new point.
	 */
	public static Dimension getDimension(String key, Dimension def) {
		try {
			String s = getString(key);
			String[] arr = s.trim().split("x");
			arr[0] = arr[0].trim();
			arr[1] = arr[1].trim();
			int width = Integer.parseInt(arr[0]);
			int height = Integer.parseInt(arr[1]);
			return new Dimension(width, height);
		} catch (Exception exc) {
			return def;
		}
	}
	
	/**
	 * Sets a new dimension value.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setDimension(String key, Dimension value) {
		int width = (int)value.getWidth();
		int height = (int)value.getHeight();
		props.setProperty(key, width +"x"+ height);
	}
	/**
	 * Grabs a font.
	 * @param key The key.
	 * @return A new font.
	 */
	public static Font getFont(String key) {
		String s = getString(key);
		if (s != null && s.length() > 0) {
			return Font.decode(s);
		} else {
			return Font.decode(null);
		}
	}
	
	/**
	 * Sets a new font value.
	 * @param key The key.
	 * @param value The value.
	 */
	public static void setFont(String key, Font value) {
		props.setProperty(key, Util.encodeFont(value));
	}
}