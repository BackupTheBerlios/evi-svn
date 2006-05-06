/**
 * @author Mike Kremer
 */
public class Pong {
	
	public static void main(String[] args){
		
		SoundManager.init();
		
		GLDisplay display = GLDisplay.createGLDisplay("Pong");
		Renderer renderContext = new Renderer();
		display.addGLEventListener(renderContext);
		display.addMouseMotionListener(new MyMouseMotionListener(renderContext));
		display.addMouseListener(new MyMouseListener(renderContext));
		display.start();
	}
}
