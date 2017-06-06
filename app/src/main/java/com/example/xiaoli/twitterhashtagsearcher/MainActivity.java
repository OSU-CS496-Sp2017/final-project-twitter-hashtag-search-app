package com.example.xiaoli.twitterhashtagsearcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twitter.sdk.android.core.Twitter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Twitter.initialize(this);
    }
}
