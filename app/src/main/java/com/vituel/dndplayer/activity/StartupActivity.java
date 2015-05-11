package com.vituel.dndplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.vituel.dndplayer.MemoryCache;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.select.SelectCharActivity;
import com.vituel.dndplayer.activity.summary.SummaryActivity;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.model.character.CharBase;

import static com.vituel.dndplayer.activity.edit_char.EditCharActivity.Mode.CREATE;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_MODE;
import static com.vituel.dndplayer.util.ActivityUtil.PREF;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_FIRST_RUN;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_OPENED_CHARACTER;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_LOAD;

/**
 * Created by Victor on 10/05/2015.
 */
public class StartupActivity extends Activity {

    public static final String TAG = StartupActivity.class.getSimpleName();

    protected MemoryCache cache;
    protected SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = (MemoryCache) getApplicationContext();
        pref = getSharedPreferences(PREF, MODE_PRIVATE);

        boolean isFirstRun = pref.getBoolean(PREF_FIRST_RUN, true);
        if (isFirstRun) {
            goToLoading();
        } else {
            findCharacterToOpen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case REQUEST_LOAD:
                        pref.edit().putBoolean(PREF_FIRST_RUN, false).apply();
                        findCharacterToOpen();
                        break;
                    case REQUEST_CHAR:
                        CharBase charToOpen = (CharBase) data.getSerializableExtra(EXTRA_CHAR);
                        goToSummary(charToOpen);
                        break;
                }
                break;
            case RESULT_CANCELED:
                finish();
                break;
        }
    }

    private void findCharacterToOpen() {
        CharDao dataSource = new CharDao(this);

        long charId = pref.getLong(PREF_OPENED_CHARACTER, 0);
        if (charId != 0) {
            CharBase lastOpenedChar = dataSource.findById(charId);
            if (lastOpenedChar != null) {
                goToSummary(lastOpenedChar);
            } else {
                Log.w(TAG, "Char not found. Id=" + charId);
                pref.edit().putLong(PREF_OPENED_CHARACTER, 0).apply();
                selectOrCreateCharacter(dataSource);
            }
        } else {
            selectOrCreateCharacter(dataSource);
        }

        dataSource.close();
    }

    private void selectOrCreateCharacter(CharDao dataSource) {
        long count = dataSource.count();
        if (count > 0) {
            goToSelectChar();
        } else {
            goToCreateChar();
        }
    }

    private void goToLoading() {
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivityForResult(intent, REQUEST_LOAD);
    }

    private void goToCreateChar() {
        Intent intent = new Intent(this, EditCharActivity.class);
        intent.putExtra(EXTRA_MODE, CREATE);
        startActivityForResult(intent, REQUEST_CHAR);
    }


    private void goToSelectChar() {
        Intent intent = new Intent(this, SelectCharActivity.class);
        startActivityForResult(intent, REQUEST_CHAR);
    }

    private void goToSummary(CharBase charToOpen) {
        pref.edit().putLong(PREF_OPENED_CHARACTER, charToOpen.getId()).apply();
        cache.setOpenedChar(charToOpen);

        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
        finish();
    }

}
