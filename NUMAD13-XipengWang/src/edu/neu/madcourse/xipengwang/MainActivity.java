package edu.neu.madcourse.xipengwang;

import edu.neu.madcourse.xipengwang.sudoku.Sudoku;
import edu.neu.mobileClass.*;


import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button aboutButton;
	private Button sudokuButton;
	private Button errorButton;
	private Button quitButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PhoneCheckAPI.doAuthorization(this);
		aboutButton = (Button)findViewById(R.id.about_button);
		sudokuButton = (Button)findViewById(R.id.sudoku_button);
		errorButton = (Button)findViewById(R.id.error_button);
		quitButton = (Button)findViewById(R.id.exit_button);
		sudokuButton.setOnClickListener(new SudokuButtonListener());
		aboutButton.setOnClickListener(new AboutButtonListener());
		errorButton.setOnClickListener(new ErrorButtonListener());
		quitButton.setOnClickListener(new ExitButtonListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class SudokuButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(MainActivity.this, Sudoku.class);
		 startActivity(intent);
		}
		
	}
	class AboutButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(MainActivity.this, AboutMe.class);
		 startActivity(intent);
		}
		
	}
	class ErrorButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 int error = 1/0;
		}
		
	}
	class ExitButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 finish();
		}
		
	}

}