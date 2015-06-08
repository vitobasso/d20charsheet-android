package com.vitobasso.d20charsheet.util.app;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Victor on 16/05/2015.
 */
public class AppPreferences {

    public static final String PREF = "d20charsheet";
    public static final String PREF_LAST_OPENED_CHAR = "last_opened_character";
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

    public long getLastOpenedCharId() {
        return pref.getLong(PREF_LAST_OPENED_CHAR, 0);
    }

    public void clearLastOpenedChar() {
        setLastOpenedCharId(0);
    }

    public void setLastOpenedCharId(long charId) {
        pref.edit().putLong(PREF_LAST_OPENED_CHAR, charId).apply();
    }

    public boolean isLastOpenedCharKnown() {
        return getLastOpenedCharId() != 0;
    }

}
