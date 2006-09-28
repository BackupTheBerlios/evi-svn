/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * The abstract base class for a playlist. This class provides navigation mechanisms.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class Playlist implements ListModel {
	protected Vector list = new Vector();
	protected Vector listDataListeners = new Vector();
	protected int playingIndex = -1;
	protected Player player;
	protected Vector listeners = new Vector();
	protected boolean playAll = true;
	protected boolean random = false;
	protected Vector queue = new Vector();
	
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
				addElement(files[i]);
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
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
	 */
	public void addListDataListener(ListDataListener listener) {
		listDataListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
	 */
	public void removeListDataListener(ListDataListener listener) {
		listDataListeners.remove(listener);
	}
	
	public synchronized void filter(String query) {
		int complete = list.size();
		int oldSize = getSize();
		for (int i = 0; i < complete; i++) {
			FileWrapper fw = (FileWrapper)list.get(i);
			fw.matches(query);
		}
		int newSize = getSize();
		if (oldSize > 0) {
			fireIntervalRemoved(0, oldSize - 1);
		}
		if (newSize > 0) {
			fireIntervalAdded(0, newSize - 1);
		}
	}
	
	/* Iindex conversion methods */

	/**
	 * Converts an index that refers to the Vector list object (that means to 
	 * the total playlist) to an index that refers to the visible list (which 
	 * might be smaller than the playlist due to a search!).
	 * @param largeIndex The index that refers to Vector list.
	 * @return An index that refers to the visible list.
	 */
	private synchronized int largeToSmall(int largeIndex) {
		int smallIndex = 0;
		while (--largeIndex >= 0) {
			if (((FileWrapper)list.get(largeIndex)).isVisible()) {
				smallIndex++;
			}
		}
		return smallIndex;
	}
	
	/**
	 * Converts an index that refers to the visible list to an index that 
	 * refers to the Vector list (which might be larger than the visible list
	 * due to a search!).
	 * @param smallIndex The index that refers to the visible list.
	 * @return An index that refers to the Vector list.
	 */
	private synchronized int smallToLarge(int smallIndex) {
		int complete = list.size();
		int largeIndex = -1;
		for (int i = 0; i < complete && smallIndex >= 0; i++) {
			FileWrapper fw = (FileWrapper)list.get(i);
			if (fw.isVisible()) {
				smallIndex--;
			}
			largeIndex++;
		}
		return largeIndex;
	}
	
	/* The following methods directly access the Vector list object */
	
	/**
	 * Returns the size of the list.
	 * @return The size.
	 */
	public int getSize() {
		int size = list.size();
		return largeToSmall(size);
	}
	
	/**
	 * Returns the index of a file or -1.
	 * @param file The searched file.
	 */
	public int indexOf(File file) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			FileWrapper fw = (FileWrapper)list.get(i);
			if (fw.isVisible() && fw.getFile().equals(file)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Adds a file at a given position.
	 * @param index The position in the list.
	 * @param f The file that is to be added to the playlist.
	 */
	public void addElementAt(int index, File f) {
		list.add(smallToLarge(index)+1, new FileWrapper(f));
		fireIntervalAdded(index, index);
	}
	
	/**
	 * Returns a specific entry of the playlist. Though the return type is
	 * <code>Object</code>, it is definetly an instance of <code>File</code>.
	 * @param index The position of the wanted entry.
	 * @return A File object.
	 */
	public Object getElementAt(int index) {
		FileWrapper fw = (FileWrapper)list.get(smallToLarge(index));
		Object o = fw.getFile();
		return o;
	}
	
	/**
	 * Removes a specific entry from the playlist.
	 * @param index The position of the entry.
	 */
	public void removeElementAt(int index) {
		list.remove(smallToLarge(index));
		fireIntervalRemoved(index, index);
	}
	
	/**
	 * Removes all entries of the playlist.
	 */
	public void removeAll() {
		int end = getSize() - 1;
		list.clear();
		fireIntervalRemoved(0, end);
	}
	
	/* The following methods indirectly access the Vector list object */
	
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
				addElement(files[i]);
			}
		}
	}
	
	/**
	 * Adds a list of new files.
	 * @param f The files that are to be added to the playlist.
	 */
	public void addElements(File[] f) {
		if (f != null) {
			for (int i = 0; i < f.length; i++) {
				addElement(f[i]);
			}
		}
	}
	
	/**
	 * Adds a new file.
	 * @param f The file that is to be added to the playlist.
	 */
	public void addElement(File f) {
		int index = getSize();
		addElementAt(index, f);
	}
	
	/**
	 * Returns all entries of the playlist.
	 * @return An array of File objects that represent the playlist's entries.
	 */
	public File[] getElements() {
		File[] arr = new File[getSize()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (File)getElementAt(i);
		}
		return arr;
	}
	
	/**
	 * Removes a specific file from the playlist.
	 * @param f The file that is to be removed from the playlist.
	 * @return <code>true</code> if the file was removed successfully.
	 */
	public void removeElement(File f) {
		int index = indexOf(f);
		removeElementAt(index);
	}
	
	/* The following methods are used for playlist navigation/settings */
	
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
	 * Enqueues a file.
	 * @param index THe index of the file which should be played.
	 */
	public void addToQueue(int index) {
		addToQueue((File)getElementAt(index));
	}
	
	/**
	 * Enqueues a file.
	 * @param file The file which should be played.
	 */
	public void addToQueue(File file) {
		queue.add(file);
	}
	
	/**
	 * Checks whether a file is in the queue.
	 * @param file The index of the file which might be in the queue or not.
	 * @return <code>true</code> if the file is enqueued.
	 */
	public boolean isInQueue(int index) {
		return isInQueue((File)getElementAt(index));
	}
	
	/**
	 * Checks whether a file is in the queue.
	 * @param file The file which might be in the queue or not.
	 * @return <code>true</code> if the file is enqueued.
	 */
	public boolean isInQueue(File file) {
		return queue.contains(file);
	}
	
	/**
	 * Removes a file from the queue.
	 * @param index The index of the file which should be removed.
	 */
	public void removeFromQueue(int index) {
		removeFromQueue((File)getElementAt(index));
	}
	
	/**
	 * Removes a file from the queue.
	 * @param file The file which should be removed.
	 */
	public void removeFromQueue(File file) {
		queue.remove(file);
	}
	
	/**
	 * Indicates whether any song is being played of the playlist at the moment.
	 * @return
	 */
	public synchronized boolean isPlaying() {
		return player != null && player.isPlaying();
	}
	
	/**
	 * Decides which song to play depending on the playlist settings.
	 */
	public void next() {
		if (queue.size() > 0) {
			File file = (File)queue.remove(0);
			int index = indexOf(file);
			play(index);
		} else if (isPlayAll()) {
			if (!isRandom()) {
				playNext();
			} else {
				playRandom();
			}
		}
	}
	
	/**
	 * Decides which song to play depending on the playlist settings.
	 */
	public void previous() {
		playPrevious();
	}
	
	/**
	 * Plays a random song.
	 */
	private void playRandom() {
		synchronized (this) {
			int size = getSize();
			if (size > 1) {
				int newIndex;
				do {
					newIndex = ((int)(Math.random() * size)) % size;
				} while (newIndex == playingIndex);
				playingIndex = newIndex;
			}
		}
		play(playingIndex);
	}

	/**
	 * Plays the subsequent song.
	 */
	private void playNext() {
		int index;
		synchronized (this) {
			index = playingIndex + 1;
			int size = getSize();
			if (size > 0 && index >= size) {
				index %= size;
			}
		}
		play(index);
	}
	
	/**
	 * Plays the previous song.
	 */
	private void playPrevious() {
		int index;
		synchronized (this) {
			index = playingIndex - 1;
			int size = getSize();
			if (size > 0 && index < 0) {
				index = size + index;
			}
		}
		play(index);
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
		int size = getSize();
		if (size == 0) {
			return;
		} else if (index < 0) {
			index = 0;
		} else if (index > size) {
			index %= size;
		}
		playingIndex = index;
		if (isPlaying()) {
			player.stop();
		}
		try {
			final File file = (File)getElementAt(index);
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
			p.addListener(getConfigPlayerListener(player));
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
	
	protected PlayerListener getConfigPlayerListener(final Player player) {
		return new PlayerListener() {
			public void playbackStarted() {
			}
			public void playbackStopped() {
			}
			public void playbackCompleted() {
				next();
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
	
	/* The following methods fire listener events */
	
	protected void fireIntervalAdded(int from, int to) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, from, to);
		for (int i = 0; i < listDataListeners.size(); i++) {
			((ListDataListener)listDataListeners.get(i)).intervalAdded(e);
		}
	}
	
	protected void fireIntervalRemoved(int from, int to) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, from, to);
		for (int i = 0; i < listDataListeners.size(); i++) {
			((ListDataListener)listDataListeners.get(i)).intervalRemoved(e);
		}
	}
	
	protected void fireContentsChanged(int from, int to) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, from, to);
		for (int i = 0; i < listDataListeners.size(); i++) {
			((ListDataListener)listDataListeners.get(i)).contentsChanged(e);
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
	
	/**
	 * Wraps a file which can be set visible or not using a query (for search).
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class FileWrapper {
		private File file;
		private boolean visible = true;
		
		public FileWrapper(File f) {
			file = f;
		}
		
		public File getFile() {
			return file;
		}
		
		public boolean matches(String query) {
			visible = (file.toString().toLowerCase().indexOf(query) != -1);
			return visible;
		}
		
		public void setVisible(boolean b) {
			visible = b;
		}
		
		public boolean isVisible() {
			return visible;
		}
	}
}
