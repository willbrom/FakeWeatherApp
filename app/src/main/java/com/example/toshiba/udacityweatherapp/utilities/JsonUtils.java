package com.example.toshiba.udacityweatherapp.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
