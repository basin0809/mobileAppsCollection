package edu.neu.madcourse.xipengwang.comm;

import java.security.PublicKey;
import java.util.Stack;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Picture;
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
import edu.neu.madcourse.xipengwang.comm.OnlineUsers.PullOthersTask.SubPullOthersTask;
import edu.neu.madcourse.xipengwang.dabble.BGMManager;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;


import edu.neu.mhealth.api.KeyValueAPI;


public class OnlineUsers extends Activity{

	private TextView oppNameText;
	private ProgressBar progressBar;
	private Button buttonN1, buttonN2, buttonN3;
	private String buttonN1RealString, buttonN2RealString, buttonN3RealString;
	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;
    
    private boolean musicGoOn;
    private boolean musicGoOn2;

 
    PullOthersTask pullOthersTask;
    SubPullOthersTask subPullOthersTask;
    CheckNameTask checkNameTask;
    AcceptTask acceptTask;
    RejectTask rejectTask;
    //PullMeTask pullMeTask;
    PullMeAfterCancelTask pullMeAfterCancelTask;
    ProgessDialogCancelTask progessDialogCancelTask;
    NotiyDialogCancelTask notiyDialogCancelTask;
    
    ProgressDialog pdialog;  
    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;
    
    AlertDialog alertDialog2;
    AlertDialog.Builder alertDialogBuilder2;
    
    CheckNetWorkTask checkNetWorkTask;
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
		setContentView(R.layout.comm_online); 
        alphaDes = new AlphaAnimation(0.3f, 0.3f);
        alphaInc = new AlphaAnimation(1.0f, 1.0f);
        alphaDes.setDuration(100);
        alphaInc.setDuration(100);
        alphaDes.setFillAfter(true);
        alphaInc.setFillAfter(true);
        oppNameText = (TextView)findViewById(R.id.NameTextN);

		
		//progressBar.setBackgroundColor(getResources().getColor(R.color.white));
		 buttonN1 = (Button)findViewById(R.id.ButtonN1);
		 buttonN2 = (Button)findViewById(R.id.ButtonN2);
		 buttonN3 = (Button)findViewById(R.id.ButtonN3);
		 if(OppNameMyName.myName.equals("Jim")){

			 buttonN1RealString = "Tom";
			 buttonN2RealString = "Mary";
			 buttonN3RealString = "Fred";
 
		 }else {
			 if(OppNameMyName.myName.equals("Tom")){

				 buttonN1RealString = "Jim";
				 buttonN2RealString = "Mary";
				 buttonN3RealString = "Fred";
				 
			 }else {
				 if(OppNameMyName.myName.equals("Mary")){

					 buttonN1RealString = "Jim";
					 buttonN2RealString = "Tom";
					 buttonN3RealString = "Fred";
					 
				 }else {
					 buttonN1RealString = "Jim";
					 buttonN2RealString = "Tom";
					 buttonN3RealString = "Mary";
				}
			}
		}
		 
		 buttonN2.startAnimation(alphaDes);
		 buttonN1.startAnimation(alphaDes);
		 buttonN3.startAnimation(alphaDes);
		
		 

	}
	
		
		@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		checkNetWorkTask.cancel(true);
		if(!musicGoOn)
            BGMManager.pause();
		System.out.println("-----------------------Pause--------------------------");
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
		pullOthersTask = new PullOthersTask(this);
		pullOthersTask.execute();
		pullMeAfterCancelTask = new PullMeAfterCancelTask(this);
		pullMeAfterCancelTask.execute();
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
					alertDialogBuilder2=new AlertDialog.Builder(OnlineUsers.this);  
					
					alertDialogBuilder2.setTitle("Opps, cannot conncet to server.")

		            .setMessage("Please check your network and then restart the application.")

		            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
		                 public void onClick(DialogInterface dialoginterface, int i){
		                	 setResult(RESULT_OK);
		                	 finish();
		                 }
		         });
					alertDialog2 = alertDialogBuilder2.show(); 
				}
			}
			
			
			
		}
		    

		
		class OpponentButtonListener implements android.view.View.OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				if(id==buttonN1.getId()){
					
					pdialog = new ProgressDialog(OnlineUsers.this);
					pdialog.setTitle("Inviting "+buttonN1.getText().toString());
					pdialog.setMessage("Please wait for "+buttonN1.getText().toString()+"'s response.");
					pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pdialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
									progessDialogCancelTask = new ProgessDialogCancelTask(OnlineUsers.this);
									progessDialogCancelTask.execute();
									//pullMeAfterCancelTask = new PullMeAfterCancelTask(OnlineUsers.this);
									//System.out.println("Second Task Starts!");
									//pullMeAfterCancelTask.execute();
								}
							});
					pdialog.show();
					checkNameTask = new CheckNameTask(OnlineUsers.this);
					String passString[] = new String[2];
					passString[0]= buttonN1RealString;
					passString[1]= buttonN1.getText().toString();
					checkNameTask.execute(passString);

				}
				if(id==buttonN2.getId()){
					pdialog = new ProgressDialog(OnlineUsers.this);
					pdialog.setTitle("Inviting "+buttonN2.getText().toString());
					pdialog.setMessage("Please wait for "+buttonN2.getText().toString()+"'s response.");
					pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pdialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
									progessDialogCancelTask = new ProgessDialogCancelTask(OnlineUsers.this);
									progessDialogCancelTask.execute();
									
								}
							});
					pdialog.show();
					checkNameTask = new CheckNameTask(OnlineUsers.this);
					String passString2[] = new String[2];
					passString2[0]= buttonN2RealString;
					passString2[1]= buttonN2.getText().toString();
					checkNameTask.execute(passString2);
				}
				if(id==buttonN3.getId()){
					pdialog = new ProgressDialog(OnlineUsers.this);
					pdialog.setTitle("Inviting "+buttonN3.getText().toString());
					pdialog.setMessage("Please wait for "+buttonN3.getText().toString()+"'s response.");
					pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pdialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
									progessDialogCancelTask = new ProgessDialogCancelTask(OnlineUsers.this);
									progessDialogCancelTask.execute();
									
								}
							});
					pdialog.show();
					checkNameTask = new CheckNameTask(OnlineUsers.this);
					String passString3[] = new String[2];
					passString3[0]= buttonN3RealString;
					passString3[1]= buttonN3.getText().toString();
					checkNameTask.execute(passString3);
				}
			}

		
		}

		class PullMeAfterCancelTask extends AsyncTask<Void, Integer, String>{
			private Context context;  
			PullMeAfterCancelTask(Context context) {  
		          this.context = context;  
		          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
		         // progressBar.startAnimation(alphaInc);
		          
		      }  
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
        		//KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#OWN");
	        	if(OppNameMyName.myName.equals("Jim")){
	        	int i=0; 
	        	String pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Jim");
                while(!pullMeTaskString.equals("#QUIT")&&!pullMeTaskString.equals("#QUIT2")){  
	                	//System.out.println("PullMMMMMMEEEEEEETASK");
	                	//System.out.println("PullMeSubTask----------------------------");
			    		//System.out.println("My Status: "+KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName));
			    		//System.out.println("Tom's Status: "+KeyValueAPI.get("basin", "basin576095", "Tom"));
			    		//System.out.println("Mary's Status: "+KeyValueAPI.get("basin", "basin576095", "Mary"));
			    		//System.out.println("Fred's Status: "+KeyValueAPI.get("basin", "basin576095", "Fred"));
	                    i++;  
	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Tom");
	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
	    	        	
	    					if(resString1.equals("@Jim")){
	    						//System.out.println("PullMMMMMMEEEEEEETASK");
	    						return buttonN1RealString;
	    					}else {
	    						if(resString2.equals("@Jim")){
	        						
	        						return buttonN2RealString;
	        					}else {
	        						if(resString3.equals("@Jim")){
	        							return buttonN3RealString;
	        						}
	        						else{
	        							try {  
	    	     	                        Thread.sleep(100);  
	    	     	                    } catch (InterruptedException e) { 
	    	     	                    	
	    	     	                    	return "NE";
	    	     	                    }
	        						}
	        					}
	    					}
	    					pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Jim");	
	                	}
	                return "NE";
	                }
	        	else {
	        		if(OppNameMyName.myName.equals("Tom")){
	    	        	int i=0; 	        		
	    	        	String pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Tom");
	                    while(!pullMeTaskString.equals("#QUIT")&&!pullMeTaskString.equals("#QUIT2")){  
	    	                    i++;  
	    	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
	    	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
	    	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
	    	    	        	
	    	    					if(resString1.equals("@Tom")){
	    	    						
	    	    						return buttonN1RealString;
	    	    					}else {
	    	    						if(resString2.equals("@Tom")){
	    	        						
	    	        						return buttonN2RealString;
	    	        					}else {
	    	        						if(resString3.equals("@Tom")){
	    	        							return buttonN3RealString;
	    	        						}
	    	        						else{
	    	        							try {  
	    	    	     	                        Thread.sleep(100);  
	    	    	     	                    } catch (InterruptedException e) { 
	    	    	     	                    	
	    	    	     	                    	return "NE";
	    	    	     	                    }
	    	        						}
	    	        					}
	    	    					}
	    	    					pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Tom");	
	    	                	}
	    	                return "NE";
	    	                }
	        			else {
	        				if(OppNameMyName.myName.equals("Mary")){
	    	    	        	int i=0; 	        		
	    	    	        	String pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Mary");
	    	                    while(!pullMeTaskString.equals("#QUIT")&&!pullMeTaskString.equals("#QUIT2")){  
	    	    	                    i++;  
	    	    	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
	    	    	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
	    	    	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
	    	    	    	        	
	    	    	    					if(resString1.equals("@Mary")){
	    	    	    						
	    	    	    						return buttonN1RealString;
	    	    	    					}else {
	    	    	    						if(resString2.equals("@Mary")){
	    	    	        						
	    	    	        						return buttonN2RealString;
	    	    	        					}else {
	    	    	        						if(resString3.equals("@Mary")){
	    	    	        							return buttonN3RealString;
	    	    	        						}
	    	    	        						else{
	    	    	        							try {  
	    	    	    	     	                        Thread.sleep(100);  
	    	    	    	     	                    } catch (InterruptedException e) { 
	    	    	    	     	                    	
	    	    	    	     	                    	return "NE";
	    	    	    	     	                    }
	    	    	        						}
	    	    	        					}
	    	    	    					}
	    	    	    					pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Mary");
	    	    	                	}
	    	    	                return "NE";
	    	    	                }
	        				else {
	        					if(OppNameMyName.myName.equals("Fred")){
		    	    	        	int i=0; 	        		
		    	    	        	String pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Fred");
		    	                    while(!pullMeTaskString.equals("#QUIT")&&!pullMeTaskString.equals("#QUIT2")){  
		    	    	                    i++;  
		    	    	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
		    	    	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
		    	    	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Mary");
		    	    	    	        	
		    	    	    					if(resString1.equals("@Fred")){
		    	    	    						
		    	    	    						return buttonN1RealString;
		    	    	    					}else {
		    	    	    						if(resString2.equals("@Fred")){
		    	    	        						
		    	    	        						return buttonN2RealString;
		    	    	        					}else {
		    	    	        						if(resString3.equals("@Fred")){
		    	    	        							return buttonN3RealString;
		    	    	        						}
		    	    	        						else{
		    	    	        							try {  
		    	    	    	     	                        Thread.sleep(100);  
		    	    	    	     	                    } catch (InterruptedException e) { 
		    	    	    	     	                    	
		    	    	    	     	                    	return "NE";
		    	    	    	     	                    }
		    	    	        						}
		    	    	        					}
		    	    	    					}
		    	    	    					pullMeTaskString = KeyValueAPI.get("basin", "basin576095", "Fred");
		    	    	                	}
		    	    	                return "NE";
		    	    	                }
	        						else{
	        							return "NE";
	        						}
								}
	        				}
						}
					}
			
		    @Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(!result.equals("NE")){
					if(result.equals(buttonN1RealString)){
						//System.out.println("SubPullOthersTask-------------creat dialog");
						
						
	                	//pullOthersTask.cancel(true);
						
						alertDialogBuilder=new AlertDialog.Builder(OnlineUsers.this);  
						
						alertDialogBuilder.setTitle(buttonN1.getText().toString()+"'s invitation")

			            .setMessage(buttonN1.getText().toString()+" invites you to play Dabble!")

			            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
			                 public void onClick(DialogInterface dialoginterface, int i){
			                	 setResult(RESULT_OK);
			                	 OppNameMyName.oppName=buttonN1RealString;
			                	 OppNameMyName.oppFakeName = buttonN1.getText().toString();
			                	 
			                	 acceptTask = new AcceptTask(OnlineUsers.this);
			                	 acceptTask.execute(buttonN1RealString);
			                	 
			                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
			        
			       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
			       		     intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
			       		  intent.putExtra("masterOrGuest", "guest");
			       		      	startActivity(intent);
			                	 
			                	finish();
			                 }
			         });
						alertDialog = alertDialogBuilder.show();
						notiyDialogCancelTask = new NotiyDialogCancelTask(OnlineUsers.this);
						notiyDialogCancelTask.execute(buttonN1RealString);
					}else {
						if (result.equals(buttonN2RealString)) {
							//System.out.println("SubPullOthersTask-------------creat dialog");
							
							
		                	//pullOthersTask.cancel(true);
							alertDialogBuilder = new AlertDialog.Builder(OnlineUsers.this);  
							
							alertDialogBuilder.setTitle(buttonN2.getText().toString()+"'s invitation")

				            .setMessage(buttonN2.getText().toString()+" invites you to play Dabble!")

				            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
				                 public void onClick(DialogInterface dialoginterface, int i){
				                	 setResult(RESULT_OK);
				                	 OppNameMyName.oppName=buttonN2RealString;
				                	 OppNameMyName.oppFakeName=buttonN2.getText().toString();
				                	 acceptTask = new AcceptTask(OnlineUsers.this);
				                	 acceptTask.execute(buttonN2RealString);
				                	 
				                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
				        
				       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
				       		     intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
				       		  intent.putExtra("masterOrGuest", "guest");
				       		      	startActivity(intent);
				                	 
				                	finish();
				                 }
				         });
							alertDialog = alertDialogBuilder.show();
							notiyDialogCancelTask = new NotiyDialogCancelTask(OnlineUsers.this);
							notiyDialogCancelTask.execute(buttonN2RealString);
						}else {
							if (result.equals(buttonN3RealString)) {
								//System.out.println("SubPullOthersTask-------------creat dialog");
								
								
			                	//pullOthersTask.cancel(true);
								alertDialogBuilder = new AlertDialog.Builder(OnlineUsers.this);  
								
								alertDialogBuilder.setTitle(buttonN3.getText().toString()+"'s invitation")

					            .setMessage(buttonN3.getText().toString()+" invites you to play Dabble!")

					            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
					                 public void onClick(DialogInterface dialoginterface, int i){
					                	 setResult(RESULT_OK);
					                	 OppNameMyName.oppName=buttonN3RealString;
					                	 OppNameMyName.oppFakeName=buttonN3.getText().toString();
					                	 acceptTask = new AcceptTask(OnlineUsers.this);
					                	 acceptTask.execute(buttonN3RealString);
					                	 
					                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
					        
					       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
					       		     intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
					       		  intent.putExtra("masterOrGuest", "guest");
					       		      	startActivity(intent);
					                	 
					                	finish();
					                 }
					         });
								alertDialog = alertDialogBuilder.show();
								notiyDialogCancelTask = new NotiyDialogCancelTask(OnlineUsers.this);
								notiyDialogCancelTask.execute(buttonN3RealString);
							}else {
							
							}
						}
					}
				}
			}
			
		}
		class PullOthersTask extends AsyncTask<Void, Integer, Void> {  
			
			private Context context;  
			PullOthersTask(Context context) {  
		          this.context = context;  
		          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
		         // progressBar.startAnimation(alphaInc);
		          
		      }  


		    @Override  
		    protected Void doInBackground(Void... params) {  
		    	System.out.println("Excute PullOthersTask");
		    	//String oppQuitStatuString =KeyValueAPI.get("basin", "basin576095", oppName);
		    	//KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.oppName);
		    	String pullOthersString = KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName);
		    	while(!pullOthersString.equals("#QUIT")&&
		    			!pullOthersString.equals("#QUIT2")){
		    		//System.out.println("PullOthersSubTask----------------------------");
		    		//System.out.println("My Status: "+KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName));
		    		//System.out.println("Tom's Status: "+KeyValueAPI.get("basin", "basin576095", "Tom"));
		    		//System.out.println("Mary's Status: "+KeyValueAPI.get("basin", "basin576095", "Mary"));
		    		//System.out.println("Fred's Status: "+KeyValueAPI.get("basin", "basin576095", "Fred"));
                    
    	        	//String resString1=KeyValueAPI.get("basin", "basin576095", "Tom");
    	        	//String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
    	        	//String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
		    		subPullOthersTask = new SubPullOthersTask(OnlineUsers.this);
		    		subPullOthersTask.execute();
		    		
		    		try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
		    		pullOthersString = KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName);
		    	}

		    	//KeyValueAPI.put("basin", "basin576095", myName, params[0]);
		    	return null;
		    }  

		    @Override  
		    protected void onCancelled() {  
		        super.onCancelled();  
		    }  



		    @Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//System.out.println("your movement: "+result);
			}


			@Override  
		    protected void onPreExecute() {  
		       
		        
		    } 
			
			

		    @Override  
		    protected void onProgressUpdate(Integer... values) {  
		        
		    	 //System.out.println("onProgressUpdate"+values[0]);  
		        
		    	// progressDialog.setProgress(values[0]);
		          
		    }  
		    class SubPullOthersTask extends AsyncTask<Void, Integer, String[]> {  
		    	
		    	private Context context;  
		    	SubPullOthersTask(Context context) {  
		              this.context = context;  
		          }  
		    
		        @Override  
		        protected String[] doInBackground(Void... params) { 
		        	String[] resStrings = new String[3];
		        	if(OppNameMyName.myName.equals("Jim")){
		        	String resString1=KeyValueAPI.get("basin", "basin576095", "Tom");
		        	if(!resString1.equals("#QUIT")&&!resString1.equals("#QUIT2")){
		        		
		        		if(!resString1.substring(0, 1).equals("#")){
		        			if (resString1.substring(0, 1).equals("@")) {
		        				resStrings[0]="@";
							}
		        			else{
		        			resStrings[0]=resString1;
		        			}
		        		}else {
		        			if(resString1.equals("#REJ")){
		        			resStrings[0]="#REJ";}
		        			else {
		        				resStrings[0]="#AFK/LOS";
							}
						}
		        		
		        		
		        	}else {
		        		resStrings[0]="#QUIT";

					}
		        	
		        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
		        	if(!resString2.equals("#QUIT")&&!resString2.equals("#QUIT2")&&!resString2.substring(0, 1).equals("@")){
		        		if(!resString2.substring(0, 1).equals("#")){
		        			if (resString2.substring(0, 1).equals("@")) {
		        				resStrings[1]="@";
							}
		        			else{
		        			resStrings[1]=resString2;
		        			}
		        		}else {
		        			if(resString2.equals("#REJ")){
			        			resStrings[1]="#REJ";}
			        			else {
			        				resStrings[1]="#AFK/LOS";
								}
						}
		        		
		        		
		        	}else {
		        		resStrings[1]="#QUIT";

					}
		        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
		        	if(!resString3.equals("#QUIT")&&!resString3.equals("#QUIT2")&&!resString3.substring(0, 1).equals("@")){
		        		if(!resString3.substring(0, 1).equals("#")){
		        			if (resString3.substring(0, 1).equals("@")) {
		        				resStrings[2]="@";
							}
		        			else{
		        			resStrings[2]=resString3;
		        			}
		        		}else {
		        			if(resString3.equals("#REJ")){
			        			resStrings[2]="#REJ";}
			        			else {
			        				resStrings[2]="#AFK/LOS";
								}
						}
		        		
		        		
		        	}else {
		        		resStrings[2]="#QUIT";

					}
		        	
		        	return resStrings;}
		        	
		        	if(OppNameMyName.myName.equals("Tom")){
		        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
		        	if(!resString1.equals("#QUIT")&&!resString1.equals("#QUIT2")&&!resString1.substring(0, 1).equals("@")){
		        		if(!resString1.substring(0, 1).equals("#")){
		        			if (resString1.substring(0, 1).equals("@")) {
		        				resStrings[0]="@";
							}
		        			else{
		        			resStrings[0]=resString1;
		        			}
		        		}else {
		        			if(resString1.equals("#REJ")){
			        			resStrings[0]="#REJ";}
			        			else {
			        				resStrings[0]="#AFK/LOS";
								}
						}
		        		
		        		
		        	}else {
		        		resStrings[0]="#QUIT";

					}
		        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
		        	if(!resString2.equals("#QUIT")&&!resString2.equals("#QUIT2")&&!resString2.substring(0, 1).equals("@")){
		        		if(!resString2.substring(0, 1).equals("#")){
		        			if (resString2.substring(0, 1).equals("@")) {
		        				resStrings[1]="@";
							}
		        			else{
		        			resStrings[1]=resString2;
		        			}
		        		}else {
		        			if(resString2.equals("#REJ")){
			        			resStrings[1]="#REJ";}
			        			else {
			        				resStrings[1]="#AFK/LOS";
								}
						}
		        		
		        		
		        	}else {
		        		resStrings[1]="#QUIT";

					}
		        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
		        	if(!resString3.equals("#QUIT")&&!resString3.equals("#QUIT2")&&!resString3.substring(0, 1).equals("@")){
		        		if(!resString3.substring(0, 1).equals("#")){
		        			if (resString3.substring(0, 1).equals("@")) {
		        				resStrings[2]="@";
							}
		        			else{
		        			resStrings[2]=resString3;
		        			}
		        		}else {
		        			if(resString3.equals("#REJ")){
			        			resStrings[2]="#REJ";}
			        			else {
			        				resStrings[2]="#AFK/LOS";
								}
						}
		        		
		        		
		        	}else {
		        		resStrings[2]="#QUIT";

					}
		        	
		        	return resStrings;}
		        	
		        	if(OppNameMyName.myName.equals("Mary")){
			        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
			        	if(!resString1.equals("#QUIT")&&!resString1.equals("#QUIT2")&&!resString1.substring(0, 1).equals("@")){
			        		if(!resString1.substring(0, 1).equals("#")){
			        			if (resString1.substring(0, 1).equals("@")) {
			        				resStrings[0]="@";
								}
			        			else{
			        			resStrings[0]=resString1;
			        			}
			        		}else {
			        			if(resString1.equals("#REJ")){
				        			resStrings[0]="#REJ";}
				        			else {
				        				resStrings[0]="#AFK/LOS";
									}
							}
			        		
			        		
			        	}else {
			        		resStrings[0]="#QUIT";

						}
			        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
			        	if(!resString2.equals("#QUIT")&&!resString2.equals("#QUIT2")&&!resString2.substring(0, 1).equals("@")){
			        		if(!resString2.substring(0, 1).equals("#")){
			        			if (resString2.substring(0, 1).equals("@")) {
			        				resStrings[1]="@";
								}
			        			else{
			        			resStrings[1]=resString2;
			        			}
			        		}else {
			        			if(resString2.equals("#REJ")){
				        			resStrings[1]="#REJ";}
				        			else {
				        				resStrings[1]="#AFK/LOS";
									}
							}
			        		
			        		
			        	}else {
			        		resStrings[1]="#QUIT";

						}
			        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
			        	if(!resString3.equals("#QUIT")&&!resString3.equals("#QUIT2")&&!resString3.substring(0, 1).equals("@")){
			        		if(!resString3.substring(0, 1).equals("#")){
			        			if (resString3.substring(0, 1).equals("@")) {
			        				resStrings[2]="@";
								}
			        			else{
			        			resStrings[2]=resString3;
			        			}
			        		}else {
			        			if(resString3.equals("#REJ")){
				        			resStrings[2]="#REJ";}
				        			else {
				        				resStrings[2]="#AFK/LOS";
									}
							}
			        		
			        		
			        	}else {
			        		resStrings[2]="#QUIT";

						}
			        	
			        	return resStrings;}
		        	if(OppNameMyName.myName.equals("Fred")){
			        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
			        	if(!resString1.equals("#QUIT")&&!resString1.equals("#QUIT2")&&!resString1.substring(0, 1).equals("@")){
			        		if(!resString1.substring(0, 1).equals("#")){
			        			if (resString1.substring(0, 1).equals("@")) {
			        				resStrings[0]="@";
								}
			        			else{
			        			resStrings[0]=resString1;
			        			}
			        		}else {
			        			if(resString1.equals("#REJ")){
				        			resStrings[0]="#REJ";}
				        			else {
				        				resStrings[0]="#AFK/LOS";
									}
							}
			        		
			        		
			        	}else {
			        		resStrings[0]="#QUIT";

						}
			        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
			        	if(!resString2.equals("#QUIT")&&!resString2.equals("#QUIT2")&&!resString2.substring(0, 1).equals("@")){
			        		if(!resString2.substring(0, 1).equals("#")){
			        			if (resString2.substring(0, 1).equals("@")) {
			        				resStrings[1]="@";
								}
			        			else{
			        			resStrings[1]=resString2;
			        			}
			        		}else {
			        			if(resString2.equals("#REJ")){
				        			resStrings[1]="#REJ";}
				        			else {
				        				resStrings[1]="#AFK/LOS";
									}
							}
			        		
			        		
			        	}else {
			        		resStrings[1]="#QUIT";

						}
			        	String resString3=KeyValueAPI.get("basin", "basin576095", "Mary");
			        	if(!resString3.equals("#QUIT")&&!resString3.equals("#QUIT2")&&!resString3.substring(0, 1).equals("@")){
			        		if(!resString3.substring(0, 1).equals("#")){
			        			if (resString3.substring(0, 1).equals("@")) {
			        				resStrings[2]="@";
								}
			        			else{
			        			resStrings[2]=resString3;
			        			}
			        		}else {
			        			if(resString3.equals("#REJ")){
				        			resStrings[2]="#REJ";}
				        			else {
				        				resStrings[2]="#AFK/LOS";
									}
							}
			        		
			        		
			        	}else {
			        		resStrings[2]="#QUIT";

						}
			        	
			        	return resStrings;}
		        	return null;
		        }  
		 
		        @Override  
		        protected void onCancelled() {  
		            super.onCancelled();  
		        }  
		 
		        @Override
				protected void onPostExecute(String[] results) {
					// TODO Auto-generated method stub
					super.onPostExecute(results);
					System.out.println("Tom's actuall status: "+results[0]);
					//System.out.println("results[0].substring(0, 0) "+results[0].substring(0, 1));
					if(!results[0].substring(0, 1).equals("#")){
						if(results[0].substring(0, 1).equals("@")||results[0].substring(0, 1).equals("$")||results[0].substring(0, 1).equals("(")){
							
							buttonN1.startAnimation(alphaDes);
							buttonN1.setOnClickListener(null);
						}
						else{
						buttonN1.startAnimation(alphaInc);
						buttonN1.setText(results[0]);
						buttonN1.setOnClickListener(new OpponentButtonListener());
						}
					}else {
						if(results[0].equals("#REJ")){
							buttonN1.startAnimation(alphaInc);
							buttonN1.setOnClickListener(new OpponentButtonListener());
						}
						else{
						//buttonN1.setText("");
						buttonN1.startAnimation(alphaDes);
						buttonN1.setOnClickListener(null);
						}
					}
					
					if(!results[1].substring(0, 1).equals("#")){
						if(results[1].substring(0, 1).equals("@")||results[0].substring(0, 1).equals("$")||results[0].substring(0, 1).equals("(")){
							
							buttonN2.startAnimation(alphaDes);
							buttonN2.setOnClickListener(null);
						}
						else{
						buttonN2.startAnimation(alphaInc);
						buttonN2.setText(results[1]);
						buttonN2.setOnClickListener(new OpponentButtonListener());
						}
					}else {
						if(results[1].equals("#REJ")){
							buttonN2.startAnimation(alphaInc);
							buttonN2.setOnClickListener(new OpponentButtonListener());
						}
						else{
							//buttonN2.setText("");
						buttonN2.startAnimation(alphaDes);
						buttonN2.setOnClickListener(null);
						}
					}
					
					if(!results[2].substring(0, 1).equals("#")){
						if(results[2].substring(0, 1).equals("@")||results[0].substring(0, 1).equals("$")||results[0].substring(0, 1).equals("(")){
							
							buttonN3.startAnimation(alphaDes);
							buttonN3.setOnClickListener(null);
						}
						else{
						buttonN3.startAnimation(alphaInc);
						buttonN3.setText(results[2]);
						buttonN3.setOnClickListener(new OpponentButtonListener());
						}
					}else {
						if(results[2].equals("#REJ")){
							buttonN3.startAnimation(alphaInc);
							buttonN3.setOnClickListener(new OpponentButtonListener());
						}
						else{
							//buttonN3.setText("");
						buttonN3.startAnimation(alphaDes);
						buttonN3.setOnClickListener(null);
						}
					}
										
					
				}

				@Override  
		        protected void onPreExecute() {            
		        }  
		 
		        @Override  
		        protected void onProgressUpdate(Integer... values) {  
		        }  
		 
		     }

		 }	
		class AcceptTask extends AsyncTask<String, Integer, Void>{
			private Context context;  
			AcceptTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          } 
			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "("+params[0]);
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "("+params[0]);
				
				return null;
			}
			
		}
		class RejectTask extends AsyncTask<Void, Integer, Void>{
			private Context context;  
			RejectTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#REJ");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#REJ");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#REJ");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#REJ");
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				finish();
				//pullMeTask = new PullMeTask(OnlineUsers.this);
           	 	//pullMeTask.execute();
			}
			
			
		}
		class CheckNameTask extends AsyncTask<String[], Integer, String> {  
	    	
	    	private Context context;  
	    	CheckNameTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
	    

	        @Override  
	        protected String doInBackground(String[]... params) {  
	        	
	        		
	        		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "@"+params[0][0]);
	        		
	        		int i=0; 	        		
	                while(i<100){  
	                    i++;  
	                    String oppStatus = KeyValueAPI.get("basin", "basin576095", params[0][0]);
	                    //System.out.println("Searching opponent:"+params[0]);	
	                    //System.out.println("Opponent's status:"+oppStatus);
	                    if(oppStatus.equals(OppNameMyName.myName)||oppStatus.equals("("+OppNameMyName.myName)){
	                    	OppNameMyName.oppName = params[0][0];
	                    	OppNameMyName.oppFakeName = params[0][1];
	                    	//System.out.println("Opponent: "+params[0]);	                   
	                    	return "#GO";
	                    }else {
							if (KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals(OppNameMyName.myFakeName)) {
								return "#CAN";
							}
						
	                    else {
	                    	if(oppStatus.equals("#REJ")){
	                    		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);
	                    		//Toast.makeText(context,params[0]+" rejects to play",Toast.LENGTH_LONG).show();
		                    	//System.out.println("Opponent Reject: "+params[0]);	                   
		                    	return "#REJ";
	                    	}
	                    	else {
	                    		 try {  
	     	                        Thread.sleep(100);  
	     	                    } catch (InterruptedException e) { 
	     	                    	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);
	     	                    	return "#EXC";
	     	                    } 
							}
	                    }  }                  
	                }  
	                KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);
	                //Toast.makeText(context,params[0]+" does not repond",Toast.LENGTH_LONG).show();
	                return "#NRES";
	        }
	 
	        @Override  
	        protected void onCancelled() {  
	            super.onCancelled();  
	        }  
	 
	        @Override  
	        protected void onPostExecute(String results) {  

	        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white));  
	        	if(results=="#GO"){
	        	   //progressBar.startAnimation(alphaDes);
	        	   //System.out.println("Opponent Connected! Loading Game");
	        	   Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
	 		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
	 		      	intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
	 		      	intent.putExtra("masterOrGuest", "master");
	 		      	startActivity(intent);
	 		      	finish();}
	        	
	        	else {
	        		if(results=="#REJ"){
			        	   //progressBar.startAnimation(alphaDes);
			        	  // System.out.println("Opp rejects to play");
			        	   Toast.makeText(context,"Invitation rejected.",Toast.LENGTH_LONG).show();  
			 		      	pdialog.dismiss();}
	        		else {
	        			if(results=="#NRES"){
				        	   //progressBar.startAnimation(alphaDes);
				        	  // System.out.println("Opp no respond");
				        	   Toast.makeText(context,"Opponent does not response.",Toast.LENGTH_LONG).show();  
				 		      	pdialog.dismiss();}
	        			else {
							
						}
					}
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
		class ProgessDialogCancelTask extends AsyncTask<String, Integer, Void> {  
	    	
	    	private Context context;  
	    	ProgessDialogCancelTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
	    

	        @Override  
	        protected Void doInBackground(String... params) {  
	        	
	        		
	        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);
	        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);

					return null;

	        }
		}
		
		class NotiyDialogCancelTask extends AsyncTask<String, Integer, Boolean> {  
	    	
	    	private Context context;  
	    	NotiyDialogCancelTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
	    

	        @Override  
	        protected Boolean doInBackground(String... params) {  
	        	
	        	int i=0;
	        	while(i<1000){
	        		i++;
	        		if(!KeyValueAPI.get("basin", "basin576095", params[0]).equals("@"+OppNameMyName.myName)){
	        			return true;
	        		}else {
	        			try {
							Thread.sleep(100);
						} catch (Exception e) {
							// TODO: handle exception
							return false;
						}
					}
	        		
	        	}
				return false;
			

	        }


			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result==true){
					alertDialog.dismiss();
					PullMeAfterCancelTask pullMeTask2 = new PullMeAfterCancelTask(OnlineUsers.this);
					System.out.println("New PullMeTask Starts!");
					pullMeTask2.execute();
				}
			}
		}
}
