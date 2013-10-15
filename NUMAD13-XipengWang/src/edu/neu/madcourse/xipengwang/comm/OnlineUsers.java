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
import edu.neu.madcourse.xipengwang.comm.OnlineUsers.PullOthersTask.SubPullOthersTask;


import edu.neu.mhealth.api.KeyValueAPI;


public class OnlineUsers extends Activity{

	private TextView oppNameText;
	private ProgressBar progressBar;
	private Button buttonN1, buttonN2, buttonN3;
	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;
    


 
    PullOthersTask pullOthersTask;
    SubPullOthersTask subPullOthersTask;
    CheckNameTask checkNameTask;
    AcceptTask acceptTask;
    RejectTask rejectTask;
    PullMeTask pullMeTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
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
			 buttonN1.setText("Tom");
			 buttonN2.setText("Mary");
			 buttonN3.setText("Fred");
			 
			 
		 }else {
			 if(OppNameMyName.myName.equals("Tom")){
				 buttonN1.setText("Jim");
				 buttonN2.setText("Mary");
				 buttonN3.setText("Fred");
				 
			 }else {
				 if(OppNameMyName.myName.equals("Mary")){
					 buttonN1.setText("Jim");
					 buttonN2.setText("Tom");
					 buttonN3.setText("Fred");
					 
				 }else {
					 buttonN1.setText("Jim");
					 buttonN2.setText("Tom");
					 buttonN3.setText("Mary");
				}
			}
		}
		 
		 buttonN2.startAnimation(alphaDes);
		 buttonN1.startAnimation(alphaDes);
		 buttonN3.startAnimation(alphaDes);
		
		 

	}

		
		@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pullOthersTask = new PullOthersTask(this);
		pullOthersTask.execute();
		pullMeTask = new PullMeTask(this);
		pullMeTask.execute();
	}


		class OpponentButtonListener implements android.view.View.OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				if(id==buttonN1.getId()){
					checkNameTask = new CheckNameTask(OnlineUsers.this);
					checkNameTask.execute(buttonN1.getText().toString());
					
				}
				if(id==buttonN2.getId()){
					checkNameTask = new CheckNameTask(OnlineUsers.this);
					checkNameTask.execute(buttonN2.getText().toString());
				}
				if(id==buttonN3.getId()){
					checkNameTask = new CheckNameTask(OnlineUsers.this);
					checkNameTask.execute(buttonN3.getText().toString());
				}
			}

		
		}
		
		class PullMeTask extends AsyncTask<Void, Integer, String>{
			private Context context;  
			PullMeTask(Context context) {  
		          this.context = context;  
		          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
		         // progressBar.startAnimation(alphaInc);
		          
		      }  
			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
        		
	        	if(OppNameMyName.myName.equals("Jim")){
	        	int i=0; 
	        		
	                while(KeyValueAPI.get("basin", "basin576095", "Jim").equals("#OWN")==true||
	                		KeyValueAPI.get("basin", "basin576095", "Jim").equals("#REJ")==true){  
	                	System.out.println("PullMMMMMMEEEEEEETASK");
	                    i++;  
	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Tom");
	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
	    	        	
	    					if(resString1.equals("@Jim")){
	    						System.out.println("PullMMMMMMEEEEEEETASK");
	    						return "Tom";
	    					}else {
	    						if(resString2.equals("@Jim")){
	        						
	        						return "Mary";
	        					}else {
	        						if(resString3.equals("@Jim")){
	        							return "Fred";
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
	    					
	                	}
	                return "NE";
	                }
	        	else {
	        		if(OppNameMyName.myName.equals("Tom")){
	    	        	int i=0; 	        		
	    	                while(KeyValueAPI.get("basin", "basin576095", "Tom").equals("#OWN")==true||
	    	                		KeyValueAPI.get("basin", "basin576095", "Tom").equals("#REJ")==true){  
	    	                    i++;  
	    	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
	    	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
	    	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
	    	    	        	
	    	    					if(resString1.equals("@Tom")){
	    	    						
	    	    						return "Jim";
	    	    					}else {
	    	    						if(resString2.equals("@Tom")){
	    	        						
	    	        						return "Mary";
	    	        					}else {
	    	        						if(resString3.equals("@Tom")){
	    	        							return "Fred";
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
	    	    					
	    	                	}
	    	                return "NE";
	    	                }
	        			else {
	        				if(OppNameMyName.myName.equals("Mary")){
	    	    	        	int i=0; 	        		
	    	    	                while(KeyValueAPI.get("basin", "basin576095", "Mary").equals("#OWN")==true||
	    	    	                		KeyValueAPI.get("basin", "basin576095", "Mary").equals("#REJ")==true){  
	    	    	                    i++;  
	    	    	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
	    	    	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
	    	    	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
	    	    	    	        	
	    	    	    					if(resString1.equals("@Mary")){
	    	    	    						
	    	    	    						return "Jim";
	    	    	    					}else {
	    	    	    						if(resString2.equals("@Mary")){
	    	    	        						
	    	    	        						return "Tom";
	    	    	        					}else {
	    	    	        						if(resString3.equals("@Mary")){
	    	    	        							return "Fred";
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
	    	    	    					
	    	    	                	}
	    	    	                return "NE";
	    	    	                }
	        				else {
	        					if(OppNameMyName.myName.equals("Fred")){
		    	    	        	int i=0; 	        		
		    	    	                while(KeyValueAPI.get("basin", "basin576095", "Fred").equals("#OWN")==true||
		    	    	                		KeyValueAPI.get("basin", "basin576095", "Fred").equals("#REJ")==true){  
		    	    	                    i++;  
		    	    	    	        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
		    	    	    	        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
		    	    	    	        	String resString3=KeyValueAPI.get("basin", "basin576095", "Mary");
		    	    	    	        	
		    	    	    					if(resString1.equals("@Fred")){
		    	    	    						
		    	    	    						return "Jim";
		    	    	    					}else {
		    	    	    						if(resString2.equals("@Fred")){
		    	    	        						
		    	    	        						return "Tom";
		    	    	        					}else {
		    	    	        						if(resString3.equals("@Fred")){
		    	    	        							return "Mary";
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
					if(result.equals("Jim")){
						System.out.println("SubPullOthersTask-------------creat dialog");
						
						
	                	//pullOthersTask.cancel(true);
						new AlertDialog.Builder(OnlineUsers.this)  
						
			            .setTitle("Jim's invitation")

			            .setMessage("Jim invites you to play Dabble!")

			            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
			                 public void onClick(DialogInterface dialoginterface, int i){
			                	 setResult(RESULT_OK);
			                	 OppNameMyName.oppName="Jim";
			                	 
			                	 acceptTask = new AcceptTask(OnlineUsers.this);
			                	 acceptTask.execute("Jim");
			                	 
			                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
			        
			       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
			       		      	
			       		      	startActivity(intent);
			                	 
			                	finish();
			                 }
			         })
			         .setNegativeButton("Reject",   new DialogInterface.OnClickListener(){
			                 public void onClick(DialogInterface dialoginterface, int i){
			                	 setResult(RESULT_CANCELED);
			                	 rejectTask = new RejectTask(OnlineUsers.this);
			                	 rejectTask.execute();
			                	 finish();
			                	 
			                 }
			         })

			            .show();
					}else {
						if (result.equals("Tom")) {
							System.out.println("SubPullOthersTask-------------creat dialog");
							
							
		                	//pullOthersTask.cancel(true);
							new AlertDialog.Builder(OnlineUsers.this)  
							
				            .setTitle("Tom's invitation")

				            .setMessage("Tom invites you to play Dabble!")

				            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
				                 public void onClick(DialogInterface dialoginterface, int i){
				                	 setResult(RESULT_OK);
				                	 OppNameMyName.oppName="Tom";
				                	 
				                	 acceptTask = new AcceptTask(OnlineUsers.this);
				                	 acceptTask.execute("Tom");
				                	 
				                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
				        
				       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
				       		      	
				       		      	startActivity(intent);
				                	 
				                	finish();
				                 }
				         })
				         .setNegativeButton("Reject",   new DialogInterface.OnClickListener(){
				                 public void onClick(DialogInterface dialoginterface, int i){
				                	 setResult(RESULT_CANCELED);
				                	 rejectTask = new RejectTask(OnlineUsers.this);
				                	 rejectTask.execute();
				                	 finish();
				                	 
				                 }
				         })

				            .show();
						}else {
							if (result.equals("Mary")) {
								System.out.println("SubPullOthersTask-------------creat dialog");
								
								
			                	//pullOthersTask.cancel(true);
								new AlertDialog.Builder(OnlineUsers.this)  
								
					            .setTitle("Mary's invitation")

					            .setMessage("Mary invites you to play Dabble!")

					            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
					                 public void onClick(DialogInterface dialoginterface, int i){
					                	 setResult(RESULT_OK);
					                	 OppNameMyName.oppName="Mary";
					                	 
					                	 acceptTask = new AcceptTask(OnlineUsers.this);
					                	 acceptTask.execute("Mary");
					                	 
					                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
					        
					       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
					       		      	
					       		      	startActivity(intent);
					                	 
					                	finish();
					                 }
					         })
					         .setNegativeButton("Reject",   new DialogInterface.OnClickListener(){
					                 public void onClick(DialogInterface dialoginterface, int i){
					                	 setResult(RESULT_CANCELED);
					                	 rejectTask = new RejectTask(OnlineUsers.this);
					                	 rejectTask.execute();
					                	 finish();
					                	 
					                 }
					         })

					            .show();
							}else {
								if (result.equals("Fred")) {
									System.out.println("SubPullOthersTask-------------creat dialog");
									
									
				                	//pullOthersTask.cancel(true);
									new AlertDialog.Builder(OnlineUsers.this)  
									
						            .setTitle("Fred's invitation")

						            .setMessage("Fred invites you to play Dabble!")

						            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
						                 public void onClick(DialogInterface dialoginterface, int i){
						                	 setResult(RESULT_OK);
						                	 OppNameMyName.oppName="Fred";
						                	 
						                	 acceptTask = new AcceptTask(OnlineUsers.this);
						                	 acceptTask.execute("Fred");
						                	 
						                	 //Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_LONG).show();  
						        
						       		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
						       		      	
						       		      	startActivity(intent);
						                	 
						                	finish();
						                 }
						         })
						         .setNegativeButton("Reject",   new DialogInterface.OnClickListener(){
						                 public void onClick(DialogInterface dialoginterface, int i){
						                	 setResult(RESULT_CANCELED);
						                	 rejectTask = new RejectTask(OnlineUsers.this);
						                	 rejectTask.execute();
						                	 finish();
						                	 
						                 }
						         })

						            .show();
								}
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
		    	
		    	//String oppQuitStatuString =KeyValueAPI.get("basin", "basin576095", oppName);
		    	//KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.oppName);
		    	while(KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("#OWN")==true||
		    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("#REJ")==true||
		    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("@Jim")==true
		    			||
		    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("@Tom")==true
		    			||
		    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("@Mary")==true
		    			||
		    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("@Fred")==true){
		    		System.out.println("PullOthersTask---while loop");
		    		subPullOthersTask = new SubPullOthersTask(OnlineUsers.this);
		    		subPullOthersTask.execute();
		    		
		    		try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
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
		    class SubPullOthersTask extends AsyncTask<Void, Integer, Boolean[]> {  
		    	
		    	private Context context;  
		    	SubPullOthersTask(Context context) {  
		              this.context = context;  
		          }  
		    
		        @Override  
		        protected Boolean[] doInBackground(Void... params) { 
		        	Boolean[] booleans = new Boolean[3];
		        	if(OppNameMyName.myName.equals("Jim")){
		        	String resString1=KeyValueAPI.get("basin", "basin576095", "Tom");
		        	if(resString1.equals("#OWN")){
		        		booleans[0]=true;
		        		
		        	}else {
						booleans[0]=false;

					}
		        	
		        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
		        	if(resString2.equals("#OWN")){
		        		booleans[1]=true;
		        
		        	}else {
						booleans[1]=false;

						
					}
		        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
		        	if(resString3.equals("#OWN")){
		        		booleans[2]=true;
		  
		        	}else {
		        		booleans[2]=false;
					}
		        	
		        	return booleans;}
		        	
		        	if(OppNameMyName.myName.equals("Tom")){
		        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
		        	if(resString1.equals("#OWN")){
		        		booleans[0]=true;
		        		
		        	}else {
						booleans[0]=false;
					
					}
		        	String resString2=KeyValueAPI.get("basin", "basin576095", "Mary");
		        	if(resString2.equals("#OWN")){
		        		booleans[1]=true;
		        	
		        	}else {
						booleans[1]=false;
						
					}
		        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
		        	if(resString3.equals("#OWN")){
		        		booleans[2]=true;
		        		
		        	}else {
						booleans[2]=false;
						
					}
		        	
		        	return booleans;}
		        	
		        	if(OppNameMyName.myName.equals("Mary")){
			        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
			        	if(resString1.equals("#OWN")){
			        		booleans[0]=true;
			        		
			        	}else {
							booleans[0]=false;
						
						}
			        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
			        	if(resString2.equals("#OWN")){
			        		booleans[1]=true;
			        		
			        	}else {
							booleans[1]=false;
							
						}
			        	String resString3=KeyValueAPI.get("basin", "basin576095", "Fred");
			        	if(resString3.equals("#OWN")){
			        		booleans[2]=true;
			        		
			        	}else {
							booleans[2]=false;
							
						}
			        	
			        	return booleans;}
		        	if(OppNameMyName.myName.equals("Fred")){
			        	String resString1=KeyValueAPI.get("basin", "basin576095", "Jim");
			        	if(resString1.equals("#OWN")){
			        		booleans[0]=true;
			        		
			        	}else {
							booleans[0]=false;
							
						}
			        	String resString2=KeyValueAPI.get("basin", "basin576095", "Tom");
			        	if(resString2.equals("#OWN")){
			        		booleans[1]=true;
			        		
			        	}else {
							booleans[1]=false;
							
						}
			        	String resString3=KeyValueAPI.get("basin", "basin576095", "Mary");
			        	if(resString3.equals("#OWN")){
			        		booleans[2]=true;
			        		
			        	}else {
							booleans[2]=false;
						
						}
			        	
			        	return booleans;}
		        	return null;
		        }  
		 
		        @Override  
		        protected void onCancelled() {  
		            super.onCancelled();  
		        }  
		 
		        @Override
				protected void onPostExecute(Boolean[] results) {
					// TODO Auto-generated method stub
					super.onPostExecute(results);

					if(results[0]==true){
						buttonN1.startAnimation(alphaInc);
						buttonN1.setOnClickListener(new OpponentButtonListener());
					}else {
						buttonN1.startAnimation(alphaDes);
						buttonN1.setOnClickListener(null);
					}
					if(results[1]==true){
						buttonN2.startAnimation(alphaInc);
						buttonN2.setOnClickListener(new OpponentButtonListener());
					}else {
						buttonN2.startAnimation(alphaDes);
						buttonN2.setOnClickListener(null);
					}
					if(results[2]==true){
						buttonN3.startAnimation(alphaInc);
						buttonN3.setOnClickListener(new OpponentButtonListener());
					}else {
						buttonN3.startAnimation(alphaDes);
						buttonN3.setOnClickListener(null);
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
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, params[0]);
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
				return null;
			}
			
		}
		class CheckNameTask extends AsyncTask<String, Integer, Boolean> {  
	    	
	    	private Context context;  
	    	CheckNameTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
	    

	        @Override  
	        protected Boolean doInBackground(String... params) {  
	        	
	        		
	        		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "@"+params[0]);
	        		
	        		int i=0; 	        		
	                while(i<100){  
	                    i++;  
	                    String oppStatus = KeyValueAPI.get("basin", "basin576095", params[0]);
	                    System.out.println("Searching opponent:"+params[0]);	
	                    System.out.println("Opponent's status:"+oppStatus);
	                    if(oppStatus.equals(OppNameMyName.myName)){
	                    	OppNameMyName.oppName = params[0];
	                    	System.out.println("Opponent: "+params[0]);	                   
	                    	return true;
	                    }
	                    else {
	                    	if(oppStatus.equals("#REJ")){
	                    		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#OWN");
	                    		//Toast.makeText(context,params[0]+" rejects to play",Toast.LENGTH_LONG).show();
		                    	System.out.println("Opponent Reject: "+params[0]);	                   
		                    	return false;
	                    	}
	                    	else {
	                    		 try {  
	     	                        Thread.sleep(100);  
	     	                    } catch (InterruptedException e) { 
	     	                    	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#OWN");
	     	                    	return false;
	     	                    } 
							}
	                    }                  
	                }  
	                KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#OWN");
	                //Toast.makeText(context,params[0]+" does not repond",Toast.LENGTH_LONG).show();
	                return false;
	        }
	 
	        @Override  
	        protected void onCancelled() {  
	            super.onCancelled();  
	        }  
	 
	        @Override  
	        protected void onPostExecute(Boolean results) {  

	        	   //progressBar.setBackgroundColor(getResources().getColor(R.color.white));  
	        	if(results==true){
	        	   //progressBar.startAnimation(alphaDes);
	        	   System.out.println("Opponent Connected! Loading Game");

	 		      	Intent intent = new Intent(OnlineUsers.this, CommGame.class);
	 		 
	 		      	startActivity(intent);
	 		      	finish();}
	        	else {
					
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
