package edu.neu.madcourse.xipengwang.finalProject;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImgPreprocess implements Preprocess{

	@Override
	public void doPreprocess(Mat original, Mat result) {
		
    	Imgproc.resize(original, result, new org.opencv.core.Size (Math.round(original.cols()/2),Math.round(original.rows()/2))); 
    	Imgproc.GaussianBlur(result, result, new org.opencv.core.Size(9,9), 0);
    	Imgproc.resize(result, result, new org.opencv.core.Size (original.cols(),original.rows()));
    	
    	Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE , new org.opencv.core.Size(21,21), new org.opencv.core.Point(2,2));
    	Imgproc.morphologyEx(result, result, Imgproc.MORPH_CLOSE, kernel);
    	Core.normalize(result, result, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
  
    	Imgproc.adaptiveThreshold(result, result, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 101,3);
    	
    	Imgproc.dilate(result, result, kernel);
    	//Imgproc.threshold(result, result, 255, 255, Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
		
	}

}
