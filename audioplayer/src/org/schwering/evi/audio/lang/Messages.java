/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.lang;

import java.util.Locale;
import java.util.ResourceBundle;

import org.schwering.evi.conf.LanguageAdministrator;

/**
 * Provides access to externalized strings. This class is 
 * used for multiple language support.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class Messages {
	private static final String BUNDLE_NAME = "org.schwering.evi.audio.lang.messages"; //$NON-NLS-1$

	private static ResourceBundle bundle;
	
	static {
		LanguageAdministrator.registerMessagesClass(Messages.class);
	}

	/**
	 * No instances allowed/useful.
	 */
	private Messages() {
	}
	
	/**
	 * Loads the <code>ResourceBundle</code> with the given locale.
	 * <br>
	 * Invoked by {@link org.schwering.evi.conf.LanguageAdministrator}.
	 * @param locale The language which is to be loaded.
	 */
	public static void load(Locale locale) {
		bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	/**
	 * Searches for a given key.
	 * @param key The key.
	 * @return The externalized string.
	 */
	public static String getString(String key) {
		try {
			return bundle.getString(key);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}
}
