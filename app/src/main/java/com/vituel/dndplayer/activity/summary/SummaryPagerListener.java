package com.vituel.dndplayer.activity.summary;

import android.support.v4.view.ViewPager;

/**
 * Created by Victor on 10/09/14.
 */
public class SummaryPagerListener extends ViewPager.SimpleOnPageChangeListener {

    private SummaryActivity activity;

    public SummaryPagerListener(SummaryActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onPageSelected(int position) {
        activity.showOrHideConditionsGui();
    }

}
