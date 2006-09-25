package org.schwering.evi.audio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import org.schwering.evi.util.ExceptionDialog;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;

/**
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
	
	public void next() {
		pause();
		playingIndex--;
		play();
	}
	
	public void prev() {
		pause();
		playingIndex++;
		play();
	}
	
	public void play() {
		try {
			int playListSize = playList.size();
			if (playListSize == 0) {
				return;
			}
			
			if (playingIndex == -1) {
				playingIndex = 0;
			}
			if (playingIndex >= playListSize) {
				playingIndex %= playListSize;
			}
			File file = (File)playList.get(playingIndex);
			stream = new FileInputStream(file);
			player = new Player(stream);
			player.play();
		} catch (Exception exc) {
			exc.printStackTrace();
			ExceptionDialog.show(exc);
		}
	}
	
	public void pause() {
		if (player != null) {
			player.close();
			try {
				stream.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
}
