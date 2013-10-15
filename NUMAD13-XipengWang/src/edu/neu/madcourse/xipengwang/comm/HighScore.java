package edu.neu.madcourse.xipengwang.comm;


import edu.neu.madcourse.xipengwang.R;
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
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.comm_score);
      setTitle("Rules ");
      highScorebackButton = (Button)findViewById(R.id.comm_highScore_button);
      highScoreContext = (TextView)findViewById(R.id.comm_highScore_content);
      highScoreContext.setText("Jim: "+HighScoreRecord.highscore[0]+"\n"+"Tom: "+HighScoreRecord.highscore[1]+"\n"+"Mary: "+HighScoreRecord.highscore[2]+"\n"+"Fred: "+HighScoreRecord.highscore[3]);
      highScorebackButton.setOnClickListener(new HighScoreBackListener());
   }
   class HighScoreBackListener implements OnClickListener{
	   @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		   finish();
	}
   }
   
}
