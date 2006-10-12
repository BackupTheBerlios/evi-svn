/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import org.schwering.evi.util.ExceptionDialog;
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
	private List list = new List(this, playlist);
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
		playlist.setShuffle(Configuration.isShuffle());
		
		playlist.addListener(new IPlaylistListener() {
			public void playbackStarted(Player player) {
				URL url = player.getResource();
				updatePlayingLabel(url);
				updateAppletTooltip(url);
				ListItem comp = (ListItem)list.getListElement(url);
				if (comp != null) {
					comp.update(true);
				}
				list.scrollToPlayingURL();
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
					URL url = player.getResource();
					if (url != null) {
						ListItem comp = (ListItem)list.getListElement(url);
						if (comp != null) {
							comp.update(false);
						}
					}
				}
			}
		});
		
		
		
		JButton addFile = new JButton(Messages.getString("MainPanel.ADD_FILE")); //$NON-NLS-1$
		addFile.addActionListener(new ActionListener() {
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
		JButton addURL = new JButton(Messages.getString("MainPanel.ADD_URL")); //$NON-NLS-1$
		addURL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = JOptionPane.showInputDialog(owner.getPanelInstance(), Messages.getString("MainPanel.INPUT_URL"));
				if (s != null) {
					try {
						URL url = new URL(s);
						playlist.addElement(url);
					} catch (Exception exc) {
						ExceptionDialog.show(exc);
					}
				}
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
		sub.add(addFile);
		sub.add(addDir);
		sub.add(addURL);
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
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (playlist.getSize() > 0) {
						list.requestFocus();
						list.setSelectedIndex(0);
					}
				}
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
	 * @param url The current song.
	 */
	private void updatePlayingLabel(URL url) {
		String s;
		if (url != null) {
			s = url.getFile();
		} else {
			s = Messages.getString("MainPanel.NOTHING"); //$NON-NLS-1
		}
		playingLabel.setText(Messages.getString("MainPanel.PLAYING") +": "+ s); //$NON-NLS-1
	}
	
	private void updateAppletTooltip(URL url) {
		if (url != null) {
			ControlPanel p = (ControlPanel)owner.getAppletInstance();
			if (p != null) {
				p.setToolTipText(Messages.getString("MainPanel.PLAYING") +": "+ url.getFile()); //$NON-NLS-1
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
		System.out.println("Stopping now");
		playlist.stop();
		System.out.println("Done");
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#requestFocus()
	 */
	public void requestFocus() {
		requestFocusInWindow();
		searchField.requestFocusInWindow();
	}
}
