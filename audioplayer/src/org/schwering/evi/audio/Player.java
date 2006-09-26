package org.schwering.evi.audio;

import java.io.File;
import java.util.Vector;

/**
 * Basic player interface.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public abstract class Player {
	protected Vector listeners = new Vector();
	
	public abstract File getFile();
	public abstract void play() throws PlayerException;
	public abstract void play(int from, int to) throws PlayerException;
	public abstract void pause();
	public abstract void stop();
	public abstract boolean isPlaying();
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
	
	public void addListener(PlayerListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(PlayerListener listener) {
		listeners.remove(listener);
	}
}
