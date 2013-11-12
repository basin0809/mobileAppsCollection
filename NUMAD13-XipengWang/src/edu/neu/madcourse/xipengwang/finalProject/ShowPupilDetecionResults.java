package edu.neu.madcourse.xipengwang.finalProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.xipengwang.R;

public class ShowPupilDetecionResults extends Activity{
	private ImageView capture;
	private TextView explain;
	private Handler mHandler  = new Handler();
	private ProgressDialog pdialog;
	Timer myTimer = new Timer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_showresults);
		capture = (ImageView)findViewById(R.id.capture);
		explain = (TextView)findViewById(R.id.explain);
		
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		PupilDectTask pupilDectTask = new PupilDectTask();
		pupilDectTask.execute();
    	//myTimer.scheduleAtFixedRate(myTimerTask, 100, 1000);	
		
	}

	Runnable swapImage = new Runnable() {
		private int counter = 0;
	    @Override
	    public void run() {
	    	if(++counter >= 10){
	    		Mat mCaptureMat = PupilImgs.pupilImgSet.get(9);
	        	int width =mCaptureMat.width(); 
				int height = mCaptureMat.height();
				Bitmap mCaptureBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
				Utils.matToBitmap(mCaptureMat, mCaptureBitmap);
				capture.setImageBitmap(mCaptureBitmap);
	    	}
	    	else{
	    	Mat mCaptureMat = PupilImgs.pupilImgSet.get(counter);
	    	Log.d("capture", "counter: "+counter);
        	int width =mCaptureMat.width(); 
			int height = mCaptureMat.height();
			Bitmap mCaptureBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Utils.matToBitmap(mCaptureMat, mCaptureBitmap);
			capture.setImageBitmap(mCaptureBitmap);
			
			Log.d("capture", "setImg"+counter);
			capture.postDelayed(this, 800);
	    	}
	    }
	};
	
	
	   
		
   class PupilDectTask extends AsyncTask<Void, Integer, Void> {
		private  ProgressDialog pdialog = new ProgressDialog(ShowPupilDetecionResults.this);

	    @Override
	    protected void onPreExecute() {
	    	this.pdialog = new ProgressDialog(ShowPupilDetecionResults.this);
	    	this.pdialog.setTitle("Pupile Detecting");
	    	this.pdialog.setMessage("Processing the video, please wait for seconds");
	    	this.pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    	this.pdialog.show();
	    }

	    // automatically done on worker thread (separate from UI thread)
	    @Override
	    protected Void doInBackground(Void... params) {
	        // Here is where we need to do the downloading of the 
	    	Mat[] processedImgs = new Mat[PupilImgs.pupilImgSet.size()];
	    	for(int i = 0; i<PupilImgs.pupilImgSet.size(); i++){
	    		publishProgress((int)(i*100/10));
	    		processedImgs[i] = EyeTracerImp.findEyes1(PupilImgs.pupilImgSet.get(i));  	
	    		Log.d("asynT", i+"");
	    		
	    	}
	    	PupilImgs.pupilImgSet.clear();
	    	for(int j = 0; j<10; j++){
	    		PupilImgs.pupilImgSet.add(processedImgs[j]);
	    	}
	    	return null;
	    }
	    // add in a progress bar update
	    @Override
	    protected void onProgressUpdate(Integer...progress) {
	        this.pdialog.setProgress(progress[0]);
	    }
	    @Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
			
			 if (this.pdialog.isShowing()) {
		            this.pdialog.dismiss();
		        }

		            Toast.makeText(ShowPupilDetecionResults.this, "Pupile Detection Done!",
		            Toast.LENGTH_SHORT).show();
		            explain.setText("the white circles indicate your iris and pupil's edge");
		            capture.removeCallbacks(swapImage);
		    		capture.postDelayed(swapImage, 300);
		}

	}
	
}
