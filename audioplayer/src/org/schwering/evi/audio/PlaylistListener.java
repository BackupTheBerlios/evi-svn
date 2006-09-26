package org.schwering.evi.audio;

/**
 * Listens to playlist actions.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public interface PlaylistListener {
	public void playbackStarted(Player player);
	public void playbackStopped(Player player);
	public void playbackCompleted(Player player);
}
