package org.schwering.evi.audio;

import java.io.File;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.ModuleContainer;

/**
 * The default playlist which reads from a Properties config file.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class DefaultPlaylist extends Playlist {
	protected Properties props;

	/**
	 * Creates and loads the default playlist.
	 */
	public DefaultPlaylist() {
		load();
	}
	
	/**
	 * Loads the playlist from a org.schwering.evi.conf.Properties file.
	 */
	public void load() {
		try {
			props = new Properties(ModuleContainer.getIdByClass(AudioPlayer.class));
			props.load();
			String s;
			for (int i = 0; (s = props.getString("entry"+i, null)) != null; i++) {
				try {
					add(new File(s));
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Saves the playlist to a org.schwering.evi.conf.Properties file.
	 */
	public void save() {
		try {
			File[] files = get();
			for (int i = 0; i < files.length; i++) {
				props.setString("entry"+ i, files[i].toString());
			}
			props.store();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
