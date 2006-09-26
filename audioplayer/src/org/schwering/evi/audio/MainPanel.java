package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;
import javax.swing.DefaultListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

/**
 * The playlist panel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class MainPanel extends JPanel {
	private Playlist playlist = new DefaultPlaylist();
	private Hashtable labeltable = new Hashtable();
	
	public MainPanel(final AudioPlayer owner) {
		super(new BorderLayout());
		
		final JList list = new JList(playlist.getListModel());
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
				ListComponent comp = (ListComponent)labeltable.get(file);
				comp.update(file, true);
			}
			public void playbackStopped(Player player) {
				File file = player.getFile();
				ListComponent comp = (ListComponent)labeltable.get(file);
				comp.update(file, false);
			}
			public void playbackCompleted(Player player) {
				File file = player.getFile();
				ListComponent comp = (ListComponent)labeltable.get(file);
				comp.update(file, false);
			}
		});
		
		
		
		JButton add = new JButton(Messages.getString("MainPanel.ADD_FILE")); //$NON-NLS-1$
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
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
					File f = fileChooser.getSelectedFile();
					playlist.add(f);
				}
			}
		});
		JButton addDir = new JButton(Messages.getString("MainPanel.ADD_DIR")); //$NON-NLS-1$
		addDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
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
					File f = fileChooser.getSelectedFile();
					playlist.addDirectory(f);
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
		JPanel p = new JPanel();
		p.add(add);
		p.add(addDir);
		p.add(del);
		add(p, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane(list);
		add(scrollPane, BorderLayout.CENTER);
		
		ControlPanel ctrlPanel = new ControlPanel(owner);
		add(ctrlPanel, BorderLayout.SOUTH);
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
	}
	
	class ListComponent extends JLabel {
		public ListComponent(String s) {
			super(s);
		}
		
		public void update(File file, boolean playing) {
			String s = file.toString() + ((playing) ? Messages.getString("MainPanel.PLAYING") : ""); //$NON-NLS-1$ //$NON-NLS-2$
			setText(s);
			
			DefaultListModel listModel = playlist.getListModel();
			int index = listModel.indexOf(file);
			ListDataListener[] listeners = listModel.getListDataListeners();
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index, index);
			for (int i = 0; i < listeners.length; i++) {
				listeners[i].contentsChanged(e);
			}
		}
	}
}
