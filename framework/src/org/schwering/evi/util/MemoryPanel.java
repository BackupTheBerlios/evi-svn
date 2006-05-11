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
public class MemoryPanel extends JPanel {
	private static final long serialVersionUID = -3385670873454994811L;
	
	/**
	 * If enabled with bitwise or (<code>|</code>) in options, 
	 * a bar is shown.
	 */
	public static final int BAR = 1;
	
	/**
	 * If enabled with bitwise or (<code>|</code>) in options, 
	 * a label is shown.
	 */
	public static final int LABEL = 2;

	/**
	 * If enabled with bitwise or (<code>|</code>) in options, 
	 * a button is shown.
	 */
	public static final int BUTTON = 4; 
	
	private int options;
	private JLabel memory = new JLabel();
	private JProgressBar bar = new JProgressBar();
	private Thread updateThread;
	private volatile boolean runUpdateThread;
	
	/**
	 * Creates a new JPanel with memory information. The panel has a 
	 * label, a bar and a button to collect (options = BAR | LABEL | COLLECT).
	 */
	public MemoryPanel() {
		this(BAR | LABEL | BUTTON);
	}
	
	/**
	 * Creates a new JPanel with memory information.
	 * @param options The options, for example BAR | LABEL | BUTTON to 
	 * display the bar, the label and the "collect"-button.
	 */
	public MemoryPanel(int options) {
		this.options = options;
		JButton collect = new JButton("Collect");
		collect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				updateLabels();
			}
		});
		
		bar.setStringPainted(true);
		
		if ((options & LABEL) != 0) {
			add(memory, BorderLayout.EAST);
		}
		if ((options & BAR) != 0) {
			add(bar, BorderLayout.CENTER);
		}
		if ((options & BUTTON) != 0) {
			add(collect, BorderLayout.WEST);
		}
		
		runUpdateThread = true;
		updateThread = new MemoryThread();
		updateThread.setDaemon(true);
		updateThread.start();
	}
	
	/**
	 * Updates the labels and the bar.
	 */
	private void updateLabels() {
		Runtime r = Runtime.getRuntime();
		long total = r.totalMemory();
		long used = total - r.freeMemory();
		double percent = 100.0 * (double)used / (double)total;
		percent = Math.round(10.0 * percent) / 10.0;
		int totalKb = (int)(total / 1024);
		int usedKb = (int)(used / 1024);
		
		if ((options & BAR) != 0) {
			bar.setMinimum(0);
			bar.setMaximum(totalKb);
			bar.setValue(usedKb);
			bar.setString(percent +"%");
		}
		if ((options & LABEL) != 0) {
			memory.setText(usedKb +" KB of "+ totalKb +" KB used  ");
		}
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
				updateLabels();
				try {
					Thread.sleep(4000);
				} catch (Exception exc) {
					runUpdateThread = false;
					exc.printStackTrace();
				}
			}
		}
	}
}
