package com.example.dragandquery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "com.example.dragandquery.UserSessionManager.PREFER_NAME";
    private static final String IS_LOGGED_IN = "com.example.dragandquery.UserSessionManager.IS_LOGGED_IN";
    public static final String KEY_NAME = "com.example.dragandquery.UserSessionManager.KEY_NAME";
    public static final String KEY_MAIL = "com.example.dragandquery.UserSessionManager.KEY_MAIL";

    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String mail){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MAIL, mail);
        editor.commit();
    }

    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){
            Intent i = new Intent(_context, Settings.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_MAIL, pref.getString(KEY_MAIL, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, Settings.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }
}
