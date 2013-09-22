package edu.neu.madcourse.xipengwang.dict;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
	private HashMap<String,String> words = new HashMap<String,String>();
	private String result="";
	//private String[] words;
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
		textView.setVerticalScrollBarEnabled(true);
		
		textWatcher = new TextWatcherListener();
		editText.addTextChangedListener(textWatcher);
		//System.out.println(testword);
		//System.out.println(length);
		//words = new String[232334];
		
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
		
		//textView.setText(""+words.containsKey("aah"));
		System.out.println("1:"+words.containsKey("aah"));

	}
	private class TextWatcherListener implements TextWatcher {
		private CharSequence inputCharSequence;
		private int onstart;
		private int atend;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			//atend = before;
			//textView.setText(s);
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			//inputCharSequence = s;
			//onstart = after;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			//textView.setText(s);
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
			System.out.println(s+" length: "+s.length());
			if((s.length()>2)){
				//int[] res = search(inputCharSequence.toString());
				if(words.containsKey(s.toString())){
					words.remove(s.toString());
					System.out.println("detect match");
					result=result+s.toString()+"\n";
					textView.setMovementMethod(new ScrollingMovementMethod()); 
					textView.setText(result);
					
					
				}
				else{
					System.out.println("undetect match");
				}
				
			}
		}
	}
}
