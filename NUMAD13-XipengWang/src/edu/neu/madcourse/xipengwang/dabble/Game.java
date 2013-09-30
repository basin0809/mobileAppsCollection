package edu.neu.madcourse.xipengwang.dabble;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.madcourse.xipengwang.dabble.WordsList;
import edu.neu.madcourse.xipengwang.R;

public class Game extends Activity{
	private HashMap<String,String> words1 = new HashMap<String,String>();
	private HashMap<String,String> words2 = new HashMap<String,String>();
	private HashMap<String,String> words3 = new HashMap<String,String>();
	private HashMap<String,String> words4 = new HashMap<String,String>();
	private String[] wordsBack = new String[200];
	private int countBack = 0;
	private String result="";
	private InputStream input = null;
	private boolean[] buttonIsPressed = new boolean[18];
	private Button[] letterButtons = new Button[18];
	private Button pauseButton, resumeButton, hintButton, backButton;
	private TextView cdText, scoreText;
	private static Random random = null;
	private static String[] scoreList = {"3","3","3","3","3","3","3","3","6","6","6","6","6","6","9","9","9","9"};
	
	private static String[] puzzle = new String[18];
	private static String[] puzzleScore = new String[18];
	final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dabble_game);
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
		for (int i =0; i<18; i++) {
			letterButtons[i].setTextColor(getResources().getColor(R.color.black));
			
			letterButtons[i].setOnClickListener(new LetterButtonsListener());
		}
		pauseButton = (Button)findViewById(R.id.Pause_button);
		resumeButton = (Button)findViewById(R.id.Resume_button);
		hintButton = (Button)findViewById(R.id.Hintbutton);
		backButton = (Button)findViewById(R.id.Backbutton);
		cdText = (TextView)findViewById(R.id.CountDown);
		scoreText = (TextView)findViewById(R.id.Score);
		
		String string = (randomWord3()+randomWord4()+randomWord5()+randomWord6());
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
		System.out.println(letterButtons[0].getText().charAt(0));
		System.out.println(letterButtons[6].getText().charAt(0));
		System.out.println(letterButtons[11].getText().charAt(0));
		System.out.println(letterButtons[15].getText().charAt(0));
		loadDict(letterButtons[0].getText().charAt(0),words1);
		loadDict(letterButtons[6].getText().charAt(0),words2);
		loadDict(letterButtons[11].getText().charAt(0),words3);
		loadDict(letterButtons[15].getText().charAt(0),words4);
		}
	class LetterButtonsListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			int buttonId = v.getId();
			switch (buttonId) {
			case R.id.button1:
				buttonIsPressed[0]= true;
				letterButtons[0].setTextColor(getResources().getColor(
			            R.color.red));
				
				swapText(0);
				
				break;
			case R.id.button2:
				buttonIsPressed[1]= true;
				letterButtons[1].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(1);
				
				break;
			case R.id.button3:
				buttonIsPressed[2]= true;
				letterButtons[2].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(2);
				
				break;
			case R.id.button4:
				buttonIsPressed[3]= true;
				letterButtons[3].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(3);
				
				break;
			case R.id.button5:
				buttonIsPressed[4]= true;
				letterButtons[4].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(4);
				
				break;
			case R.id.button6:
				buttonIsPressed[5]= true;
				letterButtons[5].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(5);
				
				break;
			case R.id.button7:
				buttonIsPressed[6]= true;
				letterButtons[6].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(6);
				
				break;
			case R.id.button8:
				buttonIsPressed[7]= true;
				letterButtons[7].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(7);
				
				break;
			case R.id.button9:
				buttonIsPressed[8]= true;
				letterButtons[8].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(8);
				
				break;
			case R.id.button10:
				buttonIsPressed[9]= true;
				letterButtons[9].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(9);
				
				break;
			case R.id.button11:
				buttonIsPressed[10]= true;
				letterButtons[10].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(10);
				
				break;
			case R.id.button12:
				buttonIsPressed[11]= true;
				letterButtons[11].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(11);
				
				break;
			case R.id.button13:
				buttonIsPressed[12]= true;
				letterButtons[12].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(12);
				
				break;
			case R.id.button14:
				buttonIsPressed[13]= true;
				letterButtons[13].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(13);
				
				break;
			case R.id.button15:
				buttonIsPressed[14]= true;
				letterButtons[14].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(14);
				
				break;
			case R.id.button16:
				buttonIsPressed[15]= true;
				letterButtons[15].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(15);
				
				break;
			case R.id.button17:
				buttonIsPressed[16]= true;
				letterButtons[16].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(16);
				
				break;
			case R.id.button18:
				buttonIsPressed[17]= true;
				letterButtons[17].setTextColor(getResources().getColor(
			            R.color.red));
				swapText(17);
				
				break;

			default:
				break;
			}
			//loadDict();
		}
		
	}
	public void loadDict(char fir, HashMap<String,String> words){
		switch (fir) {
		case 'a':
			System.out.print("--------->switch to a");
			input = getResources().openRawResource(R.raw.a);
			load(input,words);
			//match(s.toString());
			break;
		case 'b':
			System.out.print("--------->switch to b");
			input = getResources().openRawResource(R.raw.b);
			load(input,words);
			//match(s.toString());
			break;
		case 'c':
			System.out.print("--------->switch to c");
			input = getResources().openRawResource(R.raw.c);
			load(input,words);
			break;
		case 'd':
			System.out.print("--------->switch to d");
			input = getResources().openRawResource(R.raw.d);
			load(input,words);
			break;
		case 'e':
			System.out.print("--------->switch to e");
			input = getResources().openRawResource(R.raw.e);
			load(input,words);
			break;
		case 'f':
			System.out.print("--------->switch to f");
			input = getResources().openRawResource(R.raw.f);
			load(input,words);
			break;
		case 'g':
			System.out.print("--------->switch to g");
			input = getResources().openRawResource(R.raw.g);
			load(input,words);
			break;
		case 'h':
			System.out.print("--------->switch to h");
			input = getResources().openRawResource(R.raw.h);
			load(input,words);
			break;
		case 'i':
			System.out.print("--------->switch to i");
			input = getResources().openRawResource(R.raw.i);
			load(input,words);
			break;
		case 'j':
			 System.out.print("--------->switch to j");
			input = getResources().openRawResource(R.raw.j);
			load(input,words);
			break;
		case 'k':
			 System.out.print("--------->switch to k");
			input = getResources().openRawResource(R.raw.k);
			load(input,words);
			break;
		case 'l':
			 System.out.print("--------->switch to l");
			input = getResources().openRawResource(R.raw.l);
			load(input,words);
			break;
		case 'm':
			 System.out.print("--------->switch to m");
			input = getResources().openRawResource(R.raw.m);
			load(input,words);
			break;
		case 'n':
			 System.out.print("--------->switch to n");
			input = getResources().openRawResource(R.raw.n);
			load(input,words);
			break;
		case 'o':
			 System.out.print("--------->switch to o");
			input = getResources().openRawResource(R.raw.o);
			load(input,words);
			break;
		case 'p':
			 System.out.print("--------->switch to p");
			input = getResources().openRawResource(R.raw.p);
			load(input,words);
			break;
		case 'q':
			 System.out.print("--------->switch to q");
			input = getResources().openRawResource(R.raw.q);
			load(input,words);
			break;
		case 'r':
			 System.out.print("--------->switch to r");
			input = getResources().openRawResource(R.raw.r);
			load(input,words);
			break;
		case 's':
			 System.out.print("--------->switch to s");
			input = getResources().openRawResource(R.raw.s);
			load(input,words);
			break;
		case 't':
			 System.out.print("--------->switch to t");
			input = getResources().openRawResource(R.raw.t);
			load(input,words);
			break;
		case 'u':
			 System.out.print("--------->switch to u");
			input = getResources().openRawResource(R.raw.u);
			load(input,words);
			break;
		case 'v':
			 System.out.print("--------->switch to v");
			input = getResources().openRawResource(R.raw.v);
			load(input,words);
			break;
		case 'w':
			 System.out.print("--------->switch to w");
			input = getResources().openRawResource(R.raw.w);
			load(input,words);
			break;
		case 'x':
			 System.out.print("--------->switch to x");
			input = getResources().openRawResource(R.raw.x);
			load(input,words);
			break;
		case 'y':
			 System.out.print("--------->switch to y");
			input = getResources().openRawResource(R.raw.y);
			load(input,words);
			break;
		case 'z':
			 System.out.print("--------->switch to z");
			input = getResources().openRawResource(R.raw.z);
			load(input,words);
			break;
			
		default:
			break;
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
	public int match(String s, HashMap<String,String> words) {
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
			//textView.setMovementMethod(new ScrollingMovementMethod()); 
			//textView.setText(result);
		
			
			
		    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
		}
		else{
			System.out.println("undetect match");
		}
		}
		return 0;
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
			String line1 = String.valueOf(letterButtons[0].getText().charAt(0))+
								String.valueOf(letterButtons[1].getText().charAt(0))+
									String.valueOf(letterButtons[2].getText().charAt(0))+
										String.valueOf(letterButtons[3].getText().charAt(0))+
											String.valueOf(letterButtons[4].getText().charAt(0))+
												String.valueOf(letterButtons[5].getText().charAt(0));
			String line2 = String.valueOf(letterButtons[6].getText().charAt(0))+
								String.valueOf(letterButtons[7].getText().charAt(0))+
									String.valueOf(letterButtons[8].getText().charAt(0))+
										String.valueOf(letterButtons[9].getText().charAt(0))+
											String.valueOf(letterButtons[10].getText().charAt(0));
			String line3 = String.valueOf(letterButtons[11].getText().charAt(0))+
								String.valueOf(letterButtons[12].getText().charAt(0))+
									String.valueOf(letterButtons[13].getText().charAt(0))+
										String.valueOf(letterButtons[14].getText().charAt(0));
									
			String line4 = String.valueOf(letterButtons[15].getText().charAt(0))+
								String.valueOf(letterButtons[16].getText().charAt(0))+
									String.valueOf(letterButtons[17].getText().charAt(0));
									
			if(firButton==0|secButton==0){
				loadDict(letterButtons[0].getText().charAt(0),words1);
				int score1 = match(line1, words1);
				
				
			}
			if(firButton==6|secButton==6){
				loadDict(letterButtons[6].getText().charAt(0),words2);
				int score2 = match(line2, words2);
			}
			if(firButton==11|secButton==11){
				loadDict(letterButtons[11].getText().charAt(0),words3);
				int score3 = match(line3, words3);
			}
			if(firButton==15|secButton==15){
				loadDict(letterButtons[15].getText().charAt(0),words4);
				int score4 = match(line4, words4);
			}
			
			if((0<firButton&&firButton<=5)|(0<secButton&&secButton<=5)){
				int score = match(line1, words1);
			}
			if((6<firButton&&firButton<=10)|(6<secButton&&secButton<=10)){
				int score = match(line2, words2);
			}
			if((11<firButton&&firButton<=14)|(11<secButton&&secButton<=14)){
				int score = match(line3, words3);
			}
			if((15<firButton&&firButton<=17)|(15<secButton&&secButton<=17)){
				int score = match(line4, words4);
			}
		}
		
	}

	public static final String randomWord3() {
	         
	    random = new Random();
	    String selStr;
	    selStr = WordsList.words3[random.nextInt(WordsList.words3.length)];
	    return selStr;
	}
	public static final String randomWord4() {
        
	    random = new Random();
	    String selStr;
	    selStr = WordsList.words4[random.nextInt(WordsList.words4.length)];
	    return selStr;
	}
	public static final String randomWord5() {
        
	    random = new Random();
	    String selStr;
	    selStr = WordsList.words5[random.nextInt(WordsList.words5.length)];
	    return selStr;
	}
	public static final String randomWord6() {
        
	    random = new Random();
	    String selStr;
	    selStr = WordsList.words6[random.nextInt(WordsList.words6.length)];
	    return selStr;
	}
	

}
