package edu.neu.madcourse.xipengwang.comm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.mhealth.api.KeyValueAPI;


public class Regist extends Activity{
	private boolean nameIsValid;
	private boolean oppIsValid;
	private EditText name,oppName;
	private TextView desp,desp2;
	
	private Button connectGame;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_reg);  
		name = (EditText)findViewById(R.id.nameText);
		oppName = (EditText)findViewById(R.id.oppNameText);
		desp = (TextView)findViewById(R.id.descriptText);
		desp2 = (TextView)findViewById(R.id.descriptText2);
		connectGame = (Button)findViewById(R.id.searchButton);
		connectGame.setOnClickListener(new Button.OnClickListener(){  
	            public void onClick(View v) {  
	                search();  
	            }  
	        }); 
	}
	
		public void search() {
		String[] strings =new String[2];
		strings[0] = name.getText().toString();
		strings[1] = oppName.getText().toString();
			// TODO Auto-generated method stub
		CheckNameTask checkNameTask = new CheckNameTask(this);
		checkNameTask.execute(strings);
	}
		
		
	
    class CheckNameTask extends AsyncTask<String[], Integer, Boolean> {  
    	ProgressDialog pdialog;  
    	private Context context;  
    	CheckNameTask(Context context) {  
              this.context = context;  
              pdialog = new ProgressDialog(context, 0);     
              pdialog.setButton("cancel", new DialogInterface.OnClickListener() {  
               public void onClick(DialogInterface dialog, int i) {  
                dialog.cancel();  
               }  
              });  
              pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {  
               public void onCancel(DialogInterface dialog) {  
                finish();  
               }  
              });  
              pdialog.setCancelable(true);  
              pdialog.setMax(100);  
              pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
              pdialog.show();
          }  
    

        @Override  
        protected Boolean doInBackground(String[]... params) {  

        	String checkString =KeyValueAPI.get("basin", "basin576095", params[0][0]);
        	System.out.println("Enter: "+params[0][0]);
        	System.out.println("checkString: "+checkString);
        	if(checkString.equals("Error: No Such Key")){
        		KeyValueAPI.put("basin", "basin576095", params[0][0], params[0][1]);
        		int i=0;  
                while(i<10){  
                    i++;  
                    String oppStatus = KeyValueAPI.get("basin", "basin576095", params[0][1]);
                    if(oppStatus.equals(params[0][0])){
                    	return true;
                    }
                    
                    try {  
                        Thread.sleep(1000);  
                    } catch (InterruptedException e) { 
                    	return false;
                    }  
                }  
                return false;
        		
        	}
        	else {
        		return false; 
			}
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
        @Override  
        protected void onPostExecute(Boolean result) {  
            // 返回HTML页面的内容  
           if(result == true){
        	   System.out.println("Opponent Connected! Loading Game");
        	   Toast.makeText(context,"Opponent Connected! Loading Game",Toast.LENGTH_SHORT).show();  
        	   nameIsValid = true;
           }
           else {
        	   System.out.println("Opponent is not found or name is used");
        	   Toast.makeText(context,"Opponent is not found or name is used",Toast.LENGTH_SHORT).show();  
        	   nameIsValid = false;
		}	
            
        }  
 
        @Override  
        protected void onPreExecute() {  
            // 任务启动，可以在这里显示一个对话框，这里简单处理  
            //message.setText(R.string.task_started);  
        }  
 
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            // 更新进度  
              //System.out.println(""+values[0]);  
              //message.setText(""+values[0]);  
              
        }  
 
     }

}
