import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

public class Player {
	public void playmusic(String musicfile) {
		File soundFile = new File(musicfile);
		try {
			Clip clip = AudioSystem.getClip();
			if (musicfile.equals("stop"))
			{
				
				clip.stop();
				
			}
			else {
			AudioInputStream inputStream= AudioSystem.getAudioInputStream(soundFile);
			clip.open(inputStream);
			//clip.loop(clip.LOOP_CONTINUOUSLY);
			clip.start();
			
						
		}}
		
		
	catch(Exception e)
		{
		System.out.println(e);
		}
	
		
	
	}

}
