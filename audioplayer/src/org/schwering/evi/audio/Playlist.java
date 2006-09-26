package org.schwering.evi.audio;

import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListModel;

/**
 * The abstract base class for a playlist. This class provides navigation mechanisms.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
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
	protected PlayerListener configPlayerListener = new PlayerListener() {
		public void playbackStarted() {
		}
		public void playbackStopped() {
		}
		public void playbackCompleted() {
			playNext();
		}
	};
	
	public Playlist() {
	}
	
	public Playlist(File[] files) {
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				list.addElement(files[i]);
			}
		}
	}
	
	public abstract void load();
	
	public abstract void save();
	
	public DefaultListModel getListModel() {
		return list;
	}
	
	public void add(File[] f) {
		if (f != null) {
			for (int i = 0; i < f.length; i++) {
				add(f[i]);
			}
		}
	}
	
	public void add(File f) {
		list.addElement(f);
	}
	
	public void add(int index, File f) {
		list.add(index, f);
	}
	
	public File[] get() {
		File[] arr = new File[list.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = get(i);
		}
		return arr;
	}
	
	public File get(int index) {
		return (File)list.get(index);
	}
	
	public void remove(int index) {
		list.remove(index);
	}
	
	public void remove(File f) {
		list.removeElement(f);
	}
	
	public void removeAll() {
		list.removeAllElements();
	}
	
	public synchronized boolean isPlaying() {
		return player != null && player.isPlaying();
	}

	public synchronized void playNext() {
		synchronized (this) {
			playingIndex++;
			if (playingIndex >= list.size()) {
				playingIndex %= list.size();
			}
		}
		play(playingIndex);
	}
	
	public void playPrevious() {
		synchronized (this) {
			playingIndex--;
			if (playingIndex < 0) {
				playingIndex = list.size() + playingIndex;
			}
		}
		play(playingIndex);
	}
	
	public void play() {
		if (playingIndex < 0) {
			playingIndex = 0;
		}
		play(playingIndex);
	}
	
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
}
