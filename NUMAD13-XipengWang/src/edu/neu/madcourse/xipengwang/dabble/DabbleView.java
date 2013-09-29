package edu.neu.madcourse.xipengwang.dabble;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.neu.madcourse.xipengwang.R;

public class DabbleView extends View{
	private static final String TAG = "Dabble";

	   
	   private static final String SELX = "selX"; 
	   private static final String SELY = "selY";
	   private static final String VIEW_STATE = "viewState";
	   private static final int ID = 43; 

	   
	   private float width;    // width of one tile
	   private float height;   // height of one tile
	   private int selX;       // X index of selection
	   private int selY;       // Y index of selection
	   private final Rect selRect = new Rect();

	   private final TestGame game;
	public DabbleView(Context context) {
	      
	      super(context);
	      this.game = (TestGame) context;
	      setFocusable(true);
	      setFocusableInTouchMode(true);
	      
	      // ...
	      
	      setId(ID); 
	   }
	
	 @Override
	   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	      width = w / 9f;
	      height = h / 9f;
	      getRect(selX, selY, selRect);
	      Log.d(TAG, "onSizeChanged: width " + width + ", height "
	            + height);
	      super.onSizeChanged(w, h, oldw, oldh);
	   }
	 
	  @Override
	   protected void onDraw(Canvas canvas) {
	      // Draw the background...
	      Paint background = new Paint();
	      background.setColor(getResources().getColor(
	            R.color.puzzle_background));
	      canvas.drawRect(0, 0, getWidth(), getHeight(), background);

	      
	      // Draw the board...
	      
	      // Define colors for the grid lines
	      Paint dark = new Paint();
	      dark.setColor(getResources().getColor(R.color.puzzle_dark));

	      Paint hilite = new Paint();
	      hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

	      Paint light = new Paint();
	      light.setColor(getResources().getColor(R.color.puzzle_light));

	      // Draw the minor grid lines
	      /*for (int i = 0; i < 9; i++) {
	         canvas.drawLine(0, i * height, getWidth(), i * height,
	               light);
	         canvas.drawLine(0, i * height + 1, getWidth(), i * height
	               + 1, hilite);
	         canvas.drawLine(i * width, 0, i * width, getHeight(),
	               light);
	         canvas.drawLine(i * width + 1, 0, i * width + 1,
	               getHeight(), hilite);
	      }*/
	      for(int i=6; i>2;i--){
	    	  int j = 6-i;
	    	  canvas.drawLine(0, j * height, width*i, j * height,
	    			  dark);
	    	  canvas.drawLine(0, (j+1) * height, width*i, (j+1) * height,
	    			  dark);
	      }
	      for(int i=1; i<=4;i++){
	    	  int j = 6-i;
	    	  canvas.drawLine(j * width, 0, j * width, i*height,
	    			  dark);
		      canvas.drawLine((j+1) * width, 0, (j+1) * width,
		    		  i*height, dark);
		     
	      }
	      canvas.drawLine(0 * width, 0, 0 * width, 4*height,
    			  dark);
	      canvas.drawLine(1 * width, 0, 1 * width,
	    		  4*height, dark);
	      canvas.drawLine(1 * width, 0, 1 * width, 4*height,
    			  dark);
	      canvas.drawLine(2 * width, 0, 2 * width,
	    		  4*height, dark);
	      // Draw the major grid lines
	      /*for (int i = 0; i < 9; i++) {
	         if (i % 3 != 0)
	            continue;
	         canvas.drawLine(0, i * height, getWidth(), i * height,
	               dark);
	         canvas.drawLine(0, i * height + 1, getWidth(), i * height
	               + 1, hilite);
	         canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
	         canvas.drawLine(i * width + 1, 0, i * width + 1,
	               getHeight(), hilite);
	      }*/

	      // Draw the numbers...
	      // Define color and style for numbers
	      Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
	      foreground.setColor(getResources().getColor(
	            R.color.puzzle_foreground));
	      foreground.setStyle(Style.FILL);
	      foreground.setTextSize(height * 0.75f);
	      foreground.setTextScaleX(width / height);
	      foreground.setTextAlign(Paint.Align.CENTER);

	      // Draw the number in the center of the tile
	      FontMetrics fm = foreground.getFontMetrics();
	      // Centering in X: use alignment (and X at midpoint)
	      float x = width / 2;
	      // Centering in Y: measure ascent/descent first
	      float y = height / 2 - (fm.ascent + fm.descent) / 2;
	      int maxI = 6;
	      for (int j = 0; j < 4; j++) {
	    	 if(maxI>=3)
	    	 {
	    		 for (int i = 0; i < maxI; i++) {
	    			 canvas.drawText(this.game.getTileString(i, j), i
	    					 * width + x, j * height + y, foreground);
	    		 }
	    		 maxI--;
	         }
	      }
	      
	      // Draw the selection...
	      Log.d(TAG, "selRect=" + selRect);
	      Paint selected = new Paint();
	      selected.setColor(getResources().getColor(
	            R.color.puzzle_selected));
	      canvas.drawRect(selRect, selected);
	   }
	  
	  private void getRect(int x, int y, Rect rect) {
	      rect.set((int) (x * width), (int) (y * height), (int) (x
	            * width + width), (int) (y * height + height));
	   }
	  
	  public boolean onTouchEvent(MotionEvent event) {
	      if (event.getAction() != MotionEvent.ACTION_DOWN)
	         return super.onTouchEvent(event);

	      select((int) (event.getX() / width),
	            (int) (event.getY() / height));
	     // game.showKeypadOrError(selX, selY);
	      Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
	      return true;
	   }
	  
	  private void select(int x, int y) {
	      invalidate(selRect);
	      selX = Math.min(Math.max(x, 0), 8);
	      selY = Math.min(Math.max(y, 0), 8);
	      getRect(selX, selY, selRect);
	      invalidate(selRect);
	   }
}
