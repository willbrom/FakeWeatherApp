package com.example.toshiba.udacityweatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView detailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String weather = intent.getStringExtra(Intent.EXTRA_TEXT);

        detailTextView = (TextView) findViewById(R.id.tv_detail);
        detailTextView.setText(weather);
    }
}
