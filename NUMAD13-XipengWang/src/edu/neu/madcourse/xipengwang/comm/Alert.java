package edu.neu.madcourse.xipengwang.comm;

import edu.neu.madcourse.xipengwang.R;
import edu.neu.mhealth.api.KeyValueAPI;
import android.R.integer;
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

public class Alert extends Activity{
	private TextView f1;
	private TextView f2;
	
	private Button  fback;
	private static MediaPlayer mp = null;
	
	//LosTask losTask;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//losTask = new LosTask(this);
		//losTask.execute();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
		setContentView(R.layout.comm_alert);
		setTitle("Network Exception");
		mp = MediaPlayer.create(this, R.raw.notify);
        mp.start();
		f1 = (TextView)findViewById(R.id.comm_alertTitle);
		f2 = (TextView)findViewById(R.id.comm_alertMsg);
		//fscore = (TextView)findViewById(R.id.cdabble_finish_score);
		fback = (Button)findViewById(R.id.comm_alertConfirm);
		
		
		fback.setOnClickListener(new BackListener());
		
		
	}
	
	class BackListener implements OnClickListener{
		
		@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//losTask.cancel(true);
		finish();
		//musicGoOn = true;
		//Intent intent =new Intent();
		//intent.setClass(Cong.this, Dabble.class);
		//startActivity(intent);
	}
	}

}
