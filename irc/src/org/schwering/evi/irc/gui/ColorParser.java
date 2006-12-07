/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.irc.gui;

import java.awt.Color;

import org.schwering.evi.irc.conf.DefaultValues;
import org.schwering.evi.irc.conf.Profile;
import org.schwering.evi.util.TextPane;
import org.schwering.irc.lib.IRCConstants;
import org.schwering.irc.lib.IRCUtil;

/**
 * mIRC color code parser. Deals with UNDERLINE, BOLD, COLOR_REVERSE,
 * COLOR and COLOR_END indicators.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ColorParser {
	/**
	 * Strips the color codes and appends the text.
	 * @param tp The destination textpane.
	 * @param text The text.
	 */
	public synchronized static void appendPlain(TextPane tp, String text) {
		text = IRCUtil.parseColors(text);
		int i = 0;
		int j = 0;
		while (j != text.length()) {
			i = j;
			j = text.indexOf(' ', i+1);
			if (j == -1)
				j = text.length();
			String word = text.substring(i, j);
			tp.append(word);
			if (isURL(word)) {
				try {
					tp.insertComponent(new URLPinButton(tp, word));
				} catch (Exception exc) {
				}
			}
		}
	}
	
	/**
	 * Parses the color codes and inserts the colored text into the textpane.
	 * @param tp The destination textpane.
	 * @param text The text.
	 * @param profile The profile which defines the color palette.
	 */
	public synchronized static void appendColored(TextPane tp, String text, 
			Profile profile) {
		ColorParser p = new ColorParser(text, profile);
		Element e;
		
		Color fg = null;
		Color bg = null;
		boolean bold = false;
		boolean underline = false;
		
		while ((e = p.parse()) != null) {
			int i = 0;
			int j = 0;
			while (j != e.text.length()) {
				i = j;
				j = e.text.indexOf(' ', i+1);
				if (j == -1)
					j = e.text.length();
				String word = e.text.substring(i, j);
				tp.append(word, fg, bg, bold, false, underline);
				if (isURL(word)) {
					try {
						tp.insertComponent(new URLPinButton(tp, word));
					} catch (Exception exc) {
					}
				}
			}
			switch (e.type) {
				case BOLD:
					bold = !bold;
					break;
				case UNDERLINE:
					underline = !underline;
					break;
				case COLOR_REVERSE:
					Color tmp = bg;
					bg = fg;
					fg = tmp;
					break;
				case COLOR:
					fg = e.fg;
					bg = e.bg;
					break;
				case COLOR_END:
					fg = null;
					bg = null;
					break;
			}
		}
	}
	
	/**
	 * Checks whether s seems to be a URL.
	 * It is a URL if it starts with "http://", "https://" or "www.".
	 * @param s The string that is to be checked.
	 * @return true of s seems to be a http-URL.
	 */
	private static boolean isURL(String s) {
		int i;
		
		if (s == null || s.length() <= 8)
			return false;
		for (i = 0; s.charAt(i) == ' ' && i < s.length(); i++)
			;
		return	(	s.charAt(i) == 'h'
					&& s.charAt(i+1) == 't'
					&& s.charAt(i+2) == 't'
					&& s.charAt(i+3) == 'p'
						&& ((s.charAt(i+4) == ':' 
							&& s.charAt(i+5) == '/' && s.charAt(i+6) == '/')
					|| (s.charAt(i+4) == 's' && s.charAt(i+5) == ':' 
						&& s.charAt(i+6) == '/' && s.charAt(i+7) == '/')))
				|| (s.charAt(i) == 'w'
					&& s.charAt(i+1) == 'w'
					&& s.charAt(i+2) == 'w'
					&& s.charAt(i+3) == '.');
	}
	
	public static final int END = -1;
	public static final int PLAIN = 0;
	public static final int BOLD = 1;
	public static final int UNDERLINE = 2;
	public static final int COLOR_REVERSE = 4;
	public static final int COLOR = 8;
	public static final int COLOR_END = 16;
	
	private String s;
	private int pos, npos;
	private int len;
	private Color[] palette;
	
	/**
	 * Initializes a parser.
	 * @param text The text that should be parsed.
	 * @param profile The profile. If <code>null</code>, the default color 
	 * palette is used.
	 */
	public ColorParser(String text, Profile profile) {
		s = text;
		pos = npos = 0;
		len = text.length();

		if (profile != null)
			palette = profile.getColorPalette();
		if (palette == null) 
			palette = DefaultValues.DEFAULT_PALETTE;
	}
	
	/**
	 * Returns an element object. This object contains the text until "now" and 
	 * the <i>next</i> style changes.<br />
	 * Parsing makes sense like this (extract from appendColored()):
	 * <pre>
	 * 	while ((e = p.parse()) != null) {
	 * 		// first print the text
	 * 		tp.append(e.text, fg, bg, bold, false, underline);
	 *		// then deal with the following style changes
	 * 		switch (e.type) {
	 * 			case BOLD:
	 * 				bold = !bold;
	 * 				break;
	 * 			case UNDERLINE:
	 * 				underline = !underline;
	 * 				break;
	 * 			case COLOR_REVERSE:
	 * 				Color tmp = bg;
	 * 				bg = fg;
	 * 				fg = tmp;
	 * 				break;
	 * 			case COLOR:
	 * 				fg = e.fg;
	 * 				bg = e.bg;
	 * 				break;
	 * 			case COLOR_END:
	 * 				fg = null;
	 * 				bg = null;
	 * 				break;
	 * 		}
	 * 	}
	 * </pre>
	 * @return An element object.
	 */
	public Element parse() {
		if (pos >= len) {
			return null;
		}
		
		Element e = new Element();
		
		loop: for (npos = pos; npos < len; npos++) {
			switch (s.charAt(npos)) {
				case IRCConstants.BOLD_INDICATOR:
					e.type = BOLD;
					break loop;
				case IRCConstants.UNDERLINE_INDICATOR:
					e.type = UNDERLINE;
					break loop;
				case IRCConstants.COLOR_REVERSE_INDICATOR:
					e.type = COLOR_REVERSE;
					break loop;
				case IRCConstants.COLOR_INDICATOR:
					e.type = COLOR;
					break loop;
				case IRCConstants.COLOR_END_INDICATOR:
					e.type = COLOR_END;
					break loop;
			}
		}
		
		e.text = s.substring(pos, npos);
		pos = npos+1;
		
		if (e.type == COLOR) {
			int fg = -1;
			int bg = -1;
			
			if (pos < len && '0' <= s.charAt(pos) && s.charAt(pos) <= '9') {
				fg = s.charAt(pos) - '0';
				pos++;
				if (pos < len && '0' <= s.charAt(pos) && s.charAt(pos) <= '9') {
					fg *= 10;
					fg += s.charAt(pos) - '0';
					pos++;
				}
			}
			
			if (pos < len && s.charAt(pos) == ',') {
				pos++;
				if (pos < len && '0' <= s.charAt(pos) && s.charAt(pos) <= '9') {
					bg = s.charAt(pos) - '0';
					pos++;
					if (pos < len && '0' <= s.charAt(pos) && s.charAt(pos) <= '9') {
						bg *= 10;
						bg += s.charAt(pos) - '0';
						pos++;
					}
				} else {
					pos--;
				}
			}
			
			fg %= palette.length;
			bg %= palette.length;
			
			if (fg >= 0)
				e.fg = palette[fg];
			if (bg >= 0)
				e.bg = palette[bg];
		}
		
		return e;
	}

	/**
	 * Data storage object, returned by <code>parse()</code>.<br />
	 * <b>Note:</b> The returned information (type and colors) refer to the 
	 * <i>next</i> element!
	 * @see ColorParser#parse()
	 */
	public class Element {
		int type = PLAIN;
		Color fg = null;
		Color bg = null;
		String text = null;
	}
}
