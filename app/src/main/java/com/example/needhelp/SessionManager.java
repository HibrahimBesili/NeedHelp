package com.example.needhelp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;
public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USERNAME = "USERNAME";
    public static final String Phone_Number = "Phone_Number";
    public static final String ID = "ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String username, String phone_number, String id){

        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, username);
        editor.putString(Phone_Number, phone_number);
        editor.putString(ID, id);
        editor.apply();

    }

    //public boolean isLoggin(){
       // return sharedPreferences.getBoolean(LOGIN, false);
    //}

   /*public void checkLogin(){

        if (!this.isLoggin()){
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            //((HomeActivity) context).finish();
        }
    }*/

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(Phone_Number, sharedPreferences.getString(Phone_Number, null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }


}
