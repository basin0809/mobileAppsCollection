package edu.neu.madcourse.xipengwang.finalProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private ImageView grid1;
    private ImageView grid2;
    private ImageView grid3;
    private ImageView grid4;
    //private Button stopRecording = null;
    File video;
    private Camera mCamera;
    private Handler mHandler  = new Handler();
    Timer myTimer = new Timer();
    Timer myTimer2 = new Timer();
    private boolean flashOn = false;
    private boolean mCaptureFrame =false;
    private boolean mCaptureUserFrame =false;
    private int frameNumber = 0;
    private byte[] frame = new byte[1];
    //private ImageView capture;
    //ZoomControls zoomControls;
    RelativeLayout relativeLayout;
    Rect focusArea;
    VideoRecordTask videoRecordTask =new VideoRecordTask();
    private String path;
    private int imgCounter =0;
    private boolean jump= false;
    File newFile;
    
    @Override
     public void onCreate(Bundle savedInstanceState) {
    	Log.d("VPC", "final main create");
        super.onCreate(savedInstanceState);
        PupilImgs.imgPathes.clear();
   	 PupilImgs.processedImgSet.clear();
   	 PupilImgs.pupilBitMap.clear();
   	 PupilImgs.pupilImgSet.clear();
   	 PupilImgs.userflashOffIimgPathes.clear();
   	 PupilImgs.userflashOnImgPathes.clear();
   	 PupilImgs.userFlashOffPupilBitMap.clear();
   	 PupilImgs.userFlashOnPupilBitMap.clear();
   	 PupilImgs.userFlasOffImgSet.clear();
   	 PupilImgs.userFlasOnImgSet.clear();
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        setFullScreen(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.final_main);
        Log.i(null , "Video starting");
        relativeLayout = (RelativeLayout)findViewById(R.id.final_mian_layout); 
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;
        int h = size.y;
        PupilImgs.screenW = w;
        PupilImgs.screenH = h;
     
        
       
		

		grid1 = new ImageView(this);
		grid2 = new ImageView(this);
		grid3 = new ImageView(this);
		grid4 = new ImageView(this);
		
		grid1.setBackgroundResource(R.drawable.grid2);
		grid2.setBackgroundResource(R.drawable.grid2);
		grid3.setBackgroundResource(R.drawable.grid1);
		grid4.setBackgroundResource(R.drawable.grid1);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(4, h);
		params.leftMargin = (int)(w*0.375);
		params.topMargin = 0;
		
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(4, h);
		params2.leftMargin = (int)(w*0.625);
		params2.topMargin = 0;
		
		RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(w, 4);
		params3.leftMargin =0;
		params3.topMargin = (int)(h*0.375);
		
		RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(w, 4);
		params4.leftMargin = 0;
		params4.topMargin = (int)(h*0.625);
		
		relativeLayout.addView(grid1, params);
		relativeLayout.addView(grid2, params2);
		relativeLayout.addView(grid3, params3);
		relativeLayout.addView(grid4, params4);
        
        //zoomControls = (ZoomControls) findViewById(R.id.CAMERA_ZOOM_CONTROLS);

        //startRecording.setOnClickListener(new StartListener());
        
        
        
        //surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        //surfaceHolder = surfaceView.getHolder();
       // surfaceHolder.addCallback(this);
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    
    protected void outputUnProcessedImgs(Bitmap unprocessedImg, ArrayList<String> pathes){
        //Log.i("SAVE IMAGE", "start save");
		
       
        	File newFile = null;
        	try {
				newFile =File.createTempFile("unprocessed"+imgCounter, ".PNG", Environment.getExternalStorageDirectory());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {    
	        	pathes.add(newFile.getAbsolutePath());
	            FileOutputStream fos = new FileOutputStream(newFile);
	            unprocessedImg.compress(Bitmap.CompressFormat.PNG, 100, fos);
	            fos.flush();
	            fos.close();
	        } catch (IOException e) {
	            Log.e("error", e.getMessage());
	            e.printStackTrace();
	        }
	        imgCounter++;
        
	}
    
    protected void setFullScreen(Context currContext) { 
    	((Activity) currContext).requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	((Activity) currContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); 
    	}
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("VPC", "final main destroy");
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("VPC", "final main pause");
		if(jump){

			releaseCamera(); 
		}
		else{
		 PupilImgs.pupilImgSet.clear();
		  PupilImgs.userFlasOnImgSet.clear();
		  PupilImgs.userFlasOffImgSet.clear();
		try {
			mrec.stop();
			mrec.release();
			
			if(newFile.exists()){
				newFile.delete();
			}
		} catch (IllegalStateException e) {
			// TODO: handle exception
		}	
		myTimer.cancel();
		myTimer2.cancel();
		releaseCamera(); }
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("VPC", "final main resume");
		   startRecording = (Button)findViewById(R.id.video_start_button);
	        //startRecording.setVisibility(View.GONE);
	        startFocus = (Button)findViewById(R.id.video_focus_button);
	        
	        startFocus.setVisibility(View.VISIBLE);
	        //capture = (ImageView)findViewById(R.id.capture);
	        mCamera = getCameraInstance();  	 
			camPreview = new CameraPreview(this,mCamera);
			  
			 FrameLayout mainLayout = (FrameLayout) findViewById(R.id.camera_preview);
			 mainLayout.addView(camPreview);
	        
	        startFocus.setOnClickListener(new FocusListener());
	        
	        myTimer = new Timer();
	        myTimer2 = new Timer();
		 
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

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	    final double ASPECT_TOLERANCE = 0.05;
	    double targetRatio = (double) w/h;

	    if (sizes==null) return null;

	    Size optimalSize = null;

	    double minDiff = Double.MAX_VALUE;

	    int targetHeight = h;

	    // Find size
	    for (Size size : sizes) {
	        double ratio = (double) size.width / size.height;
	        if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	        if (Math.abs(size.height - targetHeight) < minDiff) {
	            optimalSize = size;
	            minDiff = Math.abs(size.height - targetHeight);
	        }
	    }

	    if (optimalSize == null) {
	        minDiff = Double.MAX_VALUE;
	        for (Size size : sizes) {
	            if (Math.abs(size.height - targetHeight) < minDiff) {
	                optimalSize = size;
	                minDiff = Math.abs(size.height - targetHeight);
	            }
	        }
	    }
	    return optimalSize;
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
            				newFile=File.createTempFile("videocapture", ".3gp", Environment.getExternalStorageDirectory());
                            
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
                            newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
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
                            newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
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
			v.setOnClickListener(null);
			startRecording.setOnClickListener(null);
			startRecording.setVisibility(View.INVISIBLE);
			mCamera.autoFocus(cb);
			//Parameters parameters = mCamera.getParameters();
			myTimer2 = new Timer();
			myTimer2.scheduleAtFixedRate(new TimerTask(){
    			private int counter = 0;

    		@Override		 
    		    public void run() {
    		        mHandler.post(new Runnable() {
    		            public void run() {
    		            	if(counter == 1) {
    		            		
    		            		
    		            		startRecording.setVisibility(View.VISIBLE);
    		            		startRecording.setOnClickListener(new StartListener());
     	    		        	myTimer2.cancel();
     	    		        	startFocus.setOnClickListener(new FocusListener());
     	    		        }
    		            	else{
    		            	
    		            	//startRecording.setVisibility(View.VISIBLE);
    		            	//startRecording.setOnClickListener(new StartListener());
    		            	counter++;
    		            	}
	    	            }
	    		        }); 
	                     
	    		    }
	    			
	    		}, 1000, 500);
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
    				startFocus.setVisibility(View.INVISIBLE);
	            	startFocus.setOnClickListener(null);
    				try {
						startRecording();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				myTimer.scheduleAtFixedRate(new TimerTask(){
    	    			private int counter = 0;

    	        		@Override		 
    	        		    public void run() {
    	        		        mHandler.post(new Runnable() {
    	        		            public void run() {
    	        		            	switch (counter) {
    	    							case 67:// JUMP TO VIDEO PLAYER ACTIVITY 67
    	    								jump = true;
    	    								 System.out.println("run finish");   	    		          		            
    	    	    	    		         myTimer.cancel();
    	    	    	    		         Intent intent = new Intent(MainActivity.this, VideoPlayer.class);
    	    	    	    		         intent.putExtra("path", path);
    	    	    	    		         intent.putExtra("flashDuration", 2);
    	    	    	    		         startActivity(intent);
    	    	    	    		         finish();
    	    								break;
    	    							case 66://COUNTER, WAIT FOR FINISHING VIDEO WRITE
    	    								counter++;
    	    								break;
    	    							case 65://COUNTER, WAIT FOR FINISHING VIDEO WRITE
    	    								counter++;
    	    								break;
    	    							case 64://COUNTER, WAIT FOR FINISHING VIDEO WRITE
    	    								counter++;
    	    								break;
    	    							case 63://COUNTER, WAIT FOR FINISHING VIDEO WRITE
    	    								counter++;
    	    								break;
    	    							case 62://COUNTER, WAIT FOR FINISHING VIDEO WRITE
    	    								counter++;
    	    								break;
    	    							
    	    							case 61://STOP RECORD VIDEO, WRITE VIDEO FILE TO SDCARD
    	    								mrec.stop();
    	        	    		            mrec.release();
    	        	    		            counter++;
    	        	    		            System.out.println("record finish");
    	    								break;
    	    								
    	    							case 0://FLASH ON
    	    								Camera.Parameters params = mCamera.getParameters(); 
    	        		        			params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);     		        				    		        			
    	       		 				 		mCamera.setParameters(params);
    	       		 				 		mCamera.startPreview();	   		 				 
    	       		 				 		System.out.println("run "+flashOn);
    	       		 				 		flashOn=!flashOn;
    	       		 				 		counter++;
    	    								break;
    	    							case 1:// LOCK WHITE BALANCE
    	    								Camera.Parameters params2 = mCamera.getParameters(); 
    	        		        			if(params2.isAutoWhiteBalanceLockSupported())
    	        		        			{
    	        		        			params2.setAutoExposureLock(true);
    	        		        			}
    	        		        			else {
    	    									params2.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
    	    								}
    	        		        			mCamera.setParameters(params2);
    	       		 				 		mCamera.startPreview();
    	    		            			counter++;
    	    								break;							
    	    							case 8:// CAPTURE CONSTRICTION FRAME
    	    								mCaptureFrame=true;
    	    								counter++;
    	    								break;
    	    							case 30:// FLASH OFF, RELEASE WHITE BALANCE LOCK								
    	        		        			Camera.Parameters params3 = mCamera.getParameters();    		        			
    	        		            		params3.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); 
    	        		            		if(params3.isAutoWhiteBalanceLockSupported())
    	        		            		{
    	        		            		params3.setAutoExposureLock(false);
    	        		            		params3.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
    	        		            		}
    	        			 				mCamera.setParameters(params3);
    	        			 				mCamera.startPreview();		    			 				
    	        			 				System.out.println("run "+flashOn);
    	        			 				flashOn=!flashOn;
    	        			 				counter++;
    	    								break;
    	    							case 31:// CHANGE WHITE BALANCE TO CLOUDY 
    	    								Camera.Parameters params4 = mCamera.getParameters();
    	    	            				if(params4.isAutoWhiteBalanceLockSupported())
    	        		            		{
    	    	            					params4.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);			    		            		
    	        		            		}
    	    	            				else {
    	    	            					params4.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT);
    	    								}
    	    	            				mCamera.setParameters(params4);
    	        			 				mCamera.startPreview();	
    	    	            				counter++;
    	    								break;
    	    							case 43:// CAPTURE DILATION FRAME
    	    								mCaptureFrame=true;
    	    								counter++;
    	    								break;
    	    							default:// CAPTURE USER FRAMES		
    	    								Log.d("ImgCapture", "User Frame captured: "+counter);
    	    								mCaptureUserFrame=true;
    	    								counter++;						
    	    								break;
    	    							}
    	        		            }}
    	        		        );
    	                         
    	        		    }
    	        			
    	        		}, 0, 66);	
    			}
    			
    		}
    		
    		
    		
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
    								
    								  if(bitmap==null){
    									  System.out.println("bitmap is null");
    								  }
    								  else {
    									  PupilImgs.pupilImgSet.add(bitmap);
    									  Log.d("ImgCapture", "pupilImgSet"+PupilImgs.pupilImgSet.size());
    									  System.out.println("Frame captured");
    									 // capture.setImageBitmap(PupilImgs.pupilImgSet.get(PupilImgs.pupilImgSet.size()-1));
    									  System.out.println("image "+PupilImgs.pupilImgSet.size());
    								  }
    								  
    								  frameNumber++;
    								  }
    								else {
										if (mCaptureUserFrame&&flashOn) {
											mCaptureUserFrame=false;
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
	    									  //outputUnProcessedImgs(bitmap);
	    									  PupilImgs.userFlasOnImgSet.add(bitmap);	    									 
	    									  Log.d("ImgCapture", "Flash On User Frame captured");
	    									 // capture.setImageBitmap(PupilImgs.userFlasOnImgSet.get(PupilImgs.userFlasOnImgSet.size()-1));
	    									  //System.out.println("image "+PupilImgs.pupilImgSet.size());
	    								  }
										}
										else {
											if (mCaptureUserFrame&&!flashOn) {
												mCaptureUserFrame=false;
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
		    									  //outputUnProcessedImgs(bitmap);
		    									  PupilImgs.userFlasOffImgSet.add(bitmap);
		    									  Log.d("ImgCapture", "Flash Off User Frame captured");		    									  
		    									  //capture.setImageBitmap(PupilImgs.userFlasOffImgSet.get(PupilImgs.userFlasOffImgSet.size()-1));
		    									 // System.out.println("image "+PupilImgs.pupilImgSet.size());
		    								  }
											}
											else {
												mCaptureUserFrame=false;
												mCaptureFrame=false;
											}
										}
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
            Camera.Parameters params2 = mCamera.getParameters();
        	//int currentZoomLevel = params2.getMaxZoom();
            //int maxZoomRatio = params2.getZoomRatios().get(params2.getZoomRatios().size());
                params2.setZoom(30);
               
                //PupilImgs.zoomLevel = currentZoomLevel;
                List<Size> sizes = params2.getSupportedPreviewSizes();
                Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

                params2.setPreviewSize(optimalSize.width, optimalSize.height);
                mCamera.setParameters(params2);
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