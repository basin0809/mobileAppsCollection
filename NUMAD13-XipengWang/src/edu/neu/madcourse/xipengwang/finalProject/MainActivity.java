package edu.neu.madcourse.xipengwang.finalProject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import edu.neu.madcourse.xipengwang.R;




public class MainActivity extends Activity implements CvCameraViewListener2{
	
	 //private FrameLayout mainLayout;
	
	 private Handler mHandler  = new Handler();
	 private Button startButton;
	 
     private boolean mCaptureFrame =false;
     private MyJavaCameraView mOpenCvCameraView;
     private MenuItem[] mFlashListItems;
     private SubMenu mFlashMenu;
     private Mat mGrayMat;
     final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
     Timer myTimer = new Timer();
	// private CameraBridgeViewBase   mOpenCvCameraView;
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
	                    Handler mHandler2  = new Handler();
	                   
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
		 
		 
	        mOpenCvCameraView = (MyJavaCameraView) findViewById(R.id.camera_preview);
	        //mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	        mOpenCvCameraView.setCvCameraViewListener(this);
	        
	}

	public class StartListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//mHandler.postDelayed(TakePicture, 300);
			tg.startTone(ToneGenerator.TONE_PROP_BEEP);
			Toast.makeText(MainActivity.this, "Start to take video",Toast.LENGTH_SHORT).show();
			v.setOnClickListener(null);
			myTimer.scheduleAtFixedRate(myTimerTask, 0, 600);	
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
	        if(++counter == 11) {
	        	//Looper.prepare();
	        	myTimer.cancel();
	        	mOpenCvCameraView.disableView();
	        	finish();
	        	Intent intent =new Intent();
				intent.setClass(MainActivity.this, ShowPupilDetecionResults.class);
				startActivity(intent);
	        		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		List<String> mFlashList = new LinkedList<String>();
	    int idx = 0;

	    mFlashMenu = menu.addSubMenu("Flash");

	   
	    mFlashList.add("Off");
	  
	    mFlashList.add("Torch");

	    mFlashListItems = new MenuItem[mFlashList.size()];

	    ListIterator<String> FlashItr = mFlashList.listIterator();
	    while(FlashItr.hasNext()){
	        // add the element to the mDetectorMenu submenu
	        String element = FlashItr.next();
	        mFlashListItems[idx] = mFlashMenu.add(1,idx,Menu.NONE,element);
	        idx++;
	    }
	    return true;

	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getGroupId() == 1)
	    {
		int flashType = item.getItemId();
	       String caption = "Flash Mode: "+ (String)item.getTitle();
	       Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();

	       mOpenCvCameraView.setFlashMode(flashType);}
		return true;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		 mGrayMat = new Mat(height, width, CvType.CV_8UC1);
	}
	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		// TODO Auto-generated method stub
		Mat grayMat = new Mat();
		inputFrame.gray().copyTo(grayMat);
		if(mCaptureFrame){
			PupilImgs.pupilImgSet.add(grayMat);
			Log.d("Take photo", "pupilImgSet"+PupilImgs.pupilImgSet.size());
		
			mCaptureFrame=false;
			return grayMat;
	
		}
		else {
			return grayMat;
		}
	}
	 public void decodeYUV(int[] out, byte[] fg, int width, int height)
             throws NullPointerException, IllegalArgumentException {
         int sz = width * height;
         if (out == null)
             throw new NullPointerException("buffer out is null");
         if (out.length < sz)
             throw new IllegalArgumentException("buffer out size " + out.length
                     + " < minimum " + sz);
         if (fg == null)
             throw new NullPointerException("buffer 'fg' is null");
         if (fg.length < sz)
             throw new IllegalArgumentException("buffer fg size " + fg.length
                     + " < minimum " + sz * 3 / 2);
         int i, j;
         int Y, Cr = 0, Cb = 0;
         for (j = 0; j < height; j++) {
             int pixPtr = j * width;
             final int jDiv2 = j >> 1;
             for (i = 0; i < width; i++) {
                 Y = fg[pixPtr];
                 if (Y < 0)
                     Y += 255;
                 if ((i & 0x1) != 1) {
                     final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
                     Cb = fg[cOff];
                     if (Cb < 0)
                         Cb += 127;
                     else
                         Cb -= 128;
                     Cr = fg[cOff + 1];
                     if (Cr < 0)
                         Cr += 127;
                     else
                         Cr -= 128;
                 }
                 int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                 if (R < 0)
                     R = 0;
                 else if (R > 255)
                     R = 255;
                 int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
                         + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                 if (G < 0)
                     G = 0;
                 else if (G > 255)
                     G = 255;
                 int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                 if (B < 0)
                     B = 0;
                 else if (B > 255)
                     B = 255;
                 out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
             }
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
