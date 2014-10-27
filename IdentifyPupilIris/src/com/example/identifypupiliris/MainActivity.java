package com.example.identifypupiliris;

import android.R.drawable;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	private Button dilationButton = null;
	private Button constrictionButton = null;
	private Button pupilButton = null;
	private Button irisButton = null;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pupil2);
		Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;
        int h = size.y;
		irisR = w/4;
		pupilR = w/6;
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
		mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGesturListener());
		dilationButton = (Button) findViewById(R.id.dilation_image_button);
		constrictionButton = (Button) findViewById(R.id.constriction_image_button);
		pupilButton = (Button) findViewById(R.id.pupil_button);
		irisButton = (Button) findViewById(R.id.iris_button);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		ButtonClickListener bl = new ButtonClickListener();
		dilationButton.setOnClickListener(bl);
		constrictionButton.setOnClickListener(bl);
		pupilButton.setOnClickListener(bl);
		irisButton.setOnClickListener(bl);
		pupilButton.setBackgroundColor(pupilColor);
		irisButton.setBackgroundColor(irisColor);
		//DrawAll();
		seekBar.setOnSeekBarChangeListener(new SeekBarListener());
		
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
			case R.id.constriction_image_button:
				isIdentifyingPupil=false;
				isIdentifyingIris=false;
				pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				if(isShowingDilation){

					mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pupil2);
					DrawAll();
					constrictionButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
					dilationButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				}
				break;
			case R.id.dilation_image_button:
				isIdentifyingPupil=false;
				isIdentifyingIris=false;
				pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				if(!isShowingDilation){

					mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pupil1);
					DrawAll();
					constrictionButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
					dilationButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
				}
				break;
			case R.id.iris_button:
				isIdentifyingPupil=false;
				isIdentifyingIris=!isIdentifyingIris;
				
				if(isIdentifyingIris){
					irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
				} else {
					irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				}
				pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				break;
			case R.id.pupil_button:
				isIdentifyingIris=false;
				isIdentifyingPupil = !isIdentifyingPupil;
				irisButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				if(isIdentifyingPupil){
					pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
				} else {
					pupilButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
				}
				
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
		//mCanvas.drawBitmap(mBitmap, mMatrix, null);
		mCanvas.setMatrix(mMatrix);
		if(mBitmap!=null){
			//mCanvas.drawBitmap(mBitmap, imgX-mBitmap.getWidth()*scaleFactor/2,imgY-topPos-mBitmap.getHeight()*scaleFactor/2,null);
			mCanvas.drawBitmap(mBitmap, imgX,imgY,null);
		} else{
			mCanvas.drawBitmap(mBitmap, imgX,imgY,null);
		}
		mCanvas.drawCircle(scaleFactor*irisX, (irisY*scaleFactor-topPos), irisR*irisFactor, irisPaint);
		mCanvas.drawCircle(scaleFactor*pupilX, (pupilY*scaleFactor-topPos), pupilR*pupilFactor, pupilPaint);
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
			irisY = event.getY()-irisR;
			DrawAll();
			 
		} else if(isIdentifyingPupil){
			pupilX = event.getX();
			pupilY = event.getY()-pupilR;
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
	

}
