package com.vituel.dndplayer.activity.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.MainNavigationActvity;
import com.vituel.dndplayer.activity.abstraction.PagerActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.character.CharSummary;

import java.util.List;

import static com.vituel.dndplayer.activity.summary.SummaryPagerAdapter.PAGE_BASIC;
import static com.vituel.dndplayer.activity.summary.SummaryPagerAdapter.PAGE_SKILLS;
import static com.vituel.dndplayer.activity.summary.SummaryPagerAdapter.PAGE_TRAITS;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CHAR;
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
        pager.setCurrentItem(PAGE_BASIC);
    }

    @Override
    protected void onStart() {
        open(cache.getOpenedChar());
        super.onStart();
    }

    @Override
    protected void navigateTo(NavigationItem nextActivity) {
        switch (nextActivity) {
            case EDIT:
                int page = getRelatedEditPage();
                goToEditChar(page);
                break;
            case BOOKS:
                goToBooks();
                break;
            case OPEN:
                goToOpenChar();
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
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() != PAGE_BASIC) {
            pager.setCurrentItem(PAGE_BASIC);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("unchecked")
    private void open(CharBase base) {
        this.charSummary = new CharSummary(this, base); //TODO replace by a "re-calculate" so the reference doesn't change
        setActionbarTitle(this, BOLD_FONT, charSummary.getBase().getName());
        updateConditionsDrawer();
        updateFragments();
    }

    private void updateConditionsDrawer() {
        if (charSummary.getReferencedConditions().isEmpty()) {
            disableRightDrawer();
        } else {
            enableRightDrawer(new ConditionAdapter(this));
        }
    }

    private void updateFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment frag : fragments) {
                if (frag instanceof PagerFragment && ((PagerFragment) frag).isReadyToPopulate()) {
                    ((PagerFragment<CharSummary, ?>) frag).refresh();
                }
            }
        }
    }

    private int getRelatedEditPage() {
        int summaryPage = pager.getCurrentItem();
        return getRelatedEditPage(summaryPage);
    }

    private int getRelatedEditPage(int summaryPage) {
        switch (summaryPage) {
            case PAGE_SKILLS:
                return EditCharPagerAdapter.PAGE_SKILLS;
            case PAGE_TRAITS:
                return EditCharPagerAdapter.PAGE_FEATS;
            default:
                return EditCharPagerAdapter.PAGE_BASIC;
        }
    }

    public void refreshUI() {
        open(charSummary.getBase());
    }

    @Override
    public CharSummary getData() {
        return charSummary;
    }
}
