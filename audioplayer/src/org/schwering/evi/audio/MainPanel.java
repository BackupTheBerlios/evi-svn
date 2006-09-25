package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileFilter;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.util.ExceptionDialog;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;

/**
 * The playlist panel.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class MainPanel extends JPanel {
	private AudioPlayer owner;
	private int playingIndex = -1;
	private DefaultListModel playList;
	private AudioDevice dev;
	private Player player;
	private InputStream stream;
	
	public MainPanel(final AudioPlayer owner) {
		super(new BorderLayout());
		this.owner = owner;
		
		try {
			dev = FactoryRegistry.systemRegistry().createAudioDevice();
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
		
		playList = new DefaultListModel();
		final JList list = new JList(playList);
		list.setCellRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, 
					int index, boolean isSelected, boolean cellHasFocus) {
				return new JLabel(value.toString());
			}
		});
		
		try {
			Properties props = new Properties(ModuleContainer.getIdByClass(AudioPlayer.class));
			props.load();
			String s;
			for (int i = 0; (s = props.getString("entry"+i, null)) != null; i++) {
				try {
					add(new File(s));
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		
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
					playList.addElement(f);
				}
			}
		});
		JButton del = new JButton("Delete File");
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] arr = list.getSelectedValues();
				if (arr != null) {
					for (int i = 0; i < arr.length; i++) {
						playList.removeElement(arr[i]);
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
	
	public synchronized void next() {
		pause();
		playingIndex++;
		play();
	}
	
	public synchronized void prev() {
		pause();
		playingIndex--;
		play();
	}
	
	public synchronized boolean isPlaying() {
		return player != null;
	}
	
	public synchronized void play() {
		try {
			int playListSize = playList.size();
			if (playListSize == 0) {
				return;
			}
			
			if (playingIndex < 0) {
				playingIndex = 0;
			}
			if (playingIndex >= playListSize) {
				playingIndex %= playListSize;
			}
			File file = (File)playList.get(playingIndex);
			stream = new FileInputStream(file);
			player = new Player(stream);
			Thread t = new Thread() {
				public void run() {
					try {
						player.play();
						if (player.isComplete()) {
							next();
						}
					} catch (Exception exc) {
						exc.printStackTrace();
						ExceptionDialog.show(exc);
					}
				}
			};
			t.setDaemon(true);
			t.start();
		} catch (Exception exc) {
			exc.printStackTrace();
			ExceptionDialog.show(exc);
		}
	}
	
	public synchronized void pause() {
		if (player != null) {
			player.close();
			stream = null;
			player = null;
		}
	}
	
	/**
	 * Save the playlist.
	 */
	public void dispose() {
		try {
			Properties props = new Properties(ModuleContainer.getIdByClass(AudioPlayer.class));
			File[] files = getList();
			for (int i = 0; i < files.length; i++) {
				props.setString("entry"+ i, files[i].toString());
			}
			props.store();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/** 
	 * Adds a file to the playlist.
	 * @param f The new file.
	 */
	public void add(File f) {
		playList.addElement(f);
	}
	
	/**
	 * Returns the files in the playlist.
	 * @return An array of java.io.File objects.
	 */
	public File[] getList() {
		int size = playList.size();
		File[] arr = new File[size];
		for (int i = 0; i < size; i++) {
			arr[i] = (File)playList.get(i);
		}
		return arr;
	}
}
