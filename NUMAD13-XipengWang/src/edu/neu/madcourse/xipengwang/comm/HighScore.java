package edu.neu.madcourse.xipengwang.comm;


import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.comm.ChooseName.CheckNetWorkTask;
import edu.neu.madcourse.xipengwang.dabble.BGMManager;
import edu.neu.mhealth.api.KeyValueAPI;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
   
   AlertDialog alertDialog;
   AlertDialog.Builder alertDialogBuilder;
   CheckNetWorkTask checkNetWorkTask;

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
	checkNetWorkTask.cancel(true);
	if(!musicGoOn)
        BGMManager.pause();
}


@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	checkNetWorkTask = new CheckNetWorkTask(this);
	checkNetWorkTask.execute();
	if(musicGoOn2==true){
		musicGoOn=false;
      BGMManager.start(this,R.raw.game);}
		else {
			
		}
}
class CheckNetWorkTask extends AsyncTask<Void, Integer, Boolean>{
	private Context context;  
	
	CheckNetWorkTask(Context context) {  
          this.context = context;  
          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
          
      }  
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		while(KeyValueAPI.isServerAvailable()==true){
			System.out.println("Check NetWork: true");
		 try {  
              Thread.sleep(1500);  
          } 
		 catch (InterruptedException e) { 
          	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, OppNameMyName.myFakeName);
          	return false;
          } 
		 }
		System.out.println("Check NetWork: false");
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result==false){
			alertDialogBuilder=new AlertDialog.Builder(HighScore.this);  
			
			alertDialogBuilder.setTitle("Opps, cannot conncet to server.")

            .setMessage("Please check your network and then restart the application.")

            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialoginterface, int i){
                	 setResult(RESULT_OK);
                	 finish();
                 }
         });
			alertDialog = alertDialogBuilder.show(); 
		}
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
