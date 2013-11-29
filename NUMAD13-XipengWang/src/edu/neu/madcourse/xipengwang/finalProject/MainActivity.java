package edu.neu.madcourse.xipengwang.finalProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera;
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
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.comm.HighScore;
import edu.neu.madcourse.xipengwang.dabble.Dabble;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;




public class MainActivity extends Activity{
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
    private boolean mCaptureFrame =false;
    private int frameNumber = 0;
    private byte[] frame = new byte[1];
    private ArrayList<Bitmap> pupilImgSet = new ArrayList<Bitmap>();
    private ImageView capture;
    VideoRecordTask videoRecordTask =new VideoRecordTask();
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);

        setContentView(R.layout.final_main);
        Log.i(null , "Video starting");
        startRecording = (Button)findViewById(R.id.vedio_start_button);
        capture = (ImageView)findViewById(R.id.capture);
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
    				myTimer.scheduleAtFixedRate(myTimerTask, 100, 500);	
    			}
    			
    		}
    		TimerTask myTimerTask= new TimerTask(){
    			private int counter = 0;

    		@Override		 
    		    public void run() {
    		        mHandler.post(new Runnable() {
    		            public void run() {
    	    		        if(counter == 11) {
    	    		        	
    	    		        	mrec.stop();
    	    		            mrec.release();
    	    		            //mrec = null;
    	    		            System.out.println("run finish");
    	    		        	myTimer.cancel();
    	    		        	Intent intent = new Intent(MainActivity.this, VideoPlayer.class);
    	    		        	intent.putExtra("path", path);
    	    		        	startActivity(intent);
    	    		        	finish();
    	    		        }
    	    		        else{
    	    		        	if(counter==3){
	    		            		mCaptureFrame=true;
	    		        			Camera.Parameters params = mCamera.getParameters(); 
	    		        			params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH); 
	   		 				 		mCamera.setParameters(params);
	   		 				 		mCamera.startPreview();
	   		 				 		System.out.println("run "+flashOn);
	   		 				 		flashOn=!flashOn;
	   		 				 		counter++;
    	    		        	}
    	    		        	else {
	    		            		if(counter==7){
		    		            		mCaptureFrame=true;
		    		        			Camera.Parameters params = mCamera.getParameters(); 
		    		            		params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF); 
		    			 				mCamera.setParameters(params);
		    			 				mCamera.startPreview();
		    			 				System.out.println("run "+flashOn);
		    			 				flashOn=!flashOn;
		    			 				counter++;
	    		            		}
	    		            		else {
	    		            			mCaptureFrame=true;
	    		            			counter++;
									}
    	    		        	}
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
		 // mCamera.setPreviewCallback(previewCallback);
		 
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