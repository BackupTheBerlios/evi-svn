package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;
import javax.swing.DefaultListModel;

/**
 * The playlist panel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MainPanel extends JPanel {
	private Playlist playlist = new DefaultPlaylist();
	private Hashtable labeltable = new Hashtable();
	private JLabel playingLabel = new JLabel();
	private JList list;
	private JTextField searchField = new JTextField("");
	
	public MainPanel(final AudioPlayer owner) {
		super(new BorderLayout());
		
		playlist.setPlayAll(Configuration.isPlayAll());
		playlist.setRandom(Configuration.isRandom());
		
		list = new JList(playlist.getListModel());
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
					comp.setForeground(Color.BLUE);
				} else {
					comp.setForeground(list.getForeground());
				}
				return comp;
			}
		});
		list.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					int index = list.getSelectedIndex();
					if (index != -1) {
						playlist.play(index);
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseExited(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
			}
			
			public void mouseReleased(MouseEvent e) {
			}
		});
		playlist.addListener(new PlaylistListener() {
			public void playbackStarted(Player player) {
				File file = player.getFile();
				updatePlayingLabel(file);
				ListComponent comp = (ListComponent)labeltable.get(file);
				if (comp != null) {
					comp.update(file, true);
				}
			}
			public void playbackStopped(Player player) {
				File file = player.getFile();
				updatePlayingLabel(null);
				ListComponent comp = (ListComponent)labeltable.get(file);
				if (comp != null) {
					comp.update(file, false);
				}
			}
			public void playbackCompleted(Player player) {
				File file = player.getFile();
				updatePlayingLabel(null);
				ListComponent comp = (ListComponent)labeltable.get(file);
				if (comp != null) {
					comp.update(file, false);
				}
			}
		});
		
		
		
		JButton add = new JButton(Messages.getString("MainPanel.ADD_FILE")); //$NON-NLS-1$
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setSelectedFile(Configuration.getLastFile());
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
					for (int i = 0; i < f.length; i++) {
						playlist.add(f[i]);
					}
					if (f.length > 0) {
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
				fileChooser.setSelectedFile(Configuration.getLastDirectory());
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
				int[] arr = list.getSelectedIndices();
				if (arr != null) {
					for (int i = arr.length - 1; i >= 0; i--) {
						playlist.remove(arr[i]);
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
		sub.add(add);
		sub.add(addDir);
		sub.add(del);
		sub.add(delAll);
		p.add(sub);
		updatePlayingLabel(null);
		p.add(playingLabel);
		add(p, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane(list);
		add(scrollPane, BorderLayout.CENTER);
		
		p = new JPanel(new GridLayout(2, 0));
		sub = new JPanel(new BorderLayout());
		sub.add(new JLabel(Messages.getString("MainPanel.SEARCH") +":"), BorderLayout.WEST); //$NON-NLS-1$ //$NON-NLS-2$
		sub.add(searchField, BorderLayout.CENTER);
		JButton searchButton = new JButton(Messages.getString("MainPanel.SEARCHBUTTON")); //$NON-NLS-1$
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playlist.fireListModelEvent(0, playlist.size() - 1);
			}
		});
		sub.add(searchButton, BorderLayout.EAST);
		p.add(sub);
		p.add(new ControlPanel(owner));
		add(p, BorderLayout.SOUTH);
	}
	
	public void updatePlayingLabel(File file) {
		String s;
		if (file != null) {
			s = file.getName();
		} else {
			s = Messages.getString("MainPanel.NOTHING"); //$NON-NLS-1
		}
		playingLabel.setText(Messages.getString("MainPanel.PLAYING") +": "+ s); //$NON-NLS-1
	}
	
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
	
	class ListComponent extends JTextField {
		public ListComponent(String s) {
			super(s);
			setEditable(false);
			setColors(false);
			setBorder(null);
		}
		
		private void setColors(boolean playing) {
			if (playing) {
				setBackground(list.getBackground().darker());
			} else {
				setBackground(list.getBackground());
			}
		}
		
		public void update(File file, boolean playing) {
			/*String s = file.toString();
			if (playing) {
				s = "*** "+ s + Messages.getString("MainPanel.PLAYING"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			setText(s);*/
			setColors(playing);
			DefaultListModel listModel = playlist.getListModel();
			int index = listModel.indexOf(file);
			playlist.fireListModelEvent(index, index);
		}
	}
}
