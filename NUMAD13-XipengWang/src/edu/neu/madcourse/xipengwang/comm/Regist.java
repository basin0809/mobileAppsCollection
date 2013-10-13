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
	private Button connectGame, stopConn;
	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;
    
    CheckNameTask checkNameTask;
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
		stopConn.startAnimation(alphaDes);
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
        	if(checkString.equals("Error: No Such Key")){
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
                    	reStrings[1] = "Error: No Such Key";
                    	return reStrings;
                    }  
                } 
                KeyValueAPI.clearKey("basin", "basin576095", params[0][0]);
                reStrings[0] = params[0][0];
            	reStrings[1] = "Error: No Such Key";
            	return reStrings;
        		
        	}
        	else {
        		reStrings[0] = params[0][0];
            	reStrings[1] = "Error: No Such Key";
            	return reStrings;
			}
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
        @Override  
        protected void onPostExecute(String[] results) {  
           
        	System.out.println(results[0]+" vs: "+results[1]);
           if(results[1].equals("Error: No Such Key")){
        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white)); 
        	   progressBar.startAnimation(alphaDes);
        	   System.out.println("Opponent is not found or name is used");
        	   
        	   Toast.makeText(context,"Opponent is not found or name is used",Toast.LENGTH_LONG).show();  
        	   nameIsValid = false;
        	
           }
           else {
        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white));  
        	   progressBar.startAnimation(alphaDes);
        	   System.out.println("Opponent Connected! Loading Game");
        	   Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_SHORT).show();  
        	   nameIsValid = true;
 		      	Intent intent = new Intent(Regist.this, CommGame.class);
 		      	intent.putExtra("me", results[0]);
 		      	intent.putExtra("opponent", results[1]);
 		      	startActivity(intent);
		}	
            
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
