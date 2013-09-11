package edu.neu.madcourse.xipengwang;



import java.net.ContentHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class AboutMe extends Activity{
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		textView= (TextView)findViewById(R.id.phoneid_text);
		textView.setText
		("MEID: "+
		((TelephonyManager)getSystemService
				(Context.TELEPHONY_SERVICE)).getDeviceId());
	}

}
