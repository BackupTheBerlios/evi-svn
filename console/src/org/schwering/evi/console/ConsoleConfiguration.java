package org.schwering.evi.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.schwering.evi.conf.Properties;
import org.schwering.evi.core.IPanel;
import org.schwering.evi.core.ModuleContainer;
import org.schwering.evi.util.ExceptionDialog;

/**
 * The small configuration for the console module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class ConsoleConfiguration extends JPanel implements IPanel {
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
	
	private JButton outColButton;
	private JButton errColButton;
	
	public ConsoleConfiguration() {
		outColButton = new JButton("    ");
		outColButton.setBackground(getOutColor());
		outColButton.setBorderPainted(false);
		outColButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, Messages.getString("ConsoleConfiguration.COLOR_OUT_TITLE"), outColButton.getBackground()); //$NON-NLS-1$
				if (c == null) {
					return;
				}
				outColButton.setBackground(c);
				if (PROPS != null) {
					PROPS.setColor("color.out", c); //$NON-NLS-1$
				}
			}
		});

		errColButton = new JButton("    ");
		errColButton.setBackground(getErrColor());
		errColButton.setBorderPainted(false);
		errColButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, Messages.getString("ConsoleConfiguration.COLOR_ERR_TITLE"), errColButton.getBackground()); //$NON-NLS-1$
				if (c == null) {
					return;
				}
				errColButton.setBackground(c);
				if (PROPS != null) {
					PROPS.setColor("color.err", c); //$NON-NLS-1$
				}
			}
		});
		
		JPanel col = new JPanel(new GridLayout(2, 2));
		col.add(new JLabel(Messages.getString("ConsoleConfiguration.COLOR_OUT_TITLE") +":")); //$NON-NLS-1$ //$NON-NLS-2$
		col.add(outColButton);
		col.add(new JLabel(Messages.getString("ConsoleConfiguration.COLOR_ERR_TITLE") +":")); //$NON-NLS-1$ //$NON-NLS-2$
		col.add(errColButton);
		
		JButton saveButton = new JButton(Messages.getString("ConsoleConfiguration.SAVE")); //$NON-NLS-1$
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PROPS.store();
				} catch (Exception exc) {
					ExceptionDialog.show(exc);
				}
			}
		});
		JPanel but = new JPanel();
		but.add(saveButton);
		
		JPanel p = new JPanel(new BorderLayout());
		p.setLayout(new BorderLayout());
		p.setBorder(new TitledBorder(Messages.getString("ConsoleConfiguration.CONSOLE_CONF_TITLE"))); //$NON-NLS-1$
		p.add(col, BorderLayout.CENTER);
		p.add(but, BorderLayout.SOUTH);
		add(p);
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
