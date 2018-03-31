package com.example.armangupta.climateapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arman Gupta on 26-03-2018.
 */

public class WeatherData {

   static private double temperature;
   static private String city;
   static private int cond;

    public static WeatherData parsingData(JSONObject response){
        WeatherData weatherData=new WeatherData();
        try {
            weatherData.temperature = response.getJSONObject("main").getDouble("temp") - 273.15;
            weatherData.city = response.getString("name");
            weatherData.cond = response.getInt("cod");
            Log.d("clima", "parsingData: Temp" + temperature + "city=" + city + "condi=" + cond);
        }
        catch (JSONException e)
        {
        }
        return  weatherData;
    }

    public static double getTemperature() {
        return temperature;
    }

    public static String getCity() {
        return city;
    }
}
