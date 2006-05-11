import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.schwering.evi.core.IPanel;

public class PongConfig extends JPanel implements IPanel {
	private static final long serialVersionUID = 374500247800517016L;

	public PongConfig() {
		add(new JLabel("hello"));
	}
	
	public void dispose() {
		System.out.println("PongConfig.dispose()");
	}

	public Icon getIcon() {
		return null;
	}

	public Component getPanelInstance() {
		return this;
	}

	public String getTitle() {
		return "Config";
	}
	

}
