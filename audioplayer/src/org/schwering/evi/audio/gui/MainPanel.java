/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import org.schwering.evi.audio.AudioPlayer;
import org.schwering.evi.audio.conf.Configuration;
import org.schwering.evi.audio.core.DefaultPlaylist;
import org.schwering.evi.audio.core.IPlaylistListener;
import org.schwering.evi.audio.core.Player;
import org.schwering.evi.audio.core.Playlist;
import org.schwering.evi.audio.core.Util;
import org.schwering.evi.audio.lang.Messages;
import org.schwering.evi.util.RightClickMenu;

/**
 * The playlist panel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = -3610628136517888050L;
	
	private AudioPlayer owner;
	private Playlist playlist = new DefaultPlaylist();
	private List list = new List(playlist);
	private JLabel playingLabel = new JLabel();
	private JTextField searchField = new JTextField("");
	
	/**
	 * Creates the main panel.
	 * @param owner The owning AudioPlayer object.
	 */
	public MainPanel(final AudioPlayer owner) {
		super(new BorderLayout());
		this.owner = owner;
		
		playlist.setPlayAll(Configuration.isPlayAll());
		playlist.setRandom(Configuration.isRandom());
		
		playlist.addListener(new IPlaylistListener() {
			public void playbackStarted(Player player) {
				File file = player.getFile();
				updatePlayingLabel(file);
				updateAppletTooltip(file);
				ListElement comp = (ListElement)list.getListElement(file);
				if (comp != null) {
					comp.update(true);
				}
				list.scrollToPlayingFile();
			}
			public void playbackStopped(Player player) {
				playbackFailed(player);
			}
			public void playbackCompleted(Player player) {
				playbackFailed(player);
			}
			public void playbackFailed(Player player) {
				updatePlayingLabel(null);
				updateAppletTooltip(null);
				if (player != null) {
					File file = player.getFile();
					if (file != null) {
						ListElement comp = (ListElement)list.getListElement(file);
						if (comp != null) {
							comp.update(false);
						}
					}
				}
			}
		});
		
		
		
		JButton add = new JButton(Messages.getString("MainPanel.ADD_FILE")); //$NON-NLS-1$
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(Configuration.getLastFile());
				fileChooser.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						return f.isDirectory() || Util.isAudioFile(f);
					}
					public String getDescription() {
						return Messages.getString("MainPanel.DOT_MP3_FILES"); //$NON-NLS-1$
					}
				});
				int ret = fileChooser.showOpenDialog(owner.getPanelInstance());
				if (ret == JFileChooser.APPROVE_OPTION) {
					File[] f = fileChooser.getSelectedFiles();
					if (f != null && f.length > 0) {
						playlist.addElements(f);
						Configuration.setLastFile(f[0]);
					}
				}
			}
		});
		JButton addDir = new JButton(Messages.getString("MainPanel.ADD_DIR")); //$NON-NLS-1$
		addDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(Configuration.getLastDirectory());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						return f.isDirectory();
					}
					public String getDescription() {
						return Messages.getString("MainPanel.DIRECTORIES"); //$NON-NLS-1$
					}
				});
				int ret = fileChooser.showOpenDialog(owner.getPanelInstance());
				if (ret == JFileChooser.APPROVE_OPTION) {
					File[] f = fileChooser.getSelectedFiles();
					for (int i = 0; i < f.length; i++) {
						playlist.addDirectory(f[i]);
					}
					if (f.length > 0) {
						Configuration.setLastDirectory(f[0]);
					}
				}
			}
		});
		JButton del = new JButton(Messages.getString("MainPanel.DELETE_FILE")); //$NON-NLS-1$
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.removeSelected();
			}
		});
		JButton delAll = new JButton(Messages.getString("MainPanel.DELETE_ALL")); //$NON-NLS-1$
		delAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playlist.removeAll();
			}
		});
		
		JPanel p, sub;
		
		p = new JPanel(new GridLayout(2, 0));
		sub = new JPanel();
		sub.add(add);
		sub.add(addDir);
		sub.add(del);
		sub.add(delAll);
		p.add(sub);
		updatePlayingLabel(null);
		updateAppletTooltip(null);
		p.add(playingLabel);
		add(p, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane(list);
		add(scrollPane, BorderLayout.CENTER);
		
		p = new JPanel(new GridLayout(2, 0));
		sub = new JPanel(new BorderLayout());
		sub.add(new JLabel(Messages.getString("MainPanel.SEARCH") +":"), BorderLayout.WEST); //$NON-NLS-1$ //$NON-NLS-2$
		sub.add(searchField, BorderLayout.CENTER);
		searchField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}
			public void keyTyped(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				if (Configuration.isSearchDirectly()) {
					String query = searchField.getText();
					if (query == null) {
						query = "";
					}
					playlist.filter(query);
				}
			}
		});
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = searchField.getText();
				if (query == null) {
					query = "";
				}
				playlist.filter(query);
				playlist.play(0);
			}
		});
		JButton resetButton = new JButton(Messages.getString("MainPanel.RESETBUTTON")); //$NON-NLS-1$
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchField.setText("");
				playlist.filter("");
			}
		});
		RightClickMenu.addRightClickMenu(searchField);
		sub.add(resetButton, BorderLayout.EAST);
		p.add(sub);
		p.add(new ControlPanel(owner));
		add(p, BorderLayout.SOUTH);
	}
	
	/**
	 * Updates the label that displays the name of the current song.
	 * @param file The current song.
	 */
	private void updatePlayingLabel(File file) {
		String s;
		if (file != null) {
			s = file.getName();
		} else {
			s = Messages.getString("MainPanel.NOTHING"); //$NON-NLS-1
		}
		playingLabel.setText(Messages.getString("MainPanel.PLAYING") +": "+ s); //$NON-NLS-1
	}
	
	private void updateAppletTooltip(File file) {
		if (file != null) {
			ControlPanel p = (ControlPanel)owner.getAppletInstance();
			if (p != null) {
				p.setToolTipText(Messages.getString("MainPanel.PLAYING") +": "+ file.getName().toString()); //$NON-NLS-1
			}
		}
	}

	
	/**
	 * The playlist object.
	 * @return The current playlist.
	 */
	public Playlist getPlaylist() {
		return playlist;
	}

	/**
	 * Save the playlist.
	 */
	public void dispose() {
		playlist.setPlayAll(false);
		playlist.save();
		Configuration.store();
	}
}