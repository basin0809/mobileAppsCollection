package edu.neu.madcourse.xipengwang.finalProject;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.R.integer;
import android.util.Log;

public class EyeTracerImp implements EyeTracer{
	private int dp=2;
	private static int minRadius=10;
	private static int dist=1;
	private static int maxRadius=1000;
	private static int cannyUpperThreshold=50;
	private static int accumulator=30;
	private double[] bestCircle = new double[3];
	private static Mat circles = new Mat();
	private static Mat circles2 = new Mat();
	private static Mat circles3 = new Mat();
	@Override
	public Mat getEyeMat(Mat originalMat) {
	
			return null;

	}
	
	public static void markCircle(Mat original,int index, boolean black){
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
	
	public static void markCircleIris(Mat original,int index, boolean black){
		if(circles2.rows()==0){
			
		}else {
			int value=0;
			if(black){
				value = 0;
			} else {
				value = 255;
			}
			double[] targetCircle = circles2.get(0, index);
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
	public static void markCirclePupil(Mat original,int index, boolean black){
		if(circles3.rows()==0){
			
		}else {
			int value=0;
			if(black){
				value = 0;
			} else {
				value = 255;
			}
			double[] targetCircle = circles3.get(0, index);
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

	

	public static Mat findEyes1(Mat sourceMat) {
		Mat processedMat = new Mat();
		return preprocessEye(sourceMat,processedMat);
		
		/*
		Imgproc.HoughCircles(processedMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1,dist, cannyUpperThreshold, accumulator, minRadius, maxRadius);
		if(circles.cols()>0){
			bestCircle = circles.get(0, 0);
		} else {
			bestCircle = null;
		}*/
	}
//	Center point of image must be inside the pupil
	private static Mat preprocessEye(Mat source, Mat result) {
		Mat resMat = new Mat();
		Mat tempResult1 = new Mat();
		Mat tempResult2 = new Mat();
		
		Imgproc.GaussianBlur(source, resMat, new Size(11,11), 0);
		resMat.copyTo(tempResult2);
    	//Imgproc.resize(source, tempResult4, new Size(Math.round(source.cols()/2),Math.round(source.rows()/2)));
    	//Imgproc.GaussianBlur(tempResult4, tempResult4, new Size(11,11), 0);
    	//Imgproc.resize(tempResult4, tempResult4, new org.opencv.core.Size (source.cols(),source.rows()));
    	Mat morKernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(17,17));
    	//Mat morKernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(17,17));
    	//Mat morKernel3 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(17,17));
    	//Imgproc.morphologyEx(tempResult2, tempResult3, Imgproc.MORPH_ERODE, morKernel1);
    	//Imgproc.morphologyEx(tempResult4, tempResult5, Imgproc.MORPH_ERODE, morKernel2);
    	//Imgproc.morphologyEx(source, tempResult1, Imgproc.MORPH_ERODE, morKernel3);
    	Imgproc.morphologyEx(resMat, resMat, Imgproc.MORPH_ERODE, morKernel1);
    	//Imgproc.morphologyEx(resMat, resMat, Imgproc.MORPH_DILATE, morKernel1);
    	//Imgproc.morphologyEx(resMat, resMat, Imgproc.MORPH_CLOSE, morKernel1);
    	
		//resMat.copyTo(tempResult4);
		int[] darkestPosition = roughLocalizePupil(resMat,1);
		//int[] centerPosition = new int[2];
		//centerPosition[0] = source.cols()/2;
		//centerPosition[1] = source.rows()/2;
		resMat.copyTo(tempResult1);
		markPosition(tempResult1,darkestPosition,false);
		//markPosition(tempResult1,centerPosition,true);
		int frameSize = 1;
		double threshold1 = getSumOfGray(resMat, darkestPosition[0], darkestPosition[1], frameSize)/(frameSize*frameSize);
		double threshold2 = getSumOfGray(resMat, darkestPosition[2], darkestPosition[3], frameSize)/(frameSize*frameSize);
		//Imgproc.threshold(resMat, resMat, threshold1/2+threshold2/2, 255, Imgproc.THRESH_BINARY_INV);
		
		
    	setMinRadius(50);
    	setMaxRadius(500);
    	setAccumulator(20);
    	setDist(source.rows()/8);
    	setCannyUpperThreshold(20);
		Imgproc.morphologyEx(resMat, resMat, Imgproc.MORPH_GRADIENT, morKernel1);
		//Imgproc.morphologyEx(resMat, resMat, Imgproc.MORPH_BLACKHAT, morKernel1);
    	
    	//Imgproc.calcHist(resMat, 1, null, hist, 10, ranges);
    	//Imgproc.Canny(resMat, resMat, (threshold2-threshold1)*0.2, (threshold2-threshold1)*0.6);
		          Imgproc.HoughCircles(resMat, circles, Imgproc.CV_HOUGH_GRADIENT, 1,dist, cannyUpperThreshold, accumulator, minRadius, maxRadius);
		//markPosition(tempResult3,darkestPosition,false);
		//markPosition(tempResult3,centerPosition,false);
		Log.d("hough", "circles:"+circles.rows());
		//resMat.copyTo(tempResult3);
		//source.copyTo(tempResult5);
		//markPosition(tempResult5,darkestPosition,false);
		//markCircle(tempResult3,0,true);
		//markCircle(tempResult5,0,false);
		
		Mat hist = new Mat();
		List<Mat> sourceList = new ArrayList<Mat>();
		Mat mask = buildMatFromCircle(source,0);
		//mask.copyTo(tempResult4);
		sourceList.add(source);
		Imgproc.calcHist(sourceList, new MatOfInt(0), mask, hist, new MatOfInt(50), new MatOfFloat(0.0F,255.0F));
		double binSize = 255.0/50.0;
		
		int pupilIndex =9;
		int irisIndex =20;
		double irisPercent = 0.80;
		double pupilPercent = 0.15;
		double histCount = 0;
		double pupilCount = 0;
		double irisCount = 0;
		for(int i = 0; i < hist.rows(); i++){
			histCount += hist.get(i, 0)[0];
		}
		
		irisCount=0;
		irisIndex=0;
		for(int i = 0; i < hist.rows(); i++){
			//pupilCount+=hist.get(i, 0)[0];
			if((irisCount/histCount)<=irisPercent && i<=20){
				irisCount+=hist.get(i, 0)[0];
				irisIndex=i+1;
			}
		}
		irisPercent = irisCount/histCount;
		pupilPercent = irisPercent/10+0.08;
		Log.d("HIST","IRIS PERCENT = "+irisCount/histCount+" index = "+irisIndex);
		//Log.d("HIST","IRIS PERCENT = "+irisCount/histCount);
		Imgproc.threshold(source, tempResult1, irisIndex*binSize, 255, Imgproc.THRESH_BINARY_INV);
		tempResult1.copyTo(mask);
		Imgproc.calcHist(sourceList, new MatOfInt(0), mask, hist, new MatOfInt(50), new MatOfFloat(0.0F,255.0F));
		
		histCount=0;
		for(int i = 0; i < hist.rows(); i++){
			histCount += hist.get(i, 0)[0];
		}
		pupilCount=0;
		for(int i = 0; i < hist.rows(); i++){
			//pupilCount+=hist.get(i, 0)[0];
			if((pupilCount/histCount)<=pupilPercent && i<=12){
				pupilCount+=hist.get(i, 0)[0];
				pupilIndex=i;
			}
		}
		Log.d("HIST","PUPIL PERCENT = "+pupilCount/histCount);
		
		//Imgproc.threshold(source, tempResult1, irisIndex*binSize, 255, Imgproc.THRESH_BINARY_INV);
		Imgproc.threshold(source, tempResult2, pupilIndex*binSize, 255, Imgproc.THRESH_BINARY_INV);
		
		/*****Detect Iris Circles*****/
		Imgproc.morphologyEx(tempResult1, tempResult1, Imgproc.MORPH_GRADIENT, morKernel1);
		Imgproc.HoughCircles(tempResult1, circles2, Imgproc.CV_HOUGH_GRADIENT, 1,dist, cannyUpperThreshold, accumulator, minRadius, maxRadius);		
		/*****Detect Pupil Circles*****/
		setMinRadius(25);
		Imgproc.morphologyEx(tempResult2, tempResult2, Imgproc.MORPH_CROSS, morKernel1);
		Imgproc.morphologyEx(tempResult2, tempResult2, Imgproc.MORPH_GRADIENT, morKernel1);
		Imgproc.HoughCircles(tempResult2, circles3, Imgproc.CV_HOUGH_GRADIENT, 1,dist, cannyUpperThreshold, accumulator, minRadius, maxRadius);
		Log.d("hough", "circles:"+circles.rows());
		if(circles2.rows()!=0){
			markCircleIris(source ,0,true);}
			else {
				markCircle(source ,0,true);
			}
		markCirclePupil(source,0,true);
		return source;
	}
	private static Mat buildMatFromCircle(Mat source,int index) {
		Mat result = new Mat(source.rows(), source.cols(), CvType.CV_8UC1, new Scalar(0));
		
		int cols = source.cols();
		int rows = source.rows();
		if(circles.rows()==0){
			
		}else {

			double[] targetCircle = circles.get(0, index);
			int centerX = (int)Math.round(targetCircle[0]);
			int centerY = (int)Math.round(targetCircle[1]);
			int radius = (int)Math.round(targetCircle[2]);
			Log.d("HIST","AREA:"+radius*radius*Math.PI);
			for(int i = 0; i < cols; i ++){
				for(int j = 0; j < rows; j++){
					if((centerX-i)*(centerX-i)+(centerY-j)*(centerY-j)<radius*radius){
						result.put(j, i, 255);
					} else {
						result.put(j, i, 0);
					}
				}
			}
	
		}
		return result;
	}
	private void markPosition(Mat tempResult1, int[] pupilPosition) {
		
		int x= pupilPosition[0];
		int y= pupilPosition[1];
		tempResult1.put(y, x, 255);
		for(int i = 0; i < 10; i ++){
			tempResult1.put(y-i, x, 255);
			tempResult1.put(y+i, x, 255);
			tempResult1.put(y, x-i, 255);
			tempResult1.put(y, x+i, 255);
		}

		
	}
	private static void markPosition(Mat tempResult1, int[] pupilPosition, boolean inverse) {
		
		int whitevalue = 255;
		int blackvalue = 0;
		
		if(inverse){
			blackvalue = 255;
			whitevalue = 0;
		}
		int darkX= pupilPosition[0];
		int darkY= pupilPosition[1];
		int brightX = pupilPosition[2];
		int brightY = pupilPosition[3];
		Log.d("MINMAX", "dark:"+darkX+" "+darkY+"  bright:"+brightX+" "+brightY);
		tempResult1.put(darkY, darkX, whitevalue);
		for(int i = 0; i < 10; i ++){
			tempResult1.put(darkY-i, darkX, whitevalue);
			tempResult1.put(darkY+i, darkX, whitevalue);
			tempResult1.put(darkY, darkX-i, whitevalue);
			tempResult1.put(darkY, darkX+i, whitevalue);
			
			tempResult1.put(brightY-i, brightX, blackvalue);
			tempResult1.put(brightY+i, brightX, blackvalue);
			tempResult1.put(brightY, brightX-i, blackvalue);
			tempResult1.put(brightY, brightX+i, blackvalue);
		}

		
	}

	private static int[] roughLocalizePupil(Mat resMat, int FrameSize) {
		int halfSize = (FrameSize-1)/2;
		int minX = 0;
		int minY = 0;
		int[] result = new int[4];
		double minSumOfGray = 255*FrameSize*FrameSize;
		
		int maxX=0;
		int maxY=0;
		double maxSumOfGray = 0;
		for(int i = halfSize; i < resMat.cols()-halfSize;i++ ){
			for(int j = halfSize; j < resMat.rows()-halfSize;j++){
				if(i < 3*resMat.cols()/4 && i>resMat.cols()/4 && j < 3*resMat.rows()/4 && j > resMat.rows()/4){
					double sumOfGray=getSumOfGray(resMat, i, j, FrameSize);
					//double sumOfGray=resMat.get(j, i)[0];
					
					if(sumOfGray < minSumOfGray){
						
						minSumOfGray = sumOfGray;
						minX=i;
						minY=j;
						Log.d("MINMAX","MIN : "+i+" "+j+" "+minSumOfGray);
					}
					if(sumOfGray > maxSumOfGray){
						maxSumOfGray = sumOfGray;
						maxX=i;
						maxY=j;
						Log.d("MINMAX","MAX : "+i+" "+j+" "+minSumOfGray);
					}
				}
			}
		}
		result[0] = minX;
		result[1] = minY;
		result[2] = maxX;
		result[3] = maxY;
		
		return result;
	}
	
	private static double getSumOfGray(Mat resMat,int i,int j,int FrameSize){
		int sumOfGray=0;
		int halfSize = (FrameSize-1)/2;
		for(int x = i-halfSize; x<=i+halfSize;x++){
			for(int y = j-halfSize;y<=j+halfSize;y++){
				sumOfGray+=resMat.get(y, x)[0];
				
			}
		}
		return sumOfGray;
	}

	public void setDp(int dp) {
		this.dp=dp;
		
	}

	public static void setMinRadius(int r) {
		minRadius=r;
		
	}

	public static void setMaxRadius(int r) {
		maxRadius=r;
		
	}
	
	public static void setDist(int d) {
		dist=d;
		
	}

	public static void setCannyUpperThreshold(int t) {
		cannyUpperThreshold=t;
		
	}

	public static void setAccumulator(int a) {
		accumulator=a;
		
	}

	@Override
	public void findEyes(Mat processedMat) {
		// TODO Auto-generated method stub
		
	}

}
