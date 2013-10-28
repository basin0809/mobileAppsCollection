package edu.neu.madcourse.xipengwang.dabble;

import edu.neu.madcourse.xipengwang.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Setting extends Activity{
	private TextView settingMusic;
	private Button onOffMusic;
	private boolean musicGoOn, musicGoOn2;
	
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
		setContentView(R.layout.dabble_setting);
		 setTitle("Options");
		//settingTitle = (TextView)findViewById(R.id.settingTitle);
		settingMusic = (TextView)findViewById(R.id.settingMusic);
		//settingSR = (TextView)findViewById(R.id.settingSR);
		onOffMusic = (Button)findViewById(R.id.onOffMusic);
		//onOffSR = (Button)findViewById(R.id.onOffSR);
		onOffMusic.setOnClickListener(new MusicSettingListener());
	}

	class MusicSettingListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				if(TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1)==0){
				
				
		        BGMManager.pause();
		        TwiceActiveCheck.musicTwicePressed.add(1);
		        onOffMusic.setText("ON");
				
			}
			else{
				musicGoOn=false;
		        BGMManager.start(Setting.this,R.raw.game);
		        TwiceActiveCheck.musicTwicePressed.add(0);
		        onOffMusic.setText("OFF");
			}
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 if(!musicGoOn)
	            BGMManager.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(TwiceActiveCheck.musicTwicePressed.get(TwiceActiveCheck.musicTwicePressed.size()-1)==1){
			
		}
		else{
		musicGoOn=false;
        BGMManager.start(this,R.raw.game);}
	}

		
}
