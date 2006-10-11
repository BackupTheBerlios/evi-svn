package org.schwering.evi.audio.core;

import java.net.URL;
import java.util.Hashtable;

/**
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class PlayerFactory {
	/**
	 * Stores the supported formats and their implementations.
	 * The keys are the file extensions in <b>lower case</b>. 
	 * The values are the <code>Class</code>es that implement the 
	 * <code>Player</code> for the format.
	 */
	private static Hashtable map = new Hashtable();
	
	static {
		map.put(".mp3", MP3Player.class);
		map.put("http", MP3Player.class);
	}
	
	/**
	 * Disallow instances.
	 */
	private PlayerFactory() {
	}
	
	/**
	 * Creates a player for the file. If there is no such player or if for 
	 * any other reason an exception is thrown, this method throws a 
	 * <codePlayerException</code>.
	 * @param url The URL that should be played.
	 * @return A <code>Player</code> object.
	 * @throws PlayerException If there is no player for the given file or if 
	 * there is such a player but it throws an instance when being instantiated.
	 */
	public static Player createPlayer(URL url) throws FormatNotSupportedException {
		Class cls = getClassByURL(url);
		if (cls == null) {
			throw new FormatNotSupportedException("Format not supported: "+ url);
		}
		try {
			Object instance = cls.newInstance();
			Player player = (Player)instance;
			player.setResource(url);
			return player;
		} catch (Exception exc) {
			throw new FormatNotSupportedException(exc);
		}
	}
	
	/**
	 * Returns the <code>Class</code> that implements the right 
	 * <code>Player</code> for the <code>file</code>. 
	 * The file-extension &lt;-&gt; player pair must be stored in the 
	 * <code>map</code> table.
	 * @param file The file that should be played.
	 * @return The <code>Class</code> that implements the right 
	 * <code>Player</code> or <code>null</code>.
	 */
	private static Class getClassByURL(URL url) {
		Class cls;
		if ((cls = getClassByExtension(url)) != null) {
			return cls;
		} else if ((cls = getClassByProtocol(url)) != null) {
			return cls;
		} else {
			return null;
		}
	}
		
	private static Class getClassByExtension(URL url) {
		String name = url.getFile();
		int index = name.lastIndexOf('.');
		if (index == -1) {
			return null;
		}
		String ext = name.substring(index).toLowerCase();
		Class cls = (Class)map.get(ext);
		return cls;
	}
	
	private static Class getClassByProtocol(URL url) {
		String protocol = url.getProtocol();
		Class cls = (Class)map.get(protocol);
		return cls;
	}
}
