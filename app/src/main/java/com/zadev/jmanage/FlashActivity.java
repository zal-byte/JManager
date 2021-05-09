package com.zadev.jmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import SharedPref.UserSession;

public class FlashActivity extends AppCompatActivity {

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        userSession = new UserSession(FlashActivity.this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (userSession.getIsLogin()) {
                    startActivity(new Intent(FlashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(FlashActivity.this, LoginActivity.class));
                }
                FlashActivity.this.finish();
            }
        }, 1000);
    }
}