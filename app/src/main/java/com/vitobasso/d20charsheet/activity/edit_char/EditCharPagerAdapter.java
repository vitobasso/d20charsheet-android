package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vitobasso.d20charsheet.R;

import static com.vitobasso.d20charsheet.util.font.FontUtil.MAIN_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setFont;

/**
 * Created by Victor on 28/02/14.
 */
public class EditCharPagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_PERSONAL = 0;
    public static final int PAGE_BASIC = 1;
    public static final int PAGE_SKILLS = 2;
    public static final int PAGE_FEATS = 3;
    public static final int PAGE_EQUIP = 4;
    public static final int PAGE_ATTACKS = 5;
    public static final int PAGE_MODIFIERS = 6;

    private Context ctx;

    public EditCharPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_PERSONAL:
                return new EditCharPersonalFragment();
            case PAGE_BASIC:
                return new EditCharBasicFragment();
            case PAGE_SKILLS:
                return new EditCharSkillsFragment();
            case PAGE_FEATS:
                return new EditCharFeatsFragment();
            case PAGE_EQUIP:
                return new EditCharEquipFragment();
            case PAGE_ATTACKS:
                return new EditCharAttacksFragment();
            case PAGE_MODIFIERS:
                return new EditCharAbilityModsFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int resId;
        switch (position) {
            case PAGE_PERSONAL:
                resId = R.string.personality;
                break;
            case PAGE_BASIC:
                resId = R.string.basic;
                break;
            case PAGE_SKILLS:
                resId = R.string.trained_skills;
                break;
            case PAGE_FEATS:
                resId = R.string.feats;
                break;
            case PAGE_EQUIP:
                resId = R.string.equip;
                break;
            case PAGE_ATTACKS:
                resId = R.string.attacks;
                break;
            case PAGE_MODIFIERS:
                resId = R.string.bonuses;
                break;
            default:
                return super.getPageTitle(position);
        }
        return setFont(ctx, MAIN_FONT, ctx.getString(resId));
    }

    @Override
    public int getCount() {
        return 7;
    }
}
