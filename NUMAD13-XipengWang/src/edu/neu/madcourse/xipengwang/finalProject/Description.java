package edu.neu.madcourse.xipengwang.finalProject;

import edu.neu.madcourse.xipengwang.MainActivity;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.dabble.Dabble;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Description extends Activity{
	private Button jump;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_description);
		jump = (Button)findViewById(R.id.p_introback_button);
		jump.setOnClickListener(new JumpToApp());
		
	}
	public class JumpToApp implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent =new Intent();
			intent.setClass(Description.this, edu.neu.madcourse.xipengwang.finalProject.MainActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	
}
