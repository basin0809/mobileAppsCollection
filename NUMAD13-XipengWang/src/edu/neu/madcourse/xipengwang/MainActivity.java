package edu.neu.madcourse.xipengwang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.madcourse.xipengwang.comm.Regist;
import edu.neu.madcourse.xipengwang.dabble.Dabble;
import edu.neu.madcourse.xipengwang.dict.Dictionary;
import edu.neu.madcourse.xipengwang.sudoku.Sudoku;


public class MainActivity extends Activity {
	
	private Button aboutButton;
	private Button sudokuButton;
	private Button errorButton;
	private Button quitButton;
	private Button dictButton;
	private Button dabbleButton;
	private Button commButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle("Xipeng Wang");
		
	//PhoneCheckAPI.doAuthorization(this);

		aboutButton = (Button)findViewById(R.id.about_button);
		sudokuButton = (Button)findViewById(R.id.sudoku_button);
		errorButton = (Button)findViewById(R.id.error_button);
		dictButton = (Button)findViewById(R.id.dict_button);
		dabbleButton = (Button)findViewById(R.id.dabble_button);
		commButton = (Button)findViewById(R.id.comm_button);
		quitButton = (Button)findViewById(R.id.exit_button);
		sudokuButton.setOnClickListener(new SudokuButtonListener());
		aboutButton.setOnClickListener(new AboutButtonListener());
		errorButton.setOnClickListener(new ErrorButtonListener());
		dictButton.setOnClickListener(new DictButtonListener());
		dabbleButton.setOnClickListener(new DabbleButtonListener());
		commButton.setOnClickListener(new CommButtonListener());
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
	class DictButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(MainActivity.this, Dictionary.class);
		 startActivity(intent);
		}
		
	}
	class DabbleButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(MainActivity.this, Dabble.class);
		 startActivity(intent);
		}
		
	}
	class CommButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(MainActivity.this, Regist.class);
		 startActivity(intent);
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
