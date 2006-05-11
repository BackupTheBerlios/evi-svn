package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A JPanel that shows frequently updated memory information.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
class MemoryPanel extends JPanel {
	private static final long serialVersionUID = -3385670873454994811L;
	private JLabel memory = new JLabel();
	private JProgressBar bar = new JProgressBar();
	private Thread updateThread;
	private volatile boolean runUpdateThread;
	
	/**
	 * Creates a new JPanel with memory information.
	 */
	public MemoryPanel() {
		JButton collect = new JButton("Collect");
		collect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				updateLabels();
			}
		});
		
		bar.setStringPainted(true);
		
		add(memory, BorderLayout.EAST);
		add(bar, BorderLayout.CENTER);
		add(collect, BorderLayout.WEST);
		
		updateLabels();
		runUpdateThread = true;
		updateThread = new MemoryThread();
		updateThread.setDaemon(true);
		updateThread.start();
	}
	
	/**
	 * Updates the labels and the bar.
	 */
	private synchronized void updateLabels() {
		Runtime r = Runtime.getRuntime();
		long total = r.totalMemory();
		long used = total - r.freeMemory();
		double percent = 100.0 * (double)used / (double)total;
		percent = Math.round(10.0 * percent) / 10.0;
		int totalKb = (int)(total / 1024);
		int usedKb = (int)(used / 1024);
		bar.setMinimum(0);
		bar.setMaximum(totalKb);
		bar.setValue(usedKb);
		bar.setString(percent +"%");
		memory.setText(usedKb +" KB of "+ totalKb +" KB used  ");
	}
	
	/**
	 * Stops the thread.
	 */
	public void stop() {
		try {
			runUpdateThread = false;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * The thread that updates the memory field.
	 */
	class MemoryThread extends Thread {
		public void run() {
			while (runUpdateThread) {
				try {
					Thread.sleep(4000);
				} catch (Exception exc) {
					runUpdateThread = false;
					exc.printStackTrace();
				}
				updateLabels();
			}
		}
	}
}
