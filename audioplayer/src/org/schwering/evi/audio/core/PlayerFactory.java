package org.schwering.evi.audio.core;

import java.io.File;
import java.util.Hashtable;

/**
 * 
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
		map.put("mp3", MP3Player.class);
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
	 * @param file
	 * @return A <code>Player</code> object.
	 * @throws PlayerException If there is no player for the given file or if 
	 * there is such a player but it throws an instance when being instantiated.
	 */
	public static Player createPlayer(File file) throws PlayerException {
		Class cls = getClassByExtension(file);
		if (cls == null) {
			throw new PlayerException("Format not supported: "+ file);
		}
		try {
			Object instance = cls.newInstance();
			Player player = (Player)instance;
			player.setFile(file);
			return player;
		} catch (Exception exc) {
			throw new PlayerException(exc);
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
	private static Class getClassByExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf('.');
		if (index == -1) {
			return null;
		}
		String ext = name.substring(index + 1).toLowerCase();
		Class cls = (Class)map.get(ext);
		return cls;
	}
}
