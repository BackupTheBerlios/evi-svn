/* Copyright (C) 2006 Christoph Schwering */
import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.schwering.evi.conf.MainConfiguration;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;

/**
 * A module that shows a label with "Hello World".
 * 
 * It extends JPanel because JPanel is a graphical component.
 * It implements IModule because it is a module.
 * It implements IPanel because it is a graphical module.
 * @author Christoph Schwering (mailto:schwering@gmail.com)
 */
public class HelloWorld extends JPanel implements IModule, IPanel {
	private static final long serialVersionUID = -6534025009528050898L;

	public HelloWorld() {
		this("Hello World!!");
	}

	public HelloWorld(String s) {
		JLabel label = new JLabel(s);
		label.setForeground(Color.YELLOW);
		label.setBackground(MainConfiguration.PROPS.getColor("color.secondary"));
		label.setFont(MainConfiguration.PROPS.getFont("font.primary"));
		setBackground(MainConfiguration.PROPS.getColor("color.secondary"));
		add(label);
	}
	
	public HelloWorld(URL url) {
		this("Hey, have a look at "+ url);
	}

	/**
	 * This module doesn't have any icon.
	 */
	public Icon getIcon() {
		return null;
	}

	/**
	 * Since this class is also the module's graphical part 
	 * (it extends JPanel).
	 */
	public Component getPanelInstance() {
		return this;
	}

	/**
	 * The title of this module in the tabbar should be 
	 * "Hello!".
	 */
	public String getTitle() {
		return "Hello!";
	}

	/**
	 * Normally, this method should close open files or 
	 * close connections. In this method, there's nothing to do.
	 * It will just print "Bye!" into the console so that you 
	 * notice when dispose() is invoked.
	 */
	public void dispose() {
		System.out.println("HelloWorld says Bye!");
	}
}
