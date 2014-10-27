package com.example.numad13_huaqiyi_player;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.numad13_huaqiyi_player.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder.Callback2;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity implements Callback{
	String TAG = "RangeSeekBarTAG";
	private Button btn_replay_flashon = null;
	private Button btn_replay_flashoff = null;
	private Button btn_review = null;
	private RangeSeekBar<Integer> rskb_flashon = null;
	private RangeSeekBar<Integer> rskb_flashoff = null;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private MediaPlayer m = null;
	private LinearLayout l_flashon = null;
	private LinearLayout l_flashoff = null;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private Boolean isPlaying = false;
	private int flashPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set the flashPosition
		flashPosition = 1000;
		
		


		
		btn_replay_flashoff = (Button) this.findViewById(R.id.replay_button_flash_off);
		btn_replay_flashon = (Button) this.findViewById(R.id.replay_button_flash_on);
		btn_review = (Button) this.findViewById(R.id.review_button);
		ButtonEventListener bel = new ButtonEventListener();
		btn_replay_flashoff.setOnClickListener(bel);
		btn_replay_flashon.setOnClickListener(bel);
		btn_review.setOnClickListener(bel);
		l_flashon = (LinearLayout) this.findViewById(R.id.layout_flash_on);
		l_flashoff = (LinearLayout) this.findViewById(R.id.layout_flash_off);
		surfaceView = (SurfaceView) this.findViewById(R.id.SurfaceView01);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setFixedSize(100,100);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//Set up MediaPlayer
		m = new MediaPlayer();
		m = MediaPlayer.create(this, R.raw.raw);
		
		try{
			m.prepare();
		} catch(IllegalStateException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		rskb_flashon = new RangeSeekBar<Integer>(0, flashPosition, this);
		rskb_flashoff = new RangeSeekBar<Integer>(flashPosition, m.getDuration(), this);
		RangeSeekBarChangeListener rsbcl = new RangeSeekBarChangeListener();
		rskb_flashon.setOnRangeSeekBarChangeListener(rsbcl);
		rskb_flashon.setOnClickListener(bel);
		rskb_flashoff.setOnClickListener(bel);
		rskb_flashoff.setOnRangeSeekBarChangeListener(rsbcl);
		l_flashon.addView(rskb_flashon);
		l_flashoff.addView(rskb_flashoff);
		

		Handler handler = new Handler();
		handler.post(updateRangeSeekBar);

		
	}
	
	class RangeSeekBarChangeListener implements OnRangeSeekBarChangeListener<Integer>{

		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
				Integer minValue, Integer maxValue) {
			Log.d(TAG,  "User selected new date range: MIN=" + minValue + ", MAX=" + maxValue);
			
			m.seekTo((Integer)bar.getSelectedMinValue());
			m.start();
			isPlaying = true;
			int playDuration = (Integer)bar.getSelectedMaxValue()-(Integer)bar.getSelectedMinValue();
			Handler handler = new Handler();
			handler.postDelayed(stopPlayerTaskRunnable, playDuration);
			
		}
		
	}
	
	class ButtonEventListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			if(v==btn_review){
				if(m.isPlaying()){
					m.pause();
				}
				m.seekTo(0);
				m.start();
				isPlaying = true;
				Log.d(TAG, "Review");
			} else if(v==btn_replay_flashoff){
				if(m.isPlaying()){
					m.pause();
				}
				m.seekTo(rskb_flashoff.getSelectedMinValue());
				m.start();
				isPlaying = true;
				int playDuration = rskb_flashoff.getSelectedMaxValue()-rskb_flashoff.getSelectedMinValue();
				Handler handler = new Handler();
				handler.postDelayed(stopPlayerTaskRunnable, playDuration);
				
				
			} else if(v==btn_replay_flashon){
				if(m.isPlaying()){
					m.pause();
				}
				m.seekTo(rskb_flashon.getSelectedMinValue());
				m.start();
				isPlaying = true;
				int playDuration = rskb_flashon.getSelectedMaxValue()-rskb_flashon.getSelectedMinValue();
				Handler handler = new Handler();
				handler.postDelayed(stopPlayerTaskRunnable, playDuration);
			} else {
				if(m.isPlaying()){
					m.pause();
				}
				isPlaying = false;
			}
			
		}
		
	}
	
	Runnable stopPlayerTaskRunnable = new Runnable(){
		public void run(){
			m.pause();
			isPlaying = false;
		}
	};
	
	Runnable updateRangeSeekBar = new Runnable(){
		public void run(){
			if(isPlaying){
				int currentPosition = m.getCurrentPosition();
				rskb_flashon.setSelectedSecondaryProgressValue(currentPosition);
				rskb_flashoff.setSelectedSecondaryProgressValue(currentPosition);
				Log.d(TAG, "Set secondary progress value to "+currentPosition);
				
				
			}
			Handler handler = new Handler();
			handler.postDelayed(updateRangeSeekBar, 10);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		m.setDisplay(surfaceHolder);
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}


}
