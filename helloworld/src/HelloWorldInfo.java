/* Copyright (C) 2006 Christoph Schwering */
import java.net.URI;

import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IParameterizable;
import org.schwering.evi.core.IURIHandler;

/**
 * A module that shows a label with "Hello World".
 * 
 * It implements IModuleInfo because this is the info class.
 * It implements IButtonable because we want the default button in the toolbar.
 * It implements IMenuable because we want the default menu in the menubar.
 * It implements IURIHandler because the module recognizes http-URLs that are given 
 * as arguments to the EVI framework (the URL is simply displayed in the label, nothing 
 * spectecular).
 * It implements IParameterizable because the user can specify an argument in the 
 * EVI module autostart (the module displays the argument in the label).
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class HelloWorldInfo implements IModuleInfo, IButtonable, 
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

	public String getInfoURL() {
		return null;
	}

	public Class<HelloWorld> getModuleClass() {
		return HelloWorld.class;
	}

	public String getName() {
		return "Hello World";
	}

	public float getVersion() {
		return 1.5f;
	}

	public IModule newInstance() {
		return new HelloWorld();
	}
	

}
