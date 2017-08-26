package com.example.toshiba.udacityweatherapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private final static String BASE_URL = "https://andfun-weather.udacity.com/weather";
    private final static String PARAM_QUERY = "q";
    private final static String FORMAT_PARAM = "mode";
    private final static String UNITS_PARAM = "units";
    private final static String DAYS_PARAM = "cnt";
    private static final String format = "json";
    private static final String units = "metric";
    private final static int numDays = 14;

    public static URL getUrl(String queryString) {
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, queryString)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString() );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

}
