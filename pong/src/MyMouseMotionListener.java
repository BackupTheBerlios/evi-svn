import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author Mike Kremer
 */
public class MyMouseMotionListener implements MouseMotionListener {

	private Renderer renderContext;
	
	private int old_x, delta_x;
	private int old_y, delta_y;
	
	private int x_coord, y_coord;
	
	public MyMouseMotionListener(Renderer render){
		this.renderContext = render;
	}
	
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent arg0) {
		x_coord = arg0.getX();
		y_coord = arg0.getY();
		
		renderContext.setPlayer1Y((float)(-(y_coord - 226))/50);
		
		delta_x = old_x - x_coord;
		renderContext.setDeltaX(delta_x);
		old_x = x_coord;
		
		if(x_coord >= 240 && x_coord <= 560)
			renderContext.setPlayer1X((float)((x_coord - 226))/50);
	}

}
