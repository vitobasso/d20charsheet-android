package com.vituel.dndplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Victor on 16/05/2015.
 */
public class AppPreferences {

    public static final String PREF = "dndplayer";
    public static final String PREF_OPENED_CHARACTER = "opened_character";
    public static final String PREF_FIRST_RUN = "first_run";

    protected SharedPreferences pref;

    public AppPreferences(Context context) {
        this.pref = context.getSharedPreferences(PREF, MODE_PRIVATE);
    }

    public boolean isFirstRun() {
        return pref.getBoolean(PREF_FIRST_RUN, true);
    }

    public void setFirstRun(boolean isFirstRun) {
        pref.edit().putBoolean(PREF_FIRST_RUN, isFirstRun).apply();
    }

    public long getLastOpenedChar() {
        return pref.getLong(PREF_OPENED_CHARACTER, 0);
    }

    public void clearOpenedChar() {
        setOpenedChar(0);
    }

    public void setOpenedChar(long charId) {
        pref.edit().putLong(PREF_OPENED_CHARACTER, charId).apply();
    }

}
