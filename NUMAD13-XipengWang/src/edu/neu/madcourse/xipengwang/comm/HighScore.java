package edu.neu.madcourse.xipengwang.comm;


import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.dabble.BGMManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HighScore extends Activity {
   
   private Button highScorebackButton;
   private TextView highScoreContext;
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
      setContentView(R.layout.comm_score);
      setTitle("High Score");
      
      highScorebackButton = (Button)findViewById(R.id.comm_highScore_button);
      highScoreContext = (TextView)findViewById(R.id.comm_highScore_content);
      highScoreContext.setText(HighScoreRecord.highscore[0]+"\n"+HighScoreRecord.highscore[1]+"\n"+HighScoreRecord.highscore[2]+"\n"+HighScoreRecord.highscore[3]);
      highScorebackButton.setOnClickListener(new HighScoreBackListener());
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


class HighScoreBackListener implements OnClickListener{
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
   
}
