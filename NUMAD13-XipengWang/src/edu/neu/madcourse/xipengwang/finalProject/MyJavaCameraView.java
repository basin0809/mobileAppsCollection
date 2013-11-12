package edu.neu.madcourse.xipengwang.finalProject;

import java.util.List;

import org.opencv.android.JavaCameraView;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;

public class MyJavaCameraView extends JavaCameraView {

public MyJavaCameraView(Context context, AttributeSet attrs) {
    super(context, attrs);
} 

public List<Camera.Size> getResolutionList() {      
    return  mCamera.getParameters().getSupportedPreviewSizes();      
}

public void setResolution(Camera.Size resolution) {
    disconnectCamera();
    connectCamera((int)resolution.width, (int)resolution.height);       
}

public void setFocusMode (int type){

    Camera.Parameters params = mCamera.getParameters();     

    switch (type){
    case 0:         
         params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        break;
    case 1:         
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        break;
    case 2:         
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
        break;
    case 3:
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        break;
    case 4:
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        break;
    case 5:
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        break;      
    case 6:
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        break;
    }

    mCamera.setParameters(params);
}   

public void setFlashMode (int type){

    Camera.Parameters params = mCamera.getParameters();   

    switch (type){
    case 0:
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);          
        break;  
    case 1:
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);            
        break;
    }

    mCamera.setParameters(params);
}   

public Camera.Size getResolution() {

    Camera.Parameters params = mCamera.getParameters(); 

    Camera.Size s = params.getPreviewSize();
    return s;
}
}