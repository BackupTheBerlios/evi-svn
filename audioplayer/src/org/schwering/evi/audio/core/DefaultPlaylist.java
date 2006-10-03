/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

import java.io.File;

import org.schwering.evi.audio.AudioPlayer;
import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.ModuleContainer;

/**
 * The default playlist which reads from a Properties config file.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class DefaultPlaylist extends Playlist {
	private static final String DEFAULT_PLAYLIST_APPENDIX = "_default_playlist";
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
			props = new Properties(ModuleContainer.getIdByClass(AudioPlayer.class) + DEFAULT_PLAYLIST_APPENDIX);
			props.load();
			String s;
			for (int i = 0; (s = props.getString("entry"+i, null)) != null; i++) {
				try {
					addElement(new File(s));
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
			playingIndex = props.getInt("playingindex", -1);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Saves the playlist to a org.schwering.evi.conf.Properties file.
	 */
	public void save() {
		try {
			props.clear();
			filter("");
			File[] files = getElements();
			for (int i = 0; i < files.length; i++) {
				props.setString("entry"+ i, files[i].toString());
			}
			props.setInt("playingindex", playingIndex);
			props.store();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
