package com.example.toshiba.udacityweatherapp.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TOSHIBA on 8/20/2017.
 */

public class JsonUtils {

    public static String[] getRequiredData(String rawJson) {

        String[] weatherData = null;

        final String LIST = "list";
        final String WEATHER = "weather";
        final String MAIN = "main";


        try {
            JSONObject jsonObject = new JSONObject(rawJson);
            JSONArray listArray = jsonObject.getJSONArray(LIST);
            weatherData = new String[listArray.length()];

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject listObject = listArray.getJSONObject(i);
                JSONArray weatherArray = listObject.getJSONArray(WEATHER);
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                String mainValue = weatherObject.getString(MAIN);
                weatherData[i] = mainValue;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherData;
    }

}
