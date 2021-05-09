package com.zadev.jmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Client.Client;
import Model.User;

public class ProfileActivity extends AppCompatActivity {
    Client client;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = new Client();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getIntent().getStringExtra("who") != null) {
            if (getIntent().getStringExtra("PUsername") != null) {
                if (Objects.equals(getIntent().getStringExtra("who"), "me")) {

                } else if (Objects.equals(getIntent().getStringExtra("who"), "people")) {

                }
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public void fetchData() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }

            @Override
            protected String doInBackground(Void... voids) {
                String res;
                res = client.GET(client.profile = "?Request=fetchProfileData&PUsername=" + getIntent().getStringExtra("PUsername"));
                return res;
            }
        }.execute();
    }

    public void parseData(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("fetchProfileData");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                User user = new User();
                user.setPName(obj.getString("PName"));
                user.setPEmail(obj.getString("PEmail"));
                user.setPUsername(obj.getString("PUsername"));
                user.setPAddress(obj.getString("PAddress"));
                user.setPPhone(obj.getString("PPhone"));
                user.setPID(obj.getString("PID"));
                user.setPProfilePicture(obj.getString("PProfilePicture"));
                user.setPRole(obj.getString("PRole"));
                users.add(user);
            }
            if (users != null) {
                if (users.size() > 0) {
                    setData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setData() {

    }

}
