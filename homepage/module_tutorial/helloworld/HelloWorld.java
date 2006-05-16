import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;

/**
 * A module that shows a label with "Hello World".
 * 
 * It extends JPanel.. well because JPanel is a graphical component.
 * It implements IModule because it is a module.
 * It implements IPanel because it is a graphical module.
 */
public class HelloWorld extends JPanel implements IModule, IPanel {
	public HelloWorld() {
		add(new JLabel("Hello World!"));
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
		System.out.println("Bye!");
	}
}
