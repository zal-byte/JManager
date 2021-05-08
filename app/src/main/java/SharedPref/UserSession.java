package SharedPref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    public String pref_name = "pref_name";
    public Activity activity;
    public String PUsername = "PUsername", PAddress = "PAddress", PPhone = "PPhone", PEmail = "PEmail", PProfilePicture = "PProfilePicture", PName = "PName";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public String isLogin = "isLogin";

    public UserSession(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginSession(String PUsername, String PAddress, String PPhone, String PEmail, String PProfilePicture, String PName) {
        editor.putBoolean(isLogin, true);
        editor.putString(this.PUsername, PUsername);
        editor.putString(this.PAddress, PAddress);
        editor.putString(this.PPhone, PPhone);
        editor.putString(this.PEmail, PEmail);
        editor.putString(this.PProfilePicture, PProfilePicture);
        editor.putString(this.PName, PName);
        editor.apply();
    }

    public void delLoginSession() {
        editor.putBoolean(isLogin, false);
        editor.putString(this.PUsername, "");
        editor.putString(this.PAddress, "");
        editor.putString(this.PPhone, "");
        editor.putString(this.PEmail, "");
        editor.putString(this.PProfilePicture, "");
        editor.putString(this.PName, "");
        editor.apply();
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean(isLogin, false);
    }

    public String getPUsername() {
        return sharedPreferences.getString(PUsername, "");
    }

    public String getPAddress() {
        return sharedPreferences.getString(PAddress, "");
    }

    public String getPPhone() {
        return sharedPreferences.getString(PPhone, "");
    }

    public String getPEmail() {
        return sharedPreferences.getString(PEmail, "");
    }

    public String getPProfilePicture() {
        return sharedPreferences.getString(PProfilePicture, "");
    }

    public String getPName() {
        return sharedPreferences.getString(PName, "");
    }
}
