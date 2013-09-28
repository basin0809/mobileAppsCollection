package edu.neu.madcourse.xipengwang.dict;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.IBinder;
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
	//private TextView loadtextView;
	private TextWatcherListener textWatcher;
	private HashMap<String,String> words = new HashMap<String,String>();
	private String[] wordsBack = new String[200];
	private int countBack = 0;
	private String result="";
	private InputStream input = null;
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
		//loadtextView =(TextView)findViewById(R.id.loadtext);
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
		//startService(intent);
		//bindService(intent, conn, BIND_AUTO_CREATE);
				
		
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
					//words.put(wordsBack[i], "");
					wordsBack[i]="";
				}
				break;
			case R.id.return_button:
				/*for(int i =0; i<=countBack; i++){
					words.put(wordsBack[i], "");
				}*/
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
		private int offset=0;
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
			onstart = s.length();
			offset = start;
			System.out.println("offset "+offset);
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			System.out.println(s+" length: "+s.length()+" count: "+atend);
			if(s.length()==0){
				words.clear();
			}
			if((onstart==0&&atend>0)|(offset==0)){
				if (s.length()!=0){
				char firChar = s.toString().charAt(0);
				
				switch (firChar) {
				case 'a':
					System.out.print("--------->switch to a");
					input = getResources().openRawResource(R.raw.a);
					load(input);
					//match(s.toString());
					break;
				case 'b':
					//System.out.print("--------->switch to b");
					input = getResources().openRawResource(R.raw.b);
					load(input);
					//match(s.toString());
					break;
				case 'c':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.c);
					load(input);
					break;
				case 'd':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.d);
					load(input);
					break;
				case 'e':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.e);
					load(input);
					break;
				case 'f':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.f);
					load(input);
					break;
				case 'g':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.g);
					load(input);
					break;
				case 'h':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.h);
					load(input);
					break;
				case 'i':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.i);
					load(input);
					break;
				case 'j':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.j);
					load(input);
					break;
				case 'k':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.k);
					load(input);
					break;
				case 'l':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.l);
					load(input);
					break;
				case 'm':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.m);
					load(input);
					break;
				case 'n':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.n);
					load(input);
					break;
				case 'o':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.o);
					load(input);
					break;
				case 'p':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.p);
					load(input);
					break;
				case 'q':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.q);
					load(input);
					break;
				case 'r':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.r);
					load(input);
					break;
				case 's':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.s);
					load(input);
					break;
				case 't':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.t);
					load(input);
					break;
				case 'u':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.u);
					load(input);
					break;
				case 'v':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.v);
					load(input);
					break;
				case 'w':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.w);
					load(input);
					break;
				case 'x':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.x);
					load(input);
					break;
				case 'y':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.y);
					load(input);
					break;
				case 'z':
					//System.out.print("--------->switch to c");
					input = getResources().openRawResource(R.raw.z);
					load(input);
					break;
					
				default:
					break;
				}
				}
			}
			if(atend>1&&s.length()>2){
				
				for(int i=0; i<s.toString().length()-1;i++){
					
					paste=s.toString().substring(0, s.length()-i);
					System.out.println("paste: "+paste);
					match(paste);
				}
			}
			
			if(atend==1&&(s.length()>2)){
				
				for(int i=0; i<s.toString().length()-1;i++){
					
					paste=s.toString().substring(0, s.length()-i);
					System.out.println("paste: "+paste);
					match(paste);
				}
			}
			if(atend==0&&(s.length()>2)){
				
				for(int i=0; i<s.toString().length()-1;i++){
					
					paste=s.toString().substring(0, s.length()-i);
					System.out.println("paste: "+paste);
					match(paste);
				}
			}
		}
		public void load(InputStream input) {
			try {
				//words.clear();
				String line;
				int i =0;
				//InputStream input = getResources().openRawResource(R.raw.wordlist);
				InputStreamReader inputStreamReader = new InputStreamReader(input, "GBK");
				BufferedReader reader =new BufferedReader(inputStreamReader);
				System.out.println("file loaded!!! ");
				while ((line = reader.readLine())!= null) {
					words.put(line,"");
					i++;
					//count = i;
					
				}
				
				System.out.println("file readed");
				//isReaded = true;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("file unreaded");
				e.printStackTrace();
			}
		}
		public void match(String s) {
			if(alreadyExistIn(s, wordsBack)){
				
			}
			else{
			if(words.containsKey(s.toString())){
				if(countBack<200){
					wordsBack[countBack]=s.toString();
					countBack++;
					}
				words.remove(s.toString());
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
		
		public boolean alreadyExistIn(String s, String[] wordsBack) {
			for(int i=0; i<wordsBack.length;i++){
				if(s.equalsIgnoreCase(wordsBack[i]))
					return true;
			}
			return false;	
		}
		
		
	}
}