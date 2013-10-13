package edu.neu.madcourse.xipengwang.comm;

import edu.neu.madcourse.xipengwang.R;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends Activity{
	private TextView f1;
	private TextView f2;
	private TextView fscore;

	private Button  fback;
	private static MediaPlayer mp = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
		setContentView(R.layout.comm_finish);
		mp = MediaPlayer.create(this, R.raw.notify);
        mp.start();
		f1 = (TextView)findViewById(R.id.cdabble_finish1);
		f2 = (TextView)findViewById(R.id.cdabble_finish2);
		fscore = (TextView)findViewById(R.id.cdabble_finish_score);
		fback = (Button)findViewById(R.id.cfinish_back_to_main);
		
		Intent intent = getIntent();
		String res = intent.getStringExtra("lastScore");
		fscore.setText(res);
		fback.setOnClickListener(new BackListener());
		
		
	}
	
	class BackListener implements OnClickListener{
		
		@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
		//musicGoOn = true;
		//Intent intent =new Intent();
		//intent.setClass(Cong.this, Dabble.class);
		//startActivity(intent);
	}
	}
	
}
