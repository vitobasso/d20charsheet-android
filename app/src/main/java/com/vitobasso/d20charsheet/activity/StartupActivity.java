package com.vitobasso.d20charsheet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vitobasso.d20charsheet.activity.edit_char.EditCharActivity;
import com.vitobasso.d20charsheet.activity.select.SelectCharActivity;
import com.vitobasso.d20charsheet.activity.summary.SummaryActivity;
import com.vitobasso.d20charsheet.dao.entity.CharDao;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.app.AppGlobals;
import com.vitobasso.d20charsheet.util.app.AppPreferences;

import static com.vitobasso.d20charsheet.activity.edit_char.EditCharActivity.Mode.CREATE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_CHAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_MODE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CHAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_LOAD;

/**
 * Created by Victor on 10/05/2015.
 */
public class StartupActivity extends Activity {

    public static final String TAG = StartupActivity.class.getSimpleName();

    protected AppGlobals cache;
    protected AppPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = (AppGlobals) getApplicationContext();
        pref = new AppPreferences(this);

        if (pref.isFirstRun()) {
            goToImport();
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
                        pref.setFirstRun(false);
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

        long charId = pref.getLastOpenedCharId();
        if (charId != 0) {
            CharBase lastOpenedChar = dataSource.findById(charId);
            if (lastOpenedChar != null) {
                goToSummary(lastOpenedChar);
            } else {
                Log.w(TAG, "Char not found. Id=" + charId);
                pref.clearLastOpenedChar();
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

    private void goToImport() {
        Intent intent = new Intent(this, ImportRulesActivity.class);
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
        pref.setLastOpenedCharId(charToOpen.getId());
        cache.setOpenedChar(charToOpen);

        Intent intent = new Intent(this, SummaryActivity.class);
        startActivity(intent);
        finish();
    }

}
