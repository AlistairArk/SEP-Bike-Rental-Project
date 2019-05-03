package com.leedsride.rentalapp.LeedsRide;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;

public class SaveSharedPreference
{
    static final String PREF_USERNAME= "username";
    static final String PREF_PASSWORD= "password";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoginDetails(Context ctx, String username, String password)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASSWORD, password);
        editor.commit();
    }

    public static void clearLoginDetails(Context ctx)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    public static String getPrefUsername(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USERNAME, "");
    }

    public static String getPrefPassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USERNAME, "");
    }
}