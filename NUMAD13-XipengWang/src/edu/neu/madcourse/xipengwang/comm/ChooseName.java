package edu.neu.madcourse.xipengwang.comm;

import java.security.PublicKey;
import java.util.Stack;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import edu.neu.madcourse.xipengwang.dabble.BGMManager;
import edu.neu.madcourse.xipengwang.dabble.Game;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;
import edu.neu.mhealth.api.KeyValueAPI;


public class ChooseName extends Activity{
	private boolean nameIsValid;
	private boolean oppIsValid;
	private boolean musicGoOn;
	private boolean musicGoOn2;
	private EditText myFakeNameET;
	private TextView chooseNameText;

	private Button userListButton  ;
	

	 AlertDialog alertDialog;
	    AlertDialog.Builder alertDialogBuilder;
	    CheckNetWorkTask checkNetWorkTask;


    NameValidTask nameValidTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		
		musicGoOn = true;
		   Intent intent = getIntent();
			int musicSt = Integer.parseInt(intent.getStringExtra("music_stuate"));
			
			if(musicSt==1){
				musicGoOn2 = false;
				
			}
			else{
				musicGoOn2 = true;
				
			}
		setContentView(R.layout.comm_choosename); 
        
		//progressBar.setBackgroundColor(getResources().getColor(R.color.white));

		 chooseNameText =(TextView)findViewById(R.id.NameText);
		 myFakeNameET = (EditText)findViewById(R.id.myFakeNameET);
		 userListButton = (Button)findViewById(R.id.userListButton);
		 userListButton.setOnClickListener(new UserListButton());


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
	              Thread.sleep(1500);  
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
				alertDialogBuilder=new AlertDialog.Builder(ChooseName.this);  
				
				alertDialogBuilder.setTitle("Opps, cannot conncet to server.")

	            .setMessage("Please check your network.")

	                 .setPositiveButton("Quit",   new DialogInterface.OnClickListener(){
	                 public void onClick(DialogInterface dialoginterface, int i){
	                	 setResult(RESULT_OK);
	                	 finish();
	                	 
	                 }
	         })
	          .setNegativeButton("Try Again",   new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialoginterface, int i){
                	 setResult(RESULT_CANCELED);
                	 onResume();
                 }
         });
				alertDialog = alertDialogBuilder.show(); 
			}
		}
		
		
		
	}
	    

	
		class UserListButton implements android.view.View.OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String fakeNameString = myFakeNameET.getText().toString();
				if(fakeNameString.isEmpty()){
					Toast.makeText(ChooseName.this,"User name can not be empty!",Toast.LENGTH_LONG).show();  
				}else {
					if(fakeNameString.substring(0, 0).equals("!")||
							fakeNameString.substring(0, 0).equals("?")||
							fakeNameString.substring(0, 0).equals("~")||
							fakeNameString.substring(0, 0).equals("`")||
							fakeNameString.substring(0, 0).equals("@")||
							fakeNameString.substring(0, 0).equals("#")||
							fakeNameString.substring(0, 0).equals("$")||
							fakeNameString.substring(0, 0).equals("^")||
							fakeNameString.substring(0, 0).equals("&")||
							fakeNameString.substring(0, 0).equals("*")||
							fakeNameString.substring(0, 0).equals("(")||
							fakeNameString.substring(0, 0).equals(")")||
							fakeNameString.substring(0, 0).equals("-")||
							fakeNameString.substring(0, 0).equals("=")||
							fakeNameString.substring(0, 0).equals("+")||
							fakeNameString.substring(0, 0).equals("<")||
							fakeNameString.substring(0, 0).equals(">")||
							fakeNameString.substring(0, 0).equals(";")||
							fakeNameString.substring(0, 0).equals(":")||
							fakeNameString.substring(0, 0).equals("'")||
							fakeNameString.substring(0, 0).equals("[")||
							fakeNameString.substring(0, 0).equals("]")||
							fakeNameString.substring(0, 0).equals("{")||
							fakeNameString.substring(0, 0).equals("}")){
						Toast.makeText(ChooseName.this,"Invalid user name: use a-z and 0-9 to create your user name.",Toast.LENGTH_LONG).show();  
					}
					else {
						nameValidTask = new NameValidTask(ChooseName.this);
						nameValidTask.execute(fakeNameString);
						Toast.makeText(ChooseName.this,"Searching for other availabe players",Toast.LENGTH_LONG).show();
					}
					
				}
			}
			
		}

		class NameValidTask extends AsyncTask<String, Integer, Boolean> {  
	    	
	    	private Context context;  
	    	NameValidTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
	    

	        @Override  
	        protected Boolean doInBackground(String... params) {  
	        	String checkString1 =KeyValueAPI.get("basin", "basin576095", "Jim");
	        	String checkString2 =KeyValueAPI.get("basin", "basin576095", "Tom");
	        	String checkString3 =KeyValueAPI.get("basin", "basin576095", "Mary");
	        	String checkString4 =KeyValueAPI.get("basin", "basin576095", "Fred");
	        	
	        	if(checkString1.equals("Error: No Such Key")||checkString1.equals("#QUIT")||checkString1.equals("#QUIT2")){
	        		KeyValueAPI.put("basin", "basin576095", "Jim",params[0]);
	        		OppNameMyName.myFakeName=params[0];
	        		OppNameMyName.myName="Jim";
	        		
	        		return true;
	        	}
	        	else {
	        		if(checkString2.equals("Error: No Such Key")||checkString2.equals("#QUIT")||checkString2.equals("#QUIT2")){
		        		KeyValueAPI.put("basin", "basin576095", "Tom",params[0]);
		        		OppNameMyName.myFakeName=params[0];
		        		OppNameMyName.myName="Tom";
		        		
		        		return true;
		        	}
	        		else {
	        			if(checkString3.equals("Error: No Such Key")||checkString3.equals("#QUIT")||checkString3.equals("#QUIT2")){
			        		KeyValueAPI.put("basin", "basin576095", "Mary",params[0]);
			        		OppNameMyName.myFakeName=params[0];
			        		OppNameMyName.myName="Mary";
			        		
			        		return true;
			        	}
	        			else {
	        				if(checkString4.equals("Error: No Such Key")||checkString4.equals("#QUIT")||checkString4.equals("#QUIT2")){
				        		KeyValueAPI.put("basin", "basin576095", "Fred",params[0]);
				        		OppNameMyName.myFakeName=params[0];
				        		OppNameMyName.myName="Fred";
				        		
				        		return true;
				        	}
	        				else {
								return false;
							}
						}
	        			
	        			
					}
					
				}

	        }  
	 
	        @Override  
	        protected void onCancelled() {  
	            super.onCancelled();  
	        }  
	 
	        @Override  
	        protected void onPostExecute(Boolean results) {  
	        	if(results==true){
	        	finish();
	        	
	        	Intent intent = new Intent(ChooseName.this, OnlineUsers.class);
	        	intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
	        	startActivity(intent);}
	        	else {
	        	Toast.makeText(context,"Server is full, please wait for a while.",Toast.LENGTH_LONG).show();  
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

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			checkNetWorkTask.cancel(true);
			if(!musicGoOn)
	            BGMManager.pause();
			
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			checkNetWorkTask = new CheckNetWorkTask(this);
			checkNetWorkTask.execute();
			if(musicGoOn2==true){
				musicGoOn=false;
		      BGMManager.start(this,R.raw.game);}
				else {
					
				}
		}



}
