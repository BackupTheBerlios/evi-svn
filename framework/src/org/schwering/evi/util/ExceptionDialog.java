package org.schwering.evi.util;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.conf.ModuleAutoStartConfiguration;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.core.ModuleLoader;

/**
 * Displays a frame with information about a thrown exception.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 * @version $Id$
 */
public class ExceptionDialog extends JFrame {
	private static final long serialVersionUID = -5456180238115727093L;

	/**
	 * Default width of the frame  (550px).
	 */
	public static int DEFAULT_WIDTH = 550;
	
	/**
	 * Default height of the frame  (550px).
	 */
	public static int DEFAULT_HEIGHT = 550;
	
	/**
	 * Creates and displays a new exception dialog.
	 * @param exc The exception.
	 */
	public static void show(Exception exc) {
		new ExceptionDialog(exc);
	}
	
	/**
	 * Creates and displays a new exception dialog.
	 * @param msg The short description or message.
	 * @param exc The exception.
	 */
	public static void show(String msg, Throwable exc) {
		new ExceptionDialog(msg, exc);
	}
	
	/**
	 * Creates and displays a new exception dialog.
	 * @param exc The exception.
	 */
	private ExceptionDialog(Throwable exc) {
		this("An error occured", exc);
	}
	
	/**
	 * Creates and displays a new exception dialog.
	 * @param msg The short description or message.
	 * @param exc The exception.
	 */
	private ExceptionDialog(String msg, Throwable exc) {
		super("Error");
		
		JPanel exceptionPanel = getExceptionPanel(msg, exc);
		JPanel environmentPanel = new EnvironmentPanel();
		JPanel configurationPanel = getConfigurationPanel();
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Exception", exceptionPanel);
		tabs.add("Environment", environmentPanel);
		tabs.add("Configuration", configurationPanel);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Util.askToExit();
			}
		});
		JButton proceed = new JButton("Proceed");
		proceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel buttons = new JPanel();
		buttons.add(exit);
		buttons.add(proceed);

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());
		main.setBorder(new TitledBorder("An error occured"));
		main.add(tabs, BorderLayout.CENTER);
		main.add(buttons, BorderLayout.SOUTH);
		
		getContentPane().add(main);
	    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Error");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Util.centerComponent(this);
		setVisible(true);
	}
	
	/**
	 * Creates a panel with a textfield which itself contains a detailed 
	 * description of the exception.
	 * @param msg The short message.
	 * @param exc The thrown exception.
	 * @return A panel with information about the exception.
	 */
	private JPanel getExceptionPanel(String msg, Throwable exc) {
		JTextArea label = new JTextArea(msg);
		label.setEditable(false);
		label.setBackground(getBackground());
		label.setCaretPosition(0);
		Font defaultFont = new JLabel().getFont();
		Font f = new Font(defaultFont.getName(), Font.BOLD, 
				defaultFont.getSize());
		label.setFont(f);
		label.setBorder(null);
		JScrollPane scrollPaneLabel = new JScrollPane(label);
		scrollPaneLabel.setBorder(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setText(Util.exceptionToString(exc));
		textArea.setAutoscrolls(true);
		textArea.setEditable(false);
		textArea.setCaretPosition(0);
		RightClickMenu.addRightClickMenu(textArea);
		JScrollPane scrollPaneException = new JScrollPane(textArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		JPanel p = new JPanel(new GridLayout(1, 0));
		p.setLayout(new BorderLayout());
		p.add(scrollPaneLabel, BorderLayout.NORTH);
		p.add(scrollPaneException, BorderLayout.CENTER);
		return p;
	}
	
	/**
	 * Draws the configuration variables into a scrollable table.
	 * @return A panel which contains a scrollable table with the configuration.
	 */
	private JPanel getConfigurationPanel() {
		String[] confKeys = MainConfiguration.getKeys();
		Arrays.sort(confKeys);
		ModuleContainer[] modules = ModuleLoader.getLoadedModules(); 
		String[] autoloadIds = ModuleAutoStartConfiguration.getIds();
		String[] autoloadArgs = ModuleAutoStartConfiguration.getArgs();
		String[][] vars = new String[confKeys.length + modules.length 
		                             + autoloadIds.length][2];
		
		for (int i = 0; i < confKeys.length; i++) {
			String key = confKeys[i];
			vars[i][0] = key;
			vars[i][1] = MainConfiguration.getString(key);
		}
		
		int j = confKeys.length;
		for (int i = 0; i < modules.length; i++) {
			vars[j+i][0] = "Module #"+ (i+1);
			vars[j+i][1] = modules[i].toString();
		}
		
		j = confKeys.length + modules.length;
		for (int i = 0; i < autoloadIds.length; i++) {
			vars[j+i][0] = "Autoload #"+ (i+1);
			vars[j+i][1] = autoloadIds[i] +" "+ autoloadArgs[i];
		}
		
		JTable table = new JTable(vars, new String[] { "Variable", "Value" }) {
			private static final long serialVersionUID = -2586950109604656442L;

			public boolean isCellEditable(int x, int y) {
				return false;
			}
		};

		JScrollPane scrollPane = new JScrollPane(table, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel p = new JPanel(new GridLayout(1, 0));
		p.add(scrollPane);
		return p;
	}
}