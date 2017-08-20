package com.example.toshiba.udacityweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.toshiba.udacityweatherapp.utilities.JsonUtils;
import com.example.toshiba.udacityweatherapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView weatherDataTextView;
    TextView errorMessageTextView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherDataTextView = (TextView) findViewById(R.id.tv_weather_data);
        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress_bar);

        loadWeatherData();
    }

    private void loadWeatherData() {
        showWeatherData();
        URL url = NetworkUtils.getUrl("94042,USA");
        new FetchWeatherTask().execute(url);
    }

    private void showWeatherData() {
        weatherDataTextView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        errorMessageTextView.setVisibility(View.VISIBLE);
        weatherDataTextView.setVisibility(View.INVISIBLE);
    }

    public class FetchWeatherTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL url = urls[0];
            String[] weatherData = null;

            try {
                String getJsonString = NetworkUtils.getHttpResponse(url);
                weatherData = JsonUtils.getRequiredData(getJsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return weatherData;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            progressBar.setVisibility(View.INVISIBLE);

            if (strings != null) {
                for (String s : strings) {
                    weatherDataTextView.append(s + "\n\n\n");
                }
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            weatherDataTextView.setText("");
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
