/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.conf;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Vector;

/**
 * The language administrator tells the <code>Messages</code> classes 
 * when to load.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class LanguageAdministrator {
	/**
	 * The default locale. This one is used if no locale is set in 
	 * user.language system property and no locale is set in the configuration
	 * either.
	 */
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
	/**
	 * All available locales.
	 */
	public static final Locale[] AVAILABLE_LOCALES = new Locale[] {
		Locale.ENGLISH,
		Locale.GERMAN
	};
	
	private static Vector list = new Vector();
	private static boolean loaded = false;
	
	/**
	 * Each <code>Messages</code> class should register itself. Then, 
	 * EVI can load its <code>messages.properties</code> file when it's
	 * time to do so.
	 * @param c The <code>Messages</code>-class. It must 
	 */
	public static void registerMessagesClass(Class c) {
		list.add(c);
		if (loaded) {
			load(c);
		}
	}
	
	/**
	 * Loads each <code>Messages</code>-class's externalized-strings-file
	 * with the locale set in <code>MainConfiguration</code> behind the 
	 * key <code>app.lang</code>.
	 */
	public static void load() {
		loaded = true;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Class c = (Class)list.get(i);
			load(c);
		}
	}
	
	/**
	 * Loads a specific <code>Messages</code>-class.
	 * @param c The <code>Messages</code>-class.
	 */
	private static void load(Class c) {
		Locale locale = MainConfiguration.PROPS.getLocale("app.lang");
		Class[] argType = new Class[] { Locale.class };
		Object[] argObject = new Object[] { locale };
		try {
			Method m = c.getMethod("load", argType);
			m.invoke(null, argObject);
		} catch (Exception exc) {
			System.err.println("Invalid Messages class ("+ c +":");
			exc.printStackTrace();
		}
	}
	
	/**
	 * Returns the Java VM user.language as Locale.
	 * @return A Locale that represents the language set in the system
	 * property user.language.
	 */
	public static Locale getUserLanguage() {
		try {
			String s = System.getProperty("user.language");
			Locale locale = new Locale(s);
			boolean found = false;
			for (int i = 0; i < AVAILABLE_LOCALES.length; i++) {
				if (AVAILABLE_LOCALES[i].equals(locale)) {
					found = true;
					break;
				}
			}
			return (found) ? locale : DEFAULT_LOCALE; 
		} catch (Exception exc) {
			return DEFAULT_LOCALE;
		}
	}
}
