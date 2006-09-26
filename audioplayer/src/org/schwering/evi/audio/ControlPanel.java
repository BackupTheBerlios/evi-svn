package org.schwering.evi.audio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A very simple panel with control buttons.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ControlPanel extends JPanel {
	private AudioPlayer owner;
	
	private JButton prev = new JButton("<<");
	private JButton play = new JButton(">");
	private JButton next = new JButton(">>");
	
	public ControlPanel(final AudioPlayer owner) {
		super(new GridLayout(0, 3));
		this.owner = owner;

		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.getPlaylist().playPrevious();
			}
		});
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
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.getPlaylist().playNext();
			}
		});
		add(prev);
		add(play);
		add(next);
	}
}
