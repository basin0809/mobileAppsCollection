package edu.neu.madcourse.pupilresponsemeter;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.neu.madcourse.pupilresponsemeter.RangeSeekBar.OnRangeSeekBarChangeListener;


public class VideoPlayer extends Activity implements Callback{
	String TAG = "RangeSeekBarTAG";
	private final int DEFAULT_DURATION = 4000;
	private final static int FLASH_ON =7;
	private final static int FLASH_OFF =53;
	private Button btn_replay_flashon = null;
	private Button btn_replay_flashoff = null;
	private Button btn_review = null;
	private Button btn_jump = null;
	private RangeSeekBar<Integer> rskb_flashon = null;
	private RangeSeekBar<Integer> rskb_flashoff = null;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private MediaPlayer m = null;
	private RelativeLayout l_sur_img = null;
	private RelativeLayout l_flashon = null;
	private RelativeLayout l_flashoff = null;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private Boolean isPlaying = false;
	private int flashPosition;
	
	private String path;
	private int flashDuration;
	private int darkDuration;
	private Boolean canPlayBack=false;
	private Boolean isPlayingFlashOff=false;
	
	private int slowDownRatio = 2;
	
	private int FPS = 45;
	
	private float constrictionTime=0;

	private float dilationTime = 0;
	
	//private ImageView capture;
	private int userFlashOnImgNum;
	private int userFlashOffImgNum;
	
	private int playDuration;
	private int cDur = 2000;
	private int dDur = 2000;
	private long startTime;
	PupilDectTask pupilDectTask;
	Handler handler;
	private boolean jump = false;
	
	private Button tutButton;
	private  ProgressDialog pdialog;
	private Handler mhandler = new Handler();;
	//private int constrictionTime;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_showvideo);
		
		Log.d("VPC", "CREATE");
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, VideoPlayer.this, new BaseLoaderCallback(this) {
	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i("ImgProcess", "OpenCV loaded successfully");    
	                    
	                    Log.i("ImgProcess", "imgs converting imgNum:"+PupilImgs.pupilImgSet.size());
	                    //Log.i("ImgProcess", "userFlasOnImgSet imgNum:"+PupilImgs.userFlasOnImgSet.size());
	                    //userFlashOnImgNum = PupilImgs.userFlasOnImgSet.size();
	                   // Log.i("ImgProcess", "userFlasOffImgSet imgNum:"+PupilImgs.userFlasOffImgSet.size());
	                    //userFlashOffImgNum = PupilImgs.userFlasOffImgSet.size();
	                    PupilImgs.convertToMat();
	                    PupilImgs.rescaleImgs();
	                    Log.i("ImgProcess", "imgs converted MatType:"+CvType.CV_8UC1);
	                    //Log.i("ImgProcess", "imgs converted MatType:"+PupilImgs.pupilBitMap.get(7).type());
	                    Log.i("ImgProcess", "imgs converted MatNum:"+PupilImgs.pupilBitMap.size());
	                    PupilImgs.outputImgs();
	                   
	                    Log.i("ImgProcess", "cicle detecting");
	                   
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	            
	        }
	        
	    });
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		flashDuration = intent.getIntExtra("flashDuration", 2);
		//darkDuration = intent.getIntExtra("darkDuration", 2);
		
		
		// Set the flashPosition
		flashPosition = flashDuration*1000;
	
		
		

		tutButton = (Button)findViewById(R.id.show_tut);
		tutButton.setOnClickListener(new TutListener());
		//capture = (ImageView)this.findViewById(R.id.play_back_imgs);
		btn_replay_flashoff = (Button) this.findViewById(R.id.replay_button_flash_off);
		btn_replay_flashon = (Button) this.findViewById(R.id.replay_button_flash_on);
		btn_review = (Button) this.findViewById(R.id.review_button);
		btn_review.setBackgroundResource(R.drawable.play);
		btn_jump = (Button)this.findViewById(R.id.identify_button);
		btn_replay_flashoff.setBackgroundResource(R.drawable.slow);
		btn_replay_flashon.setBackgroundResource(R.drawable.slow);
		//btn_review.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
		btn_jump.setBackgroundResource(R.drawable.next);
		
		l_sur_img = (RelativeLayout) this.findViewById(R.id.layout_surface_img);
		l_flashon = (RelativeLayout) this.findViewById(R.id.layout_flash_on);
		l_flashoff = (RelativeLayout) this.findViewById(R.id.layout_flash_off);
		surfaceView = (SurfaceView) this.findViewById(R.id.SurfaceView01);
		
        rskb_flashon = new RangeSeekBar<Integer>(0, flashPosition, this);
		
		rskb_flashoff = new RangeSeekBar<Integer>(flashPosition, DEFAULT_DURATION, this);
		
		l_flashon.addView(rskb_flashon);
		
		l_flashoff.addView(rskb_flashoff);
		//l_sur_img.addView(surfaceView);
		//l_sur_img.addView(capture);
		
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFixedSize(200,200);
		pdialog = new ProgressDialog(VideoPlayer.this);	
		//Set up MediaPlayer
		
		
		
		


		
	}
	
	
	
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		rskb_flashoff.setSelectedMinValue(savedInstanceState.getInt("seekBarOffMinValue"));
		rskb_flashoff.setSelectedMaxValue(savedInstanceState.getInt("seekBarOffMaxValue"));
		rskb_flashon.setSelectedMinValue(savedInstanceState.getInt("seekBarOnMinValue"));
		rskb_flashon.setSelectedMaxValue(savedInstanceState.getInt("seekBarOnMaxValue"));
		
	}





	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("seekBarOffMinValue", rskb_flashoff.getSelectedMinValue());
		outState.putInt("seekBarOffMaxValue", rskb_flashoff.getSelectedMaxValue());
		outState.putInt("seekBarOnMaxValue", rskb_flashon.getSelectedMaxValue());
		outState.putInt("seekBarOnMaxValue", rskb_flashon.getSelectedMaxValue());
		
	}





	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("VPC", "RESUME");

		handler = new Handler();
		handler.post(updateRangeSeekBar);
	}




	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("VPC", "PAUSE");
		handler.removeCallbacks(updateRangeSeekBar);
		if(jump){finish();}
		
	}




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(pdialog != null){
			pdialog.dismiss();
		}
		Log.d("VPC", "DESTROY");
	}




	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("VPC", "START");
	}





	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("VPC", "STOP");
	}


    public class TutListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			View dialogView1 = View.inflate(VideoPlayer.this, R.layout.final_dialogviewplay, null);
			View dialogView2 = View.inflate(VideoPlayer.this, R.layout.final_dialogviewstep, null);
			View dialogView3 = View.inflate(VideoPlayer.this, R.layout.final_dialogviewseek, null);
			View dialogView4 = View.inflate(VideoPlayer.this, R.layout.final_dialogviewnext, null);
			
			 final AlertDialog.Builder alertDialogBuilderNext=new AlertDialog.Builder(VideoPlayer.this);  					
			 alertDialogBuilderNext.setTitle("Tutorial 4/4")
			  .setView(dialogView4)            
		      .setNegativeButton("Get it",   new DialogInterface.OnClickListener(){
		              public void onClick(DialogInterface dialoginterface, int i){
		            	  setResult(RESULT_CANCELED);
		              }
		      });
			 
			 
			  final AlertDialog.Builder alertDialogBuilderSeek=new AlertDialog.Builder(VideoPlayer.this);  					
			  alertDialogBuilderSeek.setTitle("Tutorial 3/4")
			  .setView(dialogView3)		            
		      .setNegativeButton("Get it",   new DialogInterface.OnClickListener(){
		              public void onClick(DialogInterface dialoginterface, int i){
		            	  setResult(RESULT_CANCELED);
		              }
		      })
		      .setPositiveButton("Next one", new DialogInterface.OnClickListener(){
		           public void onClick(DialogInterface dialoginterface, int i){
			          	 setResult(RESULT_OK); 
			          	AlertDialog alertDialogNext = alertDialogBuilderNext.show(); 
			           }
			   });
			  
			  
			   final AlertDialog.Builder alertDialogBuilderStep=new AlertDialog.Builder(VideoPlayer.this);  
				
			   alertDialogBuilderStep.setTitle("Tutorial 2/4")
	           .setView(dialogView2)      
			   .setNegativeButton("Get it",   new DialogInterface.OnClickListener(){
			           public void onClick(DialogInterface dialoginterface, int i){
			         	  setResult(RESULT_CANCELED);
			         	  
			           }
			   })
			   .setPositiveButton("Next one", new DialogInterface.OnClickListener(){
			           public void onClick(DialogInterface dialoginterface, int i){
			          	 setResult(RESULT_OK); 
			          	AlertDialog alertDialogSeek = alertDialogBuilderSeek.show(); 
			           }
			   });
			   
			   
			   
			   AlertDialog.Builder alertDialogBuilderLocation=new AlertDialog.Builder(VideoPlayer.this);  				
			   alertDialogBuilderLocation.setTitle("Tutorial 1/4")
	           .setView(dialogView1)          
		       .setNegativeButton("Get it",   new DialogInterface.OnClickListener(){
		              public void onClick(DialogInterface dialoginterface, int i){
		            	  setResult(RESULT_CANCELED);
		              }
		       })
		      .setPositiveButton("Next one", new DialogInterface.OnClickListener(){
		              public void onClick(DialogInterface dialoginterface, int i){
		             	 setResult(RESULT_OK);
		             	AlertDialog alertDialogstep = alertDialogBuilderStep.show(); 
				        
		              }
		      });
			  AlertDialog alertDialogLocation = alertDialogBuilderLocation.show(); 
				
				
		}
    	
    }

	class RangeSeekBarChangeListener implements OnRangeSeekBarChangeListener<Integer>{

		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
				Integer minValue, Integer maxValue) {
			//capture.setVisibility(View.GONE);
			Log.d(TAG,  "User selected new date range: MIN=" + minValue + ", MAX=" + maxValue);
			Log.d(TAG,  "USER selected new date range: MIN=" + (Integer)bar.getSelectedMinValue() + ", MAX=" + (Integer)bar.getSelectedMaxValue());
			//bar.setSelectedSecondaryProgressValue(minValue);
			if(bar == rskb_flashoff){
				dDur = maxValue - minValue;
			} else if(bar == rskb_flashon){
				cDur = maxValue - minValue;
			}
			m.seekTo((Integer)bar.getSelectedMinValue());
			m.start();
			isPlaying = true;
			btn_review.setBackgroundResource(R.drawable.pause);
			//int playDuration = (Integer)bar.getSelectedMaxValue()-(Integer)bar.getSelectedMinValue();
			//playDuration = maxValue-minValue;
			Handler handler = new Handler();
			startTime = System.currentTimeMillis();
			handler.postDelayed(new Runnable() {					
				@Override
				public void run() {
					// TODO Auto-generated method stub							
					long endTime = System.currentTimeMillis();
					Log.d(TAG, playDuration-endTime+startTime+"");
				
					m.pause();
					btn_review.setBackgroundResource(R.drawable.play);
					isPlaying = false;
					
				}
			}, (bar==rskb_flashoff)?dDur:cDur);

			
		}
		
		
	}
	class ReplayListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//capture.setVisibility(View.VISIBLE);
			//surfaceView.setVisibility( View.GONE);
			switch (v.getId()) {
			case R.id.replay_button_flash_on:
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					
					isPlayingFlashOff=false;
					canPlayBack = true;
					if(m.isPlaying()){
					m.pause();}
					int startTime = rskb_flashon.getSelectedMinValue();
					int endTime = rskb_flashon.getSelectedMaxValue();
					m.seekTo(startTime);
					m.start();
					mhandler.postDelayed(new Runnable() {					
						@Override
						public void run() {
							// TODO Auto-generated method stub							
							if(m.isPlaying()){
								m.pause();
								btn_review.setBackgroundResource(R.drawable.play);
								isPlaying = false;}
						}
					}, cDur);
				
					//capture.removeCallbacks(swapFlasOnImage);
					
			    /*	capture.postDelayed(new Runnable() {
			    		
			    		int startTime = rskb_flashon.getSelectedMinValue();
			    		
			    		int start = (int)((userFlashOnImgNum)*startTime/2000);
			    		private int counter = start;
			    		
			    		int endTime = rskb_flashon.getSelectedMaxValue();
			    		private int stop = (int)((userFlashOnImgNum)*endTime/2000);
			    		
			    		
			    		int currentPosition = startTime;
			    		int finish =1;
			    		double interval = 0;
			    	    @Override
			    	    public void run() {
			    	    	if(stop == start){
				    			 interval = 0;
				    			}
				    	    	else {
				    	    		 interval = (endTime-startTime)/(stop-start);
								}
				    	    	
			    	    	Log.d("Time matters!", "startTime:"+startTime+" endTime:"+endTime+" startImg:"+start+" stopImg:"+stop+" currentPosition"+currentPosition);
			    	    	if(counter >= userFlashOnImgNum-1||counter>=stop){
			    	    		
			    	    		Bitmap mCaptureBitmap = PupilImgs.userFlasOnImgSet.get(counter-finish);	 			    	    				    	    		
			    				finish++;
			    	    	}
			    	    	else{
			    	    	Bitmap mCaptureBitmap = PupilImgs.userFlasOnImgSet.get(counter);
			    	    	Log.d("Time matters!", "counter: "+counter);     
			    	    	Log.d("Time matters!", "curr: "+counter);  
			    	    	Mat mCaptureMat = new Mat(mCaptureBitmap.getHeight(), mCaptureBitmap.getWidth(), CvType.CV_8UC4);
		    	    		Utils.bitmapToMat(mCaptureBitmap, mCaptureMat);
		    	    		mCaptureMat.submat((int)(mCaptureMat.rows()*0.375), (int)(mCaptureMat.rows()*0.625), 
									 	(int)(mCaptureMat.cols()*0.375), (int)(mCaptureMat.cols()*0.625)).copyTo(mCaptureMat);
		    	    		Bitmap mCaptureRSBitmap = Bitmap.createBitmap(mCaptureMat.cols(),  mCaptureMat.rows(),Bitmap.Config.ARGB_8888);;	
		    	    		Utils.matToBitmap(mCaptureMat, mCaptureRSBitmap);
		    	    		
		    	    		capture.setImageBitmap(Bitmap.createScaledBitmap(mCaptureRSBitmap, mCaptureBitmap.getWidth(), mCaptureBitmap.getHeight(), false));
			    			//capture.setImageBitmap(mCaptureBitmap);		
			    			//currentTime+=interval;
			    			
			    			rskb_flashon.setSelectedSecondaryProgressValue(currentPosition);
			    			currentPosition = (int) (currentPosition+interval);
			    			if(canPlayBack){
			    			capture.postDelayed(this, 200);	
			    			}	
			    			else {
			    				capture.removeCallbacks(this);
			    			}
			    			counter++;
			    	    	}
			    	    	
			    	    }
			    	}, 0);
					*/
				} else if(event.getAction()==MotionEvent.ACTION_UP){
					//capture.removeCallbacks(swapFlasOnImage);
					if(m.isPlaying()){m.pause();}
					constrictionTime = rskb_flashon.getSelectedSecondaryProgressValue();
					Log.d("Time matters!","constrictionTime: "+constrictionTime);
					PupilImgs.constirctionTime = (float) (constrictionTime/1000.0);
					canPlayBack = false;
				}
					
				break;
			case R.id.replay_button_flash_off:
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					isPlayingFlashOff=true;
					canPlayBack = true;
					int startTime = rskb_flashoff.getSelectedMinValue();
					int endTime = rskb_flashoff.getSelectedMaxValue();
					m.seekTo(startTime);
					m.start();
					mhandler.postDelayed(new Runnable() {					
						@Override
						public void run() {
							// TODO Auto-generated method stub							
							if(m.isPlaying()){
								m.pause();
								btn_review.setBackgroundResource(R.drawable.play);
								isPlaying = false;}
						}
					}, dDur);
					//capture.removeCallbacks(swapFlashOffImage);
			    /*	capture.postDelayed(new Runnable() {
			    		//private int counter = 0;
			    		int startTime = rskb_flashoff.getSelectedMinValue();
			    		int start = (int)((userFlashOffImgNum)*(startTime-2000)/2000);
			    		private int counter = start;
			    		int endTime = rskb_flashoff.getSelectedMaxValue();
			    		private int stop = (int)((userFlashOffImgNum)*(endTime-2000)/2000);
			    		
			    		int currentPosition = startTime;
			    		int finish =1;
			    		double interval = 0;
			    		 
			    	    @Override
			    	    public void run() {
			    	    	if(stop == start){
			    			 interval = 0;
			    			}
			    	    	else {
			    	    	 interval = (endTime-startTime)/(stop-start);
							}
			    	    	
			    	    	Log.d("Time matters!", "startTime:"+startTime+" endTime:"+endTime+" startImg:"+start+" stopImg:"+stop+" currentPosition"+currentPosition);
			    	    	if(counter >= userFlashOffImgNum-1||counter>=stop){
			    	    		
			    	    		Bitmap mCaptureBitmap = PupilImgs.userFlasOffImgSet.get(counter-finish);	        	
			    				capture.setImageBitmap(mCaptureBitmap);
			    				finish++;
			    	    	}
			    	    	else{
			    	    	Bitmap mCaptureBitmap = PupilImgs.userFlasOffImgSet.get(counter);
			    	    	Log.d("Time matters!", "counter: "+counter);     
			    	    	Log.d("Time matters!", "curr: "+counter);
			    	    	Mat mCaptureMat = new Mat(mCaptureBitmap.getHeight(), mCaptureBitmap.getWidth(), CvType.CV_8UC4);
		    	    		Utils.bitmapToMat(mCaptureBitmap, mCaptureMat);
		    	    		mCaptureMat.submat((int)(mCaptureMat.rows()*0.375), (int)(mCaptureMat.rows()*0.625), 
									 	(int)(mCaptureMat.cols()*0.375), (int)(mCaptureMat.cols()*0.625)).copyTo(mCaptureMat);
		    	    		Bitmap mCaptureRSBitmap = Bitmap.createBitmap(mCaptureMat.cols(),  mCaptureMat.rows(),Bitmap.Config.ARGB_8888);;	
		    	    		Utils.matToBitmap(mCaptureMat, mCaptureRSBitmap);
		    	    		
		    	    		capture.setImageBitmap(Bitmap.createScaledBitmap(mCaptureRSBitmap, mCaptureBitmap.getWidth(), mCaptureBitmap.getHeight(), false));
			    			
			    			
			    			rskb_flashoff.setSelectedSecondaryProgressValue(currentPosition);
			    			currentPosition = (int) (currentPosition+interval);
			    			if(canPlayBack){
			    			capture.postDelayed(this, 300);	
			    			}	
			    			else {
			    				capture.removeCallbacks(this);
			    			}
			    			counter++;
			    	    	}
			    	    }
			    	}, 0);
				*/
				} else if (event.getAction()==MotionEvent.ACTION_UP){
					//capture.removeCallbacks(swapFlashOffImage);
					dilationTime = (float) ((rskb_flashoff.getSelectedSecondaryProgressValue()-flashPosition)/1000.0);
					PupilImgs.dilationTime = dilationTime;
					Log.d("Time matters!","dilationTime: "+dilationTime);
					canPlayBack = false;
					if(m.isPlaying()){
					m.pause();
					btn_review.setBackgroundResource(R.drawable.play);
					isPlaying = false;}
					//m.pause();
					
				}
						
						
				break;
			default:
				break;
			}
			return false;
		}
		
	}
	

	class ButtonEventListener implements OnClickListener{

		@Override
		public void onClick(View v) {

			if(v==btn_review){
				//capture.setVisibility(View.GONE);
				//surfaceView.setVisibility( View.VISIBLE);
				
				if(m.isPlaying()){
					btn_review.setBackgroundResource(R.drawable.play);
					m.pause();
				}else {
					btn_review.setBackgroundResource(R.drawable.pause);
					m.start();
				}
				//m.seekTo(0);
				
				isPlaying = true;
				Log.d(TAG, "Review");
			}else if(v==btn_jump)
				{
				
				 pupilDectTask = new PupilDectTask();
				pupilDectTask.execute();			
				}
			  else{
				if(m.isPlaying()){
					m.pause();
				}
				isPlaying = false;
			}
			
		}
		
	}
	
	
	
	Runnable updateRangeSeekBar = new Runnable(){
		public void run(){
			
			if(true){
				int currentPosition = m.getCurrentPosition();
				rskb_flashon.setSelectedSecondaryProgressValue(currentPosition);
				rskb_flashoff.setSelectedSecondaryProgressValue(currentPosition);
				//Log.d(TAG, "Set secondary progress value to "+currentPosition);
				
				
			}
			
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
		
		prepareVideo();
		ButtonEventListener bel = new ButtonEventListener();
		ReplayListener rl = new ReplayListener();
		btn_replay_flashoff.setOnTouchListener(rl);
		btn_replay_flashon.setOnTouchListener(rl);
		btn_review.setOnClickListener(bel);
		btn_jump.setOnClickListener(bel);
		//rskb_flashon = new RangeSeekBar<Integer>(0, flashPosition, this);
		
		//rskb_flashoff = new RangeSeekBar<Integer>(flashPosition, m.getDuration(), this);
		//rskb_flashoff.setAbsoluteMaxValue(m.getDuration());
		
		RangeSeekBarChangeListener rsbcl = new RangeSeekBarChangeListener();
		rskb_flashon.setOnRangeSeekBarChangeListener(rsbcl);
		
		//rskb_flashon.setOnClickListener(bel);
		//rskb_flashoff.setOnClickListener(bel);
		
		rskb_flashoff.setOnRangeSeekBarChangeListener(rsbcl);

	
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	private void prepareVideo() {
        try {            
            m = new MediaPlayer();
            m.setAudioStreamType(AudioManager.STREAM_MUSIC);
            m.setScreenOnWhilePlaying(true);

            m.setDisplay(surfaceHolder);
            m.setDataSource(path);
            m.prepare();
        } catch (Exception e) {
            Log.e(TAG, "error: "+ e.getMessage(), e);
            
        }
    }

	
	
	
	
	class PupilDectTask extends AsyncTask<Void, Integer, Void> {
		
	    @Override
	    protected void onPreExecute() {
	    	
	    	pdialog.setTitle("Pupil and Iris Detecting");
	    	pdialog.setMessage("Processing the video, please wait for seconds");
	    	pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    	pdialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					

					pupilDectTask.cancel(true);
					//pullMeAfterCancelTask = new PullMeAfterCancelTask(OnlineUsers.this);
					//System.out.println("Second Task Starts!");
					//pullMeAfterCancelTask.execute();
				}
			});
	    	pdialog.show();
	    }

	   
	    @Override
	    protected Void doInBackground(Void... params) {	       
	    	
	    		
	    		Mat graphicFlahOn = Highgui.imread(PupilImgs.imgPathes.get(0), Highgui.CV_LOAD_IMAGE_GRAYSCALE);	    		
	    		PupilImgs.constrictionRes = EyeTracerImp.preprocessEye(graphicFlahOn);
	    		Bitmap tempImgFlahOn = Bitmap.createBitmap(graphicFlahOn.cols(),  graphicFlahOn.rows(),Bitmap.Config.ARGB_8888);
	            Utils.matToBitmap(graphicFlahOn, tempImgFlahOn);
	    		PupilImgs.processedImgSet.add(tempImgFlahOn);
	    		publishProgress(50);
	    		
	    		Mat graphicFlahOff = Highgui.imread(PupilImgs.imgPathes.get(1), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
	    		PupilImgs.dilationRes = EyeTracerImp.preprocessEye(graphicFlahOff);
	    		Bitmap tempImgFlahOff = Bitmap.createBitmap(graphicFlahOff.cols(),  graphicFlahOff.rows(),Bitmap.Config.ARGB_8888);
	            Utils.matToBitmap(graphicFlahOff, tempImgFlahOff);
	    		PupilImgs.processedImgSet.add(tempImgFlahOff);
	    		publishProgress(100);
		 		return null;
	    }
	    // add in a progress bar update
	    @Override
	    protected void onProgressUpdate(Integer...progress) {
	        pdialog.setProgress(progress[0]);
	    }
	    @Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
			
			 if (pdialog.isShowing()) {
		            pdialog.dismiss();
		        }
			 		handler.removeCallbacks(updateRangeSeekBar);
			 		Log.i("ImgProcess", "cicle detected");			 				 					 				 		
		           
		            
		            Intent intent = new Intent(VideoPlayer.this,ConstrictIdentifyPupilIris.class);	       
			        startActivity(intent);
			        VideoPlayer.this.finish();
			        Log.d("VPC", "FINSH");
			       
		          
		}

	}
}
