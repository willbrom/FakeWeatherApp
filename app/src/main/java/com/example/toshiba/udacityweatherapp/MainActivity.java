package com.example.toshiba.udacityweatherapp;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

import com.example.toshiba.udacityweatherapp.utilities.JsonUtils;
import com.example.toshiba.udacityweatherapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherDataAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<String[]> {

    private static final int LOADER_NUM = 11;
    private static final String URL_EXTRA = "Url_Extra";

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
        String urlToString = url.toString();

        Bundle bundle = new Bundle();
        bundle.putString(URL_EXTRA, urlToString);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String[]> loader = loaderManager.getLoader(LOADER_NUM);

        if (loader == null) {
            loaderManager.initLoader(LOADER_NUM, bundle, this);
        } else {
            loaderManager.restartLoader(LOADER_NUM, bundle, this);
        }
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
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, weatherAtPosition);
        startActivity(intent);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] weatherData = null;

            @Override
            protected void onStartLoading() {
                if (args == null)
                    return;

                if (weatherData != null) {
                    deliverResult(weatherData);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {
                String getUrl = args.getString(URL_EXTRA);

                try {
                    URL url = new URL(getUrl);
                    String getJson = NetworkUtils.getHttpResponse(url);
                    return JsonUtils.getRequiredData(getJson);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String[] data) {
                weatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        progressBar.setVisibility(View.INVISIBLE);

        if (data != null) {
            dataAdapter.setWeatherData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {
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
