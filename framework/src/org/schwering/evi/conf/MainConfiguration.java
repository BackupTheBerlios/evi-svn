package org.schwering.evi.conf;

import java.io.File;

import org.schwering.evi.util.ExceptionDialog;

/**
 * Administers the main configuration file.<br>
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
 * <li> app.lang (Locale) </li>
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
	 * The user's home directory.
	 */
	public static final File HOME_DIR;
		
	/**
	 * The program's config dir.<br>
	 * It is tried to set it to <code>${user.home}/.evi</code>.
	 */
	public static final File CONFIG_DIR;
	private static final String CONFIG_FOLDER_NAME = ".evi";
	
	private static final String MAIN_CONFIG_FILE_NAME = "conf";
	
	/**
	 * The main properties.
	 */
	public static final Properties PROPS;
	
	static {
		File homeDir = new File(System.getProperty("user.home", ""));
		HOME_DIR = homeDir;
		
		File configDir = new File(homeDir, CONFIG_FOLDER_NAME);
		if (!configDir.exists()) {
			configDir.mkdir();
		}
		if (configDir.isDirectory() && configDir.canRead()) {
			CONFIG_DIR = configDir;
		} else {
			CONFIG_DIR = new File(CONFIG_FOLDER_NAME);
		}

		Properties tmp;
		try {
			tmp = new Properties(MAIN_CONFIG_FILE_NAME);
		} catch (Exception exc) {
			tmp = null;
			ExceptionDialog.show("Error while initializing configuration\n" +
					"This will result in many other errors :-(", exc);
		}
		PROPS = tmp;
		PROPS.setShutdownHook(true);
	}
	
	/**
	 * No instances allowed/useful.
	 */
	private MainConfiguration() {
	}
}