package com.zadev.jmanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import Client.Client;
import SharedPref.UserSession;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Toolbar main_toolbar;
    public static NavigationView main_nav_view;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout main_drawer;

    public UserSession userSession;
    public Client client;
    static Activity activity;


    CardView btn_card_see, btn_card_new, btn_card_payment_manage, btn_card_payment_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity = MainActivity.this;
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


        //main_dashboard.xml
        btn_card_see = findViewById(R.id.btn_card_see);
        btn_card_new = findViewById(R.id.btn_card_new);
        btn_card_payment_manage = findViewById(R.id.btn_card_payment_manage);
        btn_card_payment_request = findViewById(R.id.btn_card_payment_request);

        btn_card_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class).putExtra("what", "see"));
            }
        });
        btn_card_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class).putExtra("what", "new"));
            }
        });
        btn_card_payment_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PaymentActivity.class).putExtra("what", "manage"));
            }
        });
        btn_card_payment_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PaymentActivity.class).putExtra("what", "request"));
            }
        });
    }

    void logic() {
        fetchData();
    }

    @SuppressLint("StaticFieldLeak")
    public static void fetchData() {
        final Client client = new Client();
        final CircleImageView imageHeader = main_nav_view.getHeaderView(0).findViewById(R.id.PProfilePicture_nav);

        imageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(((MainActivity) activity), ProfileActivity.class).putExtra("who", "me").putExtra("PUsername", ((MainActivity) activity).userSession.sharedPreferences.getString(((MainActivity) activity).userSession.PUsername, "")));
            }
        });
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result != null) {
                    if (!result.isEmpty()) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("fetchProfileData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                if (obj.getBoolean("status") == true) {
                                    Picasso.get().load(client.imgProfSrc + obj.getString("PProfilePicture")).into(imageHeader);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res;
                res = client.GET(client.profile + "?Request=fetchProfileData&PUsername=" + ((MainActivity) activity).userSession.sharedPreferences.getString(((MainActivity) activity).userSession.PUsername, ""));
                return res;
            }
        }.execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("who", "me");
                intent.putExtra("PUsername", userSession.sharedPreferences.getString(userSession.PUsername, ""));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
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