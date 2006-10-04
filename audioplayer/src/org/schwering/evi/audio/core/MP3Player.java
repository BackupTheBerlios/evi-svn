/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

import java.io.FileInputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * Implements an MP3 player using the JLayer API.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MP3Player extends Player {
	protected AdvancedPlayer player;
	protected boolean completed = false;
	protected boolean stopped = false;
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.audio.Player#play()
	 */
	public void play() throws PlayerException {
		play(0, Integer.MAX_VALUE);
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.audio.Player#play(int, int)
	 */
	public void play(int from, int to) throws PlayerException {
		completed = false;
		stopped = false;
		if (file == null) {
			throw new PlayerException("NPE: file == null. Invoke Player.setFile.");
		}
		try {
			player = new AdvancedPlayer(new FileInputStream(file));
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
			firePlaybackFailed();
			throw new PlayerException(exc);
		} finally {
			stop();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.audio.Player#pause()
	 */
	public synchronized void pause() {
		System.err.println("Not yet implemented");
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.audio.Player#stop()
	 */
	public synchronized void stop() {
		stopped = true;
		if (player != null) {
			player.close();
			player = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.audio.Player#isPlaying()
	 */
	public boolean isPlaying() {
		return player != null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.audio.Player#isCompleted()
	 */
	public boolean isCompleted() {
		return completed;
	}
}
