package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titlesList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titlesList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
    }
    public void onNewsClick(View view){
        progressBar.setVisibility(View.VISIBLE);
        DownnloadNewsTask downnloadNewsTask = new DownnloadNewsTask();
        String yourapiKey = getString(R.string.API_KEY);
        downnloadNewsTask.execute("https://newsdata.io/api/1/news?apikey="+yourapiKey+"&q=cryptocurrency");
    }
    public class DownnloadNewsTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String results = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1){
                    char ch = (char) data;
                    results += ch;
                    data = inputStreamReader.read();
                }
                return results;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("result",s);
            //JSON PARSING CODE
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String results = jsonObject.getString("results");
                String totalResults = jsonObject.getString("totalResults");
                Log.i("result status", status);
                Log.i("result totalResults", totalResults);
                //Log.i("result results", results);
                JSONArray resultArray = new JSONArray(results);
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject jsonPart = resultArray.getJSONObject(i);
                    titlesList.add(jsonPart.getString("title"));
                    Log.i("result title",jsonPart.getString("title"));
                    Log.i("result image",jsonPart.getString("image_url"));
                }
                progressBar.setVisibility(View.INVISIBLE);
                callListActivity();
            } catch (JSONException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
            }
        }
    }

    private void callListActivity() {
        Intent intent = new Intent(MainActivity.this,NewsListActivity.class);
        intent.putExtra("data",titlesList);
        startActivity(intent);
    }
}