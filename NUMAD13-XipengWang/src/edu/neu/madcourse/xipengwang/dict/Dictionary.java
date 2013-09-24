package edu.neu.madcourse.xipengwang.dict;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.dict.WordListLoadService.FirBinder;


public class Dictionary extends Activity{
	WordListLoadService boundService;
	boolean isBound;
	private Button clearButton;
	private Button returnButton;
	private Button ackButton;
	private EditText editText;
	private TextView textView;
	private ProgressBar progressBar;
	private TextView loadtextView;
	private TextWatcherListener textWatcher;
	//private HashMap<String,String> words = new HashMap<String,String>();
	private String[] wordsBack = new String[200];
	private int countBack = 0;
	private String result="";
	final  ServiceConnection conn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			FirBinder binder =(FirBinder)service;
			boundService = binder.getService();
			isBound = true;
			
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBound = false;
		}
		
	};
	final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
	//private String[] words;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		setTitle("Dictionary");
		clearButton = (Button)findViewById(R.id.clear_button);
		returnButton = (Button)findViewById(R.id.return_button);
		ackButton = (Button)findViewById(R.id.Acknowledgements_button);
		editText = (EditText)findViewById(R.id.editText);
		textView = (TextView)findViewById(R.id.display);
		//progressBar = (ProgressBar)findViewById(R.id.progressBar);
		loadtextView =(TextView)findViewById(R.id.loadtext);
		textView.setVerticalScrollBarEnabled(true);
		textWatcher = new TextWatcherListener();
		
		editText.addTextChangedListener(textWatcher);
		clearButton.setOnClickListener(new ButtonListener());
		returnButton.setOnClickListener(new ButtonListener());
		ackButton.setOnClickListener(new ButtonListener());
		Intent intent = new Intent(this, WordListLoadService.class);
		//System.out.println(boundService.equals(null));
		//System.out.println(intent.equals(null));
		//intent.setClass(Dictionary.this, WordListLoadService.class);
		startService(intent);
		bindService(intent, conn, BIND_AUTO_CREATE);
				
		
		//System.out.println("1:"+WordListLoadService.words.containsKey("aah"));
		

	}
	
	class ButtonListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int ch = v.getId();
			switch (ch) {
			case R.id.clear_button:
				textView.setText("");
				editText.setText("");
				result="";
				for(int i =0; i<=countBack; i++){
					WordListLoadService.words.put(wordsBack[i], "");
				}
				break;
			case R.id.return_button:
				for(int i =0; i<=countBack; i++){
					WordListLoadService.words.put(wordsBack[i], "");
				}
				finish();
				break;
			case R.id.Acknowledgements_button:
				Intent intent =new Intent();
				intent.setClass(Dictionary.this, Acknowledgements.class);
				startActivity(intent);
				break;

			default:
				break;
			}
			
		}
		
	}
	private class TextWatcherListener implements TextWatcher {
		private CharSequence inputCharSequence;
		private String paste;
		private int onstart;
		private int atend=0;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			//atend = before;
			//textView.setText(s);
			atend = count;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			//inputCharSequence = s;
			//onstart = after;
			
			//System.out.println("pasted "+atend);
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			System.out.println(s+" length: "+s.length()+" count: "+atend);
			if(atend>1&&s.length()>2){
				for(int i=0; i<s.toString().length()-1;i++){
					
					paste=s.toString().substring(0, s.length()-i);
					System.out.println("paste: "+paste);
					if(WordListLoadService.words.containsKey(paste)){
						if(countBack<200){
							wordsBack[countBack]=paste;
							countBack++;
							}
						WordListLoadService.words.remove(paste.toString());
						System.out.println("detect match");
						result=result+paste.toString()+"\n";
						textView.setMovementMethod(new ScrollingMovementMethod()); 
						textView.setText(result);
					
						
						
					    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
					}
				}
			}
			
			if(atend==1&&(s.length()>2)){
				//int[] res = search(inputCharSequence.toString());
				if(WordListLoadService.words.containsKey(s.toString())){
					if(countBack<200){
						wordsBack[countBack]=s.toString();
						countBack++;
						}
					WordListLoadService.words.remove(s.toString());
					System.out.println("detect match");
					result=result+s.toString()+"\n";
					textView.setMovementMethod(new ScrollingMovementMethod()); 
					textView.setText(result);
				
					
					
				    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
				}
				else{
					System.out.println("undetect match");
				}
				
			}
		}
	}
}