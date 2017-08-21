package com.example.toshiba.udacityweatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshiba.udacityweatherapp.utilities.JsonUtils;
import com.example.toshiba.udacityweatherapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherDataAdapter.ListItemClickListener {

    private TextView errorMessageTextView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private WeatherDataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.rv_weather_list);
        dataAdapter = new WeatherDataAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(dataAdapter);

        loadWeatherData();
    }

    private void loadWeatherData() {
        showWeatherData();
        URL url = NetworkUtils.getUrl("94042,USA");
        new FetchWeatherTask().execute(url);
    }

    private void showWeatherData() {
        recyclerView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        errorMessageTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void itemClickListener(String weatherAtPosition) {
        Toast.makeText(MainActivity.this, weatherAtPosition , Toast.LENGTH_SHORT).show();
    }

    class FetchWeatherTask extends AsyncTask<URL, Void, String[]> {

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
                dataAdapter.setWeatherData(strings);
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
            dataAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
