package edu.neu.madcourse.xipengwang.finalProject;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import edu.neu.madcourse.xipengwang.R;

public class IdentifyPupilIris extends Activity implements Callback{
	private TextView dilationButton = null;
	//private Button constrictionButton = null;
	private Button pupilButton = null;
	private Button irisButton = null;
	private Button jumpButton = null;
	private SeekBar seekBar = null;
	private SurfaceView mSurfaceView = null;
	private SurfaceHolder mSurfaceHolder = null;
	private ScaleGestureDetector mScaleGestureDetector = null;
	private Bitmap mBitmap = null;
	private boolean isIdentifyingIris = false;
	private boolean isIdentifyingPupil = false;
	private boolean isShowingDilation = true;
	private int pupilColor = Color.GREEN;
	private int irisColor = Color.RED;
	private int topPos;
	private float irisX = 100;
	private float irisY = 100;
	private float irisR = 100;
	private float pupilX = 100;
	private float pupilY = 100;
	private float pupilR = 50;
	private Paint irisPaint = null;
	private Paint pupilPaint = null;
	private float scaleFactor = 1;
	private float irisFactor = 1;
	private float pupilFactor =1;
	private float imgX = 0;
	private float imgY = 0;
	private float defaultIrisR;
	private float defaultPupilR;
	private Rect bitmapRect;
	private Button tutButton;
	int w;
	int h;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.final_identifypupilpris);
		mBitmap = PupilImgs.pupilImgSet.get(3);
		Log.d("bitmapSize", mBitmap.getWidth()+" "+mBitmap.getHeight());
		Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        w = size.x;
        h = size.y;
        
       // bitmapRect = new Rect(0, 0, w*w/h, w);
        defaultIrisR = h/4;
        defaultPupilR = h/6;
        irisX = (float) PupilImgs.dilationRes[2];
        irisY = (float) PupilImgs.dilationRes[3];
        pupilX = (float) PupilImgs.dilationRes[4];
        pupilY = (float) PupilImgs.dilationRes[5];
		irisR = (float) PupilImgs.dilationRes[0];
		pupilR = (float) PupilImgs.dilationRes[1];
		irisPaint = new Paint();
		irisPaint.setStyle(Paint.Style.STROKE);
		irisPaint.setStrokeWidth(8);
		irisPaint.setColor(irisColor);
		
		pupilPaint = new Paint();
		pupilPaint.setStyle(Paint.Style.STROKE);
		pupilPaint.setColor(pupilColor);
		pupilPaint.setStrokeWidth(8);
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		
		
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		//mSurfaceHolder.setFixedSize(200,200);
		mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGesturListener());
		dilationButton = (TextView) findViewById(R.id.dilation_image_button);
		
		tutButton = (Button)findViewById(R.id.show_id_tut);
		pupilButton = (Button) findViewById(R.id.pupil_button);
		irisButton = (Button) findViewById(R.id.iris_button);
		jumpButton = (Button) findViewById(R.id.dilation_confirm_button);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		ButtonClickListener bl = new ButtonClickListener();
		//dilationButton.setOnClickListener(bl);
		//constrictionButton.setOnClickListener(bl);
		jumpButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
		pupilButton.setOnClickListener(bl);
		irisButton.setOnClickListener(bl);
		jumpButton.setOnClickListener(bl);
		tutButton.setOnClickListener(bl);
		pupilButton.setBackgroundColor(pupilColor);
		irisButton.setBackgroundColor(irisColor);
		//DrawAll();
		seekBar.setOnSeekBarChangeListener(new SeekBarListener());
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DrawAll();
			}
		}, 1000);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		isIdentifyingIris=false;
		isIdentifyingPupil =true;
		irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
		
		pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class SeekBarListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if(isIdentifyingIris){
				irisFactor = (float) (progress/50.0);
			} else if(isIdentifyingPupil){
				pupilFactor = (float) (progress/50.0);
			} else {
				//scaleFactor = progress/50;
			}
			DrawAll();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {

			case R.id.iris_button:
				isIdentifyingPupil=false;
				isIdentifyingIris=true;
				
			
				irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
				
				
				pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				break;
			case R.id.pupil_button:
				
				isIdentifyingPupil = true;
				isIdentifyingIris=false;
				irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				
				pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
				
				
				break;
			case R.id.dilation_confirm_button:
				
				irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				   PupilImgs.dilationRes[2] = irisX;
			       PupilImgs.dilationRes[3] =irisY;
			       PupilImgs.dilationRes[4] =pupilX;
			       PupilImgs.dilationRes[5] =pupilY;
				   PupilImgs.dilationRes[0] =irisR;
				   PupilImgs.dilationRes[1] =pupilR;
				   if(PupilImgs.constirctionTime<=0 && PupilImgs.dilationTime<=0){
					   PupilImgs.constirctionTime=(float) 0.67;
					   PupilImgs.dilationTime=(float) 2.67;
				   }
				   else {
					if(PupilImgs.constirctionTime<=0 && PupilImgs.dilationTime>0){
						PupilImgs.constirctionTime=(float) 0.67;
					}
					else {
						if(PupilImgs.constirctionTime>0 && PupilImgs.dilationTime<=0){
							PupilImgs.dilationTime=(float) 2.67;
						}
						else {
							if(PupilImgs.constirctionTime>0 && PupilImgs.dilationTime>0){
								
							}
						}
					}
				   }
				   float constrictionRes =
						   (float) ((pupilR/irisR-PupilImgs.dilationRes[1]/PupilImgs.dilationRes[0])/PupilImgs.constirctionTime);
				   float dilationRes =
						   (float) ((PupilImgs.dilationRes[1]/PupilImgs.dilationRes[0]-pupilR/irisR)/PupilImgs.dilationTime);
				   AlertDialog.Builder alertDialogBuilder2=new AlertDialog.Builder(IdentifyPupilIris.this);  
					
					alertDialogBuilder2.setTitle("Measurement Result")

		            .setMessage("Constriction Rate: "+constrictionRes+"\n"+"Constriction Time: "+
		            PupilImgs.constirctionTime+"sec"+"\n"+"Dilation Rate: "+ dilationRes+"\n"
		            +"Dilation Time: "+PupilImgs.dilationTime+"sec")

		            
	         .setNegativeButton("Redo",   new DialogInterface.OnClickListener(){
	                 public void onClick(DialogInterface dialoginterface, int i){
	                	
	                	 Intent intent = new Intent(IdentifyPupilIris.this,ConstrictIdentifyPupilIris.class);	       
	 			        startActivity(intent);
	 			        finish();
	 			       
	                 }
	         })
	         .setNeutralButton("New Measurement", new DialogInterface.OnClickListener(){
	                 public void onClick(DialogInterface dialoginterface, int i){
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
	                		                	 
	                	 Intent intent = new Intent(IdentifyPupilIris.this,MainActivity.class);	       
		 			        startActivity(intent);
		 			        finish();	 
	                 }
	         })
	         .setPositiveButton("Quit", new DialogInterface.OnClickListener(){
	                 public void onClick(DialogInterface dialoginterface, int i){
	                	 //setResult(RESULT_OK);
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

	                	
		 			        finish();	 
	                 }
	         });
					AlertDialog alertDialog2 = alertDialogBuilder2.show(); 
				
				break;	

			case R.id.show_id_tut:
				 final AlertDialog.Builder alertDialogBuilderNext=new AlertDialog.Builder(IdentifyPupilIris.this);  					
				 alertDialogBuilderNext.setTitle("Tutorial 4/4")
				  .setMessage("Press Next button to check the final measurement result.")            
			      .setNegativeButton("Get it",   new DialogInterface.OnClickListener(){
			              public void onClick(DialogInterface dialoginterface, int i){
			            	  setResult(RESULT_CANCELED);
			              }
			      });
				 
				 
				  final AlertDialog.Builder alertDialogBuilderSeek=new AlertDialog.Builder(IdentifyPupilIris.this);  					
				  alertDialogBuilderSeek.setTitle("Tutorial 3/4")
				 .setMessage("Press Identify Iris button to modify the red circle. Touch the screen to change the circle's location. Drag the seekbar"
		           		+ "to change the circle's radius."+"\n"+"CAUTION: changing the circles will result in the changes to the final measurement result.")	            
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
				  
				  
				   final AlertDialog.Builder alertDialogBuilderStep=new AlertDialog.Builder(IdentifyPupilIris.this);  
					
				   alertDialogBuilderStep.setTitle("Tutorial 2/4")
		           .setMessage("Press Identify Puplil button to modify the green circle. Touch the screen to change the circle's location. Drag the seekbar"
		           		+ "to change the circle's radius."+"\n"+"CAUTION: changing the circles will result in the changes to the final measurement result.")     
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
				   
				   
				   
				   AlertDialog.Builder alertDialogBuilderLocation=new AlertDialog.Builder(IdentifyPupilIris.this);  				
				   alertDialogBuilderLocation.setTitle("Tutorial 1/4")
		          .setMessage("The circles displayed on the screen indicate the dilated pupil and iris of tester."+"\n"+"Red circle: iris"+"\n"+"Green circle: pupil")    
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
				
				break;
			default:
				break;
			}
			
		}
		
	}
	
	private void DrawAll(){
		topPos = mSurfaceView.getTop();
		Matrix mMatrix = new Matrix();
		mMatrix.setScale(scaleFactor, scaleFactor);
		Canvas mCanvas = mSurfaceHolder.lockCanvas();
		mCanvas.drawColor(Color.BLACK);
		//mMatrix.setRotate(90);
		//mCanvas.drawBitmap(mBitmap, mMatrix, null);
		//mCanvas.setMatrix(mMatrix);
		if(mBitmap!=null){
			//mCanvas.drawBitmap(mBitmap, imgX-mBitmap.getWidth()*scaleFactor/2,imgY-topPos-mBitmap.getHeight()*scaleFactor/2,null);
			mCanvas.drawBitmap(mBitmap, imgX, imgY, null);
		} else{
			mCanvas.drawBitmap(mBitmap, imgX, imgY, null);
		}
		mCanvas.drawCircle(scaleFactor*irisX, (irisY*scaleFactor-topPos), defaultIrisR*irisFactor, irisPaint);
		mCanvas.drawCircle(scaleFactor*pupilX, (pupilY*scaleFactor-topPos), defaultPupilR*pupilFactor, pupilPaint);
		mSurfaceHolder.unlockCanvasAndPost(mCanvas);
		mSurfaceHolder.lockCanvas(new Rect(0,0,0,0));
		mSurfaceHolder.unlockCanvasAndPost(mCanvas);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		int pointCount = event.getPointerCount();
		if (pointCount==1 &&!isIdentifyingIris&&!isIdentifyingPupil) {
			//imgX = event.getX();
			//imgY = event.getY();
			//DrawAll();
		} if(isIdentifyingIris){
			irisX = event.getX();
			irisY = event.getY()-defaultIrisR;
			DrawAll();
			 
		} else if(isIdentifyingPupil){
			pupilX = event.getX();
			pupilY = event.getY()-defaultPupilR;
			DrawAll();
		}
		/*if(pointCount==1){
			
			
			if(isIdentifyingIris){
				irisX = event.getX();
				irisY = event.getY();
				Log.d("TouchE", "Iris"+pointCount+"");
				Log.d("TouchE", "Iris "+irisX+"");
				DrawAll();
				return mScaleGestureDetector.onTouchEvent(event);
			} else if(isIdentifyingPupil){
				pupilX = event.getX();
				pupilY = event.getY();
				Log.d("TouchE", "Pupil"+pointCount+"");
				Log.d("TouchE", "Pupil "+pupilX+"");
				DrawAll();
				return mScaleGestureDetector.onTouchEvent(event);
				
			}
			
			return false;
			
		} else	{*/
			return true;//mScaleGestureDetector.onTouchEvent(event);

		
		
	}

	class ScaleGesturListener implements OnScaleGestureListener{

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if(!isIdentifyingIris&&!isIdentifyingPupil){
				
				//scaleFactor = detector.getScaleFactor();

				Log.d("TouchE", "scaleFactor "+scaleFactor+"");
				DrawAll();
				
				
			}
			if(isIdentifyingIris) {
				//irisR = irisR*detector.getScaleFactor();
				irisR = detector.getCurrentSpan()/8;
				
				irisX = detector.getFocusX();//+detector.getCurrentSpanX();
				irisY = detector.getFocusY();//+detector.getCurrentSpanY();
				DrawAll();
				Log.d("TouchE", "irisR "+irisR+"");
			} else if (isIdentifyingPupil){
				//pupilR = pupilR*detector.getScaleFactor();
				pupilR = detector.getCurrentSpan()/12;
				pupilX = detector.getFocusX();//-detector.getCurrentSpanX();
				pupilY = detector.getFocusY();//-detector.getCurrentSpanY();
				DrawAll();
				Log.d("TouchE", "pupilR "+pupilR+"");
			}
			Log.d("TouchE", "onscale");
			return false;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		DrawAll();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		DrawAll();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	

}
