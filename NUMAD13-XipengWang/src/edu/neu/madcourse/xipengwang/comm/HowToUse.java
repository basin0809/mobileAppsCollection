package edu.neu.madcourse.xipengwang.comm;


import edu.neu.madcourse.xipengwang.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HowToUse extends Activity {
   
   private Button htubackButton;

   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.comm_intro);
      setTitle("How to play?");
      htubackButton = (Button)findViewById(R.id.comm_introback_button);
      htubackButton.setOnClickListener(new HTUListener());
   }
   class HTUListener implements OnClickListener{
	   @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
   }

}
