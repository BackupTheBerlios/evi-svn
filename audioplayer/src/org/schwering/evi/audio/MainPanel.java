package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;

/**
 * The playlist panel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class MainPanel extends JPanel {
	private AudioPlayer owner;
	private Playlist playlist = new DefaultPlaylist();
	
	public MainPanel(final AudioPlayer owner) {
		super(new BorderLayout());
		this.owner = owner;
		
		final JList list = new JList(playlist.getListModel());
		list.setCellRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				File file = (File)value;
				return new JLabel(file.toString());
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
		
		
		
		JButton add = new JButton("Add File");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						return f.isDirectory() 
							|| f.toString().toLowerCase().endsWith(".mp3");
					}
					public String getDescription() {
						return "*.mp3 files";
					}
				});
				int ret = fileChooser.showOpenDialog(owner.getPanelInstance());
				if (ret == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					playlist.add(f);
				}
			}
		});
		JButton del = new JButton("Delete File");
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] arr = list.getSelectedValues();
				if (arr != null) {
					for (int i = 0; i < arr.length; i++) {
						playlist.remove((File)arr[i]);
					}
				}
			}
		});
		JPanel p = new JPanel();
		p.add(add);
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
		playlist.save();
	}
}
