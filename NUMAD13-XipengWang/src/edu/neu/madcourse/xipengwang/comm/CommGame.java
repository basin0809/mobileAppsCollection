package edu.neu.madcourse.xipengwang.comm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.xipengwang.R;
import edu.neu.madcourse.xipengwang.comm.AsyncPullService.FirBinder;
import edu.neu.madcourse.xipengwang.comm.OnlineUsers.ProgessDialogCancelTask;
import edu.neu.madcourse.xipengwang.dabble.BGMManager;
import edu.neu.mhealth.api.KeyValueAPI;


public class CommGame extends Activity{
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
	private String result1="";
	private String result2="";
	private String result3="";
	private String result4="";
	private String result5="";
	private String result6="";
	private String resultString;
	private String guestsShield;
	private String ssrString = "1";
	private InputStream input = null;
	private boolean[] buttonIsPressed = new boolean[18];
	private ArrayList<Integer> buttonTwicePressed = new ArrayList<Integer>();
	private ArrayList<Integer> pauseTwicePressed = new ArrayList<Integer>();
	
	
	private boolean[] scoreLocates = new boolean[4];
	private Button[] letterButtons = new Button[18];
	private Button csrButton, pauseButton, backButton, hintButton ;
	//private int swapLeft = 40;
	//private int modeSt;
	private TextView cdText, scoreText, invisibleScoreText, oppMsg;
	private static Random random = null;
	private static String[] scoreList = {"3","3","3","3","3","3","3","3","6","6","6","6","6","6","9","9","9","9"};
	
	private static char[] puzzleChar = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
										'n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private static String[] puzzle = new String[18];
	private static String[] puzzleScore = new String[18];
	
	private MyCount mc;
	private long timeLeft=300000;

	private boolean musicGoOn;
	private boolean musicGoOn2;
	
	private AlphaAnimation alphaDes;
    private AlphaAnimation alphaInc;
	
	private static MediaPlayer mp = null;
	
	RealTimePullTask realTimePullTask = null;
	static AFKTask aTask;
	AsyncPullService boundService;
	boolean isBound;
	private boolean ssrDetected = false;
	
	ProgressDialog pdialog; 
	private String masterOrGuest;
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
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.comm_game);
		Intent intent = getIntent();
		int musicSt = Integer.parseInt(intent.getStringExtra("music_stuate"));
		if(musicSt==1){
			musicGoOn2 = false;
			
		}
		else{
			musicGoOn2 = true;
			
		}
		masterOrGuest = intent.getStringExtra("masterOrGuest");
		 System.out.println("masterOrGuest is "+masterOrGuest);
		
		//pauseTwicePressed.add(0);
		letterButtons[0]=(Button)findViewById(R.id.cbutton1);
		letterButtons[1]=(Button)findViewById(R.id.cbutton2);
		letterButtons[2]=(Button)findViewById(R.id.cbutton3);
		letterButtons[3]=(Button)findViewById(R.id.cbutton4);
		letterButtons[4]=(Button)findViewById(R.id.cbutton5);
		letterButtons[5]=(Button)findViewById(R.id.cbutton6);
		letterButtons[6]=(Button)findViewById(R.id.cbutton7);
		letterButtons[7]=(Button)findViewById(R.id.cbutton8);
		letterButtons[8]=(Button)findViewById(R.id.cbutton9);
		letterButtons[9]=(Button)findViewById(R.id.cbutton10);
		letterButtons[10]=(Button)findViewById(R.id.cbutton11);
		letterButtons[11]=(Button)findViewById(R.id.cbutton12);
		letterButtons[12]=(Button)findViewById(R.id.cbutton13);
		letterButtons[13]=(Button)findViewById(R.id.cbutton14);
		letterButtons[14]=(Button)findViewById(R.id.cbutton15);
		letterButtons[15]=(Button)findViewById(R.id.cbutton16);
		
		letterButtons[16]=(Button)findViewById(R.id.cbutton17);
		letterButtons[17]=(Button)findViewById(R.id.cbutton18);
		 System.out.println("SDK ver: "+android.os.Build.VERSION.SDK_INT);
		Display display = this.getWindowManager().getDefaultDisplay();
	       
	    
		   
	        
			
	         
		for (int i =0; i<18; i++) {
			letterButtons[i].setTextColor(getResources().getColor(R.color.black));
			
			letterButtons[i].setOnClickListener(new LetterButtonsListener());
		}
		pauseButton = (Button)findViewById(R.id.cPause_button);
		csrButton = (Button)findViewById(R.id.csr_button);
		hintButton = (Button)findViewById(R.id.cHintbutton);
		backButton = (Button)findViewById(R.id.cBackbutton);
		backButton.setOnClickListener(new QuitListener());
		cdText = (TextView)findViewById(R.id.cCountDown);
		scoreText = (TextView)findViewById(R.id.cScore);
		oppMsg = (TextView)findViewById(R.id.cMessage);
		
		
		invisibleScoreText = (TextView)findViewById(R.id.cinVisibleScore);
		invisibleScoreText.addTextChangedListener(new ScoreTextListener());
		scoreText.setText("Score:0");
		cdText.setText("00:00");
		oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:");
		if(masterOrGuest.equals("master")){
			resultString = (randomWords(6)+randomWords(5)+randomWords(4)+randomWords(3));
			System.out.println("String: "+resultString);
			ArrayList<String> sList = new ArrayList<String>();
			for (int i =0; i<18; i++) {
				sList.add(scoreList[i]);
			}
			Collections.shuffle(sList);
			ArrayList<String> arrayList = new ArrayList<String>();
			for(int i =0; i<18; i++){
				arrayList.add(String.valueOf(resultString.charAt(i))+"\n"+"     "+sList.get(i));
				
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
				letterButtons[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			}
			String mastersWar = getCurrentStr();
			String mastersSword = getCurrentScore();
			
			pdialog = new ProgressDialog(CommGame.this);
			pdialog.setTitle("Pleas wait for seconds");
			pdialog.setMessage("Creating Dabble board for you and "+ OppNameMyName.oppFakeName);
			pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdialog.show();
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, mastersWar+mastersSword+resultString);
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, mastersWar+mastersSword+resultString);
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, mastersWar+mastersSword+resultString);
			KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, mastersWar+mastersSword+resultString);
			pdialog.dismiss();
			
		}else {
			boolean boardReceived = false;
			String board="";
			String mastersSword;
			String mastersWar;
			int i = 0;
			pdialog = new ProgressDialog(CommGame.this);
			pdialog.setTitle("Pleas wait for seconds");
			pdialog.setMessage("Creating Dabble board for you and "+ OppNameMyName.oppFakeName);
			pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdialog.show();
			while(boardReceived == false){
				if(i>100){
					boardReceived = true;
					Toast.makeText(this,"Fail to load dabble, please check your network.",Toast.LENGTH_LONG).show();
					finish();
				}else{
				board = KeyValueAPI.get("basin", "basin576095", OppNameMyName.oppName);
				if(board.length()>17){
					boardReceived = true;
				}
				i++;}
			}
			pdialog.dismiss();
			
			mastersWar = board.substring(0, 18);
			mastersSword = board.substring(18,36);
			resultString = board.substring(36, 54);
			
			for(int j =0; j<18; j++){
				puzzle[j]=mastersWar.charAt(j)+"\n"+"     "+mastersSword.charAt(j);
				//puzzleScore[i]=arrayList.get(i)[1];
				//System.out.println(puzzle[i]);
			}
			  
			for (int k =0; k<18; k++) {
				
				//letterButtons[i].setText(puzzle[i].charAt(0)+"\n"+"   "+puzzleScore[i].charAt(1));	
				letterButtons[k].setText(puzzle[k]);
				letterButtons[k].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			}
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
		
		
		//mc = new MyCount(300000, 1000);  
        //mc.start();
		
		   PackageManager packManager= getPackageManager();
	        List<ResolveInfo> intActivities = packManager.queryIntentActivities
	                        (new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	        if (intActivities.size() !=0){
	        	csrButton.setOnClickListener(new CSRListener());
	        } else {
	        	csrButton.setEnabled(false);
	            Toast.makeText(this,"Oops - Speech Recognition Not Supported!", 
	                                                 Toast.LENGTH_LONG).show();
	            } 
		//csrButton.setOnClickListener(new CSRListener());
        pauseButton.setOnClickListener(new PauseListener());
        hintButton.setOnClickListener(new HintListener());
        
        alphaDes = new AlphaAnimation(1.0f, 0.3f);
        alphaInc = new AlphaAnimation(0.3f, 1.0f);
        alphaDes.setDuration(1000);
        alphaInc.setDuration(1000);
        alphaDes.setFillAfter(true);
        alphaInc.setFillAfter(true);
        
        scoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        cdText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
       
        invisibleScoreText.setTextColor(getResources().getColor(
    	            R.color.white));
        hintButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        backButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        pauseButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		aTask= new AFKTask(this);
		aTask.execute();
		Intent intent2 =new Intent();
		intent2.setClass(CommGame.this, AsyncPullService.class);
		startService(intent2);
		
		bindService(intent2, conn, BIND_AUTO_CREATE);
		//realTimePullTask.cancel(true);
		
		mc.cancel();
		if(!musicGoOn)
            BGMManager.pause();
	    System.out.println("--------------------------------Pause");
		 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//GoOnTask gTask = new GoOnTask(this);
		//gTask.execute();
		
		oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:");
		
		stopService(new Intent(this, AsyncPullService.class));
		realTimePullTask = new RealTimePullTask(this);
		realTimePullTask.execute();
		NotificationManager mNotificationManager=
				(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(1);
		if(pauseTwicePressed.get(pauseTwicePressed.size()-1)==1){
			
		}
		else {
			mc = new MyCount(timeLeft, 1000);  
		       mc.start();
		}
		
		if(musicGoOn2==true){
			musicGoOn=false;
	        BGMManager.start(this,R.raw.game);}
			else {
				
			}
		
        System.out.println("--------------------------------Reume");

	}
	
	class CSRListener implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			System.out.println("CLICK CSR");
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak it up when you find a word!");
			startActivityForResult(intent, 123);
			
		}
		
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 123 && resultCode == RESULT_OK){
			ssrDetected = false;
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			System.out.print(results);
			
			if(results.size()>0){
			ssrString = results.get(0);
			int ssrLength = ssrString.length();
			String currentStr = getCurrentStr();
			String[] ssrLetter;
			int[] ssrLocates;
			if(ssrLength>6){}
			else{
			switch (ssrLength) {
			case 6:
				ssrLetter = new String[6];
				ssrLocates = new int[6];
				ssrLetter[0] = String.valueOf(ssrString.charAt(0));
				ssrLetter[1] = String.valueOf(ssrString.charAt(1));
				ssrLetter[2] = String.valueOf(ssrString.charAt(2));
				ssrLetter[3] = String.valueOf(ssrString.charAt(3));
				ssrLetter[4] = String.valueOf(ssrString.charAt(4));
				ssrLetter[5] = String.valueOf(ssrString.charAt(5));
				
				if(!currentStr.contains(ssrLetter[0])){}
				else {
					ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
					String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
					if(!currentStr1.contains(ssrLetter[1])){}
					else {
						ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
						String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
						if(!currentStr2.contains(ssrLetter[2])){}
						else {
							ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
							String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
							if(!currentStr3.contains(ssrLetter[3])){}
							else {
								ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
								String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
								if(!currentStr4.contains(ssrLetter[4])){}
								else {
									ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
									String currentStr5 = currentStr.replaceFirst(ssrLetter[4], "4");
									if(!currentStr5.contains(ssrLetter[5])){}
									else {
										ssrLocates[5]=currentStr.indexOf(ssrLetter[5]);
										
										swapIfValid(ssrLocates[0]);
										swapIfValid(0);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp1 = "1"+currentStr.substring(1, 18);
										ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
										swapIfValid(ssrLocates[1]);
										swapIfValid(1);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp2 = "11"+currentStr.substring(2, 18);
										ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
										swapIfValid(ssrLocates[2]);
										swapIfValid(2);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp3 = "111"+currentStr.substring(3, 18);
										ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
										swapIfValid(ssrLocates[3]);
										swapIfValid(3);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp4 = "1111"+currentStr.substring(4, 18);
										ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
										swapIfValid(ssrLocates[4]);
										swapIfValid(4);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp5 = "11111"+currentStr.substring(5, 18);
										ssrLocates[5]=currentStrTemp5.indexOf(ssrLetter[5]);
										if(ssrLocates[5]==5){ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										int len = ssrString.length();
										switch (len) {
										case 3:
											pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);
											break;
										case 4:
											pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);
											break;
										case 5:
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);
											break;
										case 6:
											pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);
											break;

										default:
											break;
										}
										}
										else{
										swapIfValid(ssrLocates[5]);
										swapIfValid(5);
										ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										int len = ssrString.length();
										switch (len) {
										case 3:
											pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);
											break;
										case 4:
											pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);
											break;
										case 5:
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);
											break;
										case 6:
											pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);
											break;

										default:
											break;
										}
										}
	
									}
								}
							}
						}
					}
				}

				break;
				
			case 5:
				ssrLetter = new String[5];
				ssrLocates = new int[5];
				ssrLetter[0] = String.valueOf(ssrString.charAt(0));
				ssrLetter[1] = String.valueOf(ssrString.charAt(1));
				ssrLetter[2] = String.valueOf(ssrString.charAt(2));
				ssrLetter[3] = String.valueOf(ssrString.charAt(3));
				ssrLetter[4] = String.valueOf(ssrString.charAt(4));
				
				
				if(!currentStr.contains(ssrLetter[0])){}
				else {
					ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
					String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
					System.out.println("Hello Done");
					if(!currentStr1.contains(ssrLetter[1])){}
					else {
						ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
						String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
						System.out.println("Hello Done");
						if(!currentStr2.contains(ssrLetter[2])){}
						else {
							ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
							String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
							System.out.println("Hello Done");
							if(!currentStr3.contains(ssrLetter[3])){}
							else {
								ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
								String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
								System.out.println("Hello Done");
								
								if(!currentStr4.contains(ssrLetter[4])){}
								else {
									ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
									System.out.println(currentStr);
									swapIfValid(ssrLocates[0]);
									swapIfValid(6);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp1 = currentStr.substring(0, 6)+"1"+currentStr.substring(7, 18);
									ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
									swapIfValid(ssrLocates[1]);
									swapIfValid(7);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp2 = currentStr.substring(0, 6)+"11"+currentStr.substring(8, 18);
									ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
									swapIfValid(ssrLocates[2]);
									swapIfValid(8);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp3 = currentStr.substring(0, 6)+"111"+currentStr.substring(9, 18);
									ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
									swapIfValid(ssrLocates[3]);
									swapIfValid(9);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp4 = currentStr.substring(0, 6)+"1111"+currentStr.substring(10, 18);
									ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
									if(ssrLocates[4]==10){ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									int len = ssrString.length();
									switch (len) {
									case 3:
										pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);
										break;
									case 4:
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);
										break;
									case 5:
										pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);
										break;
									case 6:
										pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);
										break;

									default:
										break;
									}}
									else{
									swapIfValid(ssrLocates[4]);
									swapIfValid(10);
									ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									int len = ssrString.length();
									switch (len) {
									case 3:
										pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);
										break;
									case 4:
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);
										break;
									case 5:
										pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);
										break;
									case 6:
										pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);
										break;

									default:
										break;
									}}	
								}
							}
						}
					}
				}

				break;
				
			case 4:
				ssrLetter = new String[4];
				ssrLocates = new int[4];
				ssrLetter[0] = String.valueOf(ssrString.charAt(0));
				ssrLetter[1] = String.valueOf(ssrString.charAt(1));
				ssrLetter[2] = String.valueOf(ssrString.charAt(2));
				ssrLetter[3] = String.valueOf(ssrString.charAt(3));
				
				if(!currentStr.contains(ssrLetter[0])){}
				else {
					ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
					String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
					if(!currentStr1.contains(ssrLetter[1])){}
					else {
						ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
						String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
						if(!currentStr2.contains(ssrLetter[2])){}
						else {
							ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
							String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
							if(!currentStr3.contains(ssrLetter[3])){}
							else {
								ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
								
								swapIfValid(ssrLocates[0]);
								swapIfValid(11);
								
								
								currentStr = getCurrentStr();
								String currentStrTemp1 = currentStr.substring(0, 11)+"1"+currentStr.substring(12, 18);
								ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
								swapIfValid(ssrLocates[1]);
								swapIfValid(12);
								
								
								currentStr = getCurrentStr();
								String currentStrTemp2 = currentStr.substring(0, 11)+"11"+currentStr.substring(13, 18);
								ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
								swapIfValid(ssrLocates[2]);
								swapIfValid(13);
								
								
								currentStr = getCurrentStr();
								String currentStrTemp3 = currentStr.substring(0, 11)+"111"+currentStr.substring(14, 18);
								ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
								if(ssrLocates[3]==14){ssrDetected = true;
								PushTask pushTask = new PushTask(CommGame.this);
								//pushTask.execute(ssrString.toString());
								pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
								else{
								swapIfValid(ssrLocates[3]);
								swapIfValid(14);
								ssrDetected = true;
								PushTask pushTask = new PushTask(CommGame.this);
								//pushTask.execute(ssrString.toString());
								pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
											
							}
						}
					}
				}

				break;
				
			case 3:
				ssrLetter = new String[3];
				ssrLocates = new int[3];
				ssrLetter[0] = String.valueOf(ssrString.charAt(0));
				ssrLetter[1] = String.valueOf(ssrString.charAt(1));
				ssrLetter[2] = String.valueOf(ssrString.charAt(2));
				
				if(!currentStr.contains(ssrLetter[0])){}
				else {
					ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
					String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
					if(!currentStr1.contains(ssrLetter[1])){}
					else {
						ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
						String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
						if(!currentStr2.contains(ssrLetter[2])){}
						else {
							ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
							
							swapIfValid(ssrLocates[0]);
							swapIfValid(15);
							
							
							currentStr = getCurrentStr();
							String currentStrTemp1 = currentStr.substring(0, 15)+"1"+currentStr.substring(16, 18);
							ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
							swapIfValid(ssrLocates[1]);
							swapIfValid(16);
							
							
							currentStr = getCurrentStr();
							String currentStrTemp2 = currentStr.substring(0, 15)+"11"+currentStr.substring(17, 18);
							ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
							if(ssrLocates[2]==17){ssrDetected = true;
							PushTask pushTask = new PushTask(CommGame.this);
							//pushTask.execute(ssrString.toString());
							pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
							else{
							swapIfValid(ssrLocates[2]);
							swapIfValid(17);
							ssrDetected = true;
							PushTask pushTask = new PushTask(CommGame.this);
							//pushTask.execute(ssrString.toString());
							pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
							
							
						}
					}
				}

				break;

			default:
				break;
			}
			}

			}
			if(ssrDetected == false && results.size()>1){
				ssrString = results.get(1);
				int ssrLength = ssrString.length();
				String currentStr = getCurrentStr();
				String[] ssrLetter;
				int[] ssrLocates;
				if(ssrLength>6){}
				else{
					switch (ssrLength) {
					case 6:
						ssrLetter = new String[6];
						ssrLocates = new int[6];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						ssrLetter[5] = String.valueOf(ssrString.charAt(5));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											String currentStr5 = currentStr.replaceFirst(ssrLetter[4], "4");
											if(!currentStr5.contains(ssrLetter[5])){}
											else {
												ssrLocates[5]=currentStr.indexOf(ssrLetter[5]);
												
												swapIfValid(ssrLocates[0]);
												swapIfValid(0);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp1 = "1"+currentStr.substring(1, 18);
												ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
												swapIfValid(ssrLocates[1]);
												swapIfValid(1);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp2 = "11"+currentStr.substring(2, 18);
												ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
												swapIfValid(ssrLocates[2]);
												swapIfValid(2);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp3 = "111"+currentStr.substring(3, 18);
												ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
												swapIfValid(ssrLocates[3]);
												swapIfValid(3);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp4 = "1111"+currentStr.substring(4, 18);
												ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
												swapIfValid(ssrLocates[4]);
												swapIfValid(4);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp5 = "11111"+currentStr.substring(5, 18);
												ssrLocates[5]=currentStrTemp5.indexOf(ssrLetter[5]);
												if(ssrLocates[5]==5){ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
												else{
												swapIfValid(ssrLocates[5]);
												swapIfValid(5);
												ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
			
											}
										}
									}
								}
							}
						}

						break;
						
					case 5:
						ssrLetter = new String[5];
						ssrLocates = new int[5];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							System.out.println("Hello Done");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								System.out.println("Hello Done");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									System.out.println("Hello Done");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										System.out.println("Hello Done");
										
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											
											swapIfValid(ssrLocates[0]);
											swapIfValid(6);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp1 = currentStr.substring(0, 6)+"1"+currentStr.substring(7, 18);
											ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
											swapIfValid(ssrLocates[1]);
											swapIfValid(7);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp2 = currentStr.substring(0, 6)+"11"+currentStr.substring(8, 18);
											ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
											swapIfValid(ssrLocates[2]);
											swapIfValid(8);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp3 = currentStr.substring(0, 6)+"111"+currentStr.substring(9, 18);
											ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
											swapIfValid(ssrLocates[3]);
											swapIfValid(9);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp4 = currentStr.substring(0, 6)+"1111"+currentStr.substring(10, 18);
											ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
											if(ssrLocates[4]==10){ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}
											else{
											swapIfValid(ssrLocates[4]);
											swapIfValid(10);
											ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}	
										}
									}
								}
							}
						}

						break;
						
					case 4:
						ssrLetter = new String[4];
						ssrLocates = new int[4];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										
										swapIfValid(ssrLocates[0]);
										swapIfValid(11);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp1 = currentStr.substring(0, 11)+"1"+currentStr.substring(12, 18);
										ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
										swapIfValid(ssrLocates[1]);
										swapIfValid(12);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp2 = currentStr.substring(0, 11)+"11"+currentStr.substring(13, 18);
										ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
										swapIfValid(ssrLocates[2]);
										swapIfValid(13);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp3 = currentStr.substring(0, 11)+"111"+currentStr.substring(14, 18);
										ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
										if(ssrLocates[3]==14){ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
										else{
										swapIfValid(ssrLocates[3]);
										swapIfValid(14);
										ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
													
									}
								}
							}
						}

						break;
						
					case 3:
						ssrLetter = new String[3];
						ssrLocates = new int[3];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									
									swapIfValid(ssrLocates[0]);
									swapIfValid(15);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp1 = currentStr.substring(0, 15)+"1"+currentStr.substring(16, 18);
									ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
									swapIfValid(ssrLocates[1]);
									swapIfValid(16);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp2 = currentStr.substring(0, 15)+"11"+currentStr.substring(17, 18);
									ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
									if(ssrLocates[2]==17){ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									else{
									swapIfValid(ssrLocates[2]);
									swapIfValid(17);
									ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									
									
								}
							}
						}

						break;

					default:
						break;
					}
				}
			}
			
			if(ssrDetected == false && results.size()>2){
				ssrString = results.get(2);
				int ssrLength = ssrString.length();
				String currentStr = getCurrentStr();
				String[] ssrLetter;
				int[] ssrLocates;
				if(ssrLength>6){}
				else{
					switch (ssrLength) {
					case 6:
						ssrLetter = new String[6];
						ssrLocates = new int[6];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						ssrLetter[5] = String.valueOf(ssrString.charAt(5));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											String currentStr5 = currentStr.replaceFirst(ssrLetter[4], "4");
											if(!currentStr5.contains(ssrLetter[5])){}
											else {
												ssrLocates[5]=currentStr.indexOf(ssrLetter[5]);
												
												swapIfValid(ssrLocates[0]);
												swapIfValid(0);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp1 = "1"+currentStr.substring(1, 18);
												ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
												swapIfValid(ssrLocates[1]);
												swapIfValid(1);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp2 = "11"+currentStr.substring(2, 18);
												ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
												swapIfValid(ssrLocates[2]);
												swapIfValid(2);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp3 = "111"+currentStr.substring(3, 18);
												ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
												swapIfValid(ssrLocates[3]);
												swapIfValid(3);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp4 = "1111"+currentStr.substring(4, 18);
												ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
												swapIfValid(ssrLocates[4]);
												swapIfValid(4);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp5 = "11111"+currentStr.substring(5, 18);
												ssrLocates[5]=currentStrTemp5.indexOf(ssrLetter[5]);
												if(ssrLocates[5]==5){ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
												else{
												swapIfValid(ssrLocates[5]);
												swapIfValid(5);
												ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
			
											}
										}
									}
								}
							}
						}

						break;
						
					case 5:
						ssrLetter = new String[5];
						ssrLocates = new int[5];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							System.out.println("Hello Done");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								System.out.println("Hello Done");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									System.out.println("Hello Done");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										System.out.println("Hello Done");
										
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											
											swapIfValid(ssrLocates[0]);
											swapIfValid(6);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp1 = currentStr.substring(0, 6)+"1"+currentStr.substring(7, 18);
											ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
											swapIfValid(ssrLocates[1]);
											swapIfValid(7);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp2 = currentStr.substring(0, 6)+"11"+currentStr.substring(8, 18);
											ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
											swapIfValid(ssrLocates[2]);
											swapIfValid(8);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp3 = currentStr.substring(0, 6)+"111"+currentStr.substring(9, 18);
											ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
											swapIfValid(ssrLocates[3]);
											swapIfValid(9);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp4 = currentStr.substring(0, 6)+"1111"+currentStr.substring(10, 18);
											ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
											if(ssrLocates[4]==10){ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}
											else{
											swapIfValid(ssrLocates[4]);
											swapIfValid(10);
											ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}	
										}
									}
								}
							}
						}

						break;
						
					case 4:
						ssrLetter = new String[4];
						ssrLocates = new int[4];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										
										swapIfValid(ssrLocates[0]);
										swapIfValid(11);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp1 = currentStr.substring(0, 11)+"1"+currentStr.substring(12, 18);
										ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
										swapIfValid(ssrLocates[1]);
										swapIfValid(12);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp2 = currentStr.substring(0, 11)+"11"+currentStr.substring(13, 18);
										ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
										swapIfValid(ssrLocates[2]);
										swapIfValid(13);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp3 = currentStr.substring(0, 11)+"111"+currentStr.substring(14, 18);
										ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
										if(ssrLocates[3]==14){ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
										else{
										swapIfValid(ssrLocates[3]);
										swapIfValid(14);
										ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
													
									}
								}
							}
						}

						break;
						
					case 3:
						ssrLetter = new String[3];
						ssrLocates = new int[3];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									
									swapIfValid(ssrLocates[0]);
									swapIfValid(15);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp1 = currentStr.substring(0, 15)+"1"+currentStr.substring(16, 18);
									ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
									swapIfValid(ssrLocates[1]);
									swapIfValid(16);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp2 = currentStr.substring(0, 15)+"11"+currentStr.substring(17, 18);
									ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
									if(ssrLocates[2]==17){ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									else{
									swapIfValid(ssrLocates[2]);
									swapIfValid(17);
									ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									
									
								}
							}
						}

						break;

					default:
						break;
					}
				}
			}
			if(ssrDetected == false && results.size()>3){
				ssrString = results.get(3);
				int ssrLength = ssrString.length();
				String currentStr = getCurrentStr();
				String[] ssrLetter;
				int[] ssrLocates;
				if(ssrLength>6){}
				else{
					switch (ssrLength) {
					case 6:
						ssrLetter = new String[6];
						ssrLocates = new int[6];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						ssrLetter[5] = String.valueOf(ssrString.charAt(5));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											String currentStr5 = currentStr.replaceFirst(ssrLetter[4], "4");
											if(!currentStr5.contains(ssrLetter[5])){}
											else {
												ssrLocates[5]=currentStr.indexOf(ssrLetter[5]);
												
												swapIfValid(ssrLocates[0]);
												swapIfValid(0);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp1 = "1"+currentStr.substring(1, 18);
												ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
												swapIfValid(ssrLocates[1]);
												swapIfValid(1);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp2 = "11"+currentStr.substring(2, 18);
												ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
												swapIfValid(ssrLocates[2]);
												swapIfValid(2);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp3 = "111"+currentStr.substring(3, 18);
												ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
												swapIfValid(ssrLocates[3]);
												swapIfValid(3);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp4 = "1111"+currentStr.substring(4, 18);
												ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
												swapIfValid(ssrLocates[4]);
												swapIfValid(4);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp5 = "11111"+currentStr.substring(5, 18);
												ssrLocates[5]=currentStrTemp5.indexOf(ssrLetter[5]);
												if(ssrLocates[5]==5){ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
												else{
												swapIfValid(ssrLocates[5]);
												swapIfValid(5);
												ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
			
											}
										}
									}
								}
							}
						}

						break;
						
					case 5:
						ssrLetter = new String[5];
						ssrLocates = new int[5];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							System.out.println("Hello Done");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								System.out.println("Hello Done");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									System.out.println("Hello Done");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										System.out.println("Hello Done");
										
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											
											swapIfValid(ssrLocates[0]);
											swapIfValid(6);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp1 = currentStr.substring(0, 6)+"1"+currentStr.substring(7, 18);
											ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
											swapIfValid(ssrLocates[1]);
											swapIfValid(7);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp2 = currentStr.substring(0, 6)+"11"+currentStr.substring(8, 18);
											ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
											swapIfValid(ssrLocates[2]);
											swapIfValid(8);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp3 = currentStr.substring(0, 6)+"111"+currentStr.substring(9, 18);
											ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
											swapIfValid(ssrLocates[3]);
											swapIfValid(9);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp4 = currentStr.substring(0, 6)+"1111"+currentStr.substring(10, 18);
											ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
											if(ssrLocates[4]==10){ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}
											else{
											swapIfValid(ssrLocates[4]);
											swapIfValid(10);
											ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}	
										}
									}
								}
							}
						}

						break;
						
					case 4:
						ssrLetter = new String[4];
						ssrLocates = new int[4];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										
										swapIfValid(ssrLocates[0]);
										swapIfValid(11);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp1 = currentStr.substring(0, 11)+"1"+currentStr.substring(12, 18);
										ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
										swapIfValid(ssrLocates[1]);
										swapIfValid(12);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp2 = currentStr.substring(0, 11)+"11"+currentStr.substring(13, 18);
										ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
										swapIfValid(ssrLocates[2]);
										swapIfValid(13);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp3 = currentStr.substring(0, 11)+"111"+currentStr.substring(14, 18);
										ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
										if(ssrLocates[3]==14){ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
										else{
										swapIfValid(ssrLocates[3]);
										swapIfValid(14);
										ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
													
									}
								}
							}
						}

						break;
						
					case 3:
						ssrLetter = new String[3];
						ssrLocates = new int[3];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									
									swapIfValid(ssrLocates[0]);
									swapIfValid(15);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp1 = currentStr.substring(0, 15)+"1"+currentStr.substring(16, 18);
									ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
									swapIfValid(ssrLocates[1]);
									swapIfValid(16);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp2 = currentStr.substring(0, 15)+"11"+currentStr.substring(17, 18);
									ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
									if(ssrLocates[2]==17){ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									else{
									swapIfValid(ssrLocates[2]);
									swapIfValid(17);
									ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									
									
								}
							}
						}

						break;

					default:
						break;
					}
				}
			}
			if(ssrDetected == false && results.size()>4){
				ssrString = results.get(4);
				int ssrLength = ssrString.length();
				String currentStr = getCurrentStr();
				String[] ssrLetter;
				int[] ssrLocates;
				if(ssrLength>6){}
				else{
					switch (ssrLength) {
					case 6:
						ssrLetter = new String[6];
						ssrLocates = new int[6];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						ssrLetter[5] = String.valueOf(ssrString.charAt(5));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											String currentStr5 = currentStr.replaceFirst(ssrLetter[4], "4");
											if(!currentStr5.contains(ssrLetter[5])){}
											else {
												ssrLocates[5]=currentStr.indexOf(ssrLetter[5]);
												
												swapIfValid(ssrLocates[0]);
												swapIfValid(0);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp1 = "1"+currentStr.substring(1, 18);
												ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
												swapIfValid(ssrLocates[1]);
												swapIfValid(1);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp2 = "11"+currentStr.substring(2, 18);
												ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
												swapIfValid(ssrLocates[2]);
												swapIfValid(2);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp3 = "111"+currentStr.substring(3, 18);
												ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
												swapIfValid(ssrLocates[3]);
												swapIfValid(3);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp4 = "1111"+currentStr.substring(4, 18);
												ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
												swapIfValid(ssrLocates[4]);
												swapIfValid(4);
												
												
												currentStr = getCurrentStr();
												String currentStrTemp5 = "11111"+currentStr.substring(5, 18);
												ssrLocates[5]=currentStrTemp5.indexOf(ssrLetter[5]);
												if(ssrLocates[5]==5){ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
												else{
												swapIfValid(ssrLocates[5]);
												swapIfValid(5);
												ssrDetected = true;
												PushTask pushTask = new PushTask(CommGame.this);
												//pushTask.execute(ssrString.toString());
												pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);}
			
											}
										}
									}
								}
							}
						}

						break;
						
					case 5:
						ssrLetter = new String[5];
						ssrLocates = new int[5];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						ssrLetter[4] = String.valueOf(ssrString.charAt(4));
						
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							System.out.println("Hello Done");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								System.out.println("Hello Done");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									System.out.println("Hello Done");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										String currentStr4 = currentStr.replaceFirst(ssrLetter[3], "3");
										System.out.println("Hello Done");
										
										if(!currentStr4.contains(ssrLetter[4])){}
										else {
											ssrLocates[4]=currentStr.indexOf(ssrLetter[4]);
											
											swapIfValid(ssrLocates[0]);
											swapIfValid(6);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp1 = currentStr.substring(0, 6)+"1"+currentStr.substring(7, 18);
											ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
											swapIfValid(ssrLocates[1]);
											swapIfValid(7);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp2 = currentStr.substring(0, 6)+"11"+currentStr.substring(8, 18);
											ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
											swapIfValid(ssrLocates[2]);
											swapIfValid(8);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp3 = currentStr.substring(0, 6)+"111"+currentStr.substring(9, 18);
											ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
											swapIfValid(ssrLocates[3]);
											swapIfValid(9);
											
											
											currentStr = getCurrentStr();
											String currentStrTemp4 = currentStr.substring(0, 6)+"1111"+currentStr.substring(10, 18);
											ssrLocates[4]=currentStrTemp4.indexOf(ssrLetter[4]);
											if(ssrLocates[4]==10){ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}
											else{
											swapIfValid(ssrLocates[4]);
											swapIfValid(10);
											ssrDetected = true;
											PushTask pushTask = new PushTask(CommGame.this);
											//pushTask.execute(ssrString.toString());
											pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);}	
										}
									}
								}
							}
						}

						break;
						
					case 4:
						ssrLetter = new String[4];
						ssrLocates = new int[4];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						ssrLetter[3] = String.valueOf(ssrString.charAt(3));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									String currentStr3 = currentStr.replaceFirst(ssrLetter[2], "2");
									if(!currentStr3.contains(ssrLetter[3])){}
									else {
										ssrLocates[3]=currentStr.indexOf(ssrLetter[3]);
										
										swapIfValid(ssrLocates[0]);
										swapIfValid(11);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp1 = currentStr.substring(0, 11)+"1"+currentStr.substring(12, 18);
										ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
										swapIfValid(ssrLocates[1]);
										swapIfValid(12);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp2 = currentStr.substring(0, 11)+"11"+currentStr.substring(13, 18);
										ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
										swapIfValid(ssrLocates[2]);
										swapIfValid(13);
										
										
										currentStr = getCurrentStr();
										String currentStrTemp3 = currentStr.substring(0, 11)+"111"+currentStr.substring(14, 18);
										ssrLocates[3]=currentStrTemp3.indexOf(ssrLetter[3]);
										if(ssrLocates[3]==14){ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
										else{
										swapIfValid(ssrLocates[3]);
										swapIfValid(14);
										ssrDetected = true;
										PushTask pushTask = new PushTask(CommGame.this);
										//pushTask.execute(ssrString.toString());
										pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);}
													
									}
								}
							}
						}

						break;
						
					case 3:
						ssrLetter = new String[3];
						ssrLocates = new int[3];
						ssrLetter[0] = String.valueOf(ssrString.charAt(0));
						ssrLetter[1] = String.valueOf(ssrString.charAt(1));
						ssrLetter[2] = String.valueOf(ssrString.charAt(2));
						
						if(!currentStr.contains(ssrLetter[0])){}
						else {
							ssrLocates[0]=currentStr.indexOf(ssrLetter[0]);
							String currentStr1 = currentStr.replaceFirst(ssrLetter[0], "0");
							if(!currentStr1.contains(ssrLetter[1])){}
							else {
								ssrLocates[1]=currentStr.indexOf(ssrLetter[1]);
								String currentStr2 = currentStr.replaceFirst(ssrLetter[1], "1");
								if(!currentStr2.contains(ssrLetter[2])){}
								else {
									ssrLocates[2]=currentStr.indexOf(ssrLetter[2]);
									
									swapIfValid(ssrLocates[0]);
									swapIfValid(15);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp1 = currentStr.substring(0, 15)+"1"+currentStr.substring(16, 18);
									ssrLocates[1]=currentStrTemp1.indexOf(ssrLetter[1]);
									swapIfValid(ssrLocates[1]);
									swapIfValid(16);
									
									
									currentStr = getCurrentStr();
									String currentStrTemp2 = currentStr.substring(0, 15)+"11"+currentStr.substring(17, 18);
									ssrLocates[2]=currentStrTemp2.indexOf(ssrLetter[2]);
									if(ssrLocates[2]==17){ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									else{
									swapIfValid(ssrLocates[2]);
									swapIfValid(17);
									ssrDetected = true;
									PushTask pushTask = new PushTask(CommGame.this);
									//pushTask.execute(ssrString.toString());
									pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);}
									
									
								}
							}
						}

						break;

					default:
						break;
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	
	private String getCurrentStr(){
		return String.valueOf(letterButtons[0].getText().charAt(0))+
				String.valueOf(letterButtons[1].getText().charAt(0))+
					String.valueOf(letterButtons[2].getText().charAt(0))+
						String.valueOf(letterButtons[3].getText().charAt(0))+
							String.valueOf(letterButtons[4].getText().charAt(0))+
								String.valueOf(letterButtons[5].getText().charAt(0))+String.valueOf(letterButtons[6].getText().charAt(0))+
				String.valueOf(letterButtons[7].getText().charAt(0))+
					String.valueOf(letterButtons[8].getText().charAt(0))+
						String.valueOf(letterButtons[9].getText().charAt(0))+
							String.valueOf(letterButtons[10].getText().charAt(0))+String.valueOf(letterButtons[11].getText().charAt(0))+
				String.valueOf(letterButtons[12].getText().charAt(0))+
					String.valueOf(letterButtons[13].getText().charAt(0))+
						String.valueOf(letterButtons[14].getText().charAt(0))+String.valueOf(letterButtons[15].getText().charAt(0))+
				String.valueOf(letterButtons[16].getText().charAt(0))+
					String.valueOf(letterButtons[17].getText().charAt(0));
	}
	
	private String getCurrentScore(){
		return String.valueOf(letterButtons[0].getText().charAt(letterButtons[0].getText().length()-1))+
				String.valueOf(letterButtons[1].getText().charAt(letterButtons[1].getText().length()-1))+
				String.valueOf(letterButtons[2].getText().charAt(letterButtons[2].getText().length()-1))+
				String.valueOf(letterButtons[3].getText().charAt(letterButtons[3].getText().length()-1))+
				String.valueOf(letterButtons[4].getText().charAt(letterButtons[4].getText().length()-1))+
				String.valueOf(letterButtons[5].getText().charAt(letterButtons[5].getText().length()-1))+
				String.valueOf(letterButtons[6].getText().charAt(letterButtons[6].getText().length()-1))+
				String.valueOf(letterButtons[7].getText().charAt(letterButtons[7].getText().length()-1))+
				String.valueOf(letterButtons[8].getText().charAt(letterButtons[8].getText().length()-1))+
				String.valueOf(letterButtons[9].getText().charAt(letterButtons[9].getText().length()-1))+
				String.valueOf(letterButtons[10].getText().charAt(letterButtons[10].getText().length()-1))+
				String.valueOf(letterButtons[11].getText().charAt(letterButtons[11].getText().length()-1))+
				String.valueOf(letterButtons[12].getText().charAt(letterButtons[12].getText().length()-1))+
				String.valueOf(letterButtons[13].getText().charAt(letterButtons[13].getText().length()-1))+
				String.valueOf(letterButtons[14].getText().charAt(letterButtons[14].getText().length()-1))+
				String.valueOf(letterButtons[15].getText().charAt(letterButtons[15].getText().length()-1))+
				String.valueOf(letterButtons[16].getText().charAt(letterButtons[16].getText().length()-1))+
				String.valueOf(letterButtons[17].getText().charAt(letterButtons[17].getText().length()-1));
	}
	
	class HintListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
				//System.out.println("PAUSE");
			// TODO Auto-generated method stub
				
				result1=resultString.substring(0, 6);
				result2=resultString.substring(6, 11);
				result3=resultString.substring(11, 15);
				result4=resultString.substring(15, 18);
				result=result1+"\n"+result2+"\n"+result3+"\n"+result4;
				
				new AlertDialog.Builder(CommGame.this)  
				
                .setTitle("Hint")

                .setMessage(result)

                .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
                     public void onClick(DialogInterface dialoginterface, int i){
                    	 setResult(RESULT_OK);
                         //finish();
                     }
             })

                .show();

			
		}
		
	}
	
	class QuitListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(musicGoOn2==true){
				musicGoOn = true;
			new AlertDialog.Builder(CommGame.this)  
			
            .setTitle("Are you sure to leave the game?")

            .setMessage("Your score will not be saved if disconnect now")

            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialoginterface, int i){
                	 setResult(RESULT_OK);
                	 mc.cancel();
         			 realTimePullTask.cancel(true);
                     finish();
                 }
         })
         .setNegativeButton("Cancel",   new DialogInterface.OnClickListener(){
                 public void onClick(DialogInterface dialoginterface, int i){
                	 setResult(RESULT_CANCELED);
                 }
         })

            .show();}
			else {
				new AlertDialog.Builder(CommGame.this)  
				
	            .setTitle("Are you sure to leave the game?")

	            .setMessage("Your score will not be saved if disconnect now")

	            .setPositiveButton("OK",   new DialogInterface.OnClickListener(){
	                 public void onClick(DialogInterface dialoginterface, int i){
	                	 setResult(RESULT_OK);
	                	 mc.cancel();
	         			 realTimePullTask.cancel(true);
	                     finish();
	                 }
	         })
	         .setNegativeButton("Cancel",   new DialogInterface.OnClickListener(){
	                 public void onClick(DialogInterface dialoginterface, int i){
	                	 setResult(RESULT_CANCELED);
	                 }
	         })

	            .show();
			}
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
				//letterButtons[i].setClickable(false);
				letterButtons[i].startAnimation(alphaDes);
				letterButtons[i].setOnClickListener(null);
			}
			//hintButton.setClickable(false);
			csrButton.startAnimation(alphaDes);
			csrButton.setOnClickListener(null);
			
			hintButton.startAnimation(alphaDes);
			hintButton.setOnClickListener(null);
			
			//backButton.setClickable(false);
			backButton.startAnimation(alphaDes);
			backButton.setOnClickListener(null);
			
			pauseButton.setText("Resume Game");
			pauseTwicePressed.add(1);
			}
			else{
				
				for (int i =0; i<18; i++) {
					//letterButtons[i].setTextColor(getResources().getColor(R.color.black));
			
					//letterButtons[i].setClickable(true);
					letterButtons[i].startAnimation(alphaInc);
					letterButtons[i].setOnClickListener(new LetterButtonsListener());
				}

				//hintButton.setClickable(true);
				csrButton.startAnimation(alphaInc);
				csrButton.setOnClickListener(new CSRListener());
				
				hintButton.startAnimation(alphaInc);
				hintButton.setOnClickListener(new HintListener());
				
				//backButton.setClickable(true);
				backButton.startAnimation(alphaInc);
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
        	mc.cancel();
        	PushScoreTask pushScoreTask2 = new PushScoreTask(CommGame.this);
			pushScoreTask2.execute(OppNameMyName.myName);
        	finish();
			Intent intent =new Intent();
			intent.putExtra("lastScore", scoreText.getText().toString());
			intent.setClass(CommGame.this, Finish.class);
			
			startActivity(intent);
        }     
        @Override     
        public void onTick(long millisUntilFinished) { 
        	Integer miliSec = new Integer(new Double(millisUntilFinished).intValue()); 
        	//System.out.println("--------------->TIME "+miliSec);
        	Integer cdSecs = miliSec / 1000;  

        	Integer minutes = (cdSecs % 3600) / 60;  
        	Integer seconds = (cdSecs % 3600) % 60;    

        	cdText.setText("Time Left: "+String.format("%02d", minutes) + ":"  
        	+ String.format("%02d", seconds));
        	//cdText.setText(millisUntilFinished / 1000 +""); 
        	if(cdSecs<=60){
        		cdText.setTextColor(getResources().getColor(R.color.red));
        		//cdText.setTextSize(25);
        	}
        	timeLeft = millisUntilFinished ;
           // Toast.makeText(NewActivity.this, millisUntilFinished / 1000 + "", Toast.LENGTH_LONG).show(); 
        }    
    }  
	
	class SwapTimesListener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(Integer.parseInt(s.toString())==0){
				mc.cancel();
				PushScoreTask pushScoreTask = new PushScoreTask(CommGame.this);
				pushScoreTask.execute(OppNameMyName.myName);
				finish();
				Intent intent =new Intent();
				intent.setClass(CommGame.this, Finish.class);
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
	class ScoreTextListener implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			System.out.println("------------->Score: "+s);
			if(Integer.parseInt(s.toString())==96){
				mc.cancel();
				PushScoreTask pushScoreTask = new PushScoreTask(CommGame.this);
				pushScoreTask.execute(OppNameMyName.myName);
				finish();
				Intent intent =new Intent();
				intent.setClass(CommGame.this, Cong.class);
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
			case R.id.cbutton1:
				swapIfValid(0);
				break;
			case R.id.cbutton2:
				swapIfValid(1);
				break;
			case R.id.cbutton3:
				swapIfValid(2);
				break;
			case R.id.cbutton4:
				swapIfValid(3);
				break;
			case R.id.cbutton5:
				swapIfValid(4);
				break;
			case R.id.cbutton6:
				swapIfValid(5);
				break;
			case R.id.cbutton7:
				swapIfValid(6);
				break;
			case R.id.cbutton8:
				swapIfValid(7);
				break;
			case R.id.cbutton9:
				swapIfValid(8);
				break;
			case R.id.cbutton10:
				swapIfValid(9);
				break;
			case R.id.cbutton11:
				swapIfValid(10);
				break;
			case R.id.cbutton12:
				swapIfValid(11);
				break;
			case R.id.cbutton13:
				swapIfValid(12);
				break;
			case R.id.cbutton14:
				swapIfValid(13);
				break;
			case R.id.cbutton15:
				swapIfValid(14);
				break;
			case R.id.cbutton16:
				swapIfValid(15);
				break;
			case R.id.cbutton17:
				swapIfValid(16);
				break;
			case R.id.cbutton18:
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
			scoreText.setText("Score: "+score);
			
			invisibleScore= pscore+invisibleScore-invisibleTempScore[locate-1];
			invisibleTempScore[locate-1]=pscore;
			invisibleScoreText.setText(invisibleScore+"");
			
			mp = MediaPlayer.create(CommGame.this, R.raw.match);
	        mp.start();
			//tg.startTone(ToneGenerator.TONE_DTMF_5, 200);
			return 1;
		}
		else{
		if(words.containsKey(s.toString())){
			PushTask pushTask = new PushTask(CommGame.this);
			
			if(countBack<200){
				wordsBack[countBack]=s.toString();
				countBack++;
				}
			words.remove(s.toString());
			System.out.println("detect match");
			//result=result+s.toString()+"\n";
			//textView.setMovementMethod(new ScrollingMovementMethod()); 
			//textView.setText(result);
			
			scoreLocates[locate-1]=true;
			
			score= pscore*(9-locate)+score-tempScore[locate-1];
			tempScore[locate-1]=pscore*(9-locate);
			scoreText.setText("Score: "+score);
			int len = s.length();
			switch (len) {
			case 3:
				pushTask.execute("a 3-letter word! "+"\n"+"Score:"+score);
				break;
			case 4:
				pushTask.execute("a 4-letter word! "+"\n"+"Score:"+score);
				break;
			case 5:
				pushTask.execute("a 5-letter word! "+"\n"+"Score:"+score);
				break;
			case 6:
				pushTask.execute("a 6-letter word! "+"\n"+"Score:"+score);
				break;

			default:
				break;
			}
			
			
			invisibleScore= pscore+invisibleScore-invisibleTempScore[locate-1];
			invisibleTempScore[locate-1]=pscore;
			invisibleScoreText.setText(invisibleScore+"");
			
			mp = MediaPlayer.create(CommGame.this, R.raw.match);
	        mp.start();
			//tg.startTone(ToneGenerator.TONE_DTMF_5, 200);
		    return score;
		}
		else{
			System.out.println("undetect match");
			if(scoreLocates[locate-1]==true){
				score= score-tempScore[locate-1];
				scoreText.setText("Score: "+score);
				
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
	
	
	
class PushBoardTask extends AsyncTask<String, Integer, Void>{
	private Context context;  
	PushBoardTask(Context context) {  
          this.context = context;  
          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
         // progressBar.startAnimation(alphaInc);
          
      } 
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName,params[0]);
		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName,params[0]);
		KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName,params[0]);
		return null;
	}
	
}
class PushScoreTask extends AsyncTask<String, Integer, String> {  
    	
    	private Context context;  
    	PushScoreTask(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
             // progressBar.startAnimation(alphaInc);
              
          }  
    

        @Override  
        protected String doInBackground(String... params) {  

        	String preScore = KeyValueAPI.get("basin", "basin576095", params[0]+"@HS");
        	if(preScore.equals("Error: No Such Key")){
        	KeyValueAPI.put("basin", "basin576095", params[0]+"@HFS", OppNameMyName.myFakeName+": "+score+"");
        	KeyValueAPI.put("basin", "basin576095", params[0]+"@HS", score+"");
        	return score+"";}
        	
        	else {
				int preIntScore = Integer.parseInt(preScore);
				if(score>preIntScore){
					KeyValueAPI.put("basin", "basin576095", params[0]+"@HFS", OppNameMyName.myFakeName+": "+score+"");
					KeyValueAPI.put("basin", "basin576095", params[0]+"@HS", score+"");
					return score+"";
				}
				else {
					//keep the previous score.
					return preScore;
				}
			}
        	
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
    
 
        @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("push score"+result);
		}


		@Override  
        protected void onPreExecute() {  
           
            
        }  
 
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            
        	 //System.out.println("onProgressUpdate"+values[0]);  
            
        	// progressDialog.setProgress(values[0]);
              
        }  
 
     }
class PushTask extends AsyncTask<String, Integer, String> {  
    	
    	private Context context;  
    	PushTask(Context context) {  
              this.context = context;  
              //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
             // progressBar.startAnimation(alphaInc);
              
          }  
    

        @Override  
        protected String doInBackground(String... params) {  

        	
        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "$"+params[0]);
        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "$"+params[0]);
        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "$"+params[0]);
        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "$"+params[0]);
        	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "$"+params[0]);
        	return params[0];
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
    
 
        @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("your movement: "+result);
		}


		@Override  
        protected void onPreExecute() {  
           
            
        }  
 
        @Override  
        protected void onProgressUpdate(Integer... values) {  
            
        	 //System.out.println("onProgressUpdate"+values[0]);  
            
        	// progressDialog.setProgress(values[0]);
              
        }  
 
     }
	
class RealTimePullTask extends AsyncTask<Void, Integer, Void> {  
	
	private Context context;  
	RealTimePullTask(Context context) {  
          this.context = context;  
          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
         // progressBar.startAnimation(alphaInc);
          
      }  


    @Override  
    protected Void doInBackground(Void... params) {  
    	SubRealTimePullTask subRealTimePullTask = null;
    	//String oppQuitStatuString =KeyValueAPI.get("basin", "basin576095", oppName);
    	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "("+OppNameMyName.oppName);
    	//KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "("+OppNameMyName.oppName);
    	while(KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("#AFK")==false && 
    			KeyValueAPI.get("basin", "basin576095", OppNameMyName.oppName).equals("#QUIT2")==false&& 
    				KeyValueAPI.get("basin", "basin576095", OppNameMyName.myName).equals("#QUIT2")==false){
    		subRealTimePullTask = new SubRealTimePullTask(CommGame.this);
    		subRealTimePullTask.execute();
    		try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}

    	//KeyValueAPI.put("basin", "basin576095", myName, params[0]);
    	return null;
    }  

    @Override  
    protected void onCancelled() {  
        super.onCancelled();  
    }  



    @Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//System.out.println("your movement: "+result);
	}


	@Override  
    protected void onPreExecute() {  
       
        
    }  

    @Override  
    protected void onProgressUpdate(Integer... values) {  
        
    	 //System.out.println("onProgressUpdate"+values[0]);  
        
    	// progressDialog.setProgress(values[0]);
          
    }  
    class SubRealTimePullTask extends AsyncTask<Void, Integer, String> {  
    	
    	private Context context;  
    	SubRealTimePullTask(Context context) {  
              this.context = context;  
          }  
    
        @Override  
        protected String doInBackground(Void... params) {  
        	String resString=KeyValueAPI.get("basin", "basin576095", OppNameMyName.oppName);
        	return resString;
        }  
 
        @Override  
        protected void onCancelled() {  
            super.onCancelled();  
        }  
 
        @Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result.equals("("+OppNameMyName.myName)){
			oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" joins the game");
			}
			if(!result.equals("("+OppNameMyName.myName)){
				if(result.equals("#AFK")){
					oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" is not actively playing");
					System.out.println("opponent's movement: "+result);
				}else{
					if(result.equals("#QUIT")||result.equals("#QUIT2")){
					oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" quits");
					System.out.println("opponent's movement: "+result);
					}else{
						if(result.equals("#WIN")){
							oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" wins the game!");
							System.out.println("opponent's movement: "+result);
						}else{
							if(result.equals("#LOS")){
								oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" loses the game!");
								System.out.println("opponent's movement: "+result);
							}else{
								if(result.equals("Error: IOException")){
									oppMsg.setText("Cannot access to server. Please check your NetWork.");
								}
								else{
									if(result.substring(0, 1).equals("$")){
										oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" spells "+result.substring(1, result.length()));
										System.out.println("opponent's movement: "+result);}
									else {
										oppMsg.setText(OppNameMyName.oppFakeName+"'s movement:"+"\n"+OppNameMyName.oppFakeName+" quits");
										System.out.println("opponent's movement: "+result);
									}
								
			}}}}}}
		}

		@Override  
        protected void onPreExecute() {            
        }  
 
        @Override  
        protected void onProgressUpdate(Integer... values) {  
        }  
 
     }

 }	


class AFKTask extends AsyncTask<Void, Integer, Void> {  
	
	private Context context;  
	AFKTask(Context context) {  
          this.context = context;  
          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
         // progressBar.startAnimation(alphaInc);
          
      }  


    @Override  
    protected Void doInBackground(Void... params) {  

    	
    	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "#AFK");
    	return null;
    }  

    @Override  
    protected void onCancelled() {  
        super.onCancelled();  
    }  



    @Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//System.out.println("your movement: "+result);
	}


	@Override  
    protected void onPreExecute() {  
       
        
    }  

    @Override  
    protected void onProgressUpdate(Integer... values) {  
        
    	 //System.out.println("onProgressUpdate"+values[0]);  
        
    	// progressDialog.setProgress(values[0]);
          
    }  

 }

class GoOnTask extends AsyncTask<Void, Integer, Void> {  
	
	private Context context;  
	GoOnTask(Context context) {  
          this.context = context;  
          //progressBar.setBackgroundColor(getResources().getColor(R.color.black));  
         // progressBar.startAnimation(alphaInc);
          
      }  


    @Override  
    protected Void doInBackground(Void... params) {  

    	
    	KeyValueAPI.put("basin", "basin576095", OppNameMyName.myName, "BACK");
    	return null;
    }  

    @Override  
    protected void onCancelled() {  
        super.onCancelled();  
    }  



    @Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//System.out.println("your movement: "+result);
	}


	@Override  
    protected void onPreExecute() {  
       
        
    }  

    @Override  
    protected void onProgressUpdate(Integer... values) {  
        
    	 //System.out.println("onProgressUpdate"+values[0]);  
        
    	// progressDialog.setProgress(values[0]);
          
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
