/**
 * @author Mike Kremer
 */
public interface Renderable {
	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	/*
	 * Typ des Objekts:
	 * 1: Quadrat
	 * 2: Triangle
	 * 
	 * beliebig erweiterbar
	 */
	public int getType();
}
