package com.example.jsonparsing;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    List<Dao> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        JSONObject postdetails = new JSONObject();
        try {
            postdetails.put("user_id", "870");
            postdetails.put("limit", "20");
            postdetails.put("offset", "0");


            //Log.v("TAG", "response" + postdetails);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (postdetails.length() > 0) {

            if(isConnectingToInternet(MainActivity.this)) {

                new getJsonDatafromServer().execute(String.valueOf(postdetails));
            }
            else {
                Toast.makeText(MainActivity.this, "Please Connect to Internet and try again ", Toast.LENGTH_SHORT).show();
            }

        }

    }



    class getJsonDatafromServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // List<OwnergetObject> mListView = new ArrayList<OwnergetObject>();
            String JsonResponse = null;
            String JsonDATA = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://kuurvtrackerapp.com/mobile/devicelist?user_id=870&limit=20&offset=0");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("token", "4322308d8081b4d3f9b4641e026e9abe");


                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                writer.write(JsonDATA);

                writer.close();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputline;
                while ((inputline = reader.readLine()) != null)
                    buffer.append(inputline).append("\n");
                if (buffer.length() == 0) {
                    return null;
                }


                JsonResponse = buffer.toString();


                // //Log.v("TAG", JsonResponse);
                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        //Log.v("TAG", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            Log.v("TAG","Response Is " + JsonResponse);
            if(JsonResponse ==null){
                Toast.makeText(MainActivity.this, "No internet availability", Toast.LENGTH_SHORT).show();
                // finish();
            }
            else {
                try {

                    JSONObject jsonObject = new JSONObject(JsonResponse);
                    JSONArray jsonArray =  jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject insideObject = jsonArray.getJSONObject(i);

                        String name = insideObject.getString("device_name");
                        String device_image_path = insideObject.getString("device_image_path");


                        Dao list=new Dao();
                        list.setName(name);
                        list.setImage(device_image_path);

                        mList.add(i,list);
                        recyclerView=findViewById(R.id.recycle);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        RecyclerAdapter adapter = new RecyclerAdapter(MainActivity.this, mList);


                        recyclerView.setAdapter(adapter);



                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }



        }


    }
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }



    }

