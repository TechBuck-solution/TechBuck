package com.example.dev.techbuck;


import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    public Session(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("techbuck", 0);
        editor = preferences.edit();}

    public void setLogin(String unm) {

        editor.putString("unm",unm).commit();

    }



    public String checkLogin() {
        String unm=preferences.getString("unm","");
        return unm;


    }

    public void logOut() {
        editor.clear().commit();
    }

}



