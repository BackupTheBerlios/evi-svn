/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.core;

import java.io.File;

/**
 * Provides some utilities.
 * @author Christoph Schwering (schwering@gmail.com)
 * @version $Id$
 */
public class Util {
	/**
	 * Indicates whether a given file seems to be an audio file (by extension).
	 * @param f The file.
	 * @return <code>true</code> if f is a *.mp3 file.
	 */
	public static boolean isAudioFile(File f) {
		return f.getName().toLowerCase().endsWith(".mp3");
	}
}
