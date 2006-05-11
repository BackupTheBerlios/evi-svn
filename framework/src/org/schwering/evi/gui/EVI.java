package org.schwering.evi.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.conf.ModuleAutoStartConfiguration;
import org.schwering.evi.conf.ModuleConfiguration;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleInstantiationException;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.core.Requirement;
import org.schwering.evi.core.RequirementException;
import org.schwering.evi.gui.main.HelloWorldPanel;
import org.schwering.evi.gui.main.MainFrame;
import org.schwering.evi.gui.main.ProgressFrame;
import org.schwering.evi.util.ExceptionDialog;
import org.schwering.evi.util.Util;

/**
 * Starts the GUI.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class EVI {
	/**
	 * The program's name/title.
	 */
	public static final String TITLE = "EVI";
	
	/**
	 * The program's version.
	 */
	public static final float VERSION = 0.1f;
	
	/**
	 * Gives access to the one and only instance of the application.
	 * With {@link #getInstance()} this provides simple access to the owning 
	 * main application.
	 */
	private static EVI instance = null;
	
	private MainFrame frame;
	
	/**
	 * Creates one initial instance and returns it in future.
	 * @return The current instance.
	 */
	public static EVI getInstance() {
		if (instance == null) {
			instance = new EVI(null);
		}
		return instance;
	}
	
	/**
	 * Creates one initial instance and returns it in future.
	 * Only used by <code>main</code> method.
	 * @return The current instance.
	 */
	private static EVI getInstance(String[] args) {
		if (instance == null) {
			instance = new EVI(args);
		}
		return instance;
	}
	
	/**
	 * The main method.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		try {
			EVI.getInstance(args);
		} catch (Exception exc) {
			ExceptionDialog.show(exc);
		}
	}
	
	/**
	 * Creates a new instance of the configuration GUI.
	 * @see #getInstance()
	 */
	private EVI(String[] args) {
		ProgressFrame progress = new ProgressFrame();
		
		progress.update(5, "Configuration: Loading...");
		try {
			MainConfiguration.load();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(15, "GUI: Setting Look and Feel...");
		try {
			setLookAndFeel();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(25, "Modules: Loading list...");
		try {
			ModuleConfiguration.load();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(35, "Modules: Loading JARs...");
		try {
			loadModules();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(55, "Modules: Checking dependencies...");
		try {
			checkModuleDependencies();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(65, "Modules: Loading auto-start list...");
		try {
			ModuleAutoStartConfiguration.load();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(70, "Modules: Analyzing commandline arguments...");
		try {
			startArgRelatedModules(args);
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(75, "GUI: Creating main frame...");
		try {
			initMainFrame();
		} catch (Throwable exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(90, "Modules: Auto-starting...");
		try {
			autoStartModules();
		} catch (Throwable exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading", 
					exc);
		}
		
		progress.update(100, "Dickerchen");
	}
	
	/**
	 * Loads the modules specified in the configuration.
	 */
	private void loadModules() {
		URL[] urls = ModuleConfiguration.getURLs();
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				try {
					ModuleLoader.load(urls[i]);
				} catch (Exception exc) {
					ExceptionDialog.show("Module could not be loaded:\n"+
							urls[i], exc);
				}
			}
		}
		String[] classNames = ModuleConfiguration.getClassNames();
		if (classNames != null) {
			for (int i = 0; i < classNames.length; i++) {
				try {
					ModuleLoader.load(classNames[i]);
				} catch (Exception exc) {
					ExceptionDialog.show("Module could not be loaded:\n"+ 
							classNames[i], exc);
				}
			}
		}
	}
	
	/**
	 * Checks dependencies and automatically starts respective modules.
	 */
	private void checkModuleDependencies() {
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		if (modules == null) {
			return;
		}
		for (int i = 0; i < modules.length; i++) {
			if (!Requirement.matches(modules[i], modules)) {
				RequirementException exc = Requirement.getCause(modules[i], 
						modules);
				ExceptionDialog.show("Module "+ modules[i].getId() +" requires"+
						" other modules:", exc);
			}
		}
	}
	
	/**
	 * Starts all modules listed in the 
	 * <code>ModuleAutoStartConfiguration</code>.
	 */
	private void autoStartModules() {
		String[] ids = ModuleAutoStartConfiguration.getIds();
		String[] args = ModuleAutoStartConfiguration.getArgs();
		for (int i = 0; i < ids.length; i++) {
			ModuleContainer container = ModuleLoader.getLoadedModule(ids[i]);
			if (container == null) {
				ExceptionDialog.show("Could not autostart module", 
						new ModuleInstantiationException("No module with id "+
								ids[i] +" exists"));
			} else {
				try {
					String[] argarr;
					if (args[i] == null || args[i].length() == 0) {
						argarr = null;
					} else {
						argarr = new String[] { args[i] };
					}
					ModuleFactory.newInstance(container, argarr);
				} catch (ModuleInstantiationException exc) {
					ExceptionDialog.show("Could not autostart module", exc);
				}
			}
		}
	}
	
	/**
	 * Checks all command line arguments for URLs and starts the modules that  
	 * can handle the respective protocol.
	 * @param args The command line arguments from <code>main</code> method.
	 */
	private void startArgRelatedModules(String[] args) {
		if (args == null) {
			return;
		}
		ModuleContainer[] modules = ModuleLoader.getLoadedModules();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			try {
				URL url = new URL(arg);
				String protocol = url.getProtocol();
				for (int j = 0; j < modules.length; j++) {
					if (modules[i].handlesProtocol(protocol)) {
						ModuleFactory.newInstance(modules[i], 
								new Object[] { url });
					}
				}
			} catch (Exception exc) {
			}
		}
	}
	
	/**
	 * Prepares the main window.
	 */
	private void initMainFrame() {
		frame = MainFrame.getInstance();
		if (MainConfiguration.getBoolean("app.sayhello")) {
			MainConfiguration.setBoolean("app.sayhello", false);
			frame.getMainTabBar().addTab(HelloWorldPanel.getInstance());
		}
		setSize();
		frame.setVisible(true);
	}
	
	/**
	 * Returns the main frame.
	 * @return The main frame.
	 */
	public MainFrame getMainFrame() {
		return frame;
	}

	/**
	 * Sets the L&F specified in the configuration.
	 */
	private void setLookAndFeel() {
		try {
			String laf = MainConfiguration.getString("gui.lookandfeel", 
					Util.getLookAndFeel());
			Util.setLookAndFeel(laf);
		} catch (Exception exc) {
			try {
				Util.setNativeLookAndFeel();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Sets the size specified in the configuration.
	 */
	private void setSize() {
		Point topleft = MainConfiguration.getPoint("gui.topleft", null);
		Dimension size = MainConfiguration.getDimension("gui.size", null);
		
		if (size != null) {
			frame.setSize(size);
		} else {
			frame.setSize(700, 600);
		}
		
		if (topleft != null) {
			frame.setLocation(topleft);
		} else {
			Util.centerComponent(frame);
		}
	}
}