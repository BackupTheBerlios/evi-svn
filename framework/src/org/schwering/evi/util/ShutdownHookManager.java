package org.schwering.evi.util;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Manages shutdown hooks. The difference adding and removing shutdown hooks 
 * directly via <code>java.lang.Runtime</code> are controller threads that are
 * added automatically for each shutdown hook. The controller thread kills the 
 * shutdown hook if the latter runs too long (and seems to hang).
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ShutdownHookManager {
	private static HashSet hooks = new HashSet();
	private static Hashtable controllers = new Hashtable();
	private static final long MAX_TIME = 20 * 1000;
	
	/**
	 * Creates a controller for the thread. The controller is a simple thread 
	 * that kills the shutdown hook <code>thread</code> if it is still running 
	 * after <code>MAX_TIME</code> milliseconds.
	 * @param thread The shutdown hook.
	 * @param maxTime The maximum time in milliseconds the thread my run.
	 * @return The controller thread.
	 */
	private static Thread createController(final Thread thread, final long maxTime) {
		Thread controller = new Thread() {
			public void run() {
				try {
					long millis = 0;
					final long sleepTime = 25;
					while (thread.isAlive() && millis < maxTime) {
						Thread.sleep(sleepTime);
						millis += sleepTime;
					}
					if (thread.isAlive()) {
						thread.interrupt();
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		};
		controller.setName("controller("+ thread.getName() +")");
		return controller;
	}
	
	/**
	 * Adds a shutdown hook and an additional controler.
	 * The controler kills the shutdown hook if the latter takes more than 
	 * <code>maxTime</code> seconds to finish.
	 * @param thread The shutdown hook.
	 */
	public static void addShutdownHook(Thread thread) {
		addShutdownHook(thread, MAX_TIME);
	}
	
	/**
	 * Adds a shutdown hook and an additional controler.
	 * The controler kills the shutdown hook if the latter takes more than 
	 * <code>maxTime</code> seconds to finish.
	 * @param thread The shutdown hook.
	 * @param maxTime The maximum time in milliseconds the thread my run.
	 */
	public static void addShutdownHook(Thread thread, long maxTime) {
		if (!hooks.contains(thread)) {
			Runtime r = Runtime.getRuntime();
			hooks.add(thread);
			r.addShutdownHook(thread);
			Thread controller = createController(thread, maxTime);
			controllers.put(thread, controller);
			r.addShutdownHook(controller);
		}
	}
	
	/**
	 * Removes a shutdown hook. The hook is only removed if it has been added 
	 * using <code>ShutdownHookManager.addShutdownHook</code>.
	 * @param thread The shutdown hook that should be removed.
	 */
	public static void removeShutdownHook(Thread thread) {
		if (hooks.remove(thread)) {
			Runtime r = Runtime.getRuntime();
			r.removeShutdownHook(thread);
			Thread controller = (Thread)controllers.get(thread);
			r.removeShutdownHook(controller);
		}
	}
}
