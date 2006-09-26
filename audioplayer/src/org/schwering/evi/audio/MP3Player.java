package org.schwering.evi.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * Implements an MP3 player using the JLayer API.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class MP3Player extends Player {
	protected File file;
	protected InputStream stream;
	protected AdvancedPlayer player;
	protected boolean completed = false;
	protected boolean stopped = false;
	
	public MP3Player(File f) throws PlayerException {
		try {
			file = f;
			stream = new FileInputStream(file);
			player = new AdvancedPlayer(stream);
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
			stopped = false;
			firePlaybackStarted();
			player.play(from, to);
			if (stopped) {
				completed = false;
				firePlaybackStopped();
			} else {
				completed = true;
				firePlaybackCompleted();
			}
		} catch (Exception exc) {
			completed = false;
			firePlaybackStopped();
			throw new PlayerException(exc);
		} finally {
			if (player != null) {
				player.close();
				player = null;
			}
		}
	}
	
	public void pause() {
		System.err.println("Not yet implemented");
	}
	
	public synchronized void stop() {
		stopped = true;
		if (player != null) {
			player.close();
			player = null;
		}
	}
	
	public synchronized boolean isPlaying() {
		return player != null;
	}
	
	public synchronized boolean isCompleted() {
		return completed;
	}
}
