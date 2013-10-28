package edu.neu.madcourse.xipengwang.comm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.comm.OnlineUsers.AcceptTask;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;
import edu.neu.mhealth.api.KeyValueAPI;


public class Regist extends Activity{

	

	private ProgressBar progressBar;
	private Button stopConn, menuButton, scoreButton, htuButton,chooseNamebutton  ;
	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;
    
    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;
    
    QuitTask quitTask;
    QuitTask2 quitTask2;
    
    CheckNetWorkTask checkNetWorkTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_reg); 
		//quitTask2 = new QuitTask2(this);
		//quitTask2.execute();
        alphaDes = new AlphaAnimation(1.0f, 0.0f);
        alphaInc = new AlphaAnimation(0.0f, 1.0f);
        alphaDes.setDuration(100);
        alphaInc.setDuration(100);
        alphaDes.setFillAfter(true);
        alphaInc.setFillAfter(true);

		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		 progressBar.startAnimation(alphaDes);
		//System.out.println("ini jim------------>"+KeyValueAPI.get("basin", "basin576095","Jim"));
		//System.out.println("ini tom------------>"+KeyValueAPI.get("basin", "basin576095","Tom"));
		//System.out.println("ini mary------------>"+KeyValueAPI.get("basin", "basin576095","Mary"));
		//System.out.println("ini fred------------>"+KeyValueAPI.get("basin", "basin576095","Fred"));
		//progressBar.setBackgroundColor(getResources().getColor(R.color.white));
		
		
		stopConn = (Button)findViewById(R.id.stopButton);
		menuButton = (Button)findViewById(R.id.mainMenuButton);
		scoreButton = (Button)findViewById(R.id.highScoreButton);
		htuButton = (Button)findViewById(R.id.howToUseButton);
		chooseNamebutton = (Button)findViewById(R.id.chooseNameButton);
		scoreButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				CheckScoreTask checkScoreTask = new CheckScoreTask(Regist.this);
				checkScoreTask.execute();}
				
			
			
		});
		htuButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		      	Intent intent = new Intent(Regist.this, HowToUse.class);
 		      	
 		      	startActivity(intent);
				
			}
			
		});
		chooseNamebutton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		      	Intent intent = new Intent(Regist.this, ChooseName.class);
 		      	
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

		 
	}
	

		
		
	
    @Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			stopConn.startAnimation(alphaDes);
			progressBar.startAnimation(alphaDes);
			stopConn.setOnClickListener(null);
			checkNetWorkTask = new CheckNetWorkTask(this);
			checkNetWorkTask.execute();
			quitTask = new QuitTask(this);
			quitTask.execute();
			stopService(new Intent(this, AsyncPullService.class));
			/*if(KeyValueAPI.isServerAvailable()){
				KeyValueAPI.put("basin", "basin576095", name.getText().toString(), "#QUIT");
			}*/

		}

class CheckNetWorkTask extends AsyncTask<Void, Integer, Boolean>{
	private Context context;  
	
	CheckNetWorkTask(Context context) {  
          this.context = context;  
          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
          
      }  
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		while(KeyValueAPI.isServerAvailable()==true){
			System.out.println("Check NetWork: true");
		 try {  
              Thread.sleep(1000);  
          } 
		 catch (InterruptedException e) { 
          	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);
          	return false;
          } 
		 }
		System.out.println("Check NetWork: false");
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result==false){
			alertDialogBuilder=new AlertDialog.Builder(Regist.this);  
			
			alertDialogBuilder.setTitle("Opps, cannot conncet to server.")

            .setMessage("Please check your network and then restart the application.")

            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialoginterface, int i){
                	 setResult(RESULT_OK);
                	 finish();
                 }
         });
			alertDialog = alertDialogBuilder.show(); 
		}
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
