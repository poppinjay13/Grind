package com.poppinjay13.grind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    void prefConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void writeName(String name){
        editor.putString(context.getString(R.string.pref_username),name);
        editor.apply();
    }

    String readName(){
        return sharedPreferences.getString(context.getString(R.string.pref_username),"Stranger");
    }
}
