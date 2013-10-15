package edu.neu.madcourse.xipengwang.comm;

import edu.neu.madcourse.xipengwang.R;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Cong extends Activity{
	private TextView cong1;
	private TextView cong2;
	private TextView score;
	WinTask winTask;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		winTask = new WinTask(this);
		winTask.execute();
	}



	private Button  back;
	private static MediaPlayer mp = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
		setContentView(R.layout.comm_cong);
		mp = MediaPlayer.create(this, R.raw.tada);
        mp.start();
		cong1 = (TextView)findViewById(R.id.cdabble_cong1);
		cong2 = (TextView)findViewById(R.id.cdabble_cong2);
		score = (TextView)findViewById(R.id.cdabble_cong_score);
		back = (Button)findViewById(R.id.cback_to_main);
		
		Intent intent = getIntent();
		String res = intent.getStringExtra("lastScore");
		
		score.setText(res);
		back.setOnClickListener(new BackListener());
		
		
	}
	
		   

	class BackListener implements OnClickListener{
		
		@Override
	public void onClick(View v) {
		winTask.cancel(true);
		// TODO Auto-generated method stub
		finish();
		//musicGoOn = true;
		//Intent intent =new Intent();
		//intent.setClass(Cong.this, Dabble.class);
		//startActivity(intent);
	}
	}
	
	class WinTask extends AsyncTask<Void, Integer, Void>{
	    	private Context context;  
	    	WinTask(Context context) {  
	              this.context = context;  
	              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
	             // progressBar.startAnimation(alphaInc);
	              
	          }  
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//CommGame.aTask.cancel(true);
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#WIN");
				System.out.println(OppNameMyName.myName+" wins");
				return null;
			}
	    	
	    }
}
