package org.schwering.evi.audio;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
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
		
		prev.setBorderPainted(false);
		prev.setFocusPainted(false);
		play.setBorderPainted(false);
		play.setFocusPainted(false);
		next.setBorderPainted(false);
		next.setFocusPainted(false);
		
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.prev();
			}
		});
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				if (mainPanel.isPlaying()) {
					mainPanel.pause();
				} else {
					mainPanel.play();
				}
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.next();
			}
		});
		add(prev);
		add(play);
		add(next);
	}
}
