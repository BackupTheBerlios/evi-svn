import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

//import com.sun.opengl.util.GLUT;

import java.io.*;
import java.util.*;

/**
 * Renderer
 * @author Mike Kremer
 */
class Renderer implements GLEventListener {
    private GLU glu = new GLU();
    
    private Ball ball;
    private Pad rpad;
    private Pad lpad;
    
    private int score = 0;
    private int level = 1;
    
    private float lPadSpeed = 0.1f;
    
    private boolean pause;
    private boolean is_multiplayer = false;
    
    private int x_richtung = 1;
    private int y_richtung = 1;
    
    private float fade = -12;
    
    private float delta_x = 1.0f;
    private float delta_y = 1.0f;
    
    private boolean p1_hit, p2_hit;
    
    private float y_speed = 0.2f;
    private float x_speed = 0.2f;
    
    private final int NUM_TEXTURES = 3;
    private int[] textures = new int[NUM_TEXTURES];
    private final String[] texture_filenames = {
    	"gfx/ball.png",
    	"gfx/pad.png",
    	"gfx/field.png"
    };
    
    public Renderer(){
    	ball = new Ball(0.0f, 0.0f);
    	rpad = new Pad(6.5f, 0.0f);
    	lpad = new Pad(-6.5f, 0.0f);
    	
    	reset();
    }
    
    private void reset(){
    	y_speed = 0.2f;
    	x_speed = 0.2f;
    	y_richtung = 1;
    	x_richtung = 1;
    	
    	pause = true;
    	ball.setX(0.0f);
    	ball.setY(0.0f);
    	
    	p1_hit = false;
    	p2_hit = false;
    	
    	/*if(!is_multiplayer){
    		lpad.setY(0.0f);
    	}*/
    	
    	// Dem Ball eine neue Zufallsrichtung geben:
    	float rand = (float)(new Random().nextGaussian());
    	y_speed *= (rand*rand)/10;
    	if(rand <= 0){
    		x_richtung = -x_richtung;
    		p1_hit = true;
    	}
    }
    
    private void update(){
    	if(!pause){
    		
	    	ball.moveX(x_speed * x_richtung);
	    	ball.moveY(y_speed * y_richtung);
	    	
	    	if(x_richtung == -1 && ball.getX() <= 3)
	    		for(int f = 0; f < level; f++){
	    			moveLPad();
	    		}
	    	
	    	// Kollision Rechtes Pad
	    	if(!p1_hit && (ball.getX() >= (rpad.getX() - (x_speed * 5)*rpad.getWidth()/2 - 0.3f*delta_x) && ball.getX() <= rpad.getX() + (x_speed * 5)*rpad.getWidth()/2 + 0.3f*delta_x)){
	    		if(rpad.collide(ball, this, true)){
	    			x_richtung = -x_richtung;
	    			p1_hit = true;
	    			p2_hit = false;
	    			x_speed += delta_x/8;
	    		}
	    	}
	    	else if(ball.getX() >= 6.8f){
	    		SoundManager.playSound(SoundManager.SND_LOSER);
	    		reset();
	    	}
	    	
	    	// Kollision Linkes Pad
	    	if(!p2_hit && (ball.getX() <= -6.0f && ball.getX() >= -6.6f)){
	    		if(lpad.collide(ball, this, false)){
	    			x_richtung = -x_richtung;
	    			p2_hit = true;
	    			p1_hit = false;
	    		}
	    	}
	    	else if(ball.getX() <= -6.8f){
	    		score++;
	    		SoundManager.playSound(SoundManager.SND_GOAL);
	    		reset();
	    	}
	    	
	    	// Ball oben oder unten
	    	if(ball.getY() <= -4.8f){
	    		y_richtung = -y_richtung;
	    		SoundManager.playSound(SoundManager.SND_BOUNCE);
	    	}
	    	if(ball.getY() >= 4.8f){
	    		y_richtung = -y_richtung;
	    		SoundManager.playSound(SoundManager.SND_BOUNCE);
	    	}
	    	
	    	if(score == 3){
	    		level++;
	    		score = 0;
	    		//lPadSpeed += (float)(level - 1)*0.04f;
	    	}
    	}
    }
    
    
    /** Zeigt die Objekte der Szene an.
     * 
     * @param gLDrawable Das GLAutoDrawable Objekt
     */
    public void display(GLAutoDrawable gLDrawable) {
    	update();
        final GL gl = gLDrawable.getGL();
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);
        
        // Hintergrund
        gl.glTranslatef(0.0f, 0.0f, fade);
	    gl.glBegin(GL.GL_QUADS);
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-7.0f, 5.0f, 0.0f);	// Top Left
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(7.0f, 5.0f, 0.0f);		// Top Right
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(7.0f, -5.0f, 0.0f);	// Bottom Right
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-7.0f, -5.0f, 0.0f);	// Bottom Left
	    gl.glEnd();				// Done Drawing The Quad
	    gl.glFlush();				// Done Drawing The Quad
	    
	    gl.glLoadIdentity();
        
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);
        
        // Rechtes Pad
        gl.glTranslatef(rpad.getX(), rpad.getY(), fade);
	    gl.glBegin(GL.GL_QUADS);
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-rpad.getWidth()/2, rpad.getHeight()/2, 0.0f);	// Top Left
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(rpad.getWidth()/2, rpad.getHeight()/2, 0.0f);		// Top Right
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(rpad.getWidth()/2, -rpad.getHeight()/2, 0.0f);	// Bottom Right
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-rpad.getWidth()/2, -rpad.getHeight()/2, 0.0f);	// Bottom Left
	    gl.glEnd();				// Done Drawing The Quad
	    gl.glFlush();				// Done Drawing The Quad
	    
	    gl.glLoadIdentity();
        
	    // Linkes Pad
        gl.glTranslatef(lpad.getX(), lpad.getY(), fade);
	    gl.glBegin(GL.GL_QUADS);           	// Draw A Quad
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-lpad.getWidth()/2, lpad.getHeight()/2, 0.0f);	// Top Left
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(lpad.getWidth()/2, lpad.getHeight()/2, 0.0f);		// Top Right
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(lpad.getWidth()/2, -lpad.getHeight()/2, 0.0f);	// Bottom Right
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-lpad.getWidth()/2, -lpad.getHeight()/2, 0.0f);	// Bottom Left
	    gl.glEnd();				// Done Drawing The Quad
	    gl.glFlush();				// Done Drawing The Quad
	    
	    gl.glLoadIdentity();
	    
	    
	    gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
	    
	    // Ball
        gl.glTranslatef(ball.getX(), ball.getY(), fade);
	    gl.glBegin(GL.GL_QUADS);
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-ball.getWidth()/2, ball.getHeight()/2, 0.0f);	// Top Left
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ball.getWidth()/2, ball.getHeight()/2, 0.0f);		// Top Right
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ball.getWidth()/2, -ball.getHeight()/2, 0.0f);	// Bottom Right
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-ball.getWidth()/2, -ball.getHeight()/2, 0.0f);	// Bottom Left
	    gl.glEnd();				// Done Drawing The Quad
	    gl.glFlush();
	    
        gl.glFlush();
        gl.glLoadIdentity();
    }


    /** Wird ausgef�hrt, wenn sich der Displaymodus �ndert.
     * @param gLDrawable The GLAutoDrawable object.
     * @param modeChanged Indicates if the video mode has changed.
     * @param deviceChanged Indicates if the video device has changed.
     */
    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
    }

    /** Initialisierung
     * @param gLDrawable The GLAutoDrawable object.
     */
    public void init(GLAutoDrawable gLDrawable) {
        try{
        	loadGLTextures(gLDrawable);
        }
        catch(IOException e){
        	throw new RuntimeException(e);
        }
    	
    	GL gl = gLDrawable.getGL();
        gl.glShadeModel(GL.GL_SMOOTH);              // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
        gl.glEnable(GL.GL_DOUBLEBUFFER);
        gl.glEnable(GL.GL_TEXTURE_2D);
        
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);
    }


    /**
     * Nach einem Resize
     * 
     * @param gLDrawable The GLAutoDrawable object.
     * @param x The X Coordinate of the viewport rectangle.
     * @param y The Y coordinate of the viewport rectanble.
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        final GL gl = gLDrawable.getGL();

        if (height <= 0) // avoid a divide by zero error!
            height = 1;
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    public void switchPause(){
    	if(pause == true) pause = false;
    	else pause = true;
    }
        
    private void moveLPad(){
    	
    	if(!(((ball.getY() + 0.5f) >= lpad.getY()) &&
    			((ball.getY() - 0.5f) <= lpad.getY()))){
    		if(ball.getY() > lpad.getY()){
        		lpad.moveY(lPadSpeed);
        	}
        	else if(ball.getY() < (lpad.getY() - 0.1f)){
        		lpad.moveY(-lPadSpeed);
        	}
    	}
    }
    
    public boolean isPause(){
    	return pause;
    }
    
    public void setPlayer1Y(float ny){
    	rpad.setY(ny);
    }
    
    public void setPlayer1X(float nx){
    		rpad.setX(nx);
    }
    
    public int ballGetYDirection(){
    	return y_richtung;
    }
    
    public void ballAddYSpeed(float s){
    	y_speed += s;
    }
    
    private void loadGLTextures(GLAutoDrawable glDrawable) throws IOException{
    	GL gl = glDrawable.getGL();
    	gl.glGenTextures(NUM_TEXTURES, textures, 0);
    	
    	for(int i = 0; i < NUM_TEXTURES; i++){
    		TextureReader.Texture texture = TextureReader.readTexture(texture_filenames[i]);
    		
    		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
    		
    		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, texture.getPixels());
    	}
    }
    
    public void setDeltaX(int x){
    	delta_x = (float)x/10;
    }
    
    public void setDeltaY(int y){
    	delta_y = y;
    }
}
