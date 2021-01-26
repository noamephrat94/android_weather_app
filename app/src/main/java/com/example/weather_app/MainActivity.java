package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String OW_URL   ="https://api.openweathermap.org/data/2.5/weather?q=%s&appid=5deb1221d17adbfabebaaa1ae45ba036";
    public void check(View view){

        EditText cityEditText = (EditText)findViewById(R.id.editText);
        String cityname = cityEditText.getText().toString();

        WeatherTask task = new WeatherTask();
        task.execute(String.format(OW_URL, cityname));



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public class WeatherTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... params) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }
                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

            return "FAILED!!!";
        }

        @Override
        protected void onPostExecute(String json_row_data) {
            super.onPostExecute(json_row_data);

            try {
                String weatherRes = "";
                JSONObject json = new JSONObject(json_row_data);
                String weatherInfo = json.getString("weather");
                String windInfo = json.getString("wind");
                JSONArray json_arr = new JSONArray(weatherInfo);
                for (int i=0; i<json_arr.length(); i++){
                    JSONObject partObj = json_arr.getJSONObject(i);
                    String mainProp = partObj.getString("main");
                    String descProp = partObj.getString("description");

                    if (!mainProp.equals("") && !descProp.equals(""))
                        weatherRes += mainProp + " : " + descProp + "\r\n";
                }
                JSONObject jsonWind = new JSONObject(windInfo);
                String speedProp = jsonWind.getString("speed");
                weatherRes += "wind speed: " + speedProp;

                TextView resText = (TextView)findViewById(R.id.textViewResult);
                resText.setText(weatherRes);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}

