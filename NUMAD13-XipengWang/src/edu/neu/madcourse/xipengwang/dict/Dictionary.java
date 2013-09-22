package edu.neu.madcourse.xipengwang.dict;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.security.auth.PrivateCredentialPermission;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.neu.madcourse.xipengwang.R;

public class Dictionary extends Activity{
	private Button clearButton;
	private Button returnButton;
	private Button ackButton;
	private EditText editText;
	private TextView textView;
	private TextWatcherListener textWatcher;
	private String[] words;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);
		setTitle("Xipeng Wang");
		clearButton = (Button)findViewById(R.id.clear_button);
		returnButton = (Button)findViewById(R.id.return_button);
		ackButton = (Button)findViewById(R.id.Acknowledgements_button);
		editText = (EditText)findViewById(R.id.editText);
		textView = (TextView)findViewById(R.id.display);
		textWatcher = new TextWatcherListener();
		editText.addTextChangedListener(textWatcher);
		//System.out.println(testword);
		//System.out.println(length);
		//words = new String[232334];
		final HashMap<String,String> words = new HashMap<String,String>();
		//textView.setText("qwqwrfwg");
		
		
		try {
			//System.out.println(Environment.getExternalStorageDirectory());
			//String pathString =Environment.getExternalStorageDirectory().toString()+"/wordlist.txt";
			//File input = new File(pathString);
			//File input = new File("/NUMAD13-XipengWang/res/raw/wordlist.txt");
			//InputStream inputStream = urlConnection.getInputStream();
			String line;
			int i =0;
			InputStream input = getResources().openRawResource(R.raw.wordlist2);
			InputStreamReader inputStreamReader = new InputStreamReader(input, "GBK");
			BufferedReader reader =new BufferedReader(inputStreamReader);
			System.out.println("file loaded!!! ");
			while ((line = reader.readLine())!= null) {
				words.put(line,"");
				i++;
				
			}
			/*Scanner scanner = new Scanner(input);
			
			for(int i=0;i<=432333&scanner.hasNext();i++){
				//words[i]=scanner.next();
				words.put(scanner.next(),"");
				//System.out.println(words[i]);
				
			}
			scanner.close();*/
			System.out.println("file readed");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("file unreaded");
			e.printStackTrace();
		}
		
		//textView.setText(""+words.containsKey("aalesund"));
		

	}
	private class TextWatcherListener implements TextWatcher {
		private CharSequence inputCharSequence;
		private int onstart;
		private int atend;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			//atend = before;
			textView.setText(s);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			inputCharSequence = s;
			//onstart = after;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			textView.setText(s);
			//editText.removeTextChangedListener(this);
			//editText.setText(s);
			//String text;
			/*text=editText.getText().toString();
			if(text!=null)
			{
				textView.setText(text);
			}
			else {
				textView.setText("You Have Not type anything");
			 
			}*/
			// TODO Auto-generated method stub
			/*if((s.length()>2)){
				//int[] res = search(inputCharSequence.toString());
				if(words.containsKey(inputCharSequence)){
					textView.setText(inputCharSequence);
				}
				
			}*/
		}
	}
	
	public int[] binarySearch(int low, int high, String word){
		int high2 =high;
		while(high2>=low){
			int mid =(low+high2)/2;
			if(word.compareTo(words[mid])<0){
				high2=mid-1;
				}
			else{ if(word.equals(words[mid])){
				
				System.out.println("get it");
				int[] res = new int[2];
				res[0]=mid;
				res[1]=high;
				return res;}
				else
					low=mid+1;
				}
		}
		int[] nonres = {-1,-1};
		return nonres;
	}
	public int[] search(String word){
		char fir = word.charAt(0);
		//System.out.println(fir);
		int low,high;
		switch(fir)
		{
		case 'a':
			low = 0;
			high = 28021;	
			return binarySearch(low, high, word);
		case 'b':
			low = 28022;
			high = 49782;	
			return binarySearch(low, high, word);
		case 'c':
			low = 49783;
			high = 87181;	
			return binarySearch(low, high, word);
		case 'd':
			low = 87182;
			high = 109716;	
			return binarySearch(low, high, word);
		case 'e':
			low = 109717;
			high = 126204;	
			return binarySearch(low, high, word);
		case 'f':
			low = 126205;
			high = 140362;	
			return binarySearch(low, high, word);
		case 'g':
			low = 140363;
			high = 153412;	
			return binarySearch(low, high, word);
		case 'h':
			low = 153413;
			high = 169466;	
			return binarySearch(low, high, word);
		case 'i':
			low = 169467;
			high = 184850;	
			return binarySearch(low, high, word);
		case 'j':
			low = 184851;
			high = 188109;	
			return binarySearch(low, high, word);
		case 'k':
			low = 188110;
			high = 193086;	
			return binarySearch(low, high, word);
		case 'l':
			low = 193087;
			high = 204728;	
			return binarySearch(low, high, word);
		case 'm':
			low = 204729;
			high = 228358;	
			return binarySearch(low, high, word);
		case 'n':
			low = 228359;
			high = 243726;	
			return binarySearch(low, high, word);
		case 'o':
			low = 243727;
			high = 258674;	
			return binarySearch(low, high, word);
		case 'p':
			low = 258675;
			high = 299213;	
			return binarySearch(low, high, word);
		case 'q':
			low = 299214;
			high = 301386;	
			return binarySearch(low, high, word);
		case 'r':
			low = 301387;
			high = 321643;	
			return binarySearch(low, high, word);
		case 's':
			low = 321644;
			high = 368464;	
			return binarySearch(low, high, word);
		case 't':
			low = 368465;
			high = 390514;	
			return binarySearch(low, high, word);
		case 'u':
			low = 390515;
			high = 415079;	
			return binarySearch(low, high, word);
		case 'v':
			low = 415080;
			high = 421398;	
			return binarySearch(low, high, word);
		case 'w':
			low = 421399;
			high = 429440;	
			return binarySearch(low, high, word);
		case 'x':
			low = 429441;
			high = 430015;	
			return binarySearch(low, high, word);
		case 'y':
			low = 430016;
			high = 431447;	
			return binarySearch(low, high, word);
		case 'z':
			//System.out.println("z");
			low = 431448;
			high = 432333;	
			return binarySearch(low, high, word);
		default:
			int[] nonres = {-1,-1};
			return nonres;
		}
	}
	
	
	
}
