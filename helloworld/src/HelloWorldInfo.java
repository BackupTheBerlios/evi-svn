/* Copyright (C) 2006 Christoph Schwering */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JButton;

import org.schwering.evi.core.ICustomButtonable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IParameterizable;
import org.schwering.evi.core.IURIHandler;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleFactory;
import org.schwering.evi.core.ModuleLoader;
import org.schwering.evi.util.ExceptionDialog;

public class HelloWorldInfo implements IModuleInfo, ICustomButtonable, 
								IMenuable, IURIHandler, IParameterizable {

	public boolean isMenuable() {
		return true;
	}

	public String[] getProtocols() {
		return new String[] { "http" };
	}

	public IModule newInstance(URI uri) {
		try {
			return new HelloWorld(uri.toURL());
		} catch (Exception exc) {
			return null;
		}
	}

	public boolean isButtonable() {
		return true;
	}

	public IModule newInstance(Object[] args) {
		String s = (String)args[0];
		return new HelloWorld(s);
	}

	public JButton getCustomButton() {
		JButton button = new JButton("Fool Me!");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ModuleContainer container = ModuleLoader.getLoadedModule("HelloWorld");
					ModuleFactory.newInstance(container);
				} catch (Exception exc) {
					ExceptionDialog.show(exc);
				}
			}
		});
		return button;
	}

	public String getInfoURL() {
		return null;
	}

	public Class getModuleClass() {
		return HelloWorld.class;
	}

	public String getName() {
		return "Hello!";
	}

	public float getVersion() {
		return 1.5f;
	}

	public IModule newInstance() {
		return new HelloWorld();
	}
	

}
