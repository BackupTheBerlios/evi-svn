/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

/**
 * Listens to playlist actions.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public interface IPlaylistListener {
	/**
	 * Fired when a song is started to be played.
	 * @param player The player that plays the song.
	 */
	public void playbackStarted(Player player);
	
	/**
	 * Fired when the process of playing a song is aborted.
	 * @param player The player that plays the song.
	 */
	public void playbackStopped(Player player);
	
	/**
	 * Fired when the process of playing a song is finished normally.
	 * @param player The player that plays the song.
	 */
	public void playbackCompleted(Player player);
	
	/**
	 * Fired when the process of playing failed unexpectedly.
	 * @param player The player that plays the song.
	 */
	public void playbackFailed(Player player);
	
}
