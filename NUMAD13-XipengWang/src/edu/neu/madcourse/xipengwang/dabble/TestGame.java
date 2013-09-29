package edu.neu.madcourse.xipengwang.dabble;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestGame extends Activity{

	private DabbleView dView;
	private char letter[];

	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      //Log.d(TAG, "onCreate");

	     // int diff = getIntent().getIntExtra(KEY_DIFFICULTY,
	           // DIFFICULTY_EASY);
	      //puzzle = getPuzzle(diff);
	      //calculateUsedTiles();
	      letter=getPuzzle();
	      dView = new DabbleView(this);
	      setContentView(dView);
	      dView.requestFocus();
	     
	      // ...
	      // If the activity is restarted, do a continue next time
	      //getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
	   }
	   private char[] getPuzzle() {
		      String puz="abroad"+
		    		  	 "cream"+
		    		  	 "deer"+
		    		  	 "eat";
		      
			  
		      return fromPuzzleString(puz);
		   }

	   static protected char[] fromPuzzleString(String string) {
		   char[] puz = new char[string.length()];
		      for (int i = 0; i < puz.length; i++) {
		         puz[i] = string.charAt(i);
		      }
		      return puz;
		   }
	   /** Return the tile at the given coordinates */
	   private char getTile(int x, int y) {
	      return letter[y * 9 + x];
	   }

	   /** Change the tile at the given coordinates */
	   private void setTile(int x, int y, char value) {
		   letter[y * 9 + x] = value;
	   }

	   /** Return a string for the tile at the given coordinates */
	   protected String getTileString(int x, int y) {
	      char v = getTile(x, y);
	      return String.valueOf(v);
	   }
	
}
