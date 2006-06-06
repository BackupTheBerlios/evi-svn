package org.schwering.evi.conf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import org.schwering.evi.util.Base64;
import org.schwering.evi.util.Util;

/**
 * Extended <code>java.util.Properties</code> class.
 * Offers a number of methods to store and load commonly used objects.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class Properties extends java.util.Properties {
	private static final long serialVersionUID = 2894356226433006741L;
	
	protected File propsFile;
	protected String description;
	protected boolean shutdownHookSet = false;
	protected Thread shutdownHook = new Thread() {
		public void run() {
			try {
				store();
			} catch (Exception exc) {
			}
		}
	}; 
	
	
	/**
	 * Initializes a new properties file at CONFIG_DIR/moduleId.<br>
	 * <br> 
	 * This is a shorthand for <code>Properties(moduleId, true)</code>.
	 * @param moduleId The module's id, used as filename for the config 
	 * file.
	 * @see MainConfiguration#CONFIG_DIR
	 */
	public Properties(String moduleId) throws IOException {
		this(moduleId, true);
	}
	
	/**
	 * Initializes a new properties file at CONFIG_DIR/moduleId.<br>
	 * The file is <b>not loaded</b>!
	 * <br> 
	 * <b>Example:</b> Your home directory is /home/chs (Unix example), then  
	 * the CONFIG_DIR is /home/chs/.evi/. Say your module's id is 
	 * org.schwering.evi.modules.irc.IRC, then the file will be 
	 * /home/chs/.evi/org.schwering.evi.modules.irc.IRC.<br>
	 * <br>
	 * <b>Note:</b> You don't need to pass the module id as parameter. You 
	 * can also pass anything else as long it is a valid filename. However, 
	 * the module id makes sense because it's unique. Thus, passing the 
	 * module id avoids conflicts with other module's config files.
	 * @param moduleId The module's id, used as filename for the config 
	 * file.
	 * @param create Indicates whether it is tried to create the file it 
	 * does not yet exist.
	 * @see MainConfiguration#CONFIG_DIR
	 */
	public Properties(String moduleId, boolean create) throws IOException {
		propsFile = new File(MainConfiguration.CONFIG_DIR, moduleId);
		if (!propsFile.exists()) {
			propsFile.createNewFile();
		}
	}
	
	/**
	 * Enables/disables automatic <code>store()</code> via shutdownhook.
	 * @param set <code>true</code> adds the shutdownhook, <code>false</code>
	 * removes it.
	 */
	public void setShutdownHook(boolean set) {
		Runtime r = Runtime.getRuntime();
		if (!shutdownHookSet && set) {
			r.addShutdownHook(shutdownHook);
		} else if (shutdownHookSet && !set) {
			r.removeShutdownHook(shutdownHook);
		}
	}
	
	/**
	 * Sets the description at the top of the properties file.
	 * @param desc The new description.
	 */
	public void setDescription(String desc) {
		description = desc;
	}
	
	/**
	 * Returns the description at the top of the properties file.
	 * @return The properties description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Loads the properties.
	 * @throws IOException If the file cannot be read etc.
	 */
	public void load() throws IOException {
		FileInputStream fis = null;
		fis = new FileInputStream(propsFile);
		load(fis);
		if (fis != null) {
			try {
				fis.close();
			} catch (Exception exc) {
			}
		}
	}
	
	/**
	 * Stores the new settings.
	 * @throws IOException If an IO error occurs.
	 */
	public void store() throws IOException {
		FileOutputStream fos = null;
		fos = new FileOutputStream(propsFile);
		store(fos, description);
		if (fos != null) {
			try {
				fos.close();
			} catch (Exception exc) {
			}
		}
	}
	
	/**
	 * Returns all keys in an array.
	 * @return All configuration keys.
	 */
	public String[] getKeys() {
		Enumeration keys = keys();
		String[] arr = new String[size()];
		for (int i = 0; i < arr.length && keys.hasMoreElements(); i++) {
			arr[i] = (String)keys.nextElement();
		}
		return arr;
	}
	
	/**
	 * Grabs a <code>String</code> out of the configuration.<br>
	 * Default value is <code>""</code>.
	 * @param key The key.
	 * @return The String.
	 */
	public String getString(String key) {
		return getString(key, "");
	}
	
	/**
	 * Grabs a <code>String</code> out of the configuration.<br>
	 * If the requested value does not exist, a new property with the key and 
	 * the default value is set.
	 * @param key The key.
	 * @param def The default value.
	 * @return The String.
	 */
	public String getString(String key, String def) {
		String ret = getProperty(key);
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
	public void setString(String key, String value) {
		setProperty(key, value);
	}
	
	/**
	 * Grabs a <code>String</code> out of the configuration and decodes it with 
	 * Base64.<br>
	 * Default value is <code>""</code>.
	 * @param key The key.
	 * @return The String.
	 */
	public String getBase64String(String key) {
		return getBase64String(key, "");
	}
	
	/**
	 * Grabs a <code>String</code> out of the configuration and decodes it with 
	 * Base64.<br>
	 * If the requested value does not exist, a new property with the key and 
	 * the default value is set.
	 * @param key The key.
	 * @param def The default value.
	 * @return The String.
	 */
	public String getBase64String(String key, String def) {
		String ret = getProperty(key, null);
		if (ret != null) {
			return Base64.decode(ret);
		} else {
			setBase64String(key, def);
			return def;
		}
	}
	
	/**
	 * Sets a new property after encoding it with Base64.
	 * @param key The key.
	 * @param value The value.
	 */
	public void setBase64String(String key, String value) {
		setProperty(key, Base64.encode(value));
	}
	
	/**
	 * Grabs a <code>int</code> out of the configuration.<br>
	 * The default value is 0.
	 * @param key The key.
	 * @return The int.
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	/**
	 * Grabs a <code>int</code> out of the configuration.
	 * @param key The key.
	 * @param def The default.
	 * @return The int.
	 */
	public int getInt(String key, int def) {
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
	public void setInt(String key, int value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Grabs a <code>double</code> out of the configuration.<br>
	 * The default is 0.0.
	 * @param key The key.
	 * @return The double.
	 */
	public double getDouble(String key) {
		return getDouble(key, 0.0);
	}
	
	/**
	 * Grabs a <code>double</code> out of the configuration.
	 * @param key The key.
	 * @param def The default value.
	 * @return The double.
	 */
	public double getDouble(String key, double def) {
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
	public void setDouble(String key, double value) {
		setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Grabs a <code>boolean</code> out of the configuration.<br>
	 * The default is true.
	 * @param key The key.
	 * @return The boolean.
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, true);
	}
	
	/**
	 * Grabs a <code>boolean</code> out of the configuration.
	 * @param key The key.
	 * @param def The default value.
	 * @return The boolean.
	 */
	public boolean getBoolean(String key, boolean def) {
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
	public void setBoolean(String key, boolean value) {
		setProperty(key, String.valueOf(value));
	}
	
	/**
	 * Grabs a point with format <code>(&lt;x&gt;|&lt;y&gt;)</code>.<br>
	 * The default is (0|0).
	 * @param key The key.
	 * @return A new point.
	 */
	public Point getPoint(String key) {
		return getPoint(key, new Point(0,0));
	}
	
	/**
	 * Grabs a point with format <code>(&lt;x&gt;|&lt;y&gt;)</code>.
	 * @param key The key.
	 * @param def The default point.
	 * @return A new point.
	 */
	public Point getPoint(String key, Point def) {
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
	public void setPoint(String key, Point value) {
		int x = (int)value.getX();
		int y = (int)value.getY();
		setProperty(key, "("+ x +"|"+ y +")");
	}
	
	/**
	 * Grabs a dimension with format <code>&lt;width&gt;x&lt;height&gt;</code>.
	 * <br>
	 * The default is 0x0 which is probably not very useful.
	 * @param key The key.
	 * @return A new point.
	 */
	public Dimension getDimension(String key) {
		return getDimension(key, new Dimension(0, 0));
	}
	
	/**
	 * Grabs a dimension with format <code>&lt;width&gt;x&lt;height&gt;</code>.
	 * @param key The key.
	 * @param def The default dimension.
	 * @return A new point.
	 */
	public Dimension getDimension(String key, Dimension def) {
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
	public void setDimension(String key, Dimension value) {
		int width = (int)value.getWidth();
		int height = (int)value.getHeight();
		setProperty(key, width +"x"+ height);
	}
	
	/**
	 * Grabs a font.
	 * @param key The key.
	 * @return A new font.
	 */
	public Font getFont(String key) {
		return getFont(key, Font.decode(null));
	}
	
	/**
	 * Grabs a font.
	 * @param key The key.
	 * @param def The default font.
	 * @return A new font.
	 */
	public Font getFont(String key, Font def) {
		String s = getString(key);
		if (s != null && s.length() > 0) {
			return Font.decode(s);
		} else {
			return def;
		}
	}
	
	/**
	 * Sets a new font value.
	 * @param key The key.
	 * @param value The value.
	 */
	public void setFont(String key, Font value) {
		setProperty(key, Util.encodeFont(value));
	}
	
	/**
	 * Grabs a color.
	 * @param key The key.
	 * @return A new color.
	 */
	public Color getColor(String key) {
		return getColor(key, Color.BLACK);
	}
	
	/**
	 * Grabs a color.
	 * @param key The key.
	 * @param def The default color.
	 * @return A new color.
	 */
	public Color getColor(String key, Color def) {
		String s = getString(key);
		if (s != null && s.length() > 0) {
			try {
				String[] arr = s.split(" ");
				int r = Integer.parseInt(arr[0]);
				int g = Integer.parseInt(arr[1]);
				int b = Integer.parseInt(arr[2]);
				int a = Integer.parseInt(arr[3]);
				return new Color(r, g, b, a);
			} catch (Exception exc) {
				return def;
			}
		} else {
			return def;
		}
	}
	
	/**
	 * Sets a new color value.
	 * @param key The key.
	 * @param value The value.
	 */
	public void setColor(String key, Color value) {
		int r = value.getRed();
		int g = value.getGreen();
		int b = value.getBlue();
		int a = value.getAlpha();
		String s = r +" "+ g +" "+ b +" "+ a;
		setProperty(key, s);
	}
	
	/**
	 * Grabs a locale.
	 * @param key The key.
	 * @return A new locale.
	 */
	public Locale getLocale(String key) {
		return getLocale(key, LanguageAdministrator.getUserLanguage());
	}
	
	/**
	 * Grabs a locale.
	 * @param key The key.
	 * @param def The default locale.
	 * @return A new locale.
	 */
	public Locale getLocale(String key, Locale def) {
		String s = getString(key);
		if (s != null && s.length() > 0) {
			try {
				return new Locale(s);
			} catch (Exception exc) {
				return def;
			}
		} else {
			return def;
		}
	}
	
	/**
	 * Sets a new locale value. In fact, just the language is set (e.g. "de").
	 * @param key The key.
	 * @param value The value.
	 */
	public void setLocale(String key, Locale value) {
		String s = value.getLanguage();
		setProperty(key, s);
	}
}
