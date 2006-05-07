import java.awt.Component;

import javax.swing.Icon;

import org.schwering.evi.core.IModule;
import org.schwering.evi.core.IPanel;

/**
 * @author Mike Kremer
 */
public class Pong implements IModule, IPanel {
	
	private static Pong instance = null;
	private GLDisplay display;
	
	public static void main(String[] args){
		new Pong();
	}
		
	public Pong() {
		if (instance != null) {
			throw new RuntimeException("Pong already running.");
		}
		SoundManager.init();
		display = GLDisplay.createGLDisplay();
		Renderer renderContext = new Renderer();
		display.addGLEventListener(renderContext);
		display.addMouseMotionListener(new MyMouseMotionListener(renderContext));
		display.addMouseListener(new MyMouseListener(renderContext));
		display.start();
		instance = this;
	}
	
	public Component getPanelInstance() {
		return display;
	}
	
	public String getTitle() {
		return "Pong";
	}
	
	public Icon getIcon() {
		return null;
	}
	
	public void dispose() {
		instance = null;
		display.stop();
	}
}
