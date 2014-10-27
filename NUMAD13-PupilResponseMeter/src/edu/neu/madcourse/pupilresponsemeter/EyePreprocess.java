package edu.neu.madcourse.pupilresponsemeter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.R.integer;
import android.util.Log;

public class EyePreprocess{

	int threshold = 1;
	int thickness = 5;
	int morKernelSizeOn = 5;
	int morKernelSize = 11;
	boolean showFilted = true;
	final static int PUPIL = 0;
	final static int IRIS = 1;
	void setThreshold(int i ){
		threshold=i;
	}
	void setThickness(int i ){
		thickness=i;
	}
	void setShowFiltered(boolean i ){
		showFilted=i;
	}
	void morKernelSize(int i ){
		morKernelSize=i;
	}
	
	//when original and result reference to the same address, things will become wired.
	public void doPreprocess(Mat original, Mat pupilResult,Mat irisResult,Mat preprocessedResult,boolean flashOn) {
		Mat result = new Mat();
		if(!flashOn){
			
	    	Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()/2),Math.round(original.rows()/2)));
	    	Imgproc.GaussianBlur(result, result, new org.opencv.core.Size(9,9), 0);
	    	Imgproc.resize(result, result, new org.opencv.core.Size (original.cols(),original.rows()));
	    	//Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE , new org.opencv.core.Size(5,5), new org.opencv.core.Point(2,2));
	    	
	    	//Imgproc.morphologyEx(result, result, Imgproc.MORPH_OPEN, kernel);
	    	Core.normalize(result, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
	    	doThreshold(result,pupilResult,true);
	    	doThreshold(result,irisResult,false);
	    	
	    	
	    	//Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()*2),Math.round(original.rows()*2)));
			//polarDiff(result,result,threshold,thickness);
			Imgproc.resize(pupilResult, pupilResult, new org.opencv.core.Size (original.cols(),original.rows()));
		} else {
			Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()/2),Math.round(original.rows()/2)));
	    	Imgproc.GaussianBlur(result, result, new org.opencv.core.Size(9,9), 0);
	    	Imgproc.resize(result, result, new org.opencv.core.Size (original.cols(),original.rows()));
	    	//Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE , new org.opencv.core.Size(5,5), new org.opencv.core.Point(2,2));
	    	Mat morKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(morKernelSizeOn,morKernelSizeOn));
	    	//Imgproc.morphologyEx(result, result, Imgproc.MORPH_OPEN, kernel);
	    	Core.normalize(result, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
	    	//doThreshold(original,result,true);
	    	
	    	Imgproc.morphologyEx(result, result, Imgproc.MORPH_CLOSE, morKernel);
	    	Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()*2),Math.round(original.rows()*2)));
	    	result.copyTo(preprocessedResult);
			polarDiff(result,result,threshold,thickness);
	    	//Imgproc.resize(result, result, new org.opencv.core.Size (original.cols(),original.rows()));
	    	//Imgproc.morphologyEx(result, result, Imgproc.MORPH_OPEN, morKernel);
	    	result.copyTo(pupilResult);
	    	result.copyTo(irisResult);
		}

	}
	private void doThreshold(Mat original, Mat result, boolean showPupil) {
		double pupilCenter = original.get(original.cols()/2, original.rows()/2)[0];
		double iris1 = original.get(original.cols()/4, original.rows()/4)[0];
		double iris2 = original.get(original.cols()/4, 3*original.rows()/4)[0];
		double iris3 = original.get(3*original.cols()/4, original.rows()/4)[0];
		double iris4 = original.get(3*original.cols()/4, 3*original.rows()/4)[0];
		Mat morKernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(morKernelSize,morKernelSize));
		if(showPupil){
			
			double thresh = (((iris1+iris2+iris3+iris4)/8+pupilCenter)/2)*11/10;
			
			//Imgproc.threshold(original, result, -1, 255, Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU);
			Imgproc.threshold(original, result, thresh, 255, Imgproc.THRESH_BINARY_INV);
			Imgproc.morphologyEx(result, result, Imgproc.MORPH_CLOSE, morKernel);
		} else {
			Imgproc.threshold(original, result, -1, 255, Imgproc.THRESH_BINARY_INV+Imgproc.THRESH_OTSU);
			double thresh = (((iris1+iris2+iris3+iris4)/4))*11/10;
			//Imgproc.threshold(original, result, thresh, 255, Imgproc.THRESH_BINARY_INV);
			Imgproc.morphologyEx(result, result, Imgproc.MORPH_CLOSE, morKernel);
		}
		
	}
	/*
	public void doPreprocess(Mat original, Mat result, int returnType) {
		
    	Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()/2),Math.round(original.rows()/2)));
    	Imgproc.GaussianBlur(result, result, new org.opencv.core.Size(9,9), 0);
    	Imgproc.resize(result, result, new org.opencv.core.Size (original.cols(),original.rows()));
    	Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE , new org.opencv.core.Size(5,5), new org.opencv.core.Point(2,2));
    	Imgproc.morphologyEx(result, result, Imgproc.MORPH_CLOSE, kernel);
    	Core.normalize(result, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

    	
    	//Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()*2),Math.round(original.rows()*2)));
		polarDiff(result,result,threshold,thickness, returnType);
		Imgproc.resize(result, result, new org.opencv.core.Size (original.cols(),original.rows()));

	}
	*/
	void polarDiff(Mat source, Mat result,int threshold, int minThickness){
		
		Mat res = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
		int centerX = source.cols()/2;
		int centerY = source.rows()/2;
		double stepSize = Math.sqrt(1);
		for(double deg = 0; deg < Math.PI *2; deg += Math.PI/180.0){
			int x = centerX;
			int y = centerY;
			int prevX=centerX;
			int prevY=centerY;
			int nextX = (int)(centerX + stepSize * Math.cos(deg)+0.5);
			int nextY = (int)(centerY + stepSize * Math.sin(deg)+0.5);
			for(double r = 0; (nextX > 0 && nextX < source.cols()) && (nextY > 0 && nextY < source.rows());r+=stepSize){
				double[] prevP = source.get(prevY, prevX);
				double[] nextP = source.get(nextY, nextX);
				double[] p = source.get(y,x);
				//Log.d("NULLPOINTER", "x="+x+" y="+y+" nextX="+nextX+" nextY="+nextY+" cols="+source.cols()+" rows="+source.rows());
				double nextGray = nextP[0];
				double gray = p[0];
				if(nextGray - gray > threshold){
					
					res.put(y, x, 255);
					
					/*if(res.get(prevY, prevX)[0]==255){
						res.put(prevY, prevX, 0);
					}*/
				} else {
					
					res.put(y, x, 0);
				}
				x = (int)(centerX + r * Math.cos(deg)+0.5);
				y = (int)(centerY + r * Math.sin(deg)+0.5);
				nextX = (int)(centerX + (r+stepSize) * Math.cos(deg)+0.5);
				nextY = (int)(centerY + (r+stepSize) * Math.sin(deg)+0.5);
				prevX = (int)(centerX + (r-stepSize) * Math.cos(deg)+0.5);
				prevY = (int)(centerX + (r-stepSize) * Math.sin(deg)+0.5);
				
			}
		}
		
		if(showFilted){
			thickFilter(res,result,minThickness);
		}
		
		if(!showFilted){
			res.copyTo(result);
		}
	}
	/*
	void polarDiff(Mat source, Mat result,int threshold, int minThickness, int returnType){
		
		polarDiff(source, result, threshold, minThickness);
		
		if(showFilted){
			switch (returnType) {
			case PUPIL:
				pupilMat.copyTo(result);
				break;
			case IRIS:
				irisMat.copyTo(result);
				break;
			default:
				break;
			}
		}
	}
*/
	private void thickFilter(Mat source, Mat result, int minThickness) {
		//Mat res = new Mat(source.rows(), source.cols(), CvType.CV_8UC1);
		Mat res1 = new Mat(source.rows(), source.cols(), CvType.CV_8UC1,new Scalar(0));
		
		//source.copyTo(res);		
		int centerX = source.cols()/2;
		int centerY = source.rows()/2;
		double stepSize = Math.sqrt(1);
		
		for(double deg = 0; deg < Math.PI *2; deg += Math.PI/180.0){
			int x = centerX;
			int y = centerY;
			ArrayList<Point> line = new ArrayList<Point>();
			for(double r = 0; (x >= 0 && x < source.cols()) && (y >= 0 && y < source.rows());r+=stepSize){
				//double[] prevP = source.get(prevY, prevX);
				//double[] nextP = source.get(nextY, nextX);
				
				double[] p = source.get(y,x);
				//Log.d("NULLPOINTER", "x="+x+" y="+y+" cols="+source.cols()+" rows="+source.rows());
				//double prevGray = prevP[0];
				//double nextGray = nextP[0];
				double gray = p[0];
				line.add(new Point(x, y, gray));
				x = (int)(centerX + r * Math.cos(deg)+0.5);
				y = (int)(centerY + r * Math.sin(deg)+0.5);
				
			}
			boolean putPupil = false;
			
			calculateThickness(line);
			
			for(int i =0; i<line.size();i++){
				
				Point temp = line.get(i);
				if(temp.thickness>= minThickness){
					Point startPoint = line.get(i-temp.thickness);
					res1.put((temp.y+startPoint.y)/2, (temp.x+startPoint.x)/2, 255);
					Log.d("THICKNESS", (temp.y+startPoint.y)/2+"   "+(temp.x+startPoint.x)/2);
					//res.put((startPoint.y), (startPoint.x), 255);
					//pupilMat.put((startPoint.y), (startPoint.x), 255);
					//irisMat.put((startPoint.y), (startPoint.x), 255);
					//res.put((temp.y), (temp.x), 255);
					//Log.d("THICKNESS","x="+temp.x+" y="+temp.y+" thickness="+temp.thickness);


				} else {
					//Log.d("THICKNESS", "NO");
					res1.put(temp.y, temp.x, 0);
				}
			}
		}
		
		res1.copyTo(result);
		
	}
	
	private void calculateThickness(ArrayList<Point> line) {
		if(line.size()>0){
			line.get(0).setThickness(0);
			for(int i = 1; i < line.size(); i++){
				Point temp = line.get(i);
				Point prevTemp = line.get(i-1);
				if(temp.gray==255){
					temp.setThickness(prevTemp.thickness+1);
					prevTemp.setThickness(0);
				} else {
					temp.setThickness(0);
					
				}
			}
		}
	}

	class Point{
		public int x;
		public int y;
		public double gray;
		public int thickness;
		Point(int x,int y,double gray){
			this.x=x;
			this.y=y;
			this.gray=gray;
		}
		
		void setThickness(int thickness){
			this.thickness=thickness;
		}
		
		
	}
	
	

}
