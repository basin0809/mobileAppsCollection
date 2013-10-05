package edu.neu.madcourse.xipengwang.dabble;


import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.madcourse.xipengwang.MainActivity;
import edu.neu.madcourse.xipengwang.R;

public class Dabble extends Activity{
	private Button gameButton;
	private Button quitButton;
	private Button musicButton;
	private Button introButton;
	private Button ackButton;
	private boolean musicGoOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		musicGoOn=true;
		setContentView(R.layout.activity_dabble);
		TwiceActiveCheck.musicTwicePressed.add(0);
		gameButton = (Button)findViewById(R.id.game_button);
		quitButton = (Button)findViewById(R.id.quit_button);
		musicButton = (Button)findViewById(R.id.music_button);
		introButton = (Button)findViewById(R.id.dabble_intro);
		ackButton = (Button)findViewById(R.id.dabble_ack);
		gameButton.setOnClickListener(new GameButtonListener());
		musicButton.setOnClickListener(new MusicButtonListener());
		quitButton.setOnClickListener(new QuitButtonListener());
		ackButton.setOnClickListener(new AckButtonListener());
		introButton.setOnClickListener(new IntroButtonListener());
		
		
	}
	   private void openNewGameDialog() {
		      new AlertDialog.Builder(this)
		           .setTitle(R.string.dabble_game_title)
		           .setItems(R.array.Mode,
		            new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialoginterface,
		                     int i) {
		                  startGame(i);
		                  
		               }
		            })
		           .show();
		   }

		   /** Start a new game with the given difficulty level */
		   private void startGame(int i) {
		      //Log.d(TAG, "clicked on " + i);
		      Intent intent = new Intent(this, Game.class);
		      intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
		      System.out.println("clicked on " + i);
		      intent.putExtra("gameMode", i+"");
		      startActivity(intent);
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
		if(TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1)==1){
			
		}
		else{
		musicGoOn=false;
        BGMManager.start(this,R.raw.game);}
	}
	class GameButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 openNewGameDialog();
		}
		
	}
	class MusicButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1)==0){
				
				
		        BGMManager.pause();
		        TwiceActiveCheck.musicTwicePressed.add(1);
		        musicButton.setText("Turn on the music");
				
			}
			else{
				musicGoOn=false;
		        BGMManager.start(Dabble.this,R.raw.game);
		        TwiceActiveCheck.musicTwicePressed.add(0);
	            musicButton.setText("Turn off the music");
			}
		}
		
	}
	
	class AckButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 Intent intent =new Intent();
			 intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
			 intent.setClass(Dabble.this, DabbleACK.class);
			 startActivity(intent);
		}
		
	}
	
	class IntroButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 Intent intent =new Intent();
			 intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
			 intent.setClass(Dabble.this, Introduction.class);
			 startActivity(intent);
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
