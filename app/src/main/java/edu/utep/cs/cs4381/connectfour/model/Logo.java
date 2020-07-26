package edu.utep.cs.cs4381.connectfour.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.utep.cs.cs4381.connectfour.GameView;
import edu.utep.cs.cs4381.connectfour.R;

public class Logo {
    private int x,y,speed;
    private Bitmap bitmap;

    public Logo(GameView context) {
        x = 50;
        y = 50;
        bitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.main_logo_cf);
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public Bitmap getBitmap() {
        return this.bitmap;
    }
}
