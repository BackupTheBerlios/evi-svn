package org.schwering.evi.audio;

import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListModel;

/**
 * The abstract base class for a playlist. This class provides navigation mechanisms.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class Playlist {
	protected DefaultListModel list = new DefaultListModel();
	protected int playingIndex = -1;
	protected Player player;
	protected Vector listeners = new Vector();
	protected PlayerListener passthroughPlayerListener = new PlayerListener() {
		public void playbackStarted() {
			firePlaybackStarted(player);
		}
		public void playbackStopped() {
			firePlaybackStopped(player);
		}
		public void playbackCompleted() {
			firePlaybackCompleted(player);
		}
	};
	protected boolean random = false;
	protected PlayerListener configPlayerListener = new PlayerListener() {
		public void playbackStarted() {
		}
		public void playbackStopped() {
		}
		public void playbackCompleted() {
			if (!random) {
				playNext();
			} else {
				playRandom();
			}
		}
	};
	
	/**
	 * Creates a new empty playlist.
	 */
	public Playlist() {
	}
	
	/**
	 * Creates a new playlist with given files.
	 * @param files The entries of the playlist.
	 */
	public Playlist(File[] files) {
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				list.addElement(files[i]);
			}
		}
	}
	
	/**
	 * Loads the playlist (e.g. from a file).
	 */
	public abstract void load();
	
	/**
	 * Saves the playlist (e.g. to a file).
	 */
	public abstract void save();
	
	public DefaultListModel getListModel() {
		return list;
	}
	
	/**
	 * Adds a list of new files.
	 * @param f The files that are to be added to the playlist.
	 */
	public void add(File[] f) {
		if (f != null) {
			for (int i = 0; i < f.length; i++) {
				add(f[i]);
			}
		}
	}
	
	/**
	 * Adds a new file.
	 * @param f The file that is to be added to the playlist.
	 */
	public void add(File f) {
		list.addElement(f);
	}
	
	/**
	 * Adds a file at a given position.
	 * @param index The position in the list.
	 * @param f The file that is to be added to the playlist.
	 */
	public void add(int index, File f) {
		list.add(index, f);
	}
	
	/**
	 * Returns all entries of the playlist.
	 * @return An array of File objects that represent the playlist's entries.
	 */
	public File[] get() {
		File[] arr = new File[list.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = get(i);
		}
		return arr;
	}
	
	/**
	 * Returns a specific entry of the playlist.
	 * @param index The position of the wanted entry.
	 * @return
	 */
	public File get(int index) {
		return (File)list.get(index);
	}
	
	/**
	 * Removes a specific entry from the playlist.
	 * @param index The position of the entry.
	 */
	public void remove(int index) {
		list.remove(index);
	}
	
	/**
	 * Removes a specific file from the playlist.
	 * @param f The file that is to be removed from the playlist.
	 * @return <code>true</code> if the file was removed successfully.
	 */
	public boolean remove(File f) {
		return list.removeElement(f);
	}
	
	/**
	 * Removes all entries of the playlist.
	 */
	public void removeAll() {
		list.removeAllElements();
	}
	
	/**
	 * Sets whether the next played songs are choosen randomly.
	 * @param random <code>true</code> enables random mode.
	 */
	public void setRandom(boolean random) {
		this.random = random;
	}
	
	/**
	 * Indicates whether random mode is enabled.
	 * @return <code>true</code> if random mode is enabled.
	 */
	public boolean isRandom() {
		return random;
	}
	
	/**
	 * Indicates whether any song is being played of the playlist at the moment.
	 * @return
	 */
	public synchronized boolean isPlaying() {
		return player != null && player.isPlaying();
	}
	
	/**
	 * Plays a random song.
	 */
	public synchronized void playRandom() {
		synchronized (this) {
			if (list.size() == 0) {
				return;
			} else if (list.size() > 1) {
				int newIndex;
				do {
					newIndex = ((int)(Math.random() * list.size())) % list.size();
				} while (newIndex == playingIndex);
				playingIndex = newIndex;
			}
			play(playingIndex);
		}
	}

	/**
	 * Plays the subsequent song.
	 */
	public synchronized void playNext() {
		synchronized (this) {
			playingIndex++;
			if (playingIndex >= list.size()) {
				playingIndex %= list.size();
			}
		}
		play(playingIndex);
	}
	
	/**
	 * Plays the previous song.
	 */
	public void playPrevious() {
		synchronized (this) {
			playingIndex--;
			if (playingIndex < 0) {
				playingIndex = list.size() + playingIndex;
			}
		}
		play(playingIndex);
	}
	
	/**
	 * Plays the current (or the first) song.
	 */
	public void play() {
		if (playingIndex < 0) {
			playingIndex = 0;
		}
		play(playingIndex);
	}
	
	/**
	 * Plays a given song.
	 * @param index The position of the song.
	 */
	public void play(int index) {
		if (index > list.size()) {
			throw new RuntimeException("index = "+ index +" > list.size() = "+ list.size());
		}
		playingIndex = index;
		if (isPlaying()) {
			player.stop();
		}
		try {
			final File file = (File)list.get(index);
			player = new MP3Player(file);
			player.addListener(passthroughPlayerListener);
			player.addListener(configPlayerListener);
			Thread thread = new Thread() {
				public void run() {
					try {
						player.play();
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			};
			thread.setDaemon(true);
			thread.start();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Immediately stops playing.
	 */
	public synchronized void stop() {
		player.stop();
		player = null;
	}
	
	protected void firePlaybackStarted(Player player) {
		for (int i = 0; i < listeners.size(); i++) {
			((PlaylistListener)listeners.get(i)).playbackStarted(player);
		}
	}
	
	protected void firePlaybackCompleted(Player player) {
		for (int i = 0; i < listeners.size(); i++) {
			((PlaylistListener)listeners.get(i)).playbackCompleted(player);
		}
	}
	
	protected void firePlaybackStopped(Player player) {
		for (int i = 0; i < listeners.size(); i++) {
			((PlaylistListener)listeners.get(i)).playbackStopped(player);
		}
	}
	
	/**
	 * Adds a new PlaylistListener.
	 * @param listener The new listener.
	 */
	public void addListener(PlaylistListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a PlaylistListener.
	 * @param listener The listener that's to be removed.
	 */
	public void removeListener(PlaylistListener listener) {
		listeners.remove(listener);
	}
}
