package edu.neu.madcourse.xipengwang.finalProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import edu.neu.madcourse.xipengwang.R;


public class MainActivity extends Activity implements CvCameraViewListener2{
	
	 //private FrameLayout mainLayout;
	
	 private Handler mHandler  = new Handler();
	 private Button startButton;
	 private ImageView capture;
     private boolean mCaptureFrame =false;
     Timer myTimer = new Timer();
	    private CameraBridgeViewBase   mOpenCvCameraView;
	    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
	        @Override
	        public void onManagerConnected(int status) {
	            switch (status) {
	                case LoaderCallbackInterface.SUCCESS:
	                {
	                    Log.i("OpenCV", "OpenCV loaded successfully");
	                    mOpenCvCameraView.enableView();
	                    startButton = (Button)findViewById(R.id.vedio_start_button);
	                    startButton.setOnClickListener(new StartListener());
	                   
	                } break;
	                default:
	                {
	                    super.onManagerConnected(status);
	                } break;
	            }
	            
	        }
	        
	    };
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		 setContentView(R.layout.final_main);
		 
		 
	        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_preview);
	        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	        mOpenCvCameraView.setCvCameraViewListener(this);
	        
	}

	public class StartListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//mHandler.postDelayed(TakePicture, 300);
			v.setOnClickListener(null);
			myTimer.scheduleAtFixedRate(myTimerTask, 100, 500);	
		}
		
	}
	TimerTask myTimerTask= new TimerTask(){
		private int counter = 0;
		@Override		 
	    public void run() {
	        mHandler.post(new Runnable() {
	            public void run() {
	            	mCaptureFrame=true;
	            }
	        });
	        if(++counter == 40) {
	        	myTimer.cancel();
	        }
	    }
		
	};
 
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 if (mOpenCvCameraView != null)
	            mOpenCvCameraView.disableView();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 if (mOpenCvCameraView != null)
	            mOpenCvCameraView.disableView();
		 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
		 /* mCamera = getCameraInstance();  	 
		  camPreview = new CameraPreview(this,mCamera);
		  FrameLayout mainLayout = (FrameLayout) findViewById(R.id.camera_preview);
		  mainLayout.addView(camPreview);*/
		
		
	}
	


	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		if(mCaptureFrame){
			mCaptureFrame=false;
		return pupilDetection(inputFrame);
		}
		else {
			return inputFrame.gray();
		}
	}
	private Mat pupilDetection(CvCameraViewFrame inputFrame) {
		
		Mat graphic =  inputFrame.rgba();
         Mat dummyRedFrame = new Mat(graphic.rows(), graphic.cols(), CvType.CV_8UC1);
         Mat dummyGreenFrame = new Mat(graphic.rows(), graphic.cols(), CvType.CV_8UC1);
         Mat dummyBlueFrame = new Mat(graphic.rows(), graphic.cols(), CvType.CV_8UC1);
         List<Mat> dummyRGBFrames = new ArrayList<Mat>();
         dummyRGBFrames.add(dummyRedFrame);
         dummyRGBFrames.add(dummyGreenFrame);
         dummyRGBFrames.add(dummyBlueFrame);
         List<Mat> listGraphic = new ArrayList<Mat>();
         listGraphic.add(graphic);
         //Core.split(graphic, dummyRGBFrames);
         Core.mixChannels(listGraphic, dummyRGBFrames, new MatOfInt(0,0,1,1,2,2));
         Core.normalize(dummyRedFrame, dummyRedFrame, 0, 255,Core.NORM_MINMAX, CvType.CV_8U);
         Core.normalize(dummyGreenFrame, dummyGreenFrame, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
         Core.normalize(dummyBlueFrame, dummyBlueFrame, 0, 255,Core.NORM_MINMAX, CvType.CV_8U);
         Mat mGray = dummyBlueFrame;
		return EyeTracerImp.findEyes1(mGray);
	}
}
