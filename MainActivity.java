package edu.neu.madcourse.xipengwang.finalProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import edu.neu.madcourse.xipengwang.R;

public class MainActivity extends Activity{
	private CameraPreview camPreview; 
	 //private FrameLayout mainLayout;
	 private Camera mCamera;
	 private Handler mHandler  = new Handler();
	 private Button startButton;
	 private ImageView capture;
		private SurfaceHolder mSurfHolder;
	    private Boolean takePicture = false;
	    private boolean mPreviewRunning = false;
	    private String currentPictureName;
	    private int previewlayoutWidth;
	    private int previewlayoutHeight;
	 private ArrayList<Bitmap> pupilImgSet = new ArrayList<Bitmap>();
	 private boolean mCaptureFrame =false;
	   // private boolean mCaptureFrame = false;
	    private int frameNumber = 0;
	    private byte[] frame = new byte[1];
	    Timer myTimer = new Timer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		  setContentView(R.layout.final_main);
		  capture = (ImageView)findViewById(R.id.capture);
		  startButton = (Button)findViewById(R.id.vedio_start_button);
		  startButton.setOnClickListener(new StartListener());
	}
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	 private void releaseCamera(){
	        if (mCamera != null){
	        	mCamera.setPreviewCallback(null);
	        	camPreview.getHolder().removeCallback(camPreview);
	            mCamera.release();        
	            mCamera = null;
	        }
	    }
	public class StartListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//mHandler.postDelayed(TakePicture, 300);
			v.setOnClickListener(null);
			myTimer.scheduleAtFixedRate(myTimerTask, 100, 300);	
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
	        if(++counter == 21) {
	        	myTimer.cancel();
	        }
	    }
		
	};
 
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		releaseCamera(); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		  mCamera = getCameraInstance();  	 
		  camPreview = new CameraPreview(this,mCamera);
		  FrameLayout mainLayout = (FrameLayout) findViewById(R.id.camera_preview);
		  mainLayout.addView(camPreview);
		
	}
	
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
		public CameraPreview(Context context,Camera camera)
		{
			
			 super(context);
			 mCamera = camera;
			 mSurfHolder = this.getHolder();
			 mSurfHolder.addCallback(this);
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			  //Parameters parameters;
			  //mSurfHolder = holder;
			  if (mSurfHolder.getSurface() == null){
		          // preview surface does not exist
		          return;
		        }
			   
			  try{
			  //mCamera.setParameters(parameters);
			 // mCamera.setPreviewDisplay(mSurfHolder);
			  mCamera.setPreviewCallback(previewCallback);
			  mCamera.startPreview();
			  Log.d("surfaceChanged", "camera preview set successfully");
			  }	  
			  catch(Exception e){
				  Log.d("surfaceChanged", "Error setting camera preview: " + e.getMessage());
			  }
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	            Log.d("surfaceCreated", "camera preview created successfully ");
	        } catch (IOException e) {
	            Log.d("surfaceCreated", "Error starting camera preview: " + e.getMessage());
	           // mCamera.release();
	            //mCamera = null;
	        }
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			//mCamera.setPreviewCallback(null);
			// mCamera.stopPreview();
			// mCamera.release();
			// mCamera = null;
		}
		
		
		 PreviewCallback previewCallback = new PreviewCallback (){
		 @Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			if(mCaptureFrame){
				mCaptureFrame=false;
				frame[0] = (byte)frameNumber;
			  Size previewSize = camera.getParameters().getPreviewSize(); 
			  int width = previewSize.width;
			  
			  int height = previewSize.height;
			  System.out.println("mCamera width: "+width+" mCamera height: "+height);
			  int[] argb8888 = new int[width*height*3/2];
			  decodeYUV(argb8888, data, width, height);
			  Bitmap bitmap = Bitmap.createBitmap(argb8888, width,
			                      height, Config.ARGB_8888);
			  if(bitmap==null){
				  System.out.println("bitmap is null");
			  }
			  else {
				  pupilImgSet.add(bitmap);
				  System.out.println("Frame captured");
				  capture.setImageBitmap(pupilImgSet.get(pupilImgSet.size()-1));
			  }
			  
			  frameNumber++;
			  }
		}};
		

		 
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

	}

	
}
