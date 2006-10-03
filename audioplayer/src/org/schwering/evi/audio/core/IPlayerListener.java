/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

/**
 * Listens to a specific player. You rather might be interested in the IPlaylistListener.
 * @see IPlaylistListener
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IPlayerListener {
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
	
	/**
	 * Fired when the process of playing failed unexpectedly.
	 */
	public void playbackFailed();
}
