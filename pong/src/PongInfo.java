import org.schwering.evi.core.IButtonable;
import org.schwering.evi.core.IConfigurable;
import org.schwering.evi.core.IMenuable;
import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;
import org.schwering.evi.core.IPanel;

public class PongInfo implements IModuleInfo, IButtonable, IMenuable, IConfigurable {
	public boolean isMenuable() {
		return true;
	}

	public String getInfoURL() {
		return "about.html";
	}

	public Class<Pong> getModuleClass() {
		return Pong.class;
	}

	public String getName() {
		return "Pong";
	}

	public float getVersion() {
		return 1.0f;
	}

	public IModule newInstance() {
		return new Pong();
	}

	public IPanel getConfigPanel() {
		return new PongConfig();
	}

	public boolean isButtonable() {
		return true;
	}
}
