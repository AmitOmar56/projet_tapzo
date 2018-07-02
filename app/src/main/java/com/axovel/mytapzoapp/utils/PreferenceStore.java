package com.axovel.mytapzoapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Harendra on 20-10-2016.
 * Axovel Private Limited
 */
public class PreferenceStore implements Constants {

    private static PreferenceStore preferenceStore = null;
    private static SharedPreferences preferences = null;

    public static PreferenceStore getPreference(Context context) {

//        if (preferences == null) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_MULTI_PROCESS);
//        }
        if (preferenceStore == null) {
            preferenceStore = new PreferenceStore();
        }

        return preferenceStore;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value).commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, true);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value).commit();
    }

    public void putSet(String key, Set<String> set) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, set).commit();
    }

    public Set<String> getSet(String key) {
        return preferences.getStringSet(key, null);

    }

    public int getInt(String key) {
        return preferences.getInt(key, -1);
    }

    public void flashWholePreferences() {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(KEY_USER_ID_PREF, -1);

        editor.commit();
    }
}
