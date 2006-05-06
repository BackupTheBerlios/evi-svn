import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Mike Kremer
 */
public class MyMouseListener implements MouseListener {
	
	private Renderer renderContext;
	
	public MyMouseListener(Renderer render){
		this.renderContext = render;
	}

	public void mouseClicked(MouseEvent arg0) {
		if(this.renderContext.isPause()) this.renderContext.switchPause();
    }

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
