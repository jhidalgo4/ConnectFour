//Joaquin Hidalgo - UTEP
package edu.utep.cs.cs4381.connectfour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); //for main screen


        gv = new GameView(this);
        setContentView(gv); //custom view
    }
}