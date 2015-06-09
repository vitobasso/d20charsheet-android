package com.vitobasso.d20charsheet.util.app;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Victor on 16/05/2015.
 */
public class AppPreferences {

    private static final String PREF = "d20charsheet";
    private static final String PREF_LAST_OPENED_CHAR = "last_opened_character";
    private static final String PREF_FIRST_RUN = "first_run";
    private static final String PREF_IMPORT_RULES_URL = "import_rules_url";

    private static final String DEFAULT_IMPORT_RULES_URL = "https://api.github.com/repos/vitobasso/dnd3.5-data/tarball/";

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

    public String getImportRulesUrl() {
        return pref.getString(PREF_IMPORT_RULES_URL, DEFAULT_IMPORT_RULES_URL);
    }

    public void setImportRuleUrl(String url) {
        pref.edit().putString(PREF_IMPORT_RULES_URL, url).apply();
    }

}
