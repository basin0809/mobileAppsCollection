package edu.neu.madcourse.xipengwang.comm;

import java.security.PublicKey;
import java.util.Stack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.dabble.Game;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;
import edu.neu.mhealth.api.KeyValueAPI;


public class Regist extends Activity{
	private boolean nameIsValid;
	private boolean oppIsValid;
	private EditText name,oppName;
	private TextView desp,desp2;
	private ProgressBar progressBar;
	private Button connectGame, stopConn, menuButton, scoreButton, htuButton ;
	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;
    
    CheckNameTask checkNameTask;
    QuitTask quitTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_reg); 
        alphaDes = new AlphaAnimation(1.0f, 0.0f);
        alphaInc = new AlphaAnimation(0.0f, 1.0f);
        alphaDes.setDuration(100);
        alphaInc.setDuration(100);
        alphaDes.setFillAfter(true);
        alphaInc.setFillAfter(true);
		name = (EditText)findViewById(R.id.nameText);
		oppName = (EditText)findViewById(R.id.oppNameText);
		desp = (TextView)findViewById(R.id.descriptText);
		desp2 = (TextView)findViewById(R.id.descriptText2);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		 progressBar.startAnimation(alphaDes);
		
		//progressBar.setBackgroundColor(getResources().getColor(R.color.white));
		
		connectGame = (Button)findViewById(R.id.searchButton);
		stopConn = (Button)findViewById(R.id.stopButton);
		menuButton = (Button)findViewById(R.id.mainMenuButton);
		scoreButton = (Button)findViewById(R.id.highScoreButton);
		htuButton = (Button)findViewById(R.id.howToUseButton);
		scoreButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(name.getText().toString().isEmpty()){
					Toast.makeText(Regist.this,"User name can not be empty!",Toast.LENGTH_LONG).show();  
				}else{
				CheckScoreTask checkScoreTask = new CheckScoreTask(Regist.this);
				checkScoreTask.execute(name.getText().toString());}
				
			}
			
		});
		htuButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		      	Intent intent = new Intent(Regist.this, HowToUse.class);
 		      	
 		      	startActivity(intent);
				
			}
			
		});
		menuButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		connectGame.setOnClickListener(new Button.OnClickListener(){  
	            public void onClick(View v) {  
	                search();  
	                stopConn.startAnimation(alphaInc);
	                stopConn.setOnClickListener(new Button.OnClickListener(){  
	                    public void onClick(View v) {  
	                    	
	                    	checkNameTask.cancel(true);
	                    	stopConn.startAnimation(alphaDes);
	                    	progressBar.startAnimation(alphaDes);
	                    	 stopConn.setOnClickListener(null);
	                    }  
	                });
	            }  
	        }); 
		 
	}
	
		public void search() {
		String[] strings =new String[2];
		strings[0] = name.getText().toString();
		strings[1] = oppName.getText().toString();
			// TODO Auto-generated method stub
		checkNameTask= new CheckNameTask(this);
		checkNameTask.execute(strings);
	}
		
		
	
    @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			oppName.setText("");
			stopConn.startAnimation(alphaDes);
			progressBar.startAnimation(alphaDes);
			stopConn.setOnClickListener(null);
			quitTask = new QuitTask(this);
			quitTask.execute();
			stopService(new Intent(this, AsyncPullService.class));
			/*if(KeyValueAPI.isServerAvailable()){
				KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			}*/

		}

class CheckScoreTask extends AsyncTask<String, Integer, String> {  
    	
    	private Context context;  
    	CheckScoreTask(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
              progressBar.startAnimation(alphaInc);
              
          }  
    

        @Override  
        protected String doInBackground(String... params) {  
        	String checkString =KeyValueAPI.get("basin", "basin576095", params[0]+"@HS");
        	
        	if(checkString.equals("Error: No Such Key")){
        		return "Error: No Such Key";
        	}
        	else {
				return checkString;
			}

        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
        @Override  
        protected void onPostExecute(String results) {  
        	if(results.equals("Error: No Such Key")){
        		HighScoreRecord.highscore="0";
        	}else {
				HighScoreRecord.highscore=results;
			}
        	Intent intent = new Intent(Regist.this, HighScore.class);
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
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT2");
			System.out.println(name.getText().toString()+" quits");
			return null;
		}
    	
    }
	class CheckNameTask extends AsyncTask<String[], Integer, String[]> {  
    	
    	private Context context;  
    	CheckNameTask(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
              progressBar.startAnimation(alphaInc);
              
          }  
    

        @Override  
        protected String[] doInBackground(String[]... params) {  
        	String[] reStrings = new String[2];
        	String checkString =KeyValueAPI.get("basin", "basin576095", params[0][0]);
        	System.out.println("Enter: "+params[0][0]);
        	System.out.println("checkString: "+checkString);
        	if(checkString.equals("Error: No Such Key")||checkString.equals("#QUIT")||checkString.equals("#QUIT2")||checkString.equals("#AFK")){
        		quitTask.cancel(true);
        		KeyValueAPI.put("basin", "basin576095", params[0][0], params[0][1]);
        		

        		int i=0; 
        		
                while(i<100){  
                    i++;  
                    String oppStatus = KeyValueAPI.get("basin", "basin576095", params[0][1]);
                    if(oppStatus.equals(params[0][0])){
                    	System.out.println("Opponent: "+params[0][1]);
                    	
                    	reStrings[0] = params[0][0];
                    	reStrings[1] = params[0][1];
                    	return reStrings;
                    }
                    try {  
                        Thread.sleep(100);  
                    } catch (InterruptedException e) { 
                    	KeyValueAPI.clearKey("basin", "basin576095", params[0][0]);
                    	reStrings[0] = params[0][0];
                    	reStrings[1] = "Conncetion process is interrupted";
                    	return reStrings;
                    }  
                } 
                KeyValueAPI.clearKey("basin", "basin576095", params[0][0]);
                reStrings[0] = params[0][0];
            	reStrings[1] = "Your opponent does not exist or reject to play";
            	return reStrings;
        		
        	}
        	else {
        		reStrings[0] = params[0][0];
            	reStrings[1] = "Your name is used";
            	return reStrings;
			}
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
        @Override  
        protected void onPostExecute(String[] results) {  
        	stopConn.startAnimation(alphaDes);
        	System.out.println(results[0]+" vs: "+results[1]);
           if(results[1].equals("Your opponent does not exist or reject to play")){
        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white)); 
        	   progressBar.startAnimation(alphaDes);
        	   System.out.println("Your opponent does not exist or reject to play");
        	   
        	   Toast.makeText(context,"Your opponent does not exist or reject to play",Toast.LENGTH_LONG).show();  
        	   nameIsValid = false;
        	
           }else{
           if(results[1].equals("Your name is used")){
        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white)); 
        	   progressBar.startAnimation(alphaDes);
        	   System.out.println("Your name is used");
        	   
        	   Toast.makeText(context,"Your name is used",Toast.LENGTH_LONG).show();  
        	   nameIsValid = false;
        	
           }else{
           if(results[1].equals("Conncetion process is interrupted")){
        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white)); 
        	   progressBar.startAnimation(alphaDes);
        	   System.out.println("Conncetion process is interrupted");
        	   
        	   Toast.makeText(context,"Conncetion process is interrupted",Toast.LENGTH_LONG).show();  
        	   nameIsValid = false;
        	
           }
           else {
        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white));  
        	   progressBar.startAnimation(alphaDes);
        	   System.out.println("Opponent Connected! Loading Game");
        	   Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
        	   nameIsValid = true;
 		      	Intent intent = new Intent(Regist.this, CommGame.class);
 		      	OppNameMyName.myName=results[0];
 		      	OppNameMyName.oppName=results[1];
 		      	
 		      	startActivity(intent);
		}}}
            
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

}
