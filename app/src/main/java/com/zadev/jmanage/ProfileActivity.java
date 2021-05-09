package com.zadev.jmanage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import Client.Client;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    Client client;
    ArrayList<User> users = new ArrayList<>();


    //Widget
    TextView PName, PAddress, PPhone, PEmail;
    ImageButton PAddress_edit, PPhone_edit, PEmail_edit;
    CircleImageView PProfilePicture;

    //end of Widget

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public String session_name = "session_name";
    String session = "session";
    public Dialog dialog;
    TextInputEditText profile_edit_value;
    Button profile_btn_save;
    HashMap<String, String> param = new HashMap<>();
    ArrayList<String> profile_menu = new ArrayList<>();
    AlertDialog.Builder profile_dialog;


    Dialog lihat_d;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = new Client();
        sharedPreferences = this.getSharedPreferences(session_name, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        profile_dialog = new AlertDialog.Builder(ProfileActivity.this);
        dialog = new Dialog(ProfileActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.profile_edit_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        logic();
        if (getIntent().getStringExtra("who") != null) {
            if (getIntent().getStringExtra("PUsername") != null) {
                if (Objects.equals(getIntent().getStringExtra("who"), "me")) {
                    profile_menu.add("Lihat");
                    profile_menu.add("Unggah gambar");
                } else if (Objects.equals(getIntent().getStringExtra("who"), "people")) {
                    profile_menu.add("Lihat");
                    PAddress_edit.setVisibility(View.GONE);
                    PPhone_edit.setVisibility(View.GONE);
                    PEmail_edit.setVisibility(View.GONE);
                }
            }
        }
    }

    void init() {
        PProfilePicture = findViewById(R.id.PProfilePicture);
        PName = findViewById(R.id.PName);
        PAddress = findViewById(R.id.PAddress);
        PPhone = findViewById(R.id.PPhone);
        PEmail = findViewById(R.id.PEmail);

        PAddress_edit = findViewById(R.id.PAddress_edit);
        PPhone_edit = findViewById(R.id.PPhone_edit);
        PEmail_edit = findViewById(R.id.PEmail_edit);
    }

    void logic() {
        fetchData();
        if (Objects.equals(getIntent().getStringExtra("who"), "me")) {
            profile_edit_value = dialog.findViewById(R.id.profile_edit_value);
            profile_btn_save = dialog.findViewById(R.id.profile_btn_save);
            profile_btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (getSession()) {
                        case "PAddress_edit":
                            param.put("Name", "PAddress");
                            param.put("Value", Objects.requireNonNull(profile_edit_value.getText()).toString());
                            dialog.dismiss();
                            //Save Profile Data
                            saveProfileData();
                            break;
                        case "PEmail_edit":
                            param.put("Name", "PEmail");
                            param.put("Value", Objects.requireNonNull(profile_edit_value.getText()).toString());
                            dialog.dismiss();
                            //Save Profile Data
                            saveProfileData();
                            break;
                        case "PPhone_edit":
                            param.put("Name", "PPhone");
                            param.put("Value", Objects.requireNonNull(profile_edit_value.getText()).toString());
                            dialog.dismiss();
                            //Save Profile Data
                            saveProfileData();
                            break;
                    }
                    profile_edit_value.setText("");
                }
            });
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, profile_menu);
        profile_dialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (profile_menu.get(which).equals("Lihat")) {
                    lihat_d = new Dialog(ProfileActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    ImageView img = new ImageView(ProfileActivity.this);
                    Picasso.get().load(client.imgProfSrc + users.get(0).PProfilePicture).into(img);
                    lihat_d.setContentView(img);
                    lihat_d.show();
                } else if (profile_menu.get(which).equals("Unggah gambar")) {
                    setSession("PProfilePicture_edit");
                    CropImage.activity().start(ProfileActivity.this);
                }
            }
        });
        PProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                Uri imgUri = result.getUri();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Picasso.get().load(imgUri).into(PProfilePicture);
                    param.put("Name", "ImageBase64");
                    param.put("Value", byteTobase(bitmap));

                    saveProfileData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String byteTobase(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] res = baos.toByteArray();

            return String.valueOf(Base64.encodeToString(res, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    String getSession() {
        return sharedPreferences.getString(session, "");
    }

    void setSession(String value) {
        editor.putString(session, value);
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    public void saveProfileData() {
        users.clear();
        param.put("Request", "updateProfileData");
        param.put("PUsername", getIntent().getStringExtra("PUsername"));
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                ParseUpdatedProfileData(result);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res;
                res = client.POST(client.profile, param);
                return res;
            }
        }.execute();
    }

    void ParseUpdatedProfileData(String result) {
        try {
            if (result != null) {
                if (!result.equals("")) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("updateProfileData");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.getBoolean("status")) {
                            Snack(obj.getString("message"));
                            fetchData();
                        } else {
                            Snack(obj.getString("message"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Snack(String value) {
        Snackbar.make(getWindow().getDecorView().getRootView(), value, Snackbar.LENGTH_LONG).show();
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
                parseData(result);
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res;
                res = client.GET(client.profile + "?Request=fetchProfileData&PUsername=" + getIntent().getStringExtra("PUsername"));
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
                    PEmail_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSession("PEmail_edit");
                            profile_edit_value.setText(users.get(0).PEmail);
                            dialog.show();
                        }
                    });
                    PPhone_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSession("PPhone_edit");
                            profile_edit_value.setText(users.get(0).PPhone);
                            dialog.show();
                        }
                    });
                    PAddress_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSession("PAddress_edit");
                            profile_edit_value.setText(users.get(0).PAddress);
                            dialog.show();
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setData() {
        Picasso.get().load(client.imgProfSrc + users.get(0).PProfilePicture).into(PProfilePicture);
        PName.setText(users.get(0).PName);
        PAddress.setText(users.get(0).PAddress);
        PPhone.setText(users.get(0).PPhone);
        PEmail.setText(users.get(0).PEmail);
        MainActivity.fetchData();
    }

}
