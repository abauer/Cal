package com.example.michael.cal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Anthony on 11/8/2014.
 */
public class IntroActivity extends Activity{

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        if (pref.getBoolean("doneTraining", false))
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, TrainingActivity.class));
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }


}
