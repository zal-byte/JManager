package com.zadev.jmanage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Client.Client;
import Model.User;
import SharedPref.UserSession;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class LoginActivity extends AppCompatActivity {
    UserSession userSession;
    Client client;

    //Widget
    TextInputEditText PUsername, PPassword;
    Button btn_login;
    ImageView bgImg;
    //end of Widget

    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Initialize
        userSession = new UserSession(LoginActivity.this);
        client = new Client();
        //end of Initialize

        init();
        logic();
    }

    void init() {
        btn_login = findViewById(R.id.btn_login);
        PUsername = findViewById(R.id.PUsername);
        PPassword = findViewById(R.id.PPassword);
        bgImg = findViewById(R.id.bgImg);
    }

    @SuppressLint("StaticFieldLeak")
    void logic() {
        Picasso.get().load(R.drawable.bg1).fit().into(bgImg);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, String> param = new HashMap<>();
                if (PUsername.getText().toString().length() <= 0) {
                    PUsername.setError("Masukan Username Dengan Benar");
                } else {
                    if (PPassword.getText().toString().length() <= 0) {
                        PPassword.setError("Masukan Password Dengan Benar");
                    } else {
                        param.put("PUsername", PUsername.getText().toString());
                        param.put("PPassword", PPassword.getText().toString());
                        param.put("Request", "login");

                        new AsyncTask<Void, Void, String>() {
                            ProgressDialog loading;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(LoginActivity.this, "Sedang login", "Silahkan tunggu", false, false);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                loading.dismiss();
                                String data = "Path : " + client.path + "\nURL : " + client.url + "\nLOGIN : " + client.login + "\nResponse : " + s;
                                Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
                                if (s != null) {
                                    if (!s.isEmpty()) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject obj = jsonArray.getJSONObject(i);
                                                if (obj.getBoolean("status")) {
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

                                                    //setting session
                                                    userSession.setLoginSession(obj.getString("PUsername"), obj.getString("PAddress"), obj.getString("PPhone"), obj.getString("PEmail"), obj.getString("PProfilePicture"), obj.getString("PName"));
                                                    if (userSession.sharedPreferences.getBoolean(userSession.isLogin, false)) {
                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                        LoginActivity.this.finish();
                                                    } else {
                                                        Snackbar.make(getWindow().getDecorView().getRootView(), "Couldn't login", Snackbar.LENGTH_LONG).show();
                                                    }
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
                                String res = null;
                                try {
                                    res = client.POST(client.login, param);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return res;
                            }
                        }.execute();
                    }
                }
            }
        });

    }
}