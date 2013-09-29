package edu.neu.madcourse.xipengwang.dabble;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.madcourse.xipengwang.MainActivity;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.sudoku.Sudoku;

public class Dabble extends Activity{
	private Button gameButton;
	private Button quitButton;
	private Button testButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dabble);
		gameButton = (Button)findViewById(R.id.game_button);
		quitButton = (Button)findViewById(R.id.quit_button);
		testButton = (Button)findViewById(R.id.test_button);
		gameButton.setOnClickListener(new GameButtonListener());
		testButton.setOnClickListener(new TestButtonListener());
		quitButton.setOnClickListener(new QuitButtonListener());
		
		
	}
	class GameButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(Dabble.this, Game.class);
		 startActivity(intent);
		}
		
	}
	class TestButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 Intent intent =new Intent();
		 intent.setClass(Dabble.this, TestGame.class);
		 startActivity(intent);
		}
		
	}
	class QuitButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		 finish();
		}
		
	}
	
}
