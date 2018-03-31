package com.example.armangupta.climateapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {


    TextView text2,text1;Button btn;
    final String WEATHER_URL="http://api.openweathermap.org/data/2.5/weather/";
    final String APP_ID="f290b33c84b7a514a28cfc2a99b3e483";
    final int REQUEST_CODE=123;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         text1=(TextView)findViewById(R.id.text1);
         text2=(TextView)findViewById(R.id.text2);
         btn=(Button)findViewById(R.id.button1);
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(MainActivity.this,citycontroller.class);
                 startActivity(intent);
             }
         });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("clima", "onResume() called");
        Log.d("clima", "getting current location");
        Intent myIntent=getIntent();
        String city=myIntent.getStringExtra("city");
        if(city!=null)
        {
          RequestParams params=new RequestParams();
          params.put("q",city);
          params.put("appid",APP_ID);
          sendParamsToAPI(params);
        }
        else
        {
            getCurrentLocation();
        }
    }

    public void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("clima", "onLocationChanged() called");
                String lat=String.valueOf(location.getLatitude());
                String lang=String.valueOf(location.getLongitude());

                Log.d("clima", "onLocationChanged: latitude"+lat);
                Log.d("clima", "onLocationChanged: longitude"+lang);
                RequestParams params=new RequestParams();
                params.put("lat",lat);
                params.put("lon",lang);
                params.put("appid",APP_ID);
                sendParamsToAPI(params);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("clima", "provider is disabled");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1000, locationListener);

    }

    private void sendParamsToAPI(RequestParams params) {
        AsyncHttpClient client=new AsyncHttpClient();
         client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("clima", "onSuccess: " + response.toString());
                 WeatherData weatherData = WeatherData.parsingData(response);
                 updateAPI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("clima", "onFailure: ", throwable);
                Log.d("clima", "onFailure: " + statusCode);
                Toast.makeText(MainActivity.this, "Failed", LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Log.d("clima", "permission granted");
                getCurrentLocation();
            }
            else {
                Log.d("clima", "permission denied");
                finish();
            }
        }

    }

    public void updateAPI(WeatherData weatherData)
    {
        text1.setText("Temperature:"+weatherData.getTemperature());
        text2.setText("City:"+weatherData.getCity());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null)
        locationManager.removeUpdates(locationListener);
    }
}
