/* Copyright (C) 2006 Christoph Schwering */
package org.schwering.evi.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.util.ColorSelector;
import org.schwering.evi.util.ExceptionDialog;

/**
 * The small configuration for the console module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ConsoleConfiguration extends JPanel implements IPanel {
	private static final long serialVersionUID = -9125520462899226085L;

	public static Properties PROPS;
	
	static {
		try {
			PROPS = new Properties(ModuleContainer.getIdByClass(Console.class));
			PROPS.setShutdownHook(true);
			PROPS.load();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	private static ConsoleConfiguration instance;
	private static int instanceCount = 0;
	
	public static ConsoleConfiguration getInstance() {
		if (instance == null) {
			instance = new ConsoleConfiguration();
		}
		instanceCount++;
		return instance;
	}

	
	private ColorSelector outCol = new ColorSelector();
	private ColorSelector errCol = new ColorSelector();
	
	private ConsoleConfiguration() {
		super(new BorderLayout());
		
		String outTitle = Messages.getString("ConsoleConfiguration.COLOR_OUT_TITLE"); //$NON-NLS-1$
		String errTitle = Messages.getString("ConsoleConfiguration.COLOR_ERR_TITLE"); //$NON-NLS-1$
		
		outCol.setTitle(outTitle);
		errCol.setTitle(errTitle);
		
		outCol.setColor(getOutColor());
		errCol.setColor(getErrColor());
		
		JButton saveButton = new JButton(Messages.getString("ConsoleConfiguration.SAVE")); //$NON-NLS-1$
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PROPS.setColor("color.out", outCol.getColor());
					PROPS.setColor("color.err", errCol.getColor());
					PROPS.store();
				} catch (Exception exc) {
					ExceptionDialog.show(exc);
				}
			}
		});
		JPanel but = new JPanel();
		but.add(saveButton);
		
		JPanel colorPanel = new JPanel(new GridLayout(2, 2));
		colorPanel.add(new JLabel(Messages.getString("ConsoleConfiguration.COLOR_OUT_TITLE") +":")); //$NON-NLS-1$ //$NON-NLS-2$
		colorPanel.add(outCol);
		colorPanel.add(new JLabel(Messages.getString("ConsoleConfiguration.COLOR_ERR_TITLE") +":")); //$NON-NLS-1$ //$NON-NLS-2$
		colorPanel.add(errCol);
		
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(Messages.getString("ConsoleConfiguration.CONSOLE_CONF_TITLE"))); //$NON-NLS-1$
		p.add(colorPanel, BorderLayout.CENTER);
		p.add(but, BorderLayout.SOUTH);
		JPanel sub = new JPanel();
		sub.add(p);
		add(sub, BorderLayout.WEST);
	}
	
	public static Color getOutColor() {
		return (PROPS != null) ? PROPS.getColor("color.out", Color.BLACK): Color.BLACK; //$NON-NLS-1$
	}
	
	public static Color getErrColor() {
		return (PROPS != null) ? PROPS.getColor("color.err", Color.RED): Color.RED; //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * @see org.schwering.evi.core.IPanel#dispose()
	 */
	public void dispose() {
		instanceCount--;
		if (instanceCount == 0) {
			instance = null;
		}
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
		return Messages.getString("ConsoleConfiguration.CONSOLE_CONF_TITLE"); //$NON-NLS-1$
	}
}
