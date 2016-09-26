package com.danielhan.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.danielhan.sample.library.RoundTopLayout;

public class MainActivity extends Activity {
    private RoundTopLayout rtl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rtl = (RoundTopLayout) findViewById(R.id.rtl);
        rtl.setBackColor(Color.RED);
    }
}
