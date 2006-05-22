package org.schwering.evi.core;

import java.awt.Component;

/**
 * Interface for applet modules.<br />
 * <br />
 * This interface must be implemented by modules which want to display a small
 * panel at the main application's toolbar. This small panel is typically at 
 * the top right. It is always displayed, even if the owning module is not 
 * "on top".<br />
 * <br />
 * This interface does not extend {@link IModule}. The reason is that 
 * <code>IApplet</code>'s function is quite analogous to <code>IPanel</code>'s.
 * <code>IPanel</code> itself is does not extend <code>IModule</code>; because
 * also non-module-classes might want to be <code>IPanel</code>s. While this 
 * doesn't make much sense for <code>IApplet</code>, <code>IApplet</code> 
 * is still very similar to <code>IPanel</code> in its idea and therefore 
 * it does not extend <code>IModule</code> either.
 * <br />
 * <a name="modules_imodule_iapplet"></a>
 * <u>To put it in a nutshell:</u> Modules that are inteded to display an 
 * applet should implement both, <code>IModule</code> and <code>IApplet</code>. 
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IApplet {
	/**
	 * Returns a reference to the module instance's applet part as 
	 * {@link java.awt.Component}. Each invokation of 
	 * <code>getAppletInstance</code> must return a reference to the same 
	 * object. This means that this method must not create a new applet but 
	 * return just a reference to a once created 
	 * <code>java.awt.Component</code>.<br />
	 * <br />
	 * If the implementing module class <code>extends java.awt.Component</code>
	 * (e.g. if it <code>extends javax.swing.JPanel</code>) this method simply 
	 * might return <code>this</code>.<br />
	 * @return The concrete applet-part as <code>java.awt.Component</code>.
	 */
	public Component getAppletInstance();
	
	/**
	 * Should clean up used resources when the panel is shut down.<br />
	 * <br />
	 * This method is also declared in <code>IModule</code> 
	 * ({@link IModule#dispose()}). This makes it easy to write an applet 
	 * module that implements both, <code>IModule</code> and 
	 * <code>IApplet</code>. See <a href="#modules_imodule_iapplet">these weird
	 * explanations</a>.
	 * @see IModule#dispose()
	 */
	public void dispose();
}