package edu.neu.madcourse.xipengwang.dabble;


import edu.neu.madcourse.xipengwang.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DabbleACK extends Activity {
   
   private Button backButton;
   private int modeSt;
   private boolean musicGoOn;
   private boolean musicGoOn2;
   @Override
   protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
	   musicGoOn = true;
	   Intent intent = getIntent();
		int musicSt = Integer.parseInt(intent.getStringExtra("music_stuate"));
		
		if(musicSt==1){
			musicGoOn2 = false;
			
		}
		else{
			musicGoOn2 = true;
			
		}
      setContentView(R.layout.dabble_ack);
      setTitle("Acknowledgements");
      backButton = (Button)findViewById(R.id.dabble_back_button);
      backButton.setOnClickListener(new BackListener());
   }
   class BackListener implements OnClickListener{
	   @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		   if(musicGoOn2==true){
				musicGoOn = true;
				finish();}
				else {
					finish();
				}
	}
   }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 if(!musicGoOn)
	            BGMManager.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(musicGoOn2==true){
		musicGoOn=false;
       BGMManager.start(this,R.raw.game);}
		else {
			
		}
	}
}