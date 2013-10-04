package edu.neu.madcourse.xipengwang.dabble;

import edu.neu.madcourse.xipengwang.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class BGMManager 
{
	static MediaPlayer bgmplayer;
	
	 
	
	private static int musicGoOn = -1;
	private static int musicStop = -1;


	public static void start(Context context, int music) 
	{
	    start(context, music, false);
	}
	
	public static void start(Context context, int music, boolean force) {
		if (!force && musicGoOn > -1) {
		 
			return;
		}

		if (music == -1) {
			
			music = musicStop;
		}
		if (musicGoOn == music) {
		 
			return;
		}
		if (musicGoOn != -1) {
			musicStop = musicGoOn;
			
			pause();
		}
		musicGoOn = music;
		
		if (bgmplayer != null) {
			if (!bgmplayer.isPlaying()) {
				bgmplayer.start();
			}
		}
		else
		{
			bgmplayer=MediaPlayer.create(context, R.raw.game); 
		}
		
		if (bgmplayer == null) 
		{
			
		}
		else {
			try {
				bgmplayer.setLooping(true);
				bgmplayer.start();
			} 
			catch (Exception e) {
				
			}
		}
	}

    public static void pause() {
		if(bgmplayer!=null)
		{
		    if (bgmplayer.isPlaying()) 
		    {
		    	bgmplayer.pause();
		    }
		}


		if (musicGoOn != -1) 
		{
			{
				musicStop = musicGoOn;
				
			}
			musicGoOn = -1;
			
		}
	}

	public static void release() {
		    
		try {
			if (bgmplayer != null) {
				if (bgmplayer.isPlaying()) {
					bgmplayer.stop();
				}
				bgmplayer.release();
			}
		} catch (Exception e) {
			
		}
		
		if (musicGoOn != -1) {
			musicStop = musicGoOn;
			
		}
		musicGoOn = -1;
		
	}
}