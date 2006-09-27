package org.schwering.evi.audio;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

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
	protected boolean playAll = true;
	protected boolean random = false;
	
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
	
	/**
	 * Returns the ListModel.
	 * @return The ListModel which is in fact a DefaultListModel.
	 */
	public DefaultListModel getListModel() {
		return list;
	}
	
	/**
	 * Returns the current player or <code>null</code>.
	 * @return the current player or <code>null</code>.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Returns the currently played file or <code>null</code>.
	 * @return the currently played file or <code>null</code>.
	 */
	public File getPlayingFile() {
		return (player != null) ? player.getFile() : null;
	}
	
	/**
	 * Returns the size of the list.
	 * @return The size.
	 */
	public int size() {
		return (list != null) ? list.size() : -1;
	}
	
	/**
	 * Adds a directory including its subdirectories.
	 * @param f The base path.
	 */
	public void addDirectory(File f) {
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("f is no directory");
		}
		File[] files = f.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory() || Util.isAudioFile(f);
			}
		});
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				addDirectory(files[i]);
			} else {
				add(files[i]);
			}
		}
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
	 * Sets whether the next song should be played or not.<br />
	 * <code>true</code> by default.
	 * @param playAll If <code>true</code>, not just song will be played but 
	 * also the subsequent ones.
	 */
	public void setPlayAll(boolean playAll) {
		this.playAll = playAll;
	}
	
	/**
	 * Indicates whether the next song will be played or not.<br />
	 * <code>true</code> by default.
	 * @return <code>true</code> if the next songs will be played automagically,
	 * <code>false</code> if just the current song will be played.
	 */
	public boolean isPlayAll() {
		return playAll;
	}
	
	/**
	 * Sets whether the next played songs are choosen randomly.<br />
	 * <code>false</code> by default.
	 * @param random <code>true</code> enables random mode.
	 */
	public void setRandom(boolean random) {
		this.random = random;
	}
	
	/**
	 * Indicates whether random mode is enabled.<br />
	 * <code>false</code> by default.
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
	public void playRandom() {
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
		}
		play(playingIndex);
	}

	/**
	 * Plays the subsequent song.
	 */
	public void playNext() {
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
			/* 
			 * To avoid very much synchronizing, we do not directly manipulate the 
			 * class field "player". Instead, we firstly create a object "p" and set 
			 * "player" to this value. 
			 * Otherwise, later listener events would refer to the class field "player"
			 * whose value might have changed already. (Then the events do not refer to 
			 * the correct Player object.)
			 * Another way to work around this would be synchronizing, but this should 
			 * be faster, I think.
			 */
			final Player p = new MP3Player(file);
			this.player = p;
			p.addListener(getPassthroughPlayerListener(player));
			p.addListener(getConfigPlayerListener());
			Thread thread = new Thread() {
				public void run() {
					try {
						p.play();
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
	
	protected PlayerListener getPassthroughPlayerListener(final Player player) {
		return new PlayerListener() {
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
	}
	
	protected PlayerListener getConfigPlayerListener() {
		return new PlayerListener() {
			public void playbackStarted() {
			}
			public void playbackStopped() {
			}
			public void playbackCompleted() {
				if (isPlayAll()) {
					if (!isRandom()) {
						playNext();
					} else {
						playRandom();
					}
				}
			}
		};
	}

	/**
	 * Immediately stops playing.
	 */
	public synchronized void stop() {
		if (player != null) {
			player.stop();
			player = null;
		}
	}
	
	/**
	 * Fires the <code>ListDataListener.contentsChanged</code> event of the 
	 * ListModel.
	 * @param from The beginning index.
	 * @param to The last index (must be greater than or equal to <code>from</code>).
	 */
	public void fireListModelEvent(int from, int to) {
		ListDataListener[] listeners = list.getListDataListeners();
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, from, to);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].contentsChanged(e);
		}
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
