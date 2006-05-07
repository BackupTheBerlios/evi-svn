import net.java.games.joal.*;
import net.java.games.joal.util.*;
import java.nio.*;

/**
 * @author Mike Kremer
 */
public class SoundManager {
	
	static final int NUM_SOUNDS = 5;
	static AL al = ALFactory.getAL();
	
	// Buffer fuer die Sounddaten:
	static int[] buffer = new int[NUM_SOUNDS];
	
	// Sources (Art Pointer) auf die S-Daten:
	static int[] source = new int[NUM_SOUNDS];
	
	static final int SND_PLAYER_PAD = 0;
	static final int SND_ENEMY_PAD = 1;
	static final int SND_GOAL = 2;
	static final int SND_BOUNCE = 3;
	static final int SND_LOSER = 4;
	
	static final String[] files = {
		"sounds/boop.wav",
		"sounds/boop2.wav",
		"sounds/yeah.wav",
		"sounds/bounce.wav",
		"sounds/loser.wav"
	};
	
	static float[] sourcePos = {0.0f, 0.0f, 0.0f};
	static float[] sourceVel = {0.0f, 0.0f, 0.0f};
	static float[] listenerPos = {0.0f, 0.0f, 0.0f};
	static float[] listenerVel = {0.0f, 0.0f, 0.0f};
	
	static float[] listenerOri = {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f};
	
	static int loadALData(){
		// Variablen, die geladen werden
		int[] format = new int[NUM_SOUNDS];
		int[] size = new int[NUM_SOUNDS];
		ByteBuffer[] data = new ByteBuffer[NUM_SOUNDS];
		int[] freq = new int[NUM_SOUNDS];
		int[] loop = new int[NUM_SOUNDS];
		
				
		// Lade wav Daten in Buffer:
		al.alGenBuffers(NUM_SOUNDS, buffer, 0);
		if(al.alGetError() != AL.AL_NO_ERROR)
			return AL.AL_FALSE;
		
		for(int c = 0; c < NUM_SOUNDS; c++) {
			try {
				ALut.alutLoadWAVFile(ResourceRetriever.getResourceAsStream(files[c]), 
						format, data, size, freq, loop);
				al.alBufferData(buffer[c], format[0], data[0], size[0], freq[0]);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		
		// Sourcen binden:
		al.alGenSources(NUM_SOUNDS, source, 0);
		if(al.alGetError() != AL.AL_NO_ERROR)
			return AL.AL_FALSE;
		for(int i = 0; i < NUM_SOUNDS; i++)
		{
			al.alSourcei(source[i], AL.AL_BUFFER, buffer[i]);
			al.alSourcef(source[i], AL.AL_PITCH, 1.0f);
			al.alSourcef(source[i], AL.AL_GAIN, 1.0f);
			al.alSourcefv(source[i], AL.AL_POSITION, sourcePos, 0);
			al.alSourcefv(source[i], AL.AL_VELOCITY, sourceVel, 0);
			al.alSourcei(source[i], AL.AL_LOOPING, loop[i]);
		}
		
		// Error check
		if(al.alGetError() == AL.AL_NO_ERROR)
			return AL.AL_TRUE;
		
		return AL.AL_FALSE;
	}
	
	static void setListenerValues(){
		al.alListenerfv(AL.AL_POSITION, listenerPos, 0);
		al.alListenerfv(AL.AL_VELOCITY, listenerVel, 0);
		al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
	}
	
	static void killAllData(){
		al.alDeleteBuffers(NUM_SOUNDS, buffer, 0);
		al.alDeleteSources(NUM_SOUNDS, source, 0);
	}
	
	static boolean init(){
		ALut.alutInit();
		if(al.alGetError() != AL.AL_NO_ERROR)
			return false;
		
		if(loadALData() == AL.AL_FALSE)
			return false;
		
		setListenerValues();
		
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(
				new Thread(
						new Runnable(){
							public void run(){
								killAllData();
							}
						}
				)
		);
		
		return true;
	}
	
	static void playSound(int i){
		al.alSourcePlay(source[i]);
	}
	
	static void pauseSound(int i){
		al.alSourcePause(source[i]);
	}
}
