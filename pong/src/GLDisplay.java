import com.sun.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.media.opengl.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * @author Pepijn Van Eeckhoudt
 */
public class GLDisplay extends JPanel {
	private static final long serialVersionUID = 1355664716068674634L;
	private GLCanvas glCanvas;
	private FPSAnimator animator;
	private int width;
	private int height;
	private GraphicsDevice usedDevice;
	
	private MyHelpOverlayGLEventListener helpOverlayGLEventListener = new MyHelpOverlayGLEventListener();
	
	public static GLDisplay createGLDisplay() {
		return new GLDisplay();
	}
	
	private GLDisplay() {
		
		GLCapabilities caps = new GLCapabilities();
		
		caps.setRedBits(8);
		caps.setBlueBits(8);
		caps.setGreenBits(8);
		caps.setAlphaBits(8);
		
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		
		glCanvas = new GLCanvas(caps);
		glCanvas.setSize( width, height );
		glCanvas.setIgnoreRepaint( true );
		glCanvas.addGLEventListener( helpOverlayGLEventListener );
		
		setLayout(new BorderLayout());
		add(glCanvas, BorderLayout.CENTER);
		
		try {
			// TODO exception under linux / x11 / fluxbox -- why?
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(
					new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
					new Point(0, 0), "Custom Cursor"
			);
			setCursor(c);
		} catch (Exception exc) {
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		
		addKeyListener(new MyKeyAdapter());
		
		animator = new FPSAnimator( glCanvas, 60 );
		animator.setRunAsFastAsPossible(false);
	}
	
	public void start() {
		glCanvas.requestFocus();
		animator.start();
	}
	
	public void stop() {
		animator.stop();
	}
	
	public void addGLEventListener( GLEventListener glEventListener ) {
		this.helpOverlayGLEventListener.addGLEventListener( glEventListener );
	}
	
	public void removeGLEventListener( GLEventListener glEventListener ) {
		this.helpOverlayGLEventListener.removeGLEventListener( glEventListener );
	}
	
	public void addKeyListener( KeyListener l ) {
		glCanvas.addKeyListener( l );
	}
	
	public void addMouseListener( MouseListener l ) {
		glCanvas.addMouseListener( l );
	}
	
	public void addMouseMotionListener( MouseMotionListener l ) {
		glCanvas.addMouseMotionListener( l );
	}
	
	public void removeKeyListener( KeyListener l ) {
		glCanvas.removeKeyListener( l );
	}
	
	public void removeMouseListener( MouseListener l ) {
		glCanvas.removeMouseListener( l );
	}
	
	public void removeMouseMotionListener( MouseMotionListener l ) {
		glCanvas.removeMouseMotionListener( l );
	}
	
	public void registerKeyStrokeForHelp( KeyStroke keyStroke, String description ) {
		helpOverlayGLEventListener.registerKeyStroke( keyStroke, description );
	}
	
	public void registerMouseEventForHelp( int id, int modifiers, String description ) {
		helpOverlayGLEventListener.registerMouseEvent( id, modifiers, description );
	}
	
	private class MyKeyAdapter extends KeyAdapter {
		public MyKeyAdapter() {
			registerKeyStrokeForHelp( KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ), "Show/hide help message" );
			registerKeyStrokeForHelp( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "Quit demo" );
		}
		
		public void keyReleased( KeyEvent e ) {
			switch ( e.getKeyCode() ) {
			case KeyEvent.VK_ESCAPE:
				stop();
				break;
			case KeyEvent.VK_F1:
				helpOverlayGLEventListener.toggleHelp();
				break;
			}
		}
	}
	
	private static class MyHelpOverlayGLEventListener implements GLEventListener {
		private java.util.List eventListeners = new ArrayList();
		private HelpOverlay helpOverlay = new HelpOverlay();
		private boolean showHelp = false;
		
		public void toggleHelp() {
			showHelp = !showHelp;
		}
		
		public void registerKeyStroke( KeyStroke keyStroke, String description ) {
			helpOverlay.registerKeyStroke( keyStroke, description );
		}
		
		public void registerMouseEvent( int id, int modifiers, String description ) {
			helpOverlay.registerMouseEvent( id, modifiers, description );
		}
		
		public void addGLEventListener( GLEventListener glEventListener ) {
			eventListeners.add( glEventListener );
		}
		
		public void removeGLEventListener( GLEventListener glEventListener ) {
			eventListeners.remove( glEventListener );
		}
		
		public void display( GLAutoDrawable glDrawable ) {
			for ( int i = 0; i < eventListeners.size(); i++ ) {
				( (GLEventListener) eventListeners.get( i ) ).display( glDrawable );
			}
			if ( showHelp )
				helpOverlay.display( glDrawable );
		}
		
		public void displayChanged( GLAutoDrawable glDrawable, boolean b, boolean b1 ) {
			for ( int i = 0; i < eventListeners.size(); i++ ) {
				( (GLEventListener) eventListeners.get( i ) ).displayChanged( glDrawable, b, b1 );
			}
		}
		
		public void init( GLAutoDrawable glDrawable ) {
			for ( int i = 0; i < eventListeners.size(); i++ ) {
				( (GLEventListener) eventListeners.get( i ) ).init( glDrawable );
			}
		}
		
		public void reshape( GLAutoDrawable glDrawable, int i0, int i1, int i2, int i3 ) {
			for ( int i = 0; i < eventListeners.size(); i++ ) {
				( (GLEventListener) eventListeners.get( i ) ).reshape( glDrawable, i0, i1, i2, i3 );
			}
		}
	}
}
