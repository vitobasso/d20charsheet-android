package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.LoadingActivity;
import com.vituel.dndplayer.activity.abstraction.MainNavigationActvity;
import com.vituel.dndplayer.activity.abstraction.PagerActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.character.CharSummary;

import java.util.List;

import static com.vituel.dndplayer.activity.abstraction.MainNavigationActvity.NavigationItem.EDIT;
import static com.vituel.dndplayer.activity.abstraction.MainNavigationActvity.NavigationItem.OPEN;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.PREF;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_FIRST_RUN;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_OPENED_CHARACTER;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_LOAD;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

public class SummaryActivity extends MainNavigationActvity implements PagerActivity<CharSummary> {

    private CharSummary charSummary;

    private ViewPager pager;

    @Override
    protected int getContentLayout() {
        return R.layout.summary;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SummaryPagerAdapter(getSupportFragmentManager(), this));
        pager.setCurrentItem(SummaryPagerAdapter.PAGE_BASIC);
    }

    @Override
    protected void onStart() {
        CharBase openedChar = cache.getOpenedChar();
        if (openedChar != null) {
            open(openedChar);
            super.onStart();
        } else {
            super.onStart();
            loadContentOrOpenChar();
        }
    }

    @Override
    protected void navigateTo(NavigationItem nextActivity) {
        switch (nextActivity) {
            case EDIT:
                navigateToEditChar();
                break;
            case BOOKS:
                navigateToBooks();
                break;
            case OPEN:
                navigateToOpenChar();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHAR:
                switch (resultCode) {
                    case RESULT_OK:
                        CharBase charToOpen = (CharBase) data.getSerializableExtra(EXTRA_CHAR);
                        open(charToOpen);
                }
                break;
            case REQUEST_LOAD:
                pref.edit().putBoolean(PREF_FIRST_RUN, false).apply();
        }
    }

    //TODO move to separate startup activity
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

    private void selectOrCreateCharacter(CharDao dataSource) {
        List<CharBase> list = dataSource.listAll();
        if (!list.isEmpty()) {
            //select a character
            navigateTo(OPEN);
        } else {
            navigateTo(EDIT);
        }
    }

    @SuppressWarnings("unchecked")
    private void open(CharBase base) {
        this.charSummary = new CharSummary(this, base); //TODO replace by a "re-calculate" so the reference doesn't change
        pref.edit().putLong(PREF_OPENED_CHARACTER, charSummary.getBase().getId()).apply();
        cache.setOpenedChar(base);
        refreshUI();
    }

    public void refreshUI() {
        charSummary = new CharSummary(this, charSummary.getBase()); //TODO replace by a "re-calculate" so the reference doesn't change
        setActionbarTitle(this, BOLD_FONT, charSummary.getBase().getName());
        setupRightDrawer(new ConditionAdapter(this));

        //update fragments (only the ones already loaded)
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment frag : fragments) {
                if (frag instanceof PagerFragment && ((PagerFragment) frag).isReadyToPopulate()) {
                    ((PagerFragment<CharSummary, ?>) frag).refresh();
                }
            }
        }

    }

    @Override
    public CharSummary getData() {
        return charSummary;
    }
}
