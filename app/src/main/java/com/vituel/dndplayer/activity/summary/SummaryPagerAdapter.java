package com.vituel.dndplayer.activity.summary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vituel.dndplayer.R;

import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFont;

/**
 * Created by Victor on 28/02/14.
 */
public class SummaryPagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_TEMP_EFFECTS = 0;
    public static final int PAGE_BASIC = 1;
    public static final int PAGE_SKILLS = 2;
    public static final int PAGE_TRAITS = 3;

    private Context ctx;

    public SummaryPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_BASIC:
                return new SummaryMainFragment();
            case PAGE_SKILLS:
                return new SummarySkillsFragment();
            case PAGE_TRAITS:
                return new SummaryTraitsFragment();
            case PAGE_TEMP_EFFECTS:
                return new SummaryTempEffectsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int resId;
        switch (position) {
            case PAGE_BASIC:
                resId = R.string.page_summary;
                break;
            case PAGE_SKILLS:
                resId = R.string.skills;
                break;
            case PAGE_TRAITS:
                resId = R.string.traits;
                break;
            case PAGE_TEMP_EFFECTS:
                resId = R.string.summary_temp;
                break;
            default:
                return super.getPageTitle(position);
        }
        return setFont(ctx, MAIN_FONT, ctx.getString(resId));
    }

    @Override
    public int getCount() {
        return 4;
    }

    public boolean shouldShowConditionsGui(int position){
        return position == PAGE_BASIC || position == PAGE_SKILLS;
    }

}
