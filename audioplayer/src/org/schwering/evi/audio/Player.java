package org.schwering.evi.audio;

import java.io.File;

/**
 * Basic player interface.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public interface Player {
	public File getFile();
	public void play() throws PlayerException;
	public void play(int from, int to) throws PlayerException;
	public void pause();
	public void stop();
	public boolean isPlaying();
	public boolean isCompleted();
	public void addListener(PlayerListener listener);
	public void removeListener(PlayerListener listener);
}
