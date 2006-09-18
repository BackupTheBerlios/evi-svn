package org.schwering.evi.irc.conf;

import java.awt.Color;

public class DefaultValues {
	public static final int DEFAULT_PORT = 6667;
	public static final int DEFAULT_SSL_PORT = 443;

	public static final Color[] DEFAULT_PALETTE = new Color[] {
		Color.white, Color.black, Color.blue, Color.green, Color.red, 
		new Color(165, 42, 42), new Color(128, 0, 128), Color.orange, 
		Color.yellow, new Color(144, 238, 144), new Color(0, 128, 128), 
		new Color(0, 255, 255), new Color(65, 105, 255), Color.pink, Color.gray, 
		Color.lightGray
	};
	public static final int PALETTE_SIZE = DEFAULT_PALETTE.length;
}
