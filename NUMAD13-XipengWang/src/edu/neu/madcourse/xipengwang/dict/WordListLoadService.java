package edu.neu.madcourse.xipengwang.dict;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import edu.neu.madcourse.xipengwang.R;

public class WordListLoadService extends Service{
	
	public static HashMap<String,String> words = new HashMap<String,String>();
	public static int count = 0;
	public static boolean isReaded = false;
	private IBinder binder = new FirBinder();
	class FirBinder extends Binder{
		WordListLoadService getService(){
			return WordListLoadService.this;
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
		
		
		MyThread thread = new MyThread();
		thread.start();
			
		
		

	}
	class MyThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				
				String line;
				int i =0;
				InputStream input = getResources().openRawResource(R.raw.wordlist);
				InputStreamReader inputStreamReader = new InputStreamReader(input, "GBK");
				BufferedReader reader =new BufferedReader(inputStreamReader);
				System.out.println("file loaded!!! ");
				while ((line = reader.readLine())!= null) {
					words.put(line,"");
					i++;
					count = i;
					
				}
				
				System.out.println("file readed");
				isReaded = true;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("file unreaded");
				e.printStackTrace();
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
		return super.onStartCommand(intent, flags, startId);
	}
	
	
}
