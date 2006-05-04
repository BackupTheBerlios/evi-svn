package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.schwering.evi.core.IPanel;

/**
 * A panel which contains a scrollable table with the environment information.
 * @author Christoph Schwering (schwering@gmail.com)
 */
public class EnvironmentPanel extends JPanel implements IPanel {
	private static final long serialVersionUID = -4204646793461931426L;

	/**
	 * Default title of the panel.
	 */
	public static final String DEFAULT_TITLE = "Environment";
	
	private MemoryPanel memoryPanel = new MemoryPanel();
	
	/**
	 * Draws a panel which contains a scrollable table with the environment 
	 * information.
	 */
	public EnvironmentPanel() {
		super(new BorderLayout());
		add(createMemoryPanel(), BorderLayout.CENTER);
		add(memoryPanel, BorderLayout.NORTH);
	}
	
	private JPanel createMemoryPanel() {
		Properties sysProps = System.getProperties();
		Enumeration sysKeys = sysProps.keys();
		int sysLen = sysProps.size();
		String[][] vars = new String[sysLen][2];
		
		String[] keys = new String[sysLen];
		for (int i = 0; i < sysLen && sysKeys.hasMoreElements(); i++) {
			keys[i] = (String)sysKeys.nextElement();
		}
		Arrays.sort(keys);
		
		for (int i = 0; i < sysLen; i++) {
			vars[i][0] = keys[i];
			vars[i][1] = sysProps.getProperty(keys[i]);
		}
		
		JTable table = new JTable(vars, new String[] { "Variable", "Value" }) {
			private static final long serialVersionUID = 2298937007677219773L;

			public boolean isCellEditable(int x, int y) {
				return false;
			}
		};
		
		JScrollPane scrollPane = new JScrollPane(table, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel panel = new JPanel(new GridLayout(1, 0));
		panel.add(scrollPane);
		return panel;
	}
	
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		memoryPanel.stop();
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getIcon()
	 */
	public Icon getIcon() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getPanelInstance()
	 */
	public Component getPanelInstance() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#getTitle()
	 */
	public String getTitle() {
		return DEFAULT_TITLE;
	}
	
	/**
	 * A JPanel that shows frequently updated memory information.
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
		private void updateLabels() {
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
		private void stop() {
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
}