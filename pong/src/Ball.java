/**
 * Der Spielball, eine 4x4 Pixel grosse Kugel.
 * @author Mike Kremer
 */
public class Ball implements Renderable {
	
	private float x, y;
	private float width, height;
	
	public Ball(float nx, float ny){
		this.x = nx;
		this.y = ny;
		
		this.width = 0.2f;
		this.height = 0.2f;
	}
	
	public void moveX(float ax){
		this.x += ax;
	}
	
	
	public void moveY(float ay){
		this.y += ay;
	}
	
	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}
	
	public void setX(float nx){
		this.x = nx;
	}
	
	public void setY(float ny){
		this.y = ny;
	}
	
	public int getType(){
		// Kreis
		return 1;
	}
}
