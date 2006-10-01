/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;

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
	private Hashtable labeltable = new Hashtable();
	private JLabel playingLabel = new JLabel();
	private JList list;
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
		
		list = new JList(playlist);
		list.setCellRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				File file = (File)value;
				
				if (!labeltable.containsKey(file)) {
					ListComponent label = new ListComponent(file.toString());
					labeltable.put(file, label);
				}
				ListComponent comp = (ListComponent)labeltable.get(file);
				if (isSelected) {
					comp.setForeground(list.getSelectionForeground());
					comp.setBackground(list.getSelectionBackground());
				} else {
					comp.setForeground(list.getForeground());
					comp.setBackground(list.getBackground());
				}
				return comp;
			}
		});
		final JPopupMenu popup = new JPopupMenu();
		final JMenuItem playItem = new JMenuItem(Messages.getString("MainPanel.PLAY")); //$NON-NLS-1$
		playItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playSelected();
			}
		});
		final JMenuItem removeItem = new JMenuItem(Messages.getString("MainPanel.REMOVE")); //$NON-NLS-1$
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeSelected();
			}
		});
		final JMenuItem addToQueueItem = new JMenuItem(Messages.getString("MainPanel.ADD_TO_QUEUE")); // $NON-NLS-1$
		addToQueueItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indices = list.getSelectedIndices();
				for (int i = 0; i < indices.length; i++) {
					playlist.addToQueue(indices[i]);
				}
			}
		});
		final JMenuItem removeFromQueueItem = new JMenuItem(Messages.getString("MainPanel.REMOVE_FROM_QUEUE")); // $NON-NLS-1$
		removeFromQueueItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] indices = list.getSelectedIndices();
				for (int i = 0; i < indices.length; i++) {
					playlist.removeFromQueue(indices[i]);
				}
			}
		});
		popup.add(playItem);
		popup.add(removeItem);
		popup.add(addToQueueItem);
		popup.add(removeFromQueueItem);
		list.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					playSelected();
				}
			}
			
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1 
						&& e.getButton() == MouseEvent.BUTTON3) {
					int index = list.getSelectedIndex();
					boolean enable = index != -1;
					playItem.setEnabled(enable);
					removeItem.setEnabled(enable);
					addToQueueItem.setEnabled(enable && !playlist.isInQueue(index));
					removeFromQueueItem.setEnabled(enable && playlist.isInQueue(index));
					popup.show(list, e.getX(), e.getY());
				}
			}
			
			public void mouseReleased(MouseEvent e) {
			}
		});
		playlist.addListener(new IPlaylistListener() {
			public void playbackStarted(Player player) {
				File file = player.getFile();
				updatePlayingLabel(file);
				updateAppletTooltip(file);
				ListComponent comp = (ListComponent)labeltable.get(file);
				if (comp != null) {
					comp.update(true);
				}
				scrollToPlayingFile();
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
						ListComponent comp = (ListComponent)labeltable.get(file);
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
				removeSelected();
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
	 * Scrolls to the currently played file. In fact, it tries to scroll to 
	 * an area +/- 5 songs around the current file.<br />
	 * TODO: Sometimes it scroll to the middle. Java Bug?
	 */
	private void scrollToPlayingFile() {
		try {
			File file = playlist.getPlayingFile();
			if (playlist == null || file == null) {
				return;
			}
			int index = playlist.indexOf(file);
			if (index != -1) {
				int max = list.getLastVisibleIndex() - list.getFirstVisibleIndex();
				int index0 = index - max / 3;
				int index1 = index + max / 3;
				while (index0 < 0) {
					index0++;
				}
				while (index1 >= playlist.getSize()) {
					index1--;
				}
				
				Point p0 = indexToLocation(index0);
				Point p1 = indexToLocation(index1);
				
				int x = p0.x;
				int y = p0.y;
				int width = p1.x - x;
				int height = p1.y - y;
				Rectangle r = new Rectangle(x, y, width, height);
				list.scrollRectToVisible(r);
				list.repaint();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Wraps JList.indexToLocation. Because JList.indexToLocation seems to 
	 * be buggy, it tries up to 6 times to calculate the point. Each result 
	 * of JList.indexToLocation is compared with the result of 
	 * JList.locationToIndex.<br />
	 * <b>Note:</b> Swing has bugs. <code>JList.indexToLocation</code> might 
	 * do what it wants. Hence, encapsulate all calls of this method in 
	 * try/catch blocks.
	 * @param index The index of the line.
	 * @return The point.
	 */
	private Point indexToLocation(int index) {
		Point p = list.indexToLocation(index);
		for (int i = 0; i < 10 && list.locationToIndex(p) != index; i++) {
			p = list.indexToLocation(index);
		}
		return p;
	}
	
	/**
	 * Plays a single selected file.
	 */
	private void playSelected() {
		int index = list.getSelectedIndex();
		if (index != -1) {
			playlist.play(index);
		}
	}
	
	/**
	 * Removes all selected files from the list.
	 */
	private void removeSelected() {
		int[] arr = list.getSelectedIndices();
		if (arr != null) {
			for (int i = arr.length - 1; i >= 0; i--) {
				playlist.removeElementAt(arr[i]);
			}
		}
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
	
	/**
	 * A wrapper class for the components displayed in the JList.
	 * @author Christoph Schwering (schwering@gmail.com)
	 */
	class ListComponent extends JTextField {
		private static final long serialVersionUID = -6566828953377453228L;
		private boolean playing = false;

		/**
		 * Creates a new element component.
		 * It is based on a not-editable and border-less JTextField.
		 * @param s
		 */
		public ListComponent(String s) {
			super(s);
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
				super.setBackground(list.getForeground());
				super.setForeground(list.getBackground());
			} else {
				super.setBackground(list.getBackground());
				super.setForeground(list.getForeground());
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
}
