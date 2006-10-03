package org.schwering.evi.audio.gui;

import java.awt.Color;
import javax.swing.JTextField;

/**
 * A wrapper class for the components displayed in the JList.
 * @author Christoph Schwering (schwering@gmail.com)
 */
class ListElement extends JTextField {
	private static final long serialVersionUID = -6566828953377453228L;
	
	private ListComponent owner;
	private boolean playing = false;

	/**
	 * Creates a new element component.
	 * It is based on a not-editable and border-less JTextField.
	 * @param s
	 */
	public ListElement(ListComponent owner, String s) {
		super(s);
		this.owner = owner;
		setEditable(false);
		setBorder(null);
		update(false);
	}
	
	/**
	 * Updates the colors depending on whether the file represented by this 
	 * component is currently being played or not. If 
	 * <code>playing = true</code>, the owning <code>JList</code>s 
	 * fore- and background are taken and applied permuted on this 
	 * component.<br />
	 * <b>Note:</b> This looks not so good under J2SE 1.4 with Windows LaF.
	 * Seems to be a bug in J2SE 1.4's <code>JList</code>, I guess it 
	 * handles its fore-/background and selection-fore-/background colors 
	 * wrong.
	 * @param playing Indicates whether the component should be highlighted
	 * or not.
	 */
	public void update(boolean playing) {
		this.playing = playing;
		if (playing) {
			super.setBackground(owner.getForeground());
			super.setForeground(owner.getBackground());
		} else {
			super.setBackground(owner.getBackground());
			super.setForeground(owner.getForeground());
		}
	}
	
	/**
	 * Invokes <code>super.setForeground</code> only if the song represented
	 * by this component is not playing currently. This is done to preserve 
	 * the currently-playing-colors set by <code>update</code>.
	 */
	public void setForeground(Color c) {
		if (!playing) {
			super.setForeground(c);
		}
	}
	
	/**
	 * Invokes <code>super.setForeground</code> only if the song represented
	 * by this component is not playing currently. This is done to preserve 
	 * the currently-playing-colors set by <code>update</code>.
	 */
	public void setBackground(Color c) {
		if (!playing) {
			super.setBackground(c);
		}
	}
}
