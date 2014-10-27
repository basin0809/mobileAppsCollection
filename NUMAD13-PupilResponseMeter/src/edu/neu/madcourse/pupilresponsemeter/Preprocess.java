package edu.neu.madcourse.pupilresponsemeter;

import org.opencv.core.Mat;

public interface Preprocess {
	void doPreprocess(Mat original, Mat result);
}
