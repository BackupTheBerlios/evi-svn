package org.schwering.evi.audio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * A very simple panel with control buttons.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ControlPanel extends JPanel {
	public static final int PREV = 1;
	public static final int PLAY = 2;
	public static final int NEXT = 4;
	public static final int RANDOM = 8;
	
	private JButton prev;
	private JButton play;
	private JButton next;
	private JToggleButton random;
	
	/**
	 * Creates a new control panel.
	 * @param owner The owning AudioPlayer object which is controlled by this 
	 * panel.
	 */
	public ControlPanel(final AudioPlayer owner) {
		this(owner, PREV | PLAY | NEXT | RANDOM);
	}
	
	/**
	 * Creates a new control panel.
	 * @param owner The owning AudioPlayer object which is controlled by this 
	 * panel.
	 */
	public ControlPanel(final AudioPlayer owner, int buttons) {
		super();
		
		int buttonCount = 0;
		if ((buttons & PREV) != 0) buttonCount++;
		if ((buttons & PLAY) != 0) buttonCount++;
		if ((buttons & NEXT) != 0) buttonCount++;
		if ((buttons & RANDOM) != 0) buttonCount++;
		
		setLayout(new GridLayout(0, buttonCount));

		if ((buttons & PREV) != 0) {
			prev = new JButton("Ç");
			prev.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					mainPanel.getPlaylist().playPrevious();
				}
			});
			add(prev);
		}
		
		if ((buttons & PLAY) != 0) {
			play = new JButton(">");
			play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					if (mainPanel.getPlaylist().isPlaying()) {
						mainPanel.getPlaylist().stop();
					} else {
						mainPanel.getPlaylist().play();
					}
				}
			});
			add(play);
		}
			
		if ((buttons & NEXT) != 0) {
			next = new JButton("È");
			next.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					if (mainPanel.getPlaylist().isRandom()) {
						mainPanel.getPlaylist().playRandom();
					} else {
						mainPanel.getPlaylist().playNext();
					}
				}
			});
			add(next);
		}
			
		if ((buttons & RANDOM) != 0) {
			random = new JToggleButton("R", false);
			random.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
					mainPanel.getPlaylist().setRandom(random.isSelected());
				}
			});
			add(random);
		}
	}
	
	public void setBordersPainted(boolean b) {
		if (prev != null) {
			prev.setBorderPainted(b);
		}
		if (play != null) {
			play.setBorderPainted(b);
		}
		if (next != null) {
			next.setBorderPainted(b);
		}
		if (random != null) {
			random.setBorderPainted(b);
		}
	}
}
