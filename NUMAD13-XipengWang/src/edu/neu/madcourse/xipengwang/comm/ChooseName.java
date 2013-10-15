package edu.neu.madcourse.xipengwang.comm;

import java.security.PublicKey;
import java.util.Stack;

import android.R.integer;
import android.app.Activity;
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
import edu.neu.madcourse.xipengwang.dabble.Game;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;
import edu.neu.mhealth.api.KeyValueAPI;


public class ChooseName extends Activity{
	private boolean nameIsValid;
	private boolean oppIsValid;
	private EditText name,oppName;
	private TextView chooseNameText;
	private ProgressBar progressBar;
	private Button jimButton, tomButton, maryButton, fredButton ;
	

    


    NameValidTask nameValidTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_choosename); 
        
		//progressBar.setBackgroundColor(getResources().getColor(R.color.white));
		 jimButton = (Button)findViewById(R.id.JimButton);
		 tomButton = (Button)findViewById(R.id.TomButton);
		 maryButton = (Button)findViewById(R.id.MaryButton);
		 fredButton = (Button)findViewById(R.id.FredButton);
		 chooseNameText =(TextView)findViewById(R.id.NameText);
		 jimButton.setOnClickListener(new NameButtonListener());
		 tomButton.setOnClickListener(new NameButtonListener());
		 maryButton.setOnClickListener(new NameButtonListener());
		 fredButton.setOnClickListener(new NameButtonListener());

	}

		
		class NameButtonListener implements android.view.View.OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				if(id==jimButton.getId()){
					nameValidTask = new NameValidTask(ChooseName.this);
					nameValidTask.execute("Jim");
					
				}
				if(id==tomButton.getId()){
					nameValidTask = new NameValidTask(ChooseName.this);
					nameValidTask.execute("Tom");
				}
				if(id==maryButton.getId()){
					nameValidTask = new NameValidTask(ChooseName.this);
					nameValidTask.execute("Mary");
				}
				if(id==fredButton.getId()){
					nameValidTask = new NameValidTask(ChooseName.this);
					nameValidTask.execute("Fred");
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
	        	String checkString =KeyValueAPI.get("basin", "basin576095", params[0]);
	        	
	        	if(checkString.equals("Error: No Such Key")||checkString.equals("#QUIT")||checkString.equals("#QUIT2")){
	        		KeyValueAPI.put("basin", "basin576095", params[0],"#OWN");
	        		OppNameMyName.myName=params[0];
	        		return true;
	        	}
	        	else {
					return false;
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
	        	startActivity(intent);}
	        	else {
	        	Toast.makeText(context,"This name is used by another player",Toast.LENGTH_LONG).show();  
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
