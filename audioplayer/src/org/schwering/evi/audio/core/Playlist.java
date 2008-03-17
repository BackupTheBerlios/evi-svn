/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.LinkedList;
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
	/** Maximum count of elements in the history. Can be changed without causing erros. */
	public static final int HISTORY_SIZE = 30;
	
	/** Contains ItemWrapper objects that build the list. */
	protected Vector<ItemWrapper> list = new Vector<ItemWrapper>();
	
	/** Contains javax.swing.event.ListDataListeners as this is a javax.swing.ListModel. */
	protected Vector<ListDataListener> listDataListeners = new Vector<ListDataListener>(3);
	
	/** The currently played index. */
	protected int playingIndex = -1;
	
	/** The thread used to play the current file. */
	protected PlayerThread playerThread;
	
	/** Contains IPlaylistListeners. */
	protected Vector<IPlaylistListener> listeners = new Vector<IPlaylistListener>(3);
	
	/** If true, the next song is played automatically. */
	protected boolean playAll = true;
	
	/** If true and if playAll is true, the next song is chosen randomly. */
	protected boolean shuffle = false;
	
	/** The first element is the next-to-play; new entries are added at the ending. */
	protected LinkedList<URL> queue = new LinkedList<URL>();
	
	/** The last element is the last-played; the first element is the oldest in history. */
	protected LinkedList<URL> history = new LinkedList<URL>();
	
	/**
	 * Creates a new empty playlist.
	 */
	public Playlist() {
	}
	
	/**
	 * Creates a new playlist with given files.
	 * @param urls The entries of the playlist.
	 */
	public Playlist(URL[] urls) {
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				addElement(urls[i]);
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
	
	/* Listeners as requested by the javax.swing.ListModel interface */
	
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
	
	/* Index conversion methods */

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
			if (((ItemWrapper)list.get(largeIndex)).isVisible()) {
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
			ItemWrapper iw = (ItemWrapper)list.get(i);
			if (iw.isVisible()) {
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
	 * Returns the index of a URL or -1.
	 * @param url The searched URL.
	 */
	public int indexOf(URL url) {
		int size = list.size();
		for (int i = 0, j = 0; i < size; i++) {
			ItemWrapper iw = (ItemWrapper)list.get(i);
			if (iw.isVisible()) {
				if (iw.getURL().equals(url)) {
					return j;
				}
				j++;
			}
		}
		return -1;
	}
	
	/**
	 * Adds a URL at a given position.
	 * @param index The position in the list.
	 * @param url The URL that is to be added to the playlist.
	 */
	public void addElementAt(int index, URL url) {
		list.add(smallToLarge(index)+1, new ItemWrapper(url));
		fireIntervalAdded(index, index);
	}
	
	/**
	 * Returns a specific entry of the playlist. Though the return type is
	 * <code>Object</code>, it is definetly an instance of <code>URL</code>.
	 * @param index The position of the wanted entry.
	 * @return A URL object.
	 */
	public Object getElementAt(int index) {
		ItemWrapper iw = (ItemWrapper)list.get(smallToLarge(index));
		Object o = iw.getURL();
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
	 * @param files The files that are to be added to the playlist.
	 */
	public void addElements(File[] files) {
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				addElement(files[i]);
			}
		}
	}
	
	/**
	 * Adds a list of new URLs.
	 * @param urls The URL that are to be added to the playlist.
	 */
	public void addElements(URL[] urls) {
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				addElement(urls[i]);
			}
		}
	}
	
	/**
	 * Adds a new File.
	 * @param f The file that is to be added to the playlist.
	 */
	public void addElement(File f) {
		try {
			int index = getSize();
			addElementAt(index, f.toURL());
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
	
	/**
	 * Adds a new URL.
	 * @param url The URL that is to be added to the playlist.
	 */
	public void addElement(URL url) {
		int index = getSize();
		addElementAt(index, url);
	}
	
	/**
	 * Returns all entries of the playlist.
	 * @return An array of URL objects that represent the playlist's entries.
	 */
	public URL[] getElements() {
		URL[] arr = new URL[getSize()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (URL)getElementAt(i);
		}
		return arr;
	}
	
	/**
	 * Removes a specific URL from the playlist.
	 * @param url The URL that is to be removed from the playlist.
	 * @return <code>true</code> if the URL was removed successfully.
	 */
	public void removeElement(URL url) {
		int index = indexOf(url);
		removeElementAt(index);
	}
	
	/* The following methods are used for playlist navigation/settings */
	
	/**
	 * Returns the current player or <code>null</code>.
	 * @return the current player or <code>null</code>.
	 */
	public Player getPlayer() {
		return (playerThread != null) ? playerThread.getPlayer() : null;
	}
	
	/**
	 * Returns the currently played URL or <code>null</code>.
	 * @return the currently played URL or <code>null</code>.
	 */
	public URL getPlayingURL() {
		return (getPlayer() != null) ? getPlayer().getResource() : null;
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
	 * @param random <code>true</code> enables shuffle mode.
	 */
	public void setShuffle(boolean random) {
		this.shuffle = random;
	}
	
	/**
	 * Indicates whether shuffle mode is enabled.<br />
	 * <code>false</code> by default.
	 * @return <code>true</code> if shuffle mode is enabled.
	 */
	public boolean isShuffle() {
		return shuffle;
	}
	
	/* Search methods */
	
	/**
	 * Filters the playlist. The algorithm is defined in the 
	 * <code>matches(URLWrapper, String)</code> method. This method should be overridden 
	 * by subclasses to change the algorithm.
	 * @param query
	 * @see #largeToSmall(int)
	 * @see #smallToLarge(int)
	 * @see ItemWrapper
	 * @see ItemWrapper#matches(String)
	 */
	public synchronized void filter(String query) {
		int complete = list.size();
		int oldSize = getSize();
		for (int i = 0; i < complete; i++) {
			ItemWrapper iw = (ItemWrapper)list.get(i);
			iw.setVisible(matches(iw, query));
		}
		int newSize = getSize();
		if (oldSize > 0) {
			fireIntervalRemoved(0, oldSize - 1);
		}
		if (newSize > 0) {
			fireIntervalAdded(0, newSize - 1);
		}
	}
	
	/**
	 * The algorithm that checks whether a playlist-entry matches a query.
	 * Override this method in subclasses to change the algorithm.<br />
	 * The default algorithm splits the query at each whitespace and then searches for 
	 * each element using in the entry's URL (using <code>String.indexOf</code>).
	 * The method  is case-insensitive.
	 * @param iw The filewrapper.
	 * @param query The search query.
	 * @return <code>true</code> if the file matches the query.
	 */
	protected boolean matches(ItemWrapper iw, String query) {
		String name = iw.getURL().toString().toLowerCase();
		query = query.toLowerCase();
		String[] elements = query.split("\\s");
		
		for (int i = 0; i < elements.length; i++) {
			if (name.indexOf(elements[i]) == -1) {
				return false;
			}
		}
		return true;
	}
	
	/* Queue methods */
	
	/**
	 * Enqueues a URL.
	 * @param index The index of the URL which should be played.
	 */
	public void addToQueue(int index) {
		addToQueue((URL)getElementAt(index));
	}
	
	/**
	 * Enqueues a URL.
	 * @param url The URL which should be played.
	 */
	public void addToQueue(URL url) {
		queue.addLast(url);
	}
	
	/**
	 * Checks whether a URL is in the queue.
	 * @param index The index of the URL which might be in the queue or not.
	 * @return <code>true</code> if the URL is enqueued.
	 */
	public boolean isInQueue(int index) {
		return isInQueue((URL)getElementAt(index));
	}
	
	/**
	 * Checks whether a URL is in the queue.
	 * @param url The URL which might be in the queue or not.
	 * @return <code>true</code> if the URL is enqueued.
	 */
	public boolean isInQueue(URL url) {
		return queue.contains(url);
	}
	
	/**
	 * Removes a URL from the queue.
	 * @param index The index of the URL which should be removed.
	 */
	public void removeFromQueue(int index) {
		removeFromQueue((URL)getElementAt(index));
	}
	
	/**
	 * Removes a URL from the queue.
	 * @param url The URL which should be removed.
	 */
	public void removeFromQueue(URL url) {
		queue.remove(url);
	}
	
	/* Player controlling */
	
	/**
	 * Indicates whether any song is being played of the playlist at the moment.
	 * @return <code>true</code> is the player is playing.
	 */
	public synchronized boolean isPlaying() {
		return getPlayer() != null && getPlayer().isPlaying();
	}
	
	/**
	 * Decides which song to play depending on the playlist settings.
	 */
	public void next() {
		if (queue.size() > 0) {
			URL url = (URL)queue.removeFirst();
			int index = indexOf(url);
			play(index);
		} else if (isPlayAll()) {
			if (!isShuffle()) {
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
	private synchronized void playRandom() {
		int size = getSize();
		if (size > 1) {
			int newIndex;
			do {
				newIndex = ((int)(Math.random() * size)) % size;
			} while (newIndex == playingIndex);
			playingIndex = newIndex;
		}
		play(playingIndex);
	}

	/**
	 * Plays the subsequent song.
	 */
	private synchronized void playNext() {
		int index;
		index = playingIndex + 1;
		int size = getSize();
		if (size > 0 && index >= size) {
			index %= size;
		}
		play(index);
	}
	
	/**
	 * Plays the previous song.
	 */
	private synchronized void playPrevious() {
		int index = -1;
		if (history.size() > 0) {
			URL previous = (URL)history.removeLast();
			index = indexOf(previous);
			if (index == playingIndex) {
				playPrevious();
				return;
			}
		}
		if (index == -1) {
			index = playingIndex - 1;
		}
		int size = getSize();
		if (size > 0 && index < 0) {
			index = size + index;
		}
		play(index);
		if (history.size() > 0) {
			/* play() added the played URL again, so we need to remove once more: */
			history.removeLast(); 
		}
	}
	
	/**
	 * Plays the current (or the first) song.
	 */
	public synchronized void play() {
		if (playingIndex < 0) {
			playingIndex = 0;
		}
		play(playingIndex);
	}
	
	/**
	 * Plays a given song.
	 * @param index The position of the song.
	 */
	public synchronized void play(int index) {
		int size = getSize();
		if (size == 0) {
			return;
		} else if (index < 0) {
			index = 0;
		} else if (index > size) {
			index %= size;
		}
		playingIndex = index;
		stop();
		try {
			final URL url = (URL)getElementAt(index);
			if (history.size() > HISTORY_SIZE) {
				history.removeFirst();
			}
			history.addLast(url);
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
			final Player p = PlayerFactory.createPlayer(url);
			p.addListener(getPassthroughPlayerListener(p));
			p.addListener(getConfigPlayerListener(p));
			stop(); // interrupt()s the thread
			playerThread = new PlayerThread(p);
			playerThread.start();
		} catch (Exception exc) {
			exc.printStackTrace();
			firePlaybackFailed(null);
		}
	}
	
	protected IPlayerListener getPassthroughPlayerListener(final Player player) {
		return new IPlayerListener() {
			public void playbackStarted() {
				firePlaybackStarted(player);
			}
			public void playbackStopped() {
				firePlaybackStopped(player);
			}
			public void playbackCompleted() {
				firePlaybackCompleted(player);
			}
			public void playbackFailed() {
				firePlaybackCompleted(player);
			}
		};
	}
	
	protected IPlayerListener getConfigPlayerListener(final Player player) {
		return new IPlayerListener() {
			public void playbackStarted() {
			}
			public void playbackStopped() {
			}
			public void playbackCompleted() {
				next();
			}
			public void playbackFailed() {
				next();
			}
		};
	}

	/**
	 * Immediately stops playing.
	 */
	public synchronized void stop() {
		Player player = getPlayer();
		if (player != null) {
			player.stop();
			player = null;
		}
		if (playerThread != null && !playerThread.isInterrupted()) {
			playerThread.interrupt();
		}
	}
	
	/* The following methods fire listener events */
	
	protected void fireIntervalAdded(int from, int to) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, from, to);
		for (int i = listDataListeners.size() - 1; i >= 0; i--) {
			((ListDataListener)listDataListeners.get(i)).intervalAdded(e);
		}
	}
	
	protected void fireIntervalRemoved(int from, int to) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, from, to);
		for (int i = listDataListeners.size() - 1; i >= 0; i--) {
			((ListDataListener)listDataListeners.get(i)).intervalRemoved(e);
		}
	}
	
	protected void fireContentsChanged(int from, int to) {
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, from, to);
		for (int i = listDataListeners.size() - 1; i >= 0; i--) {
			((ListDataListener)listDataListeners.get(i)).contentsChanged(e);
		}
	}
	
	protected void firePlaybackStarted(Player player) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IPlaylistListener)listeners.get(i)).playbackStarted(player);
		}
	}
	
	protected void firePlaybackCompleted(Player player) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IPlaylistListener)listeners.get(i)).playbackCompleted(player);
		}
	}
	
	protected void firePlaybackStopped(Player player) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IPlaylistListener)listeners.get(i)).playbackStopped(player);
		}
	}
	
	protected void firePlaybackFailed(Player player) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((IPlaylistListener)listeners.get(i)).playbackFailed(player);
		}
	}
	
	/**
	 * Adds a new IPlaylistListener.
	 * @param listener The new listener.
	 */
	public void addListener(IPlaylistListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a IPlaylistListener.
	 * @param listener The listener that's to be removed.
	 */
	public void removeListener(IPlaylistListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Wraps a URL which can be set visible or not using a query (for search).
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	protected class ItemWrapper {
		private URL url;
		private boolean visible = true;
		
		public ItemWrapper(URL url) {
			this.url = url;
		}
		
		public URL getURL() {
			return url;
		}
		
		public void setVisible(boolean b) {
			visible = b;
		}
		
		public boolean isVisible() {
			return visible;
		}
	}
	
	/**
	 * Plays a <code>Player</code> in a different thread.
	 * @author Christoph Schwering (mailto:schwering@gmail.com)
	 */
	protected class PlayerThread extends Thread {
		private Player p;
		
		public PlayerThread(Player p) {
			setDaemon(true);
			this.p = p;
		}
		
		public synchronized Player getPlayer() {
			return p;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				p.play();
				p.stop();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
}
