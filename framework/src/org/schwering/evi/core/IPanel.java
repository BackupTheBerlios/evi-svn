package org.schwering.evi.core;

import java.awt.Component;

import javax.swing.Icon;

/**
 * Interface for graphical modules.<br />
 * <br />
 * This interface must be implemented by modules which have a graphical
 * frontend which might be accessible through a tabbar, for example.<br />
 * <br />
 * This interface does not extend {@link IModule}. The reason is that 
 * <code>IPanel</code>s might (and also must) also be used for configuration 
 * dialogs (see {@link IConfigurable#getConfigPanel()}) and so on. Nevertheless, 
 * the <code>IPanel</code> interface defines a <code>dispose()</code> method
 * same like the <code>IModule</code> interface. Hence, each class that 
 * implements <code>IPanel</code> can also implement <code>IModule</code> and
 * vice versa.<br />
 * <br />
 * <u>To put it in a nutshell:</u> Modules that are inteded to have a GUI 
 * should implement both, <code>IModule</code> and <code>IPanel</code>. 
 * <code>IPanel</code> is seperate, because it is important for classes that
 * are no modules (more precisely: that are not a module's main-class). An 
 * example for this case are module-configuratoin-dialogs.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IPanel {
	/**
	 * Returns a reference to the module instance's GUI part as 
	 * {@link java.awt.Component}. Each invokation of 
	 * <code>getPanelInstance</code> must return a reference to the same 
	 * object. This means that this method must not create a new panel but 
	 * return just a reference to a once created panel.<br />
	 * <br />
	 * If the implementing module <code>extends java.awt.Component</code> (e.g.
	 * if it <code>extends javax.swing.JPanel</code>) this method simply might 
	 * return <code>this</code>.<br />
	 * <br />
	 * @return The concrete GUI-part as <code>java.awt.Component</code>.
	 */
	public Component getPanelInstance();
	
	/**
	 * Returns the panel's title. The title is to be used in the tabbar.
	 * @return The title.
	 */
	public String getTitle();
	
	/**
	 * Returns the panel's icon. If the panel should have no icon, this method
	 * must return <code>null</code>.
	 * @return The panel's icon or <code>null</code>.
	 * @see javax.swing.ImageIcon
	 */
	public Icon getIcon();
	
	/**
	 * Should clean up used resources when the panel is shut down.<br />
	 * This method is also declared in <code>IModule</code> 
	 * ({@link IModule#dispose()}). This makes it easy to write a graphical 
	 * module that implements both. See the weird explanations at the top.
	 * @see IModule#dispose()
	 */
	public void dispose();
}