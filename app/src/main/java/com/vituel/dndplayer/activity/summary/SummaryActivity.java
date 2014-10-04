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
import com.vituel.dndplayer.activity.SelectCharActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.abstraction.ParentActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.dao.CharDao;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.Character;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.util.AppUtil;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.PREF;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_OPENED_CHARACTER;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

public class SummaryActivity extends FragmentActivity implements ParentActivity<Character> {

    private Character character;

    private ViewPager pager;
    private ConditionGuiManager conditionsGuiManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        conditionsGuiManager = new ConditionGuiManager(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SummaryPagerAdapter(getSupportFragmentManager(), this));
        pager.setCurrentItem(SummaryPagerAdapter.PAGE_BASIC);
        pager.setOnPageChangeListener(new SummaryPagerListener(this));

        AppUtil.onLaunch(this);
        findCharacterToOpen();
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

    /**
     * Called by SummaryMainFragment after it's done creating views
     */
    public void findCharacterToOpen() {
        CharDao dataSource = new CharDao(this);

        SharedPreferences pref = getSharedPreferences(PREF, MODE_PRIVATE);
        long lastOpenedCharId = pref.getLong(PREF_OPENED_CHARACTER, 0);
        if (lastOpenedCharId != 0) {
            CharBase lastOpenedChar = dataSource.findById(lastOpenedCharId);
            if (lastOpenedChar != null) {
                //open character
                open(lastOpenedChar);
            } else {
                pref = getSharedPreferences(PREF, MODE_PRIVATE);
                pref.edit().putLong(PREF_OPENED_CHARACTER, 0).commit();
                selectOrCreateCharacter(dataSource);
            }
        } else {
            selectOrCreateCharacter(dataSource);
        }

        dataSource.close();
    }

    @SuppressWarnings("unchecked")
    private void open(CharBase base) {
        this.character = new Character(this, base);
        SharedPreferences pref = getSharedPreferences(PREF, MODE_PRIVATE);
        pref.edit().putLong(PREF_OPENED_CHARACTER, character.getBase().getId()).commit();
        refreshUI();
    }

    public void refreshUI() {
        character = new Character(this, character.getBase());
        setActionbarTitle(this, BOLD_FONT, character.getBase().getName());
        conditionsGuiManager.populate(character);

        //update fragments
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment frag : fragments) {
                if (frag instanceof PagerFragment && ((PagerFragment) frag).isReadyToPopulate()) {
                    ((PagerFragment<Character, ?>) frag).update();
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
    public Character getData() {
        return character;
    }
}
