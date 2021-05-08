package com.qiva.nakiriayame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import DayNightMode.DayNight;
import Services.MyCustomBroadcastReceiver;
import Services.RecordingService;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView main_navigationview;
    Toolbar toolbar;
    DrawerLayout main_drawer;
    ActionBarDrawerToggle toggle;
    SwitchCompat switchCompat;
    DayNight DN;


    GridLayout main_gridlayout;
    MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DN = new DayNight(MainActivity.this);
        if (DN.getChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        init();
        logic();
    }


    void init() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        main_navigationview = (NavigationView) findViewById(R.id.main_navigationview);
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(MainActivity.this, main_drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        main_drawer.setDrawerListener(toggle);
        toggle.syncState();
        switchCompat = new SwitchCompat(MainActivity.this);

        main_gridlayout = (GridLayout) findViewById(R.id.main_gridlayout);
    }

    void logic() {
        main_navigationview.setNavigationItemSelectedListener(this);
        setSingleEvent(main_gridlayout);
    }

    private void setSingleEvent(GridLayout layout) {
        for (int i = 0; i < main_gridlayout.getChildCount(); i++) {
            CardView cardView = (CardView) layout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (finalI) {
                        case 0:
                            Intent intent = new Intent(MainActivity.this, RecordingService.class);
                            startService(intent);
                            break;
                        case 1:
                            srcAppend();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    void srcAppend() {
        StringBuilder sb = new StringBuilder();
        sb.append("android.resource://");
        sb.append(getPackageName());
        sb.append("/");
        sb.append(R.raw.ayamenoise1);
        Uri uri = Uri.parse(sb.toString());
        cleanUpMediaPlayer();
        if (player == null) {
            player = MediaPlayer.create(MainActivity.this, uri);
        }
        player.start();

    }

    void cleanUpMediaPlayer() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }
        }
    }


    public void startOrStopService() {
        if (RecordingService.isRunning) {
            //Stop Service
            Intent intent = new Intent(this, RecordingService.class);
            intent.putExtra("TITLE", "HELLO");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            stopService(intent);
        } else {
            //Start service
            Intent intent = new Intent(this, RecordingService.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("TITLE",