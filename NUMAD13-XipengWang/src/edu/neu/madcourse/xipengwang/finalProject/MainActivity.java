package edu.neu.madcourse.xipengwang.finalProject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
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
import edu.neu.madcourse.xipengwang.R;




public class MainActivity extends Activity implements SurfaceHolder.Callback {
	private CameraPreview camPreview; 
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    public MediaRecorder mrec = new MediaRecorder();
    private Button startRecording = null;
    private CamcorderProfile camcorderProfile;
    //private Button stopRecording = null;
    File video;
    private Camera mCamera;
    private Handler mHandler  = new Handler();
    Timer myTimer = new Timer();
    private boolean flashOn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        setContentView(R.layout.final_main);
        Log.i(null , "Video starting");
        startRecording = (Button)findViewById(R.id.vedio_start_button);
        startRecording.setOnClickListener(new StartListener());
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
		  mCamera = getCameraInstance();  	 
		  		  camPreview = new CameraPreview(this,mCamera);
		  	  FrameLayout mainLayout = (FrameLayout) findViewById(R.id.camera_preview);
		  		  mainLayout.addView(camPreview);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "StartRecording");
        menu.add(0, 1, 0, "StopRecording");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case 0:
            try {
                startRecording();
            } catch (Exception e) {
                String message = e.getMessage();
                Log.i(null, "Problem Start"+message);
                mrec.release();
            }
            break;

        case 1: //GoToAllNotes
            mrec.stop();
            mrec.release();
            mrec = null;
            break;

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
                            System.out.println(newFile.getName());
                    } catch (IOException e) {
                            Log.v("mr","Couldn't create file");
                            e.printStackTrace();
                            finish();
                    }
            } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            try {
                            File newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
                            mrec.setOutputFile(newFile.getAbsolutePath());
                            System.out.println(newFile.getName());
                    } catch (IOException e) {
                            Log.v("mr","Couldn't create file");
                            e.printStackTrace();
                            finish();
                    }
            } else {
            try {
                            File newFile = File.createTempFile("videocapture", ".mp4", Environment.getExternalStorageDirectory());
                            mrec.setOutputFile(newFile.getAbsolutePath());
                            System.out.println(newFile.getName());
                    } catch (IOException e) {
                            Log.v("mr","Couldn't create file");
                            e.printStackTrace();
                            finish();
                    }

            }
        
        mrec.prepare();
        mrec.start();
    }

    protected void stopRecording() {
        mrec.stop();
        mrec.release();
        mCamera.release();
    }

    private void releaseMediaRecorder(){
        if (mrec != null) {
            mrec.reset();   // clear recorder configuration
            mrec.release(); // release the recorder object
            mrec = null;
            //mCamera.lock();           // lock camera for later use
        }
    }


    public class StartListener implements OnClickListener{
    	
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				//mHandler.postDelayed(TakePicture, 300);
    				v.setOnClickListener(null);
    				try {
    	                startRecording();
    	            } catch (Exception e) {
    	                String message = e.getMessage();
    	                Log.i(null, "Problem Start"+message);
    	                mrec.release();
    	            }
    				myTimer.scheduleAtFixedRate(myTimerTask, 100, 2000);	
    			}
    			
    		}
    		TimerTask myTimerTask= new TimerTask(){
    			private int counter = 0;

    		@Override		 
    		    public void run() {
    		        mHandler.post(new Runnable() {
    		            public void run() {
    	    		        if(counter == 3) {
    	    		        	
    	    		        	mrec.stop();
    	    		            mrec.release();
    	    		            mrec = null;
    	    		            System.out.println("run finish");
    	    		        	myTimer.cancel();
    	    		        }else{
    		            	if(flashOn){
    		        			Camera.Parameters params = mCamera.getParameters(); 
    		            	params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH
    		            			); 
   		 				 	mCamera.setParameters(params);
   		 				   mCamera.startPreview();
   		 				System.out.println("run "+flashOn);
   		 				    flashOn=!flashOn;
    		            	}
    		            	else {
    		        			Camera.Parameters params = mCamera.getParameters(); 
    		            		params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); 
    			 				mCamera.setParameters(params);
    			 				 mCamera.startPreview();
    			 				System.out.println("run "+flashOn);
    			 				flashOn=!flashOn;
    			 				
							}
    		            	counter++;}
    	            }
    		        });

    		    }
    			
    		};
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    			public CameraPreview(Context context,Camera camera)
    			{
    				
    				 super(context);
    				 mCamera = camera;
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
		  //mCamera.setParameters(parameters);
		 // mCamera.setPreviewDisplay(mSurfHolder);
		//  mCamera.setPreviewCallback(previewCallback);
		 
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
            Log.d("surfaceCreated", "camera preview created successfully ");
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

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	 PreviewCallback previewCallback = new PreviewCallback (){
		 	 @Override
		 		public void onPreviewFrame(byte[] data, Camera camera) {
		 		  
		 			// TODO Auto-generated method stub
		 			
		 			  }
		 		};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
}