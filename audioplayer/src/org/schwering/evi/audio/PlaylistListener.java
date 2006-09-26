package org.schwering.evi.audio;

/**
 * Listens to playlist actions.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public interface PlaylistListener {
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
}
