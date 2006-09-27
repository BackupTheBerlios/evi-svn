/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

import java.io.File;
import java.util.Vector;

/**
 * Basic player interface.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public abstract class Player {
	protected Vector listeners = new Vector();
	
	/**
	 * Returns the file that is played.
	 * @return The File object that points to the played audio file.
	 */
	public abstract File getFile();
	
	/**
	 * Plays the total song.
	 * @throws PlayerException If anything fails.
	 */
	public abstract void play() throws PlayerException;
	
	/**
	 * Plays parts of the song.
	 * @param from The first frame.
	 * @param to The last frame.
	 * @throws PlayerException If anything fails.
	 */
	public abstract void play(int from, int to) throws PlayerException;
	
	/**
	 * Pauses the playing.
	 */
	public abstract void pause();
	
	/**
	 * Stops the playing ("forever").
	 */
	public abstract void stop();
	
	/**
	 * Indicates whether the song is being played at the moment.
	 * @return <code>true</code> if the song is being played at the moment.
	 */
	public abstract boolean isPlaying();
	
	/**
	 * Indicates whether the song was completed normally. 
	 * For example if <code>stop()</code> was invoked before the song was 
	 * finished, this method returns <code>false</code>.
	 * @return <code>true</code> if the song was completed normally.
	 */
	public abstract boolean isCompleted();
	
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
	
	/**
	 * Adds a new PlayerListener.
	 * @param listener The new listener.
	 */
	public void addListener(PlayerListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a PlayerListener.
	 * @param listener The listener that's to be removed.
	 */
	public void removeListener(PlayerListener listener) {
		listeners.remove(listener);
	}
}
