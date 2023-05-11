import javax.sound.sampled.AudioInputStream;		// for playing sound clips
import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;				// for storing sound clips


public class SoundManager {				// a Singleton class
	HashMap<String, Clip> clips;

	private static SoundManager instance = null;	// keeps track of Singleton instance

	private SoundManager () {
		clips = new HashMap<>();

		Clip clip = loadClip("sounds/background1.wav");
		clips.put("background1", clip);		// background theme sound option1

		clip = loadClip("sounds/background2.wav");
		clips.put("background2", clip);		// background theme sound option 2

		clip = loadClip("sounds/footstep.wav");
		clips.put("footstep", clip);		// played when player is moving right or left

		clip = loadClip("sounds/jump 1.wav");
		clips.put("jump", clip);		// player is jumping

		clip = loadClip("sounds/bubble 1.wav");
		clips.put("bubble", clip);		// moving to another level

		clip = loadClip("sounds/collect.wav");
		clips.put("collect", clip);		// player is collecting food

		clip = loadClip("sounds/heart.wav");
		clips.put("heart", clip);		// player is collecting heart

		clip = loadClip("sounds/crate open 7.wav");
		clips.put("open", clip);

		clip = loadClip("sounds/crate close.wav");
		clips.put("close", clip);

		clip = loadClip("sounds/die.wav"); //Player dies or losses a heart
		clips.put("die", clip);
//
//		clip = loadClip("sounds/crate close.wav");
//		clips.put("close", clip);

	}


	public static SoundManager getInstance() {	// class method to get Singleton instance
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


	public Clip getClip (String title) {

		return clips.get(title);		// gets a sound by supplying key
	}


    	public Clip loadClip (String fileName) {	// gets clip from the specified file
 		AudioInputStream audioIn;
		Clip clip = null;

		try {
    			File file = new File(fileName);
    			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
    			clip = AudioSystem.getClip();
    			clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("Error opening sound files: " + e);
		}
    		return clip;
    	}


    	public void playSound(String title, Boolean looping) {
			Clip clip = getClip(title);
			if (clip != null) {
				clip.setFramePosition(0);
				if (looping)
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				else
					clip.start();
			}
    	}


    	public void stopSound(String title) {
			Clip clip = getClip(title);
			if (clip != null) {
				clip.stop();
			}
    	}

}