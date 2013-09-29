package edu.neu.madcourse.xipengwang.dabble;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import edu.neu.madcourse.xipengwang.R;

public class Game extends Activity{
	private HashMap<String,String> words = new HashMap<String,String>();
	private String[] wordsBack = new String[200];
	private int countBack = 0;
	private String result="";
	private InputStream input = null;
	private Button[] letterButtons = new Button[18];
	private Button pauseButton, resumeButton, hintButton, backButton;
	private TextView cdText, scoreText;
	private static Random random = null;
	private static String[] puzzle = new String[18];
	private static String[] words3 = {
								"aah","ape","bab","byz","cab","com",
								"dec","des","eat","ear","fab","fat",
								"gab","guy","hab","hat","iao","ick",
								"jar","joy","kab","kid","lab","low",
								"mad","man","oak","oil",
								"see","sea","tea","try",
								"war","web"};
	private static String[] words4 = {"zeal","zimb","yale",
								"aals","acad","beef","bonk","cell","cool",
								"duet","dare","earn","each","furs","fury",
								"gear","grab","hold","heat","inter","icon",
								"jeep","jump","king","kind","lamb","lose",
								"math","move","need","neck","obey","oral",
								"poor","pour","root","room",
								"star","size","tied","tale","udon","ugly",
								"work","walk","vote"};
	private static String[] words5 = {"zaman","zoist","young","yeech","xyris","xylem",
								"autos","altar","booth","brack","caked","canid",
								"drawl","delay","error","extra","forty","forum",
								"glass","grade","haste","hythe","intel","intue",
								"japan","joist","kebab","knowe","light","lumen",
								"mimic","musty","nervy","never","order","owner",
								"prize","proud","quite","queen","river","resid",
								"scout","south","teeth","teach","urban","under",
								"worth","wines","voice"};
	private static String[] words6 = {"zocors","zoloft","yahoos","yucker","xuthus","xmases",
								"acrose","aspect","banana","beleve","clunch","cramel",
								"dogate","detach","evolve","effect","fabric","furtum",
								"groats","gender","health","hostle","indeed","intent",
								"joyful","judged","kababs","kecksy","legman","length",
								"master","mental","nation","nature","orange","outcry",
								"please","polite","repeat","revote",
								"screen","smooth","though","threat",
								"worker","wilder"};
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
		pauseButton = (Button)findViewById(R.id.Pause_button);
		resumeButton = (Button)findViewById(R.id.Resume_button);
		hintButton = (Button)findViewById(R.id.Hintbutton);
		backButton = (Button)findViewById(R.id.Backbutton);
		cdText = (TextView)findViewById(R.id.CountDown);
		scoreText = (TextView)findViewById(R.id.Score);
		
		String string = (randomWord3()+randomWord4()+randomWord5()+randomWord6());
		System.out.println("String: "+string);
		ArrayList<String> arrayList = new ArrayList<String>();
		for(int i =0; i<18; i++){
			arrayList.add(String.valueOf(string.charAt(i)));
		}
		Collections.shuffle(arrayList);
		for(int i =0; i<18; i++){
			puzzle[i]=arrayList.get(i);
			System.out.println(puzzle[i]);
		}
		}
	

	public static final String randomWord3() {
	         
	    random = new Random();
	    String selStr;
	    selStr = words3[random.nextInt(words3.length)];
	    return selStr;
	}
	public static final String randomWord4() {
        
	    random = new Random();
	    String selStr;
	    selStr = words4[random.nextInt(words4.length)];
	    return selStr;
	}
	public static final String randomWord5() {
        
	    random = new Random();
	    String selStr;
	    selStr = words5[random.nextInt(words5.length)];
	    return selStr;
	}
	public static final String randomWord6() {
        
	    random = new Random();
	    String selStr;
	    selStr = words6[random.nextInt(words6.length)];
	    return selStr;
	}
	

}
