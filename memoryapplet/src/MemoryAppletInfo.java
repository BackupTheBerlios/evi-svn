import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IModuleInfo;

public class MemoryAppletInfo implements IModuleInfo {

	public String getInfoURL() {
		return null;
	}

	public Class getModuleClass() {
		return MemoryApplet.class;
	}

	public String getName() {
		return "Memory";
	}

	public float getVersion() {
		return 1.0f;
	}

	public IModule newInstance() {
		return new MemoryApplet();
	}
	
}
