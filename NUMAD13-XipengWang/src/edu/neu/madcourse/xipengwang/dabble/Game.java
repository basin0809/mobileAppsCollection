package edu.neu.madcourse.xipengwang.dabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.madcourse.xipengwang.R;


public class Game extends Activity{
	private HashMap<String,String> words1 = new HashMap<String,String>();
	private HashMap<String,String> words2 = new HashMap<String,String>();
	private HashMap<String,String> words3 = new HashMap<String,String>();
	private HashMap<String,String> words4 = new HashMap<String,String>();
	private String[] wordsBack = new String[200];
	private int countBack = 0;
	private int score =0;
	private int invisibleScore =0;
	private int[] tempScore = new int[4];
	private int[] invisibleTempScore = new int[4];
	private String result="";
	private InputStream input = null;
	private boolean[] buttonIsPressed = new boolean[18];
	private ArrayList<Integer> buttonTwicePressed = new ArrayList<Integer>();
	private ArrayList<Integer> pauseTwicePressed = new ArrayList<Integer>();
	private boolean[] scoreLocates = new boolean[4];
	private Button[] letterButtons = new Button[18];
	private Button pauseButton, hintButton, backButton;
	private TextView cdText, scoreText, invisibleScoreText ;
	private static Random random = null;
	private static String[] scoreList = {"3","3","3","3","3","3","3","3","6","6","6","6","6","6","9","9","9","9"};
	
	private static char[] puzzleChar = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
										'n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static String[] puzzle = new String[18];
	private static String[] puzzleScore = new String[18];
	
	private MyCount mc;
	private long timeLeft=30000;
	private boolean musicGoOn;
	private boolean musicGoOn2;
	private ArrayList<Integer> musicTwicePressed2 = new ArrayList<Integer>();
	private static MediaPlayer mp = null;
	final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		musicGoOn = true;
		setContentView(R.layout.dabble_game);
		Intent intent = getIntent();
		int musicSt = Integer.parseInt(intent.getStringExtra("music_stuate"));
		if(musicSt==1){
			musicGoOn2 = false;
			
		}
		else{
			musicGoOn2 = true;
			
		}
		//pauseTwicePressed.add(0);
		letterButtons[0]=(Button)findViewById(R.id.button1);
		letterButtons[1]=(Button)findViewById(R.id.button2);
		letterButtons[2]=(Button)findViewById(R.id.button3);
		letterButtons[3]=(Button)findViewById(R.id.button4);
		letterButtons[4]=(Button)findViewById(R.id.button5);
		letterButtons[5]=(Button)findViewById(R.id.button6);
		letterButtons[6]=(Button)findViewById(R.id.button7);
		letterButtons[7]=(Button)findViewById(R.id.button8);
		letterButtons[8]=(Button)findViewById(R.id.button9);
		letterButtons[9]=(Button)findViewById(R.id.button10);
		letterButtons[10]=(Button)findViewById(R.id.button11);
		letterButtons[11]=(Button)findViewById(R.id.button12);
		letterButtons[12]=(Button)findViewById(R.id.button13);
		letterButtons[13]=(Button)findViewById(R.id.button14);
		letterButtons[14]=(Button)findViewById(R.id.button15);
		letterButtons[15]=(Button)findViewById(R.id.button16);
		
		letterButtons[16]=(Button)findViewById(R.id.button17);
		letterButtons[17]=(Button)findViewById(R.id.button18);
		 System.out.println("SDK ver: "+android.os.Build.VERSION.SDK_INT);
		Display display = this.getWindowManager().getDefaultDisplay();
	       
	    
		   
	        
			
	         
		for (int i =0; i<18; i++) {
			letterButtons[i].setTextColor(getResources().getColor(R.color.black));
			
			letterButtons[i].setOnClickListener(new LetterButtonsListener());
		}
		pauseButton = (Button)findViewById(R.id.Pause_button);
		
		hintButton = (Button)findViewById(R.id.Hintbutton);
		backButton = (Button)findViewById(R.id.Backbutton);
		backButton.setOnClickListener(new QuitListener());
		cdText = (TextView)findViewById(R.id.CountDown);
		scoreText = (TextView)findViewById(R.id.Score);
		invisibleScoreText = (TextView)findViewById(R.id.inVisibleScore);
		invisibleScoreText.addTextChangedListener(new ScoreTextListener());
		scoreText.setText("0");
		cdText.setText("00:00");
		String string = (randomWords(6)+randomWords(5)+randomWords(4)+randomWords(3));
		System.out.println("String: "+string);
		ArrayList<String> sList = new ArrayList<String>();
		for (int i =0; i<18; i++) {
			sList.add(scoreList[i]);
		}
		Collections.shuffle(sList);
		ArrayList<String> arrayList = new ArrayList<String>();
		for(int i =0; i<18; i++){
			arrayList.add(String.valueOf(string.charAt(i))+"\n"+"     "+sList.get(i));
			
		}
		Collections.shuffle(arrayList);
		for(int i =0; i<18; i++){
			puzzle[i]=arrayList.get(i);
			//puzzleScore[i]=arrayList.get(i)[1];
			//System.out.println(puzzle[i]);
		}
		
		for (int i =0; i<18; i++) {
			
			//letterButtons[i].setText(puzzle[i].charAt(0)+"\n"+"   "+puzzleScore[i].charAt(1));	
			letterButtons[i].setText(puzzle[i]);
			letterButtons[i].setTextSize(10);
		}
		
		for (int i =0; i<18; i++) {
			buttonIsPressed[i]= false;
		}
		
		for (int i =0; i<4; i++) {
			scoreLocates[i]= false;
		}
		for (int i =0; i<4; i++) {
			tempScore[i]= 0;
		}
		for (int i =0; i<4; i++) {
			invisibleTempScore[i]= 0;
		}
		
		System.out.println(letterButtons[0].getText().charAt(0));
		System.out.println(letterButtons[6].getText().charAt(0));
		System.out.println(letterButtons[11].getText().charAt(0));
		System.out.println(letterButtons[15].getText().charAt(0));
		loadDict(letterButtons[0].getText().charAt(0),words1,6);
		loadDict(letterButtons[6].getText().charAt(0),words2,5);
		loadDict(letterButtons[11].getText().charAt(0),words3,4);
		loadDict(letterButtons[15].getText().charAt(0),words4,3);
		String line1 = String.valueOf(letterButtons[0].getText().charAt(0))+
				String.valueOf(letterButtons[1].getText().charAt(0))+
					String.valueOf(letterButtons[2].getText().charAt(0))+
						String.valueOf(letterButtons[3].getText().charAt(0))+
							String.valueOf(letterButtons[4].getText().charAt(0))+
								String.valueOf(letterButtons[5].getText().charAt(0));
		int pscore1 = Integer.parseInt(String.valueOf(letterButtons[0].getText().charAt(letterButtons[0].getText().length()-1)))+
				Integer.parseInt(String.valueOf(letterButtons[1].getText().charAt(letterButtons[0].getText().length()-1)))+
					Integer.parseInt(String.valueOf(letterButtons[2].getText().charAt(letterButtons[0].getText().length()-1)))+
						Integer.parseInt(String.valueOf(letterButtons[3].getText().charAt(letterButtons[0].getText().length()-1)))+
							Integer.parseInt(String.valueOf(letterButtons[4].getText().charAt(letterButtons[0].getText().length()-1)))+
								Integer.parseInt(String.valueOf(letterButtons[5].getText().charAt(letterButtons[0].getText().length()-1)));
		String line2 = String.valueOf(letterButtons[6].getText().charAt(0))+
				String.valueOf(letterButtons[7].getText().charAt(0))+
					String.valueOf(letterButtons[8].getText().charAt(0))+
						String.valueOf(letterButtons[9].getText().charAt(0))+
							String.valueOf(letterButtons[10].getText().charAt(0));
		int pscore2 = Integer.parseInt(String.valueOf(letterButtons[6].getText().charAt(letterButtons[0].getText().length()-1)))+
				Integer.parseInt(String.valueOf(letterButtons[7].getText().charAt(letterButtons[0].getText().length()-1)))+
					Integer.parseInt(String.valueOf(letterButtons[8].getText().charAt(letterButtons[0].getText().length()-1)))+
						Integer.parseInt(String.valueOf(letterButtons[9].getText().charAt(letterButtons[0].getText().length()-1)))+
							Integer.parseInt(String.valueOf(letterButtons[10].getText().charAt(letterButtons[0].getText().length()-1)));
		String line3 = String.valueOf(letterButtons[11].getText().charAt(0))+
				String.valueOf(letterButtons[12].getText().charAt(0))+
					String.valueOf(letterButtons[13].getText().charAt(0))+
						String.valueOf(letterButtons[14].getText().charAt(0));
		int pscore3 = Integer.parseInt(String.valueOf(letterButtons[11].getText().charAt(letterButtons[0].getText().length()-1)))+
				Integer.parseInt(String.valueOf(letterButtons[12].getText().charAt(letterButtons[0].getText().length()-1)))+
					Integer.parseInt(String.valueOf(letterButtons[13].getText().charAt(letterButtons[0].getText().length()-1)))+
						Integer.parseInt(String.valueOf(letterButtons[14].getText().charAt(letterButtons[0].getText().length()-1)));						
		String line4 = String.valueOf(letterButtons[15].getText().charAt(0))+
				String.valueOf(letterButtons[16].getText().charAt(0))+
					String.valueOf(letterButtons[17].getText().charAt(0));
		int pscore4 = Integer.parseInt(String.valueOf(letterButtons[15].getText().charAt(letterButtons[0].getText().length()-1)))+
				Integer.parseInt(String.valueOf(letterButtons[16].getText().charAt(letterButtons[0].getText().length()-1)))+
					Integer.parseInt(String.valueOf(letterButtons[17].getText().charAt(letterButtons[0].getText().length()-1)));
		int score1 = match(line1, words1, pscore1, 1);
		displayword1(score1);
		int score2 = match(line2, words2, pscore2, 2);
		displayword2(score2);
		int score3 = match(line3, words3, pscore3, 3);
		displayword3(score3);
		int score4 = match(line4, words4, pscore4, 4);
		displayword4(score4);
		buttonTwicePressed.add(18);
		pauseTwicePressed.add(0);
		mc = new MyCount(300000, 1000);  
        mc.start();
        pauseButton.setOnClickListener(new PauseListener());
        
        
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
	class QuitListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(musicGoOn2==true){
			musicGoOn = true;}
			finish();
		}
		
	}
	class PauseListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(pauseTwicePressed.get(pauseTwicePressed.size()-1)==0){
				System.out.println("PAUSE");
			// TODO Auto-generated method stub
			mc.cancel();
			for (int i =0; i<18; i++) {
				//letterButtons[i].setTextColor(getResources().getColor(R.color.black));
				letterButtons[i].getBackground().setAlpha(70);
				letterButtons[i].setOnClickListener(null);
			}
			
			hintButton.getBackground().setAlpha(70);
			hintButton.setOnClickListener(null);
			
			backButton.getBackground().setAlpha(70);
			backButton.setOnClickListener(null);
			
			pauseButton.setText("Continue");
			pauseTwicePressed.add(1);
			}
			else{
				System.out.println("CONTINUE");
				for (int i =0; i<18; i++) {
					//letterButtons[i].setTextColor(getResources().getColor(R.color.black));
			
					letterButtons[i].getBackground().setAlpha(255);
					letterButtons[i].setOnClickListener(new LetterButtonsListener());
				}
				
				hintButton.getBackground().setAlpha(255);
				hintButton.setOnClickListener(null);
				
				backButton.getBackground().setAlpha(255);
				backButton.setOnClickListener(new QuitListener());
				
				pauseButton.setText("Pause");
				mc = new MyCount(timeLeft, 1000);  
		        mc.start();
				pauseTwicePressed.add(0);
			}
		}
		
	}

	class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {     
        	cdText.setText("finish");        
        }     
        @Override     
        public void onTick(long millisUntilFinished) { 
        	Integer miliSec = new Integer(new Double(millisUntilFinished).intValue());  
        	Integer cdSecs = miliSec / 1000;  

        	Integer minutes = (cdSecs % 3600) / 60;  
        	Integer seconds = (cdSecs % 3600) % 60;    

        	cdText.setText(String.format("%02d", minutes) + ":"  
        	+ String.format("%02d", seconds));
        	//cdText.setText(millisUntilFinished / 1000 +""); 
        	if(cdSecs<=270){
        		cdText.setTextColor(getResources().getColor(R.color.red));
        		cdText.setTextSize(20);
        	}
        	timeLeft = millisUntilFinished ;
           // Toast.makeText(NewActivity.this, millisUntilFinished / 1000 + "", Toast.LENGTH_LONG).show(); 
        }    
    }  
	class ScoreTextListener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			System.out.println("------------->Score: "+s);
			if(Integer.parseInt(s.toString())==96){
				finish();
				Intent intent =new Intent();
				intent.setClass(Game.this, Cong.class);
				intent.putExtra("lastScore", scoreText.getText().toString());
				startActivity(intent);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		

		
		
	}
		
	class LetterButtonsListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			int buttonId = v.getId();
			//mp = MediaPlayer.create(Game.this, R.raw.balloon);
	       // mp.start();
			tg.startTone(ToneGenerator.TONE_PROP_BEEP);
			switch (buttonId) {
			case R.id.button1:
				swapIfValid(0);
				break;
			case R.id.button2:
				swapIfValid(1);
				break;
			case R.id.button3:
				swapIfValid(2);
				break;
			case R.id.button4:
				swapIfValid(3);
				break;
			case R.id.button5:
				swapIfValid(4);
				break;
			case R.id.button6:
				swapIfValid(5);
				break;
			case R.id.button7:
				swapIfValid(6);
				break;
			case R.id.button8:
				swapIfValid(7);
				break;
			case R.id.button9:
				swapIfValid(8);
				break;
			case R.id.button10:
				swapIfValid(9);
				break;
			case R.id.button11:
				swapIfValid(10);
				break;
			case R.id.button12:
				swapIfValid(11);
				break;
			case R.id.button13:
				swapIfValid(12);
				break;
			case R.id.button14:
				swapIfValid(13);
				break;
			case R.id.button15:
				swapIfValid(14);
				break;
			case R.id.button16:
				swapIfValid(15);
				break;
			case R.id.button17:
				swapIfValid(16);
				break;
			case R.id.button18:
				swapIfValid(17);
				break;

			default:
				break;
			}
			//loadDict();
		}
		
	}
	public void swapIfValid(int index){
		buttonIsPressed[index]= true;
		buttonTwicePressed.add(index);
		if(buttonTwicePressed.get(buttonTwicePressed.size()-1)!=buttonTwicePressed.get(buttonTwicePressed.size()-2)){
			letterButtons[index].setTextColor(getResources().getColor(
	            R.color.red));
			swapText(index);
			//buttonTwicePressed.add(18);
		}
		else {
			letterButtons[index].setTextColor(getResources().getColor(
		            R.color.black));
			buttonIsPressed[index]= false;
			buttonTwicePressed.add(18);
		}
	}
	public void loadDict(char fir, HashMap<String,String> words, int line){
		switch (fir) {
		case 'a':
			System.out.print("--------->switch to a");
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.a3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.a4);
				load(input,words);
				break;
			case 5:
				input = getResources().openRawResource(R.raw.a5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.a6);
				load(input,words);
				break;

			default:
				break;
			}
			
			//match(s.toString());
			break;
		case 'b':
			System.out.print("--------->switch to b");
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.b3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.b4);
				load(input,words);
				break;
			case 5:
				input = getResources().openRawResource(R.raw.b5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.b6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'c':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.c3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.c4);
				load(input,words);
				break;
			case 5:
				input = getResources().openRawResource(R.raw.c5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.c6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'd':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.d3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.d4);
				load(input,words);
				break;
			case 5:
				input = getResources().openRawResource(R.raw.d5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.d6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'e':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.e3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.e4);
				load(input,words);
				break;
			case 5:
				input = getResources().openRawResource(R.raw.e5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.e6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'f':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.f3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.f4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.f5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.f6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'g':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.g3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.g4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.g5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.g6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'h':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.h3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.h4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.h5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.h6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'i':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.i3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.i4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.i5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.i6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'j':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.j3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.j4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.j5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.j6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'k':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.k3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.k4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.k5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.k6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'l':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.l3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.l4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.l5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.l6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'm':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.m3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.m4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.m5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.m6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'n':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.n3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.n4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.n5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.n6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'o':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.o3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.o4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.o5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.o6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'p':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.p3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.p4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.p5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.p6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'q':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.q3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.q4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.q5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.q6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'r':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.r3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.r4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.r5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.r6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 's':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.s3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.s4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.s5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.s6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 't':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.t3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.t4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.t5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.t6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'u':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.u3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.u4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.u5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.u6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'v':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.v3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.v4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.v5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.v6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'w':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.w3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.w4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.w5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.w6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'x':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.x3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.x4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.x5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.x6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'y':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.y3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.y4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.y5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.y6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
		case 'z':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.z3);
				load(input,words);
				break;
			case 4:
				input = getResources().openRawResource(R.raw.z4);
				load(input,words);
			break;
				case 5:
				input = getResources().openRawResource(R.raw.z5);
				load(input,words);
				break;
			case 6:
				input = getResources().openRawResource(R.raw.z6);
				load(input,words);
				break;

			default:
				break;
			}
			break;
			
		default:
			break;
		}
	}
	
	
	public String loadDictRandom(char fir, HashMap<String,String> words, int line){
		switch (fir) {
		case 'a':
			System.out.print("--------->switch to a");
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.a3);
				return loadRandom(input,words,152);
			case 4:
				input = getResources().openRawResource(R.raw.a4);
				return loadRandom(input,words,458);
			case 5:
				input = getResources().openRawResource(R.raw.a5);
				return loadRandom(input,words,1195);
			case 6:
				input = getResources().openRawResource(R.raw.a6);
				return loadRandom(input,words,2136);

			default:
				return "";
			}
			
			//match(s.toString());
			
		case 'b':
			System.out.print("--------->switch to b");
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.b3);
				return loadRandom(input,words,93);
			case 4:
				input = getResources().openRawResource(R.raw.b4);
				return loadRandom(input,words,479);
			case 5:
				input = getResources().openRawResource(R.raw.b5);
				return loadRandom(input,words,1272);
			case 6:
				input = getResources().openRawResource(R.raw.b6);
				return loadRandom(input,words,2384);

			default:
				return "";
			}
			 
		case 'c':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.c3);
				return loadRandom(input,words,104);
			case 4:
				input = getResources().openRawResource(R.raw.c4);
				return loadRandom(input,words,439);
			case 5:
				input = getResources().openRawResource(R.raw.c5);
				return loadRandom(input,words,1277);
			case 6:
				input = getResources().openRawResource(R.raw.c6);
				return loadRandom(input,words,2700);

			default:
				return "";
			}
			 
		case 'd':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.d3);
				return loadRandom(input,words,100);
			case 4:
				input = getResources().openRawResource(R.raw.d4);
				return loadRandom(input,words,407);
			case 5:
				input = getResources().openRawResource(R.raw.d5);
				return loadRandom(input,words,910);
			case 6:
				input = getResources().openRawResource(R.raw.d6);
				return loadRandom(input,words,1691);

			default:
				return "";
			}
			 
		case 'e':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.e3);
				return loadRandom(input,words,98);
			case 4:
				input = getResources().openRawResource(R.raw.e4);
				return loadRandom(input,words,252);
			case 5:
				input = getResources().openRawResource(R.raw.e5);
				return loadRandom(input,words,470);
			case 6:
				input = getResources().openRawResource(R.raw.e6);
				return loadRandom(input,words,1049);

			default:
				return "";
			}
			 
		case 'f':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.f3);
				return loadRandom(input,words,80);
			case 4:
				input = getResources().openRawResource(R.raw.f4);
				return loadRandom(input,words,323);
			case 5:
				input = getResources().openRawResource(R.raw.f5);
				return loadRandom(input,words,750);
			case 6:
				input = getResources().openRawResource(R.raw.f6);
				return loadRandom(input,words,1336);

			default:
				return "";
			}
			 
		case 'g':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.g3);
				return loadRandom(input,words,94);
			case 4:
				input = getResources().openRawResource(R.raw.g4);
				return loadRandom(input,words,381);
			case 5:
				input = getResources().openRawResource(R.raw.g5);
				return loadRandom(input,words,832);
			case 6:
				input = getResources().openRawResource(R.raw.g6);
				return loadRandom(input,words,1465);

			default:
				return "";
			}
			 
		case 'h':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.h3);
				return loadRandom(input,words,86);
			case 4:
				input = getResources().openRawResource(R.raw.h4);
				return loadRandom(input,words,343);
			case 5:
				input = getResources().openRawResource(R.raw.h5);
				return loadRandom(input,words,664);
			case 6:
				input = getResources().openRawResource(R.raw.h6);
				return loadRandom(input,words,1155);

			default:
				return "";
			}
			 
		case 'i':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.i3);
				return loadRandom(input,words,74);
			case 4:
				input = getResources().openRawResource(R.raw.i4);
				return loadRandom(input,words,146);
			case 5:
				input = getResources().openRawResource(R.raw.i5);
				return loadRandom(input,words,299);
			case 6:
				input = getResources().openRawResource(R.raw.i6);
				return loadRandom(input,words,647);

			default:
				return "";
			}
			 
		case 'j':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.j3);
				return loadRandom(input,words,48);
			case 4:
				input = getResources().openRawResource(R.raw.j4);
				return loadRandom(input,words,173);
			case 5:
				input = getResources().openRawResource(R.raw.j5);
				return loadRandom(input,words,276);
			case 6:
				input = getResources().openRawResource(R.raw.j6);
				return loadRandom(input,words,498);

			default:
				return "";
			}
			 
		case 'k':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.k3);
				return loadRandom(input,words,67);
			case 4:
				input = getResources().openRawResource(R.raw.k4);
				return loadRandom(input,words,285);
			case 5:
				input = getResources().openRawResource(R.raw.k5);
				return loadRandom(input,words,553);
			case 6:
				input = getResources().openRawResource(R.raw.k6);
				return loadRandom(input,words,852);

			default:
				return "";
			}
			 
		case 'l':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.l3);
				return loadRandom(input,words,95);
			case 4:
				input = getResources().openRawResource(R.raw.l4);
				return loadRandom(input,words,365);
			case 5:
				input = getResources().openRawResource(R.raw.l5);
				return loadRandom(input,words,778);
			case 6:
				input = getResources().openRawResource(R.raw.l6);
				return loadRandom(input,words,1261);
			default:
				return "";
			}
			 
		case 'm':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.m3);
				return loadRandom(input,words,102);
			case 4:
				input = getResources().openRawResource(R.raw.m4);
				return loadRandom(input,words,417);
			case 5:
				input = getResources().openRawResource(R.raw.m5);
				return loadRandom(input,words,934);
			case 6:
				input = getResources().openRawResource(R.raw.m6);
				return loadRandom(input,words,1745);

			default:
				return "";
			}
			 
		case 'n':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.n3);
				return loadRandom(input,words,69);
			case 4:
				input = getResources().openRawResource(R.raw.n4);
				return loadRandom(input,words,232);
			case 5:
				input = getResources().openRawResource(R.raw.n5);
				return loadRandom(input,words,482);
			case 6:
				input = getResources().openRawResource(R.raw.n6);
				return loadRandom(input,words,749);

			default:
				return "";
			}
			 
		case 'o':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.o3);
				return loadRandom(input,words,96);
			case 4:
				input = getResources().openRawResource(R.raw.o4);
				return loadRandom(input,words,225);
			case 5:
				input = getResources().openRawResource(R.raw.o5);
				return loadRandom(input,words,363);
			case 6:
				input = getResources().openRawResource(R.raw.o6);
				return loadRandom(input,words,777);

			default:
				return "";
			}
			 
		case 'p':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.p3);
				return loadRandom(input,words,121);
			case 4:
				input = getResources().openRawResource(R.raw.p4);
				return loadRandom(input,words,440);
			case 5:
				input = getResources().openRawResource(R.raw.p5);
				return loadRandom(input,words,1109);
			case 6:
				input = getResources().openRawResource(R.raw.p6);
				return loadRandom(input,words,2207);

			default:
				return "";
			}
			 
		case 'q':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.q3);
				return loadRandom(input,words,13);
			case 4:
				input = getResources().openRawResource(R.raw.q4);
				return loadRandom(input,words,35);
			case 5:
				input = getResources().openRawResource(R.raw.q5);
				return loadRandom(input,words,100);
			case 6:
				input = getResources().openRawResource(R.raw.q6);
				return loadRandom(input,words,178);

			default:
				return "";
			}
			 
		case 'r':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.r3);
				return loadRandom(input,words,80);
			case 4:
				input = getResources().openRawResource(R.raw.r4);
				return loadRandom(input,words,328);
			case 5:
				input = getResources().openRawResource(R.raw.r5);
				return loadRandom(input,words,788);
			case 6:
				input = getResources().openRawResource(R.raw.r6);
				return loadRandom(input,words,1759);

			default:
				return "";
			}
			 
		case 's':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.s3);
				return loadRandom(input,words,125);
			case 4:
				input = getResources().openRawResource(R.raw.s4);
				return loadRandom(input,words,659);
			case 5:
				input = getResources().openRawResource(R.raw.s5);
				return loadRandom(input,words,2010);
			case 6:
				input = getResources().openRawResource(R.raw.s6);
				return loadRandom(input,words,3670);

			default:
				return "";
			}
			 
		case 't':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.t3);
				return loadRandom(input,words,128);
			case 4:
				input = getResources().openRawResource(R.raw.t4);
				return loadRandom(input,words,446);
			case 5:
				input = getResources().openRawResource(R.raw.t5);
				return loadRandom(input,words,1105);
			case 6:
				input = getResources().openRawResource(R.raw.t6);
				return loadRandom(input,words,2041);

			default:
				return "";
			}
			 
		case 'u':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.u3);
				return loadRandom(input,words,63);
			case 4:
				input = getResources().openRawResource(R.raw.u4);
				return loadRandom(input,words,112);
			case 5:
				input = getResources().openRawResource(R.raw.u5);
				return loadRandom(input,words,352);
			case 6:
				input = getResources().openRawResource(R.raw.u6);
				return loadRandom(input,words,991);

			default:
				return "";
			}
			 
		case 'v':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.v3);
				return loadRandom(input,words,41);
			case 4:
				input = getResources().openRawResource(R.raw.v4);
				return loadRandom(input,words,135);
			case 5:
				input = getResources().openRawResource(R.raw.v5);
				return loadRandom(input,words,320);
			case 6:
				input = getResources().openRawResource(R.raw.v6);
				return loadRandom(input,words,595);

			default:
				return "";
			}
			 
		case 'w':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.w3);
				return loadRandom(input,words,70);
			case 4:
				input = getResources().openRawResource(R.raw.w4);
				return loadRandom(input,words,296);
			case 5:
				input = getResources().openRawResource(R.raw.w5);
				return loadRandom(input,words,530);
			case 6:
				input = getResources().openRawResource(R.raw.w6);
				return loadRandom(input,words,876);

			default:
				return "";
			}
			 
		case 'x':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.x3);
				return loadRandom(input,words,11);
			case 4:
				input = getResources().openRawResource(R.raw.x4);
				return loadRandom(input,words,18);
			case 5:
				input = getResources().openRawResource(R.raw.x5);
				return loadRandom(input,words,27);
			case 6:
				input = getResources().openRawResource(R.raw.x6);
				return loadRandom(input,words,32);

			default:
				return "";
			}
			 
		case 'y':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.y3);
				return loadRandom(input,words,51);
			case 4:
				input = getResources().openRawResource(R.raw.y4);
				return loadRandom(input,words,171);
			case 5:
				input = getResources().openRawResource(R.raw.y5);
				return loadRandom(input,words,219);
			case 6:
				input = getResources().openRawResource(R.raw.y6);
				return loadRandom(input,words,255);

			default:
				return "";
			}
			 
		case 'z':
			switch (line) {
			case 3:
				input = getResources().openRawResource(R.raw.z3);
				return loadRandom(input,words,24);
			case 4:
				input = getResources().openRawResource(R.raw.z4);
				return loadRandom(input,words,59);
			case 5:
				input = getResources().openRawResource(R.raw.z5);
				return loadRandom(input,words,91);
			case 6:
				input = getResources().openRawResource(R.raw.z6);
				return loadRandom(input,words,144);

			default:
				return "";
			}
			 
			
		default:
			return "";
		}
	}
	
	public void load(InputStream input,HashMap<String,String> words) {
		try {
			words.clear();
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
	
	public String loadRandom(InputStream input,HashMap<String,String> words, int len) {
		try {
			words.clear();
			String line;
			String selStr="";
			Random random = new Random();
			int selStrIndex = random.nextInt(len-3)+1;
			int i =0;
			//InputStream input = getResources().openRawResource(R.raw.wordlist);
			InputStreamReader inputStreamReader = new InputStreamReader(input, "GBK");
			BufferedReader reader =new BufferedReader(inputStreamReader);
			System.out.println("file loaded!!! ");
			while ((line = reader.readLine())!= null) {
				if(i==selStrIndex){
					selStr = line;
				}
				words.put(line,"");
				i++;
				//count = i;
				
			}
			
			System.out.println("file readed");
			//isReaded = true;
			return selStr;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("file unreaded");
			e.printStackTrace();
			return "";
		}
	}
	
	public int match(String s, HashMap<String,String> words, int pscore, int locate) {
		
		if(alreadyExistIn(s, wordsBack)){
			scoreLocates[locate-1]=true;
			
			score= pscore*(9-locate)+score-tempScore[locate-1];
			tempScore[locate-1]=pscore*(9-locate);
			scoreText.setText(score+"");
			
			invisibleScore= pscore+invisibleScore-invisibleTempScore[locate-1];
			invisibleTempScore[locate-1]=pscore;
			invisibleScoreText.setText(invisibleScore+"");
			
			mp = MediaPlayer.create(Game.this, R.raw.match);
	        mp.start();
			//tg.startTone(ToneGenerator.TONE_DTMF_5, 200);
			return 1;
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
			//textView.setMovementMethod(new ScrollingMovementMethod()); 
			//textView.setText(result);
			
			scoreLocates[locate-1]=true;
			
			score= pscore*(9-locate)+score-tempScore[locate-1];
			tempScore[locate-1]=pscore*(9-locate);
			scoreText.setText(score+"");
			
			
			invisibleScore= pscore+invisibleScore-invisibleTempScore[locate-1];
			invisibleTempScore[locate-1]=pscore;
			invisibleScoreText.setText(invisibleScore+"");
			
			mp = MediaPlayer.create(Game.this, R.raw.match);
	        mp.start();
			//tg.startTone(ToneGenerator.TONE_DTMF_5, 200);
		    return score;
		}
		else{
			System.out.println("undetect match");
			if(scoreLocates[locate-1]==true){
				score= score-tempScore[locate-1];
				scoreText.setText(score+"");
				
				invisibleScore= invisibleScore-invisibleTempScore[locate-1];
				invisibleScoreText.setText(invisibleScore+"");
				
				scoreLocates[locate-1]=false;
				
				tempScore[locate-1]=0;
				invisibleTempScore[locate-1]=0;
			}
			return 0;
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
	public void swapText(int firButton ) {
		int secButton = -1;
		
		for (int i =0; i<18; i++) {
			if(buttonIsPressed[i] == true && i!=firButton){;
			secButton = i;
			}
			
		}
		
		if(secButton>=0){
			
			
			String temp= letterButtons[firButton].getText().toString();
			letterButtons[firButton].setText(letterButtons[secButton].getText().toString());
			letterButtons[secButton].setText(temp);
			buttonIsPressed[firButton]=false;
			buttonIsPressed[secButton]=false;
			letterButtons[firButton].setTextColor(getResources().getColor(R.color.black));
			letterButtons[secButton].setTextColor(getResources().getColor(R.color.black));
			buttonTwicePressed.add(18);
			String line1 = String.valueOf(letterButtons[0].getText().charAt(0))+
								String.valueOf(letterButtons[1].getText().charAt(0))+
									String.valueOf(letterButtons[2].getText().charAt(0))+
										String.valueOf(letterButtons[3].getText().charAt(0))+
											String.valueOf(letterButtons[4].getText().charAt(0))+
												String.valueOf(letterButtons[5].getText().charAt(0));
			int pscore1 = Integer.parseInt(String.valueOf(letterButtons[0].getText().charAt(letterButtons[0].getText().length()-1)))+
								Integer.parseInt(String.valueOf(letterButtons[1].getText().charAt(letterButtons[0].getText().length()-1)))+
									Integer.parseInt(String.valueOf(letterButtons[2].getText().charAt(letterButtons[0].getText().length()-1)))+
										Integer.parseInt(String.valueOf(letterButtons[3].getText().charAt(letterButtons[0].getText().length()-1)))+
											Integer.parseInt(String.valueOf(letterButtons[4].getText().charAt(letterButtons[0].getText().length()-1)))+
												Integer.parseInt(String.valueOf(letterButtons[5].getText().charAt(letterButtons[0].getText().length()-1)));
			String line2 = String.valueOf(letterButtons[6].getText().charAt(0))+
								String.valueOf(letterButtons[7].getText().charAt(0))+
									String.valueOf(letterButtons[8].getText().charAt(0))+
										String.valueOf(letterButtons[9].getText().charAt(0))+
											String.valueOf(letterButtons[10].getText().charAt(0));
			int pscore2 = Integer.parseInt(String.valueOf(letterButtons[6].getText().charAt(letterButtons[0].getText().length()-1)))+
								Integer.parseInt(String.valueOf(letterButtons[7].getText().charAt(letterButtons[0].getText().length()-1)))+
									Integer.parseInt(String.valueOf(letterButtons[8].getText().charAt(letterButtons[0].getText().length()-1)))+
										Integer.parseInt(String.valueOf(letterButtons[9].getText().charAt(letterButtons[0].getText().length()-1)))+
											Integer.parseInt(String.valueOf(letterButtons[10].getText().charAt(letterButtons[0].getText().length()-1)));
			String line3 = String.valueOf(letterButtons[11].getText().charAt(0))+
								String.valueOf(letterButtons[12].getText().charAt(0))+
									String.valueOf(letterButtons[13].getText().charAt(0))+
										String.valueOf(letterButtons[14].getText().charAt(0));
			int pscore3 = Integer.parseInt(String.valueOf(letterButtons[11].getText().charAt(letterButtons[0].getText().length()-1)))+
								Integer.parseInt(String.valueOf(letterButtons[12].getText().charAt(letterButtons[0].getText().length()-1)))+
									Integer.parseInt(String.valueOf(letterButtons[13].getText().charAt(letterButtons[0].getText().length()-1)))+
										Integer.parseInt(String.valueOf(letterButtons[14].getText().charAt(letterButtons[0].getText().length()-1)));						
			String line4 = String.valueOf(letterButtons[15].getText().charAt(0))+
								String.valueOf(letterButtons[16].getText().charAt(0))+
									String.valueOf(letterButtons[17].getText().charAt(0));
			int pscore4 = Integer.parseInt(String.valueOf(letterButtons[15].getText().charAt(letterButtons[0].getText().length()-1)))+
								Integer.parseInt(String.valueOf(letterButtons[16].getText().charAt(letterButtons[0].getText().length()-1)))+
									Integer.parseInt(String.valueOf(letterButtons[17].getText().charAt(letterButtons[0].getText().length()-1)));
		/*if((firButton==0|firButton==6|firButton==11|firButton==15)&&
		   (secButton==0|secButton==6|secButton==11|secButton==15)){
			int score1 = match(line1, words1, pscore1, 1);
			displayword1(score1);
			int score2 = match(line2, words2, pscore2, 2);
			displayword2(score2);
			int score3 = match(line3, words3, pscore3, 3);
			displayword3(score3);
			int score4 = match(line4, words4, pscore4, 4);
			displayword4(score4);
		}*/
		
			if(firButton==0|secButton==0){
				loadDict(letterButtons[0].getText().charAt(0),words1,6);
				int score1 = match(line1, words1, pscore1, 1);
				displayword1(score1);
				
			}
			if(firButton==6|secButton==6){
				loadDict(letterButtons[6].getText().charAt(0),words2,5);
				int score2 = match(line2, words2, pscore2, 2);
				displayword2(score2);
			}
			if(firButton==11|secButton==11){
				loadDict(letterButtons[11].getText().charAt(0),words3,4);
				int score3 = match(line3, words3, pscore3, 3);
				displayword3(score3);
			}
			if(firButton==15|secButton==15){
				loadDict(letterButtons[15].getText().charAt(0),words4,3);
				int score4 = match(line4, words4, pscore4, 4);
				displayword4(score4);
			}
			
			if((0<firButton&&firButton<=5)|(0<secButton&&secButton<=5)){
				int score1 = match(line1, words1, pscore1, 1);
				displayword1(score1);
			}
			if((6<firButton&&firButton<=10)|(6<secButton&&secButton<=10)){
				int score2 = match(line2, words2, pscore2, 2);
				displayword2(score2);
			}
			if((11<firButton&&firButton<=14)|(11<secButton&&secButton<=14)){
				int score3 = match(line3, words3, pscore3, 3);
				displayword3(score3);
			}
			if((15<firButton&&firButton<=17)|(15<secButton&&secButton<=17)){
				int score4 = match(line4, words4, pscore4, 4);
				displayword4(score4);
			}
		
		}
		
		
		
		
	}
	
	public void displayword1(int score1) {
        
		if(score1>0){
			letterButtons[0].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[1].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[2].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[3].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[4].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[5].setTextColor(getResources().getColor(
		            R.color.red));
		}
		else{
			letterButtons[0].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[1].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[2].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[3].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[4].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[5].setTextColor(getResources().getColor(
		            R.color.black));
		}
	}
	
	public void displayword2(int score2) {
        
		if(score2>0){
			letterButtons[6].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[7].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[8].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[9].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[10].setTextColor(getResources().getColor(
		            R.color.red));
			
		}
		else{
			letterButtons[6].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[7].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[8].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[9].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[10].setTextColor(getResources().getColor(
		            R.color.black));
			
		}
	}
	
	public void displayword3(int score3) {
        
		if(score3>0){
			letterButtons[11].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[12].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[13].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[14].setTextColor(getResources().getColor(
		            R.color.red));
			
			
		}
		else{
			letterButtons[11].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[12].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[13].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[14].setTextColor(getResources().getColor(
		            R.color.black));
			
		}
	}
	
	public void displayword4(int score4) {
        
		if(score4>0){
			letterButtons[15].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[16].setTextColor(getResources().getColor(
		            R.color.red));
			letterButtons[17].setTextColor(getResources().getColor(
		            R.color.red));
			
		}
		else{
			letterButtons[15].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[16].setTextColor(getResources().getColor(
		            R.color.black));
			letterButtons[17].setTextColor(getResources().getColor(
		            R.color.black));
			
		}
	}
	
	
	
	public String randomWords(int len) {
	         
	    random = new Random();
	    char selChar1,selChar2,selChar3,selChar4;
	    
	   
	    switch (len) {
		case 6:
			 selChar1 = puzzleChar[random.nextInt(26)];
			return loadDictRandom(selChar1, words1, 6);
			 
		case 5:
			 selChar2 = puzzleChar[random.nextInt(26)];
			return loadDictRandom(selChar2, words2, 5);
			 
		case 4:
			 selChar3 = puzzleChar[random.nextInt(26)];
			return loadDictRandom(selChar3, words3, 4);
			  
		case 3:
			 selChar4 = puzzleChar[random.nextInt(26)];
			return loadDictRandom(selChar4, words4, 3);
			 
		default:
			return "";
			 
		}
	   
	}
 
	

}
