package org.schwering.evi.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.net.URI;
import java.net.URL;

import org.schwering.evi.conf.LanguageAdministrator;
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
	public static final String TITLE = "EVI"; //$NON-NLS-1$
	
	/**
	 * The program's version.
	 */
	public static final float VERSION = 0.1f;
	
	/**
	 * The protocol name which makes EVI load a module.
	 * If a URL like <code>eviload:http://evi.berlios.de/test.jar</code> is 
	 * given as command line argument, the test.jar is tried to be loaded 
	 * as module.
	 */
	public static final String LOAD_SCHEME = "eviload";
	
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
		
		progress.update(5, "Configuration: Loading..."); //$NON-NLS-1$
		try {
			MainConfiguration.load();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading",  //$NON-NLS-1$
					exc);
		}
		
		progress.update(10, "Configuration: Loading language..."); //$NON-NLS-1$
		try {
			LanguageAdministrator.load();
		} catch (Exception exc) {
			ExceptionDialog.show("Unexcepted exception caught while loading",  //$NON-NLS-1$
					exc);
		}
		
		progress.update(15, Messages.getString("EVI.3")); //$NON-NLS-1$
		try {
			setLookAndFeel();
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(25, Messages.getString("EVI.5")); //$NON-NLS-1$
		try {
			ModuleConfiguration.load();
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(35, Messages.getString("EVI.7")); //$NON-NLS-1$
		progress.setIndeterminate(true);
		try {
			loadModules();
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		progress.setIndeterminate(false);
		
		progress.update(45, Messages.getString("EVI.7")); //$NON-NLS-1$
		progress.setIndeterminate(true);
		try {
			loadArgRelatedModules(args);
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		progress.setIndeterminate(false);
		
		progress.update(50, Messages.getString("EVI.9")); //$NON-NLS-1$
		try {
			checkModuleDependencies();
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(55, Messages.getString("EVI.11")); //$NON-NLS-1$
		try {
			ModuleAutoStartConfiguration.load();
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(60, Messages.getString("EVI.13")); //$NON-NLS-1$
		try {
			initMainFrame();
		} catch (Throwable exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(65, Messages.getString("EVI.15")); //$NON-NLS-1$
		try {
			autoStartModules();
		} catch (Throwable exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(75, Messages.getString("EVI.17")); //$NON-NLS-1$
		try {
			startArgRelatedModules(args);
		} catch (Exception exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		/* just for fun: (from incl. 85% to incl. 90%) */
		try {
			String[] murks = new String[] {
					"Eva Valder", //$NON-NLS-1$
					"ohne dich", //$NON-NLS-1$
					"geht es nicht", //$NON-NLS-1$
					":-)", //$NON-NLS-1$
			};
			for (int i = 0; i < murks.length; i++) {
				progress.update(85 + i, murks[i]);
				for (int j = 0; j < 2; j++) {
					Thread.sleep(20 * (murks.length - i));
				}
			}
		} catch (Exception exc) {
		}
		
		progress.update(91, Messages.getString("EVI.20")); //$NON-NLS-1$
		try {
			makeVisible();
		} catch (Throwable exc) {
			ExceptionDialog.show(Messages.getString("EVI.2"),  //$NON-NLS-1$
					exc);
		}
		
		progress.update(100, Messages.getString("EVI.22")); //$NON-NLS-1$
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
					ExceptionDialog.show(Messages.getString("EVI.23")+  //$NON-NLS-1$
							urls[i] +"\n"+ //$NON-NLS-1$
							Messages.getString("EVI.25"), exc); //$NON-NLS-1$
					ModuleConfiguration.remove(urls[i]);
				}
			}
		}
		String[] classNames = ModuleConfiguration.getClassNames();
		if (classNames != null) {
			for (int i = 0; i < classNames.length; i++) {
				try {
					ModuleLoader.load(classNames[i]);
				} catch (Exception exc) {
					ExceptionDialog.show(Messages.getString("EVI.23")+  //$NON-NLS-1$
							classNames[i] +"\n"+ //$NON-NLS-1$
							Messages.getString("EVI.25"), exc); //$NON-NLS-1$
					ModuleConfiguration.remove(classNames[i]);
				}
			}
		}
	}
	
	/**
	 * Loads modules specified in the command line arguments.
	 * @param args
	 */
	private void loadArgRelatedModules(String[] args) {
		for (int i = 0; i < args.length; i++) {
			try {
				URI uri = new URI(args[i]);
				if (uri.getScheme().equalsIgnoreCase(LOAD_SCHEME)) {
					String m = uri.getSchemeSpecificPart();
					try {
						if (m.toLowerCase().endsWith("jar")) {
							URL url = new URL(m);
							ModuleLoader.load(url);
						} else {
							ModuleLoader.load(m);
						}
					} catch (Exception exc) {
						ExceptionDialog.show(Messages.getString("EVI.23")+  //$NON-NLS-1$
								m +"\n"+ //$NON-NLS-1$
								Messages.getString("EVI.25"), exc); //$NON-NLS-1$
					}
				}
			} catch (Exception exc) {
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
				ExceptionDialog.show(Messages.getString("EVI.29")+ modules[i].getId() +Messages.getString("EVI.30")+ //$NON-NLS-1$ //$NON-NLS-2$
						Messages.getString("EVI.31"), exc); //$NON-NLS-1$
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
				ExceptionDialog.show(Messages.getString("EVI.32"),  //$NON-NLS-1$
						new ModuleInstantiationException(Messages.getString("EVI.33")+ //$NON-NLS-1$
								ids[i] +Messages.getString("EVI.34"))); //$NON-NLS-1$
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
					ExceptionDialog.show(
							Messages.getString("EVI.32") +":\n"+  //$NON-NLS-1$  //$NON-NLS-2$
							container.getId(), exc);
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
				URI uri = new URI(arg);
				String scheme = uri.getScheme();
				for (int j = 0; j < modules.length; j++) {
					if (modules[j].handlesURI(scheme)) {
						ModuleFactory.newInstance(modules[j], 
								new Object[] { uri });
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
		if (MainConfiguration.getBoolean("app.sayhello")) { //$NON-NLS-1$
			MainConfiguration.setBoolean("app.sayhello", false); //$NON-NLS-1$
			frame.getMainTabBar().addTab(HelloWorldPanel.getInstance());
		}
		setSize();
	}
	
	/**
	 * Sets the mainframe to visible.
	 */
	private void makeVisible() {
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
			String laf = MainConfiguration.getString("gui.lookandfeel",  //$NON-NLS-1$
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
		Point topleft = MainConfiguration.getPoint("gui.topleft", null); //$NON-NLS-1$
		Dimension size = MainConfiguration.getDimension("gui.size", null); //$NON-NLS-1$
		
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