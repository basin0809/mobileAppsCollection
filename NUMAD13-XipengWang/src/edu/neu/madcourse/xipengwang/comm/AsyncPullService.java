package edu.neu.madcourse.xipengwang.comm;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.mhealth.api.KeyValueAPI;

public class AsyncPullService extends Service{
	private static ArrayList<String> pullTwiceCheck = new ArrayList<String>();
	
	private IBinder binder = new FirBinder();
	class FirBinder extends Binder{
		AsyncPullService getService(){
			return AsyncPullService.this;
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return binder;
	}
	public String getCurrentTime() {
		
		return "20:22";
	}

	//@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}
	
	class MyThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#AFK");
			while(KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("#AFK")==true&& 
	    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.oppName).equals("#QUIT2")==false){
				SubAsyncPullTask subAsyncPullTask = new SubAsyncPullTask(AsyncPullService.this);
	    		subAsyncPullTask.execute();
	    		try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO: handle exception
				}
	    	}
		 
			stopSelf();
		}
		
		class SubAsyncPullTask extends AsyncTask<Void, Integer, String> {  
	    	
	    	private Context context;  
	    	SubAsyncPullTask(Context context) {  
	              this.context = context;  
	          }  
	    
	        @Override  
	        protected String doInBackground(Void... params) {  
	        	String resString=KeyValueAPI.get("basin", "basin576095", OppNameMyName.oppName);
	        	return resString;
	        }  
	 
	        @Override  
	        protected void onCancelled() {  
	            super.onCancelled();  
	        }  
	 
	        @Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
	        	
				super.onPostExecute(result);
				
				System.out.println("[Service]opponent's movement: "+result);
				String result2 ="";
				if(result.equals("("+OppNameMyName.myName)){
					result2=OppNameMyName.oppFakeName+" joins the game";
				}
				if(!result.equals("("+OppNameMyName.myName)){
					if(result.equals("#AFK")){
						result2=OppNameMyName.oppFakeName+" is not actively playing";
					}else{
					if(result.equals("#QUIT")||result.equals("#QUIT2")){
						result2=OppNameMyName.oppFakeName+" quits";
					}
					else{
						if(result.equals("#WIN")){
							result2=OppNameMyName.oppFakeName+" wins the game!";
						}
						else{
							if(result.equals("#LOS")){
								result2=OppNameMyName.oppFakeName+" loses the game!";
							}
							else{
								if(result.equals("ERROR: IOException")){
									result2="Cannot access to server. Please check your NetWork.";
								}
								else{
									if(result.substring(0, 1).equals("$")){
										result2=OppNameMyName.oppFakeName+" spells: "+result.substring(1, result.length());}
									else {
										result2=OppNameMyName.oppFakeName+" quits";
									}
				}}}}}}
				
				if(!result.equals(pullTwiceCheck.get(pullTwiceCheck.size()-1))){
					
					NotificationManager mNotificationManager=
							(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE); 
					Intent notificationIntent=new Intent(this.context,CommGame.class);
					notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
							 					Intent.FLAG_ACTIVITY_SINGLE_TOP);

			        PendingIntent contentIntent=PendingIntent.getActivity(this.context, 0, notificationIntent, 0);  
					Notification notification;
					if(android.os.Build.VERSION.SDK_INT>=11){
						notification = new Notification.Builder(AsyncPullService.this)
				         .setContentTitle("NUMAD13")
				         .setContentText(result2)
				         .setSmallIcon(R.drawable.ic_launcher)
				         .setContentIntent(contentIntent)
				         .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notifybar))
				         
				         .build();
						notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
						}
					else {
						notification=new
								Notification(R.drawable.ic_launcher, "NUMAD13", System.currentTimeMillis());
					
						Context context=getApplicationContext();      
				        CharSequence contextTitle="NUMAD13";  
				        CharSequence contextText=result2;  
				        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.notifybar);
				        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
				        notification.setLatestEventInfo(context, contextTitle, contextText, contentIntent);
				        }
				        mNotificationManager.notify(1, notification); 
					pullTwiceCheck.add(result);
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
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//return super.onStartCommand(intent, flags, startId);
		pullTwiceCheck.add("-");

		MyThread thread = new MyThread();
		thread.start();
		 return Service.START_STICKY;
	}
	
	
}