package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.LoadingActivity;
import com.vituel.dndplayer.activity.abstraction.PagerActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.select.SelectCharActivity;
import com.vituel.dndplayer.dao.CharDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.character.CharSummary;
import com.vituel.dndplayer.util.ActivityUtil;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.PREF;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_FIRST_RUN;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_OPENED_CHARACTER;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_LOAD;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

public class SummaryActivity extends FragmentActivity implements PagerActivity<CharSummary> {

    private CharSummary charSummary;

    private ViewPager pager;
    private ConditionGuiManager conditionsGuiManager;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        pref = getSharedPreferences(PREF, MODE_PRIVATE);
        conditionsGuiManager = new ConditionGuiManager(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SummaryPagerAdapter(getSupportFragmentManager(), this));
        pager.setCurrentItem(SummaryPagerAdapter.PAGE_BASIC);
        pager.setOnPageChangeListener(new SummaryPagerListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadContentOrOpenChar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT:
                switch (resultCode) {
                    case RESULT_OK:

                        //open selected character
                        CharBase selectedChar = (CharBase) data.getSerializableExtra(EXTRA_SELECTED);
                        open(selectedChar);
                }
                break;
            case REQUEST_EDIT:
                switch (resultCode) {
                    case RESULT_OK:

                        //open edited/created character
                        CharBase editedChar = (CharBase) data.getSerializableExtra(EXTRA_EDITED);
                        open(editedChar);
                }
                break;
            case REQUEST_LOAD:
                pref.edit().putBoolean(PREF_FIRST_RUN, false).apply();
                findCharacterToOpen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:

                //select another character
                Intent intent = new Intent(this, SelectCharActivity.class);
                startActivityForResult(intent, REQUEST_SELECT);

                return true;

            default:
                return ActivityUtil.defaultOnOptionsItemSelected(item, this);
        }
    }

    public void loadContentOrOpenChar() {
        if (pref.getBoolean(PREF_FIRST_RUN, true)) {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivityForResult(intent, REQUEST_LOAD);
        } else {
            findCharacterToOpen();
        }
    }

    /**
     * Called by SummaryMainFragment after it's done creating views
     */
    public void findCharacterToOpen() {
        CharDao dataSource = new CharDao(this);

        long lastOpenedCharId = pref.getLong(PREF_OPENED_CHARACTER, 0);
        if (lastOpenedCharId != 0) {
            CharBase lastOpenedChar = dataSource.findById(lastOpenedCharId);
            if (lastOpenedChar != null) {
                //open character
                open(lastOpenedChar);
            } else {
                pref = getSharedPreferences(PREF, MODE_PRIVATE);
                pref.edit().putLong(PREF_OPENED_CHARACTER, 0).apply();
                selectOrCreateCharacter(dataSource);
            }
        } else {
            selectOrCreateCharacter(dataSource);
        }

        dataSource.close();
    }

    @SuppressWarnings("unchecked")
    private void open(CharBase base) {
        this.charSummary = new CharSummary(this, base); //TODO replace by a "re-calculate" so the reference doesn't change
        pref.edit().putLong(PREF_OPENED_CHARACTER, charSummary.getBase().getId()).apply();
        refreshUI();
    }

    public void refreshUI() {
        charSummary = new CharSummary(this, charSummary.getBase()); //TODO replace by a "re-calculate" so the reference doesn't change
        setActionbarTitle(this, BOLD_FONT, charSummary.getBase().getName());
        conditionsGuiManager.populate(charSummary);

        //update fragments (only the ones already loaded)
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment frag : fragments) {
                if (frag instanceof PagerFragment && ((PagerFragment) frag).isReadyToPopulate()) {
                    ((PagerFragment<CharSummary, ?>) frag).update();
                }
            }
        }

    }

    public void showOrHideConditionsGui() {
        SummaryPagerAdapter adapter = (SummaryPagerAdapter) pager.getAdapter();
        boolean showConditions = adapter.shouldShowConditionsGui(pager.getCurrentItem());
        conditionsGuiManager.setVisibility(showConditions);
    }

    private void selectOrCreateCharacter(CharDao dataSource) {
        List<CharBase> list = dataSource.listAll();
        if (!list.isEmpty()) {
            //select a character
            Intent intent = new Intent(this, SelectCharActivity.class);
            startActivityForResult(intent, REQUEST_SELECT);
        } else {
            //create new character
            Intent intent = new Intent(this, EditCharActivity.class);
            startActivityForResult(intent, REQUEST_EDIT);
        }
    }

    @Override
    public CharSummary getData() {
        return charSummary;
    }
}
