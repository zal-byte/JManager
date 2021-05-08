package com.zadev.jmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import Client.Client;
import SharedPref.UserSession;

public class LoginActivity extends AppCompatActivity {
    UserSession userSession;
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize
        userSession = new UserSession(LoginActivity.this);
        client = new Client();
        //end of Initialize
    }
    void init(){

    }
    @SuppressLint("StaticFieldLeak")
    void logic(){

    }
}