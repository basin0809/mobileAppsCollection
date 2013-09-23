package edu.neu.madcourse.xipengwang.dict;


import edu.neu.madcourse.xipengwang.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Acknowledgements extends Activity {
   
   private Button backButton;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.acknowledgements);
      setTitle("Acknowledgements");
      backButton = (Button)findViewById(R.id.back_button);
      backButton.setOnClickListener(new BackListener());
   }
   class BackListener implements OnClickListener{
	   @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
   }
}