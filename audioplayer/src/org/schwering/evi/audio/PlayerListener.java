package org.schwering.evi.audio;

/**
 * Listens to a specific player. You rather might be interested in the PlaylistListener.
 * @see PlaylistListener
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface PlayerListener {
	/**
	 * Fired when a song is started to be played.
	 */
	public void playbackStarted();
	
	/**
	 * Fired when the process of playing a song is aborted.
	 */
	public void playbackStopped();
	
	/**
	 * Fired when the process of playing a song is finished normally.
	 */
	public void playbackCompleted();
}
