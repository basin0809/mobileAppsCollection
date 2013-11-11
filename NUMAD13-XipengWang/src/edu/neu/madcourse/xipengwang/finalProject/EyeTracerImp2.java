package edu.neu.madcourse.xipengwang.finalProject;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.R.integer;
import android.util.Log;

public class EyeTracerImp2 implements EyeTracer{
	private int dp=2;
	private int minRadius=100;
	private int dist;
	private int maxRadius=400;
	private int cannyUpperThreshold=100;
	private int accumulator=300;
	private double[] bestCircle = new double[3];
	private Mat circles = new Mat();
	@Override
	public Mat getEyeMat(Mat originalMat) {
		
		if(bestCircle!=null){
			int x = (int)Math.round(bestCircle[0]);
			int y = (int)Math.round(bestCircle[1]);
			int radius = (int)Math.round(bestCircle[2]);
			//TODO
			//Need to know the sequence of x and y.
			return originalMat.submat(Math.max(x-radius, 0), Math.min(x+radius, originalMat.cols()), Math.max(y-radius, 0), Math.min(y+radius,originalMat.rows()));
		} else {
			return null;
		}
	}
	
	public void markCircle(Mat original,int index, boolean black){
		if(circles.rows()==0){
			
		}else {
			int value=0;
			if(black){
				value = 0;
			} else {
				value = 255;
			}
			double[] targetCircle = circles.get(0, index);
			int centerX = (int)Math.round(targetCircle[0]);
			int centerY = (int)Math.round(targetCircle[1]);
			int radius = (int)Math.round(targetCircle[2]);
			for(double theata = 0; theata < Math.PI*2; theata+=Math.PI/180){
				int x = Math.max(Math.min((int)(centerX + radius * Math.cos(theata)+0.5),original.cols()-1),0);
				int y = Math.max(Math.min((int)(centerY + radius * Math.sin(theata)+0.5),original.rows()-1),0);
				original.put(y, x, value);
				original.put(y+1, x, value);
				original.put(y, x+1, value);
				original.put(y-1, x, value);
				original.put(y, x-1, value);
				
			}
			
			
		}
	}
	
	public Mat getEyeMat(Mat originalMat, double[] bc, Boolean flashIsOn) {
		
		if(bc!=null){
			int x = (int)Math.round(bc[0]);
			int y = (int)Math.round(bc[1]);
			if(flashIsOn){
			int radius = 80;
			int rowStart = Math.max(y-radius, 0);
			int rowEnd = Math.min(y+radius, originalMat.rows()-1);
			int colStart = Math.max(x-radius, 0); 
			int colEnd = Math.min(x+radius,originalMat.cols()-1);
			//Log.d("findEye", "rowStart: "+rowStart+" rowEnd: "+rowEnd+" colStart: "+colStart+" colEnd"+colEnd);
			Log.d("findEye", "rowStart: "+rowStart+" rowEnd: "+rowEnd+" colStart: "+colStart+" colEnd "+colEnd +" originalRow: "+originalMat.rows()+" originalCol: "+originalMat.cols());
			//TODO
			//Need to know the sequence of x and y.
			return originalMat.submat(rowStart, rowEnd, colStart, colEnd);}
			else {
				int radius = (int)Math.round(bc[2]);
				int rowStart = Math.max(y-radius, 0);
				int rowEnd = Math.min(y+radius, originalMat.rows()-1);
				int colStart = Math.max(x-radius, 0); 
				int colEnd = Math.min(x+radius,originalMat.cols()-1);
				return originalMat.submat(rowStart, rowEnd, colStart, colEnd);
			}
			
		} else {
			return null;
		}
	}
	
	public ArrayList<Mat> getEyeMats(Mat originalMat, Boolean flashIsON){
		ArrayList<Mat> result = new ArrayList<Mat>();
		for(int i = 0; i < circles.cols();i++){
			double[] tempCircle = circles.get(0,i);
			result.add(getEyeMat(originalMat,tempCircle,flashIsON));
		}
		return result;
	}

	@Override
	public double[] getBestCircle() {
		 	 
		return bestCircle;
		 
	}

	
	@Override
	public void findEyes(Mat processedMat) {
		Imgproc.HoughCircles(processedMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1,dist, cannyUpperThreshold, accumulator, minRadius, maxRadius);
		if(circles.cols()>0){
			bestCircle = circles.get(0, 0);
		} else {
			bestCircle = null;
		}
	}

	public void setDp(int dp) {
		this.dp=dp;
		
	}

	public void setMinRadius(int r) {
		this.minRadius=r;
		
	}

	public void setMaxRadius(int r) {
		this.maxRadius=r;
		
	}
	
	public void setDist(int d) {
		this.dist=d;
		
	}

	public void setCannyUpperThreshold(int t) {
		this.cannyUpperThreshold=t;
		
	}

	public void setAccumulator(int a) {
		this.accumulator=a;
		
	}

}
