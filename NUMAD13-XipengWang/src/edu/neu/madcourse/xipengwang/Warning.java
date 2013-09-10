package edu.neu.madcourse.xipengwang;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class Warning extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warning);
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		};
		handler.postDelayed(runnable, 2999);
	}

}
