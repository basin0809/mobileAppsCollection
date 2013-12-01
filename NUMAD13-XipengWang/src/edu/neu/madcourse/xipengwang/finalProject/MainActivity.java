package edu.neu.madcourse.xipengwang.finalProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;
import edu.neu.madcourse.xipengwang.R;




public class MainActivity extends Activity{
	private CameraPreview camPreview; 
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();
    private Button startRecording = null;
    private Button startFocus = null;
    private CamcorderProfile camcorderProfile;
    //private Button stopRecording = null;
    File video;
    private Camera mCamera;
    private Handler mHandler  = new Handler();
    Timer myTimer = new Timer();
    Timer myTimer2 = new Timer();
    private boolean flashOn = false;
    private boolean mCaptureFrame =false;
    private int frameNumber = 0;
    private byte[] frame = new byte[1];
    private ArrayList<Bitmap> pupilImgSet = new ArrayList<Bitmap>();
    private ImageView capture;
    Rect focusArea;
    VideoRecordTask videoRecordTask =new VideoRecordTask();
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        setContentView(R.layout.final_main);
        Log.i(null , "Video starting");
        startRecording = (Button)findViewById(R.id.video_start_button);
        startRecording.setVisibility(View.GONE);
        startFocus = (Button)findViewById(R.id.video_focus_button);
        capture = (ImageView)findViewById(R.id.capture);
        mCamera = getCameraInstance();  	 
		  camPreview = new CameraPreview(this,mCamera);
		  
		  FrameLayout mainLayout = (FrameLayout) findViewById(R.id.camera_preview);
		  mainLayout.addView(camPreview);
        setZoomControl(mCamera.getParameters());
        startFocus.setOnClickListener(new FocusListener());
        //startRecording.setOnClickListener(new StartListener());
        
        
        
        //surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        //surfaceHolder = surfaceView.getHolder();
       // surfaceHolder.addCallback(this);
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    
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
		 
	}

	public void setZoomControl(Camera.Parameters params) {
	     
	    ZoomControls zoomControls = (ZoomControls) findViewById(R.id.CAMERA_ZOOM_CONTROLS);

	    if (params.isZoomSupported()) {
	        final int maxZoomLevel = params.getMaxZoom();
	        
	        Log.i("max ZOOM ", "is " + maxZoomLevel);
	        zoomControls.setIsZoomInEnabled(true);
	        zoomControls.setIsZoomOutEnabled(true);

	        zoomControls.setOnZoomInClickListener(new OnClickListener(){
	            public void onClick(View v){
	            	Camera.Parameters params2 = mCamera.getParameters();
	            	int currentZoomLevel = params2.getZoom();
	                if(currentZoomLevel < maxZoomLevel){
	                    currentZoomLevel++;
	                    //mCamera.startSmoothZoom(currentZoomLevel);
	                    params2.setZoom(currentZoomLevel);
	                    mCamera.setParameters(params2);
	                }
	            }
	        });

	        zoomControls.setOnZoomOutClickListener(new OnClickListener(){
	            public void onClick(View v){
	            	Camera.Parameters params2 = mCamera.getParameters();
	            	int currentZoomLevel = params2.getZoom();
	                if(currentZoomLevel > 0){
	                    currentZoomLevel--;
	                    params2.setZoom(currentZoomLevel);
	                    mCamera.setParameters(params2);
	                }
	            }
	        });    
	    }
	    else
	        zoomControls.setVisibility(View.GONE);
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "ISO 100");
        menu.add(0, 1, 0, "ISO 200");
        menu.add(0, 2, 0, "ISO 400");
        menu.add(0, 3, 0, "ISO 800");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case 0:
        	 mCamera.stopPreview();
        	 Camera.Parameters params = mCamera.getParameters(); 		     
		     params.set("iso", "ISO100");
		     mCamera.setParameters(params);
		     mCamera.startPreview();
		     String[] supportedISOs = mCamera.getParameters().get("iso").split(",");
	            int len = supportedISOs.length;
	            for(int i=0; i<len; i++){
	            Log.d("iso", supportedISOs[i]);
	            }
            break;

        case 1: //GoToAllNotes
        	mCamera.stopPreview();
       	     Camera.Parameters params1 = mCamera.getParameters(); 		     
		     params1.set("iso", "ISO200");
		     mCamera.setParameters(params1);
		     mCamera.startPreview();
		     String[] supportedISOs1 = mCamera.getParameters().get("iso").split(",");
	            int len1 = supportedISOs1.length;
	            for(int i=0; i<len1; i++){
	            Log.d("iso", supportedISOs1[i]);
	            }
            break;
            
        case 2: //GoToAllNotes
        	mCamera.stopPreview();
       	     Camera.Parameters params2 = mCamera.getParameters(); 		     
		     params2.set("iso", "ISO400");
		     mCamera.setParameters(params2);
		     mCamera.startPreview();
		     String[] supportedISOs2 = mCamera.getParameters().get("iso").split(",");
	            int len2 = supportedISOs2.length;
	            for(int i=0; i<len2; i++){
	            Log.d("iso", supportedISOs2[i]);
	            }
            break;
        
        case 3: //GoToAllNotes
        	mCamera.stopPreview();
       	     Camera.Parameters params3 = mCamera.getParameters(); 		     
		     params3.set("iso", "ISO800");
		     mCamera.setParameters(params3);
		     mCamera.startPreview();
		     String[] supportedISOs3 = mCamera.getParameters().get("iso").split(",");
	            int len3 = supportedISOs3.length;
	            for(int i=0; i<len3; i++){
	            Log.d("iso", supportedISOs3[i]);
	            };

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
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
    
    protected void startRecording() throws IOException 
    {
        mrec = new MediaRecorder();  // Works well
        mCamera.unlock();

        mrec.setCamera(mCamera);

        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setAudioSource(MediaRecorder.AudioSource.MIC); 

        mrec.setProfile(camcorderProfile);
        mrec.setPreviewDisplay(surfaceHolder.getSurface());
        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) {
            try {
                            File newFile =File.createTempFile("videocapture", ".3gp", Environment.getExternalStorageDirectory());
                            mrec.setOutputFile(newFile.getAbsolutePath());
                            path = newFile.getAbsolutePath();
                            System.out.println(path);
                    } catch (IOException e) {
                            Log.v("mr","Couldn't create file");
                            e.printStackTrace();
                            finish();
                    }
            } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            try {
                            File newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
                            mrec.setOutputFile(newFile.getAbsolutePath());
                            path = newFile.getAbsolutePath();
                            System.out.println(path);
                    } catch (IOException e) {
                            Log.v("mr","Couldn't create file");
                            e.printStackTrace();
                            finish();
                    }
            } else {
            try {
                            File newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
                            mrec.setOutputFile(newFile.getAbsolutePath());
                            path = newFile.getAbsolutePath();
                            System.out.println(path);
                    } catch (IOException e) {
                            Log.v("mr","Couldn't create file");
                            e.printStackTrace();
                            finish();
                    }

            }
        
        mrec.prepare();
        mrec.start();
        mCamera.setPreviewCallback(previewCallback);
    }

    
    
    
    protected void stopRecording() {
    	mCamera.setPreviewCallback(null);
        mrec.stop();
        mrec.release();          
        
    }

    private void releaseMediaRecorder(){
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            //mCamera.lock();           // lock camera for later use
        }
    }

    public class FocusListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mCamera.autoFocus(cb);
			//Parameters parameters = mCamera.getParameters();
			
			myTimer.scheduleAtFixedRate(myTimerStartVideoTask, 3000, 500);
		}
		
	    AutoFocusCallback  cb = new AutoFocusCallback (){

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				// TODO Auto-generated method stub
				 Camera.Parameters params = mCamera.getParameters(); 
			     
			     params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
			     mCamera.setParameters(params);
			}
	    	
	    };
	}
    
    public class StartListener implements OnClickListener{
    	
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				//mHandler.postDelayed(TakePicture, 300);
    				//videoRecordTask =new VideoRecordTask();
    				//videoRecordTask.execute();
    				v.setOnClickListener(null);
    				try {
						startRecording();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				myTimer.scheduleAtFixedRate(myTimerTakeVideoTask, 100, 250);	
    			}
    			
    		}
    		TimerTask myTimerTakeVideoTask= new TimerTask(){
    			private int counter = 0;

    		@Override		 
    		    public void run() {
    		        mHandler.post(new Runnable() {
    		            public void run() {
    		            	
    	    		        if(counter == 17) {
    	    		     
    	    		        	mrec.stop();
    	    		            mrec.release();
    	    		            //mrec = null;
    	    		            System.out.println("run finish");
    	    		        	myTimer.cancel();
    	    		        	Intent intent = new Intent(MainActivity.this, VideoPlayer.class);
    	    		        	intent.putExtra("path", path);
    	    		        	intent.putExtra("flashDuration", 2);
    	    		        	//intent.putExtra("darkDuration", 2);
    	    		        	startActivity(intent);
    	    		        	finish();
    	    		        }
    	    		        else{
    	    		        	if(counter==0){
	    		            		
	    		        			Camera.Parameters params = mCamera.getParameters(); 
	    		        			params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);     		        			
	    		        			//params.setExposureCompensation(params.getExposureCompensation());
	    		        			//params.setExposureCompensation(-12);
	   		 				 		mCamera.setParameters(params);
	   		 				 		mCamera.startPreview();
	   		 				 	Log.d("iso", params.getMaxExposureCompensation()+"");
    		        			Log.d("iso", params.getExposureCompensation()+"");
    		        			Log.d("iso", params.getMinExposureCompensation()+"");
	   		 				 		System.out.println("run "+flashOn);
	   		 				 		flashOn=!flashOn;
	   		 				 		counter++;
    	    		        	}
    	    		        	else {
    	    		        		if(counter==1)
    	    		        		{
    	    		        			mCaptureFrame=true;
    	    		        			Camera.Parameters params = mCamera.getParameters(); 
    	    		        			if(params.isAutoWhiteBalanceLockSupported())
    	    		        			{
    	    		        			params.setAutoExposureLock(true);
    	    		        			}
    	    		        			else {
    										params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
    									}
    	    		        			mCamera.setParameters(params);
    	   		 				 		mCamera.startPreview();
	    		            			counter++;
    	    		        		}
    	    		        		else{
	    		            		if(counter==8){
		    		            		mCaptureFrame=true;
		    		        			Camera.Parameters params = mCamera.getParameters();
		    		        			
		    		            		params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); 
		    		            		if(params.isAutoWhiteBalanceLockSupported())
		    		            		{
		    		            		params.setAutoExposureLock(false);
		    		            		params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
		    		            		}
		    		            		//params.setExposureCompensation(12);
		    			 				mCamera.setParameters(params);
		    			 				mCamera.startPreview();		    			 				
		    			 				System.out.println("run "+flashOn);
		    			 				flashOn=!flashOn;
		    			 				counter++;
	    		            		}
	    		            		else {
	    		            			if(counter==9)
	    		            			{
	    		            				Camera.Parameters params = mCamera.getParameters();
	    		            				if(params.isAutoWhiteBalanceLockSupported())
			    		            		{
			    		            		params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);			    		            		
			    		            		}
	    		            				else {
												params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
											}
	    		            				mCamera.setParameters(params);
			    			 				mCamera.startPreview();	
	    		            				counter++;
	    		            			}
	    		            			else{
	    		            			mCaptureFrame=true;
	    		            			counter++;
	    		            			}
									}
    	    		        		}
    	    		        	}
    		            	}
    	    		        
    	            }
    		        });
                     
    		    }
    			
    		};
    	
    		
    		
    		
    		TimerTask myTimerStartVideoTask= new TimerTask(){
    			private int counter = 0;

    		@Override		 
    		    public void run() {
    		        mHandler.post(new Runnable() {
    		            public void run() {
    		            	if(counter == 1) {
     	    		        	myTimer2.cancel();
     	    		        }
    		            	else{
    		            	startFocus.setVisibility(View.GONE);
    		            	startFocus.setOnClickListener(null);
    		            	startRecording.setVisibility(View.VISIBLE);
    		            	startRecording.setOnClickListener(new StartListener());
    		            	counter++;
    		            	}
	    	            }
	    		        });
	                     
	    		    }
	    			
	    		};
    		
    		 PreviewCallback previewCallback = new PreviewCallback (){
    				public void onPreviewFrame(byte[] data, Camera camera) {
    								// TODO Auto-generated method stub
    						System.out.println("callback "+mCaptureFrame);
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
    								/*  Mat mat = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
    								  Mat partFrameMat = new Mat();
    								  Utils.bitmapToMat(bitmap, mat);
    								  mat.submat((int)(mat.rows()*0.33), 
    										  (int)(mat.rows()*0.67), 
    										  (int)(mat.cols()*0.33), 
    										  (int)(mat.cols()*0.67)).copyTo(partFrameMat);
    								  Bitmap partFrameBitmap = Bitmap.createBitmap(partFrameMat.rows(), partFrameMat.cols(), Config.ARGB_8888);*/
    								  if(bitmap==null){
    									  System.out.println("bitmap is null");
    								  }
    								  else {
    									  pupilImgSet.add(bitmap);
    									  System.out.println("Frame captured");
    									  capture.setImageBitmap(pupilImgSet.get(pupilImgSet.size()-1));
    									  System.out.println("image "+pupilImgSet.size());
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
   
    				
    				
    	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    			public CameraPreview(Context context,Camera camera)
    			{
    				
    				 super(context);
    				
    				 mCamera = camera;
    				 focusArea = new Rect(-300, -300, 300, 300);
    				 Camera.Area cArea = new Camera.Area(focusArea, 500);
    				 ArrayList<Camera.Area> cAreas = new ArrayList<Camera.Area>();
    				 cAreas.add(cArea);
    				 Parameters parameters = mCamera.getParameters();
    				 parameters.setFocusAreas(cAreas);
    				 Log.d("focusArea",cAreas.get(0).toString());
    				 mCamera.setParameters(parameters);
    				 surfaceHolder = this.getHolder();
    				 surfaceHolder.addCallback(this);
    			}
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    	  // TODO Auto-generated method stub
		  //Parameters parameters;
		  //mSurfHolder = holder;
		  if (surfaceHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }
		   
		  try{	      
	      
		  mCamera.startPreview();
		  Log.d("surfaceChanged", "camera preview set successfully");
		  }	  
		  catch(Exception e){
			  Log.d("surfaceChanged", "Error setting camera preview: " + e.getMessage());
		  }
    }
    
    
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	try {
            mCamera.setPreviewDisplay(holder);
           
            mCamera.startPreview(); 
            
            String[] supportedISOs = mCamera.getParameters().get("iso").split(",");
            int len = supportedISOs.length;
            for(int i=0; i<len; i++){
            Log.d("iso", supportedISOs[i]);
            }
            
        } catch (IOException e) {
            Log.d("surfaceCreated", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }


    }
    
    class VideoRecordTask extends AsyncTask<Void, Integer, Void> {
		//private  ProgressDialog pdialog = new ProgressDialog(ShowPupilDetecionResults.this);

	    @Override
	    protected void onPreExecute() {
	    	
	    }

	    // automatically done on worker thread (separate from UI thread)
	    @Override
	    protected Void doInBackground(Void... params) {
	        // Here is where we need to do the downloading of the 
	    	//Mat[] processedImgs = new Mat[PupilImgs.pupilImgSet.size()];
	    	try {
				startRecording();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return null;
	    }
	    // add in a progress bar update

	    @Override
		protected void onPostExecute(Void result) {
			
	    	Toast.makeText(MainActivity.this, "Video Recording Done!",
		            Toast.LENGTH_SHORT).show();
		}

	}

	
}