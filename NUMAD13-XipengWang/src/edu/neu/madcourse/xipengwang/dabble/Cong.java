package edu.neu.madcourse.xipengwang.dabble;

import edu.neu.madcourse.xipengwang.R;
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

public class Cong extends Activity{
	private TextView cong1;
	private TextView cong2;
	private TextView score;
	private TextView cand;
	private Button  back, cagain;
	private static MediaPlayer mp = null;
	private int modeSt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
		setContentView(R.layout.dabble_cong);
		mp = MediaPlayer.create(this, R.raw.tada);
        mp.start();
		cong1 = (TextView)findViewById(R.id.dabble_cong1);
		cong2 = (TextView)findViewById(R.id.dabble_cong2);
		score = (TextView)findViewById(R.id.dabble_cong_score);
		cand = (TextView)findViewById(R.id.dabble_cong_or);
		back = (Button)findViewById(R.id.back_to_main);
		cagain = (Button)findViewById(R.id.cong_try_again);
		Intent intent = getIntent();
		String res = intent.getStringExtra("lastScore");
		modeSt = Integer.parseInt(intent.getStringExtra("gameMode"));
		score.setText(res);
		back.setOnClickListener(new BackListener());
		cagain.setOnClickListener(new CAListener());
		
	}
	
		   
	class CAListener implements OnClickListener{
		
		@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
		
		//musicGoOn = true;
		Intent intent =new Intent();
		intent.putExtra("music_stuate", TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1).toString());
		intent.putExtra("gameMode", modeSt+"");
		intent.setClass(Cong.this, Game.class);
		startActivity(intent);
	}
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
