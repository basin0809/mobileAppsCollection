package edu.neu.madcourse.xipengwang.dabble;


import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import edu.neu.madcourse.xipengwang.MainActivity;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.comm.AsyncPullService;
import edu.neu.madcourse.xipengwang.comm.ChooseName;
import edu.neu.madcourse.xipengwang.comm.HighScore;
import edu.neu.madcourse.xipengwang.comm.HighScoreRecord;
import edu.neu.madcourse.xipengwang.comm.OppNameMyName;
import edu.neu.madcourse.xipengwang.comm.Regist;
import edu.neu.mhealth.api.KeyValueAPI;

public class Dabble extends Activity{
	
	/**these are views of single game**/
	private Button gameButton;
	private Button quitButton;
	private Button musicButton;
	private Button introButton;
	private Button ackButton;
	private boolean musicGoOn;
	
	/**these are views of two player game**/
	private ProgressBar progressBar;
	private Button stopConn, menuButton, scoreButton, htuButton,TPGamebutton  ;	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;   
    QuitTask quitTask;
    QuitTask2 quitTask2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 
		musicGoOn=true;
		setContentView(R.layout.activity_dabble);
		alphaDes = new AlphaAnimation(1.0f, 0.0f);
        alphaInc = new AlphaAnimation(0.0f, 1.0f);
        alphaDes.setDuration(100);
        alphaInc.setDuration(100);
        alphaDes.setFillAfter(true);
        alphaInc.setFillAfter(true);

		progressBar = (ProgressBar)findViewById(R.id.finalProgressBar);
		progressBar.startAnimation(alphaDes);
	
		
		TwiceActiveCheck.musicTwicePressed.add(0);
		
		gameButton = (Button)findViewById(R.id.game_button);
		TPGamebutton = (Button)findViewById(R.id.onLineGame_button);
		
		scoreButton = (Button)findViewById(R.id.finalHighScore_button);
		
		quitButton = (Button)findViewById(R.id.quit_button);
		musicButton = (Button)findViewById(R.id.music_button);
		introButton = (Button)findViewById(R.id.dabble_intro);
		ackButton = (Button)findViewById(R.id.dabble_ack);
		
		gameButton.setOnClickListener(new GameButtonListener());
		musicButton.setOnClickListener(new MusicButtonListener());
		quitButton.setOnClickListener(new QuitButtonListener());
		ackButton.setOnClickListener(new AckButtonListener());
		introButton.setOnClickListener(new IntroButtonListener());
		
		scoreButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				CheckScoreTask checkScoreTask = new CheckScoreTask(Dabble.this);
				checkScoreTask.execute();}
				
			
			
		});
		
		TPGamebutton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		      	Intent intent = new Intent(Dabble.this, ChooseName.class);
		      	intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
 		      	startActivity(intent);
				
			}
			
		});
		
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
		
		
		progressBar.startAnimation(alphaDes);
		
		quitTask = new QuitTask(this);
		quitTask.execute();
		stopService(new Intent(this, AsyncPullService.class));
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
			Intent intent =new Intent();
			 intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
			 intent.setClass(Dabble.this, Setting.class);
			 startActivity(intent);
			// TODO Auto-generated method stub
			/*if(TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1)==0){
				
				
		        BGMManager.pause();
		        TwiceActiveCheck.musicTwicePressed.add(1);
		        musicButton.setText("Turn on the music");
				
			}
			else{
				musicGoOn=false;
		        BGMManager.start(Dabble.this,R.raw.game);
		        TwiceActiveCheck.musicTwicePressed.add(0);
	            musicButton.setText("Turn off the music");
			}*/
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
	

class CheckScoreTask extends AsyncTask<String, Integer, String[]> {  
    	
    	private Context context;  
    	CheckScoreTask(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
              progressBar.startAnimation(alphaInc);
              
          }  
    

        @Override  
        protected String[] doInBackground(String... params) {  
        	String checkString1 =KeyValueAPI.get("basin", "basin576095", "Jim"+"@HS");
        	String checkString1f =KeyValueAPI.get("basin", "basin576095", "Jim"+"@HFS");
        	String checkString2 =KeyValueAPI.get("basin", "basin576095", "Tom"+"@HS");
        	String checkString2f =KeyValueAPI.get("basin", "basin576095", "Tom"+"@HFS");
        	String checkString3 =KeyValueAPI.get("basin", "basin576095", "Mary"+"@HS");
        	String checkString3f =KeyValueAPI.get("basin", "basin576095", "Mary"+"@HFS");
        	String checkString4 =KeyValueAPI.get("basin", "basin576095", "Fred"+"@HS");
        	String checkString4f =KeyValueAPI.get("basin", "basin576095", "Fred"+"@HFS");
        	
        	if(checkString1.equals("Error: No Such Key")){
        		checkString1="0";
        		checkString1f=" ";
        	}
        	if(checkString2.equals("Error: No Such Key")){
        		checkString2="0";
        		checkString2f=" ";
        	}
        	if(checkString3.equals("Error: No Such Key")){
        		checkString3="0";
        		checkString3f=" ";
        	}
        	if(checkString4.equals("Error: No Such Key")){
        		checkString4="0";
        		checkString4f=" ";
        	}
        	
        	String[] strings = new String[4];
        	strings[0]=checkString1f;
        	strings[1]=checkString2f;
        	strings[2]=checkString3f;
        	strings[3]=checkString4f;
        	return strings;
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
        @Override  
        protected void onPostExecute(String[] results) {  
        	
        	HighScoreRecord.highscore[0]=results[0];
        	HighScoreRecord.highscore[1]=results[1];
        	HighScoreRecord.highscore[2]=results[2];
        	HighScoreRecord.highscore[3]=results[3];
        	
        	Intent intent = new Intent(Dabble.this, HighScore.class);
        	intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
        	startActivity(intent);
            
        }  
 
        @Override  
        protected void onPreExecute() {  
           
            
        }  
 
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            
        	 //System.out.println("onProgressUpdate"+values[0]);  
            
        	// progressDialog.setProgress(values[0]);
              
        }  
 
     }
    class QuitTask extends AsyncTask<Void, Integer, Void>{
    	private Context context;  
    	QuitTask(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
             // progressBar.startAnimation(alphaInc);
              
          }  
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//CommGame.aTask.cancel(true);
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#QUIT");
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#QUIT");
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#QUIT");
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#QUIT");

			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#QUIT2");
			System.out.println(OppNameMyName.myName+" quits");
			return null;
		}
    	
    }
    class QuitTask2 extends AsyncTask<Void, Integer, Void>{
    	private Context context;  
    	QuitTask2(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
             // progressBar.startAnimation(alphaInc);
              
          }  
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//CommGame.aTask.cancel(true);
			/*KeyValueAPI.put("basin", "basin576095","Jim", "#QUIT");
			KeyValueAPI.put("basin", "basin576095","Tom", "#QUIT");
			KeyValueAPI.put("basin", "basin576095","Mary", "#QUIT");
			KeyValueAPI.put("basin", "basin576095","Fred", "#QUIT");
			
			KeyValueAPI.put("basin", "basin576095","Jim", "#QUIT2");
			KeyValueAPI.put("basin", "basin576095","Tom", "#QUIT2");
			KeyValueAPI.put("basin", "basin576095","Mary", "#QUIT2");
			KeyValueAPI.put("basin", "basin576095","Fred", "#QUIT2");*/
			String checkString1 = KeyValueAPI.get("basin", "basin576095","Jim");
			String checkString2 = KeyValueAPI.get("basin", "basin576095","Tom");
			String checkString3 = KeyValueAPI.get("basin", "basin576095","Mary");
			String checkString4 = KeyValueAPI.get("basin", "basin576095","Fred");
			
			
			if(!checkString1.substring(0, 1).equals("#")){
			KeyValueAPI.put("basin", "basin576095","Jim", "#QUIT");}
			if(!KeyValueAPI.get("basin", "basin576095","Tom").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Tom", "#QUIT");}
			if(!KeyValueAPI.get("basin", "basin576095","Mary").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Mary", "#QUIT");}
			if(!KeyValueAPI.get("basin", "basin576095","Fred").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Fred", "#QUIT");}
			if(!KeyValueAPI.get("basin", "basin576095","Jim").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Jim", "#QUIT2");}
			if(!KeyValueAPI.get("basin", "basin576095","Tom").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Tom", "#QUIT2");}
			if(!KeyValueAPI.get("basin", "basin576095","Mary").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Mary", "#QUIT2");}
			if(!KeyValueAPI.get("basin", "basin576095","Fred").equals("#OWN")){
			KeyValueAPI.put("basin", "basin576095","Fred", "#QUIT2");}
			System.out.println(" quits");
			return null;
		}
    	
    }
	


}
