/**
 * Das Spielpad.
 * @author Mike Kremer
 */
public class Pad implements Renderable {
	
	private float x, y;
	private float width, height;
	
	public Pad(float nx, float ny){
		this.x = nx;
		this.y = ny;
		
		this.width = 0.5f;
		this.height = 2.0f;
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
	
	public boolean collide(Renderable b, Renderer renderContext, boolean player){
		if((b.getY() - b.getHeight()/2 >= (this.y - this.height/2))
				&& (b.getY() + b.getHeight()/2 <= this.y + this.height/2)){
			setDrift(b, renderContext);
			
			if(player)
				SoundManager.playSound(SoundManager.SND_PLAYER_PAD);
			else 
				SoundManager.playSound(SoundManager.SND_ENEMY_PAD);
			
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * Verleiht dem Ball 'Drift',
	 * d.h. der Austrittswinkel veraendert sich mit
	 * der Position, an der der Ball auf das Pad trifft.
	 */
	public void setDrift(Renderable b, Renderer renderContext){
			if(renderContext.ballGetYDirection() == -1){
				renderContext.ballAddYSpeed(-((float)((b.getY() - this.y))/10)); 
			}
			else{
				renderContext.ballAddYSpeed(((float)((b.getY() - this.y))/10));
			}
	}
}
