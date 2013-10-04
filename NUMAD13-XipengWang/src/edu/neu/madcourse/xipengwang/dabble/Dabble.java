package edu.neu.madcourse.xipengwang.dabble;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.madcourse.xipengwang.R;

public class Dabble extends Activity{
	private Button gameButton;
	private Button quitButton;
	private Button musicButton;
	private boolean musicGoOn;
	private ArrayList<Integer> musicTwicePressed = new ArrayList<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		musicGoOn=true;
		setContentView(R.layout.activity_dabble);
		musicTwicePressed.add(0);
		gameButton = (Button)findViewById(R.id.game_button);
		quitButton = (Button)findViewById(R.id.quit_button);
		musicButton = (Button)findViewById(R.id.music_button);
		gameButton.setOnClickListener(new GameButtonListener());
		musicButton.setOnClickListener(new MusicButtonListener());
		quitButton.setOnClickListener(new QuitButtonListener());
		
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
        if(!musicGoOn)
            BGMManager.pause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(musicTwicePressed.get(musicTwicePressed.size()-1)==1){
			
		}
		else{
		musicGoOn=false;
        BGMManager.start(this,R.raw.game);}
	}
	class GameButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.putExtra("music_stuate", musicTwicePressed.get(musicTwicePressed.size()-1).toString());
		 intent.setClass(Dabble.this, Game.class);
		 startActivity(intent);
		}
		
	}
	class MusicButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(musicTwicePressed.get(musicTwicePressed.size()-1)==0){
				
				
		        BGMManager.pause();
		        musicTwicePressed.add(1);
		        musicButton.setText("Turn on the music");
				
			}
			else{
				musicGoOn=false;
		        BGMManager.start(Dabble.this,R.raw.game);
		        musicTwicePressed.add(0);
	            musicButton.setText("Turn off the music");
			}
		}
		
	}
	class QuitButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 finish();
		}
		
	}
	
}
