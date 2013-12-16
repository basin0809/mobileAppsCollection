package edu.neu.madcourse.xipengwang.finalProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;


public class PupilImgs {
	
	public static float dilationTime;
	public static float constirctionTime;
	public static double[] constrictionRes = new double[6];
	public static double[] dilationRes = new double[6];
	public static int screenW;
	public static int screenH;
	public static ArrayList<Bitmap> pupilImgSet = new ArrayList<Bitmap>();
	public static ArrayList<Bitmap> processedImgSet = new ArrayList<Bitmap>();
	//public static ArrayList<Bitmap> userFlasOnImgSet = new ArrayList<Bitmap>();
	//public static ArrayList<Bitmap> userFlasOffImgSet = new ArrayList<Bitmap>();
	
	
	public static ArrayList<String> imgPathes = new ArrayList<String>();
	//public static ArrayList<String> processedImgPathes = new ArrayList<String>();
	//public static ArrayList<String> userflashOnImgPathes = new ArrayList<String>();
	//public static ArrayList<String> userflashOffIimgPathes = new ArrayList<String>();
	
	public static ArrayList<Mat> pupilBitMap = new ArrayList<Mat>();
	//public static ArrayList<Mat> userFlashOnPupilBitMap = new ArrayList<Mat>();
	//public static ArrayList<Mat> userFlashOffPupilBitMap = new ArrayList<Mat>();
	
	//public Mat mat = Highgui.imread(Utils.exportResource(this, R.drawable.pupil29), Highgui.CV_LOAD_IMAGE_COLOR);
	//public static int zoomLevel;
	public static void rescaleImgs(){
		int imgNum = pupilImgSet.size();
		Mat[] mats = new Mat[imgNum];
		for(int i =0; i<imgNum; i++){
		Bitmap tempImg = pupilImgSet.get(i);
		mats[i] = new Mat(tempImg.getHeight(), tempImg.getWidth(), CvType.CV_8UC4);
		Utils.bitmapToMat(tempImg, mats[i]);
		mats[i].submat((int)(mats[i].rows()*0.375), (int)(mats[i].rows()*0.625), 
				 	(int)(mats[i].cols()*0.375), (int)(mats[i].cols()*0.625)).copyTo(mats[i]);
		Bitmap mCaptureRSBitmap = Bitmap.createBitmap(mats[i].cols(),  mats[i].rows(),Bitmap.Config.ARGB_8888);;	
		Utils.matToBitmap(mats[i], mCaptureRSBitmap);
		
		pupilImgSet.add(Bitmap.createScaledBitmap(mCaptureRSBitmap, tempImg.getWidth(), tempImg.getHeight(), false));
		
		}
		convertToMat2();
	}
	public static ArrayList<Mat> convertToMat(){
		
		int imgNum = pupilImgSet.size();
		Mat[] mats = new Mat[imgNum];
		Mat[] mats2 = new Mat[imgNum];
		for(int i =0; i<imgNum; i++){
			Bitmap tempImg = pupilImgSet.get(i);
			mats[i] = new Mat(tempImg.getHeight(), tempImg.getWidth(), CvType.CV_8UC1);
			//mats[i] = Highgui.imread(Utils.exportResource(PupilImgs.this, R.drawable.pupil29), Highgui.CV_LOAD_IMAGE_COLOR);
			//mats2[i] = new Mat();			
			Utils.bitmapToMat(tempImg, mats[i]);
			
			List<Mat> listGraphic = new ArrayList<Mat>();
	        listGraphic.add(mats[i]);
	        
	        Mat dummyRedFrame = new Mat(mats[i].rows(), mats[i].cols(), CvType.CV_8UC1);
	        Mat dummyGreenFrame = new Mat(mats[i].rows(), mats[i].cols(), CvType.CV_8UC1);
	        Mat dummyBlueFrame = new Mat(mats[i].rows(), mats[i].cols(), CvType.CV_8UC1);
	        List<Mat> dummyRGBFrames = new ArrayList<Mat>();
	         dummyRGBFrames.add(dummyRedFrame);
	         dummyRGBFrames.add(dummyGreenFrame);
	         dummyRGBFrames.add(dummyBlueFrame);
	         
			Core.mixChannels(listGraphic, dummyRGBFrames, new MatOfInt(0,0,1,1,2,2));
			
			Core.normalize(dummyBlueFrame, dummyBlueFrame, 0, 255,Core.NORM_MINMAX, CvType.CV_8U);
			dummyBlueFrame.submat((int)(dummyBlueFrame.rows()*0.375), (int)(dummyBlueFrame.rows()*0.625), 
								 (int)(dummyBlueFrame.cols()*0.375), (int)(dummyBlueFrame.cols()*0.625)).copyTo(dummyBlueFrame);
			//Log.i("ImgProcess", zoomLevel+"");
			
			pupilBitMap.add(dummyBlueFrame);
		}
		return pupilBitMap;
	}
	
	public static void outputImgs(){
        //Log.i("SAVE IMAGE", "start save");
		
        int imgNum = pupilImgSet.size();
        for(int i=2; i<=3; i++){
        	File newFile = null;
        	try {
				newFile =File.createTempFile("IMGcapture"+i, ".PNG", Environment.getExternalStorageDirectory());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {          
	        	imgPathes.add(i-2, newFile.getAbsolutePath()); 
	        	Log.d("ImgProcess", newFile.getAbsolutePath());
	            FileOutputStream fos = new FileOutputStream(newFile);
	            Mat tempMat = pupilBitMap.get(i);
	            Bitmap tempImg = Bitmap.createBitmap(tempMat.cols(),  tempMat.rows(),Bitmap.Config.ARGB_8888);
	            Utils.matToBitmap(tempMat, tempImg);
	            
	            
	            tempImg.compress(Bitmap.CompressFormat.PNG, 100, fos);
	            fos.flush();
	            fos.close();
	        } catch (IOException e) {
	            Log.e("error", e.getMessage());
	            e.printStackTrace();
	        }
        }
	}
	
public static ArrayList<Mat> convertToMat2(){
		
		int imgNum = pupilImgSet.size();
		Mat[] mats = new Mat[imgNum];
		
		for(int i =2; i<imgNum; i++){
			Bitmap tempImg = pupilImgSet.get(i);
			mats[i] = new Mat(tempImg.getHeight(), tempImg.getWidth(), CvType.CV_8UC1);
			//mats[i] = Highgui.imread(Utils.exportResource(PupilImgs.this, R.drawable.pupil29), Highgui.CV_LOAD_IMAGE_COLOR);
			//mats2[i] = new Mat();			
			Utils.bitmapToMat(tempImg, mats[i]);
			
			List<Mat> listGraphic = new ArrayList<Mat>();
	        listGraphic.add(mats[i]);
	        
	        Mat dummyRedFrame = new Mat(mats[i].rows(), mats[i].cols(), CvType.CV_8UC1);
	        Mat dummyGreenFrame = new Mat(mats[i].rows(), mats[i].cols(), CvType.CV_8UC1);
	        Mat dummyBlueFrame = new Mat(mats[i].rows(), mats[i].cols(), CvType.CV_8UC1);
	        List<Mat> dummyRGBFrames = new ArrayList<Mat>();
	         dummyRGBFrames.add(dummyRedFrame);
	         dummyRGBFrames.add(dummyGreenFrame);
	         dummyRGBFrames.add(dummyBlueFrame);
	         
			Core.mixChannels(listGraphic, dummyRGBFrames, new MatOfInt(0,0,1,1,2,2));
			
			Core.normalize(dummyBlueFrame, dummyBlueFrame, 0, 255,Core.NORM_MINMAX, CvType.CV_8U);
			pupilBitMap.add(dummyBlueFrame);
		}
		return pupilBitMap;
	}
}
