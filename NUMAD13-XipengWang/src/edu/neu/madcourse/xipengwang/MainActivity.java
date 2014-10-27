package edu.neu.madcourse.xipengwang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.neu.madcourse.xipengwang.dabble.Dabble;
import edu.neu.madcourse.xipengwang.dabble.TwiceActiveCheck;
import edu.neu.madcourse.xipengwang.dict.Dictionary;
import edu.neu.madcourse.xipengwang.finalProject.Description;
import edu.neu.madcourse.xipengwang.sudoku.Sudoku;
import edu.neu.mhealth.api.KeyValueAPI;
import edu.neu.mobileClass.PhoneCheckAPI;


public class MainActivity extends Activity {
	
	private Button aboutButton;
	private Button sudokuButton;
	private Button errorButton;
	private Button quitButton;
	private Button dictButton;
	private Button dabbleButton;
	private Button commButton;
	private Button twoPDabbleButton;
	private Button finalProject;
	private Button pupilResponse;
    private ImageView grid1;
    private ImageView grid2;
    private ImageView grid3;
    private ImageView grid4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("VPC", "main create");
		setContentView(R.layout.activity_main);
		
		setTitle("Xipeng Wang");
		
		//PhoneCheckAPI.doAuthorization(this);
		TwiceActiveCheck.musicTwicePressed.add(0);
		aboutButton = (Button)findViewById(R.id.about_button);
		sudokuButton = (Button)findViewById(R.id.sudoku_button);
		errorButton = (Button)findViewById(R.id.error_button);
		dictButton = (Button)findViewById(R.id.dict_button);
		dabbleButton = (Button)findViewById(R.id.dabble_button);
		commButton = (Button)findViewById(R.id.comm_button);
		twoPDabbleButton = (Button)findViewById(R.id.twoPDabble_button);
		finalProject = (Button)findViewById(R.id.finalProject_button);
		pupilResponse = (Button)findViewById(R.id.pupilResponce_button);
		quitButton = (Button)findViewById(R.id.exit_button);
		sudokuButton.setOnClickListener(new SudokuButtonListener());
		aboutButton.setOnClickListener(new AboutButtonListener());
		errorButton.setOnClickListener(new ErrorButtonListener());
		dictButton.setOnClickListener(new DictButtonListener());
		dabbleButton.setOnClickListener(new DabbleButtonListener());
		twoPDabbleButton.setOnClickListener(new DabbleButtonListener());
		commButton.setOnClickListener(new CommButtonListener());
		finalProject.setOnClickListener(new FinalProListener());
		pupilResponse.setOnClickListener(new PupilResponseListener());
		quitButton.setOnClickListener(new ExitButtonListener());
		
		
		
		

		
		
		
		
		
		
		//System.out.println("!!! "+KeyValueAPI.get("basin", "basin576095", "Jim"));
		//KeyValueAPI.clearKey("basin", "basin576095", "Jim"+"@HS");
		//KeyValueAPI.clearKey("basin", "basin576095", "Tom"+"@HS");
		//KeyValueAPI.clearKey("basin", "basin576095", "Mary"+"@HS");
		//KeyValueAPI.clearKey("basin", "basin576095", "Fred"+"@HS");
		System.out.println("1"+KeyValueAPI.get("basin", "basin576095", "Jim"));
		System.out.println("2"+KeyValueAPI.get("basin", "basin576095", "Tom"));
		System.out.println("3"+KeyValueAPI.get("basin", "basin576095", "Mary"));
		System.out.println("4"+KeyValueAPI.get("basin", "basin576095", "Fred"));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("VPC", "main destroy");
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("VPC", "main pause");
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("VPC", "main resume");
	}


	public void OutputJar()
	{
		Intent intent =new Intent();
		 intent.setClass(MainActivity.this, edu.neu.madcourse.xipengwang.finalProject.MainActivity.class);
		 startActivity(intent);
	}
	class FinalProListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 Intent intent =new Intent();
			 intent.setClass(MainActivity.this, edu.neu.madcourse.xipengwang.finalProject.MainActivity.class);
			 startActivity(intent);
		}
		
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
		 intent.setClass(MainActivity.this, Dabble.class);
		 startActivity(intent);
		}
		
	}
	class PupilResponseListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent =new Intent();
			intent.setClass(MainActivity.this, Description.class);
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
