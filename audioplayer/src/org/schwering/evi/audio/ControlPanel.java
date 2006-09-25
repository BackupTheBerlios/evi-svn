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
	private JButton pause = new JButton("||");
	private JButton play = new JButton(">");
	private JButton next = new JButton(">>");
	
	public ControlPanel(final AudioPlayer owner) {
		super(new GridLayout(0, 4));
		this.owner = owner;
		
		prev.setBorderPainted(false);
		pause.setBorderPainted(false);
		play.setBorderPainted(false);
		next.setBorderPainted(false);
		
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.prev();
			}
		});
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.pause();
			}
		});
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.play();
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel mainPanel = (MainPanel)owner.getPanelInstance();
				mainPanel.next();
			}
		});
		add(prev);
		add(pause);
		add(play);
		add(next);
	}
}
