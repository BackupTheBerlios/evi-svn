package org.schwering.evi.core;

/**
 * Interface for configurable modules.<br />
 * <br />
 * This interface must be implemented by modules that can be configured
 * in a graphical user interface.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IConfigurable extends IModule {
	/**
	 * Returns the configuration dialog.
	 * @return The configuration dialog. 
	 */
	public IPanel getConfigPanel();
}
