package com.zadev.jmanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

import Client.Client;
import SharedPref.UserSession;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar main_toolbar;
    NavigationView main_nav_view;
    ActionBarDrawerToggle toggle;
    DrawerLayout main_drawer;

    UserSession userSession;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        userSession = new UserSession(MainActivity.this);
        client = new Client();
        //

        init();
        logic();
        setSupportActionBar(main_toolbar);
        toggle = new ActionBarDrawerToggle(this, main_drawer, main_toolbar, R.string.open, R.string.close);
        main_drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        main_nav_view.setNavigationItemSelectedListener(this);
    }

    void init() {
        main_toolbar = findViewById(R.id.main_toolbar);
        main_nav_view = findViewById(R.id.main_nav_view);
        main_drawer = findViewById(R.id.main_drawer);
    }

    void logic() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
            case R.id.profile:
            default:
                break;
        }
        return false;
    }

    public void logout() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        Button btn_logout_yes, btn_logout_no;
        btn_logout_yes = dialog.findViewById(R.id.btn_logout_yes);
        btn_logout_no = dialog.findViewById(R.id.btn_logout_no);

        btn_logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSession.delLoginSession();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
            }
        });
        btn_logout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}