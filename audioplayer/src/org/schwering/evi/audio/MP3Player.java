package org.schwering.evi.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 * Implements an MP3 player using the JLayer API.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class MP3Player implements Player {
	protected File file;
	protected InputStream stream;
	protected AdvancedPlayer player;
	protected boolean completed = false;
	protected Vector listeners = new Vector();
	
	public MP3Player(File f) throws PlayerException {
		try {
			file = f;
			stream = new FileInputStream(file);
			player = new AdvancedPlayer(stream);
			player.setPlayBackListener(new PlaybackListener() {
				public void playbackStarted(PlaybackEvent e) {
					firePlaybackStarted();
				}
				
				public void playbackFinished(PlaybackEvent e) {
					if (completed) {
						firePlaybackCompleted();
					} else {
						firePlaybackStopped();
					}
					player.close();
					player = null;
				}
			});
		} catch (Exception exc) {
			throw new PlayerException(exc);
		}
	}
	
	public File getFile() {
		return file;
	}
	
	public void play() throws PlayerException {
		play(0, Integer.MAX_VALUE);
	}
	
	public void play(int from, int to) throws PlayerException {
		try {
			completed = false;
			if (player.play(from, to)) {
				completed = true;
			}
		} catch (Exception exc) {
			throw new PlayerException(exc);
		}
	}
	
	public void pause() {
		System.err.println("Not yet implemented");
	}
	
	public synchronized void stop() {
		if (player != null) {
			player.stop();
		}
	}
	
	public synchronized boolean isPlaying() {
		return player != null;
	}
	
	public synchronized boolean isCompleted() {
		return completed;
	}
	
	public void addListener(PlayerListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(PlayerListener listener) {
		listeners.remove(listener);
	}
	
	protected void firePlaybackStarted() {
		for (int i = 0; i < listeners.size(); i++) {
			((PlayerListener)listeners.get(i)).playbackStarted();
		}
	}
	
	protected void firePlaybackCompleted() {
		for (int i = 0; i < listeners.size(); i++) {
			((PlayerListener)listeners.get(i)).playbackCompleted();
		}
	}
	
	protected void firePlaybackStopped() {
		for (int i = 0; i < listeners.size(); i++) {
			((PlayerListener)listeners.get(i)).playbackStopped();
		}
	}
}
