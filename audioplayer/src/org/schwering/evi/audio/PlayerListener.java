package org.schwering.evi.audio;

/**
 * Listens to a specific player. You rather might be interested in the PlaylistListener.
 * @see PlaylistListener
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public interface PlayerListener {
	public void playbackStarted();
	public void playbackStopped();
	public void playbackCompleted();
}
