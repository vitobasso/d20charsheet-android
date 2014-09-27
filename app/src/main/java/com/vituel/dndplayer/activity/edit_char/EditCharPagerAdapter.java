package com.vituel.dndplayer.activity.edit_char;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.vituel.dnd_character_sheet.R;

import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFont;

/**
 * Created by Victor on 28/02/14.
 */
public class EditCharPagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_BASIC = 0;
    public static final int PAGE_FEATS = 1;
    public static final int PAGE_SKILLS = 2;
    public static final int PAGE_EQUIP = 3;
    public static final int PAGE_ATTACKS = 4;
    public static final int PAGE_PERSONAL = 5;

    private Context ctx;

    public EditCharPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_BASIC:
                return new EditCharBasicFragment();
            case PAGE_FEATS:
                return new EditCharFeatsFragment();
            case PAGE_SKILLS:
                return new EditCharSkillsFragment();
            case PAGE_EQUIP:
                return new EditCharEquipFragment();
            case PAGE_ATTACKS:
                return new EditCharAttacksFragment();
            case PAGE_PERSONAL:
                return new EditCharPersonalFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int resId;
        switch (position) {
            case PAGE_BASIC:
                resId = R.string.basic;
                break;
            case PAGE_FEATS:
                resId = R.string.feats;
                break;
            case PAGE_SKILLS:
                resId = R.string.trained_skills;
                break;
            case PAGE_EQUIP:
                resId = R.string.equip;
                break;
            case PAGE_ATTACKS:
                resId = R.string.attacks;
                break;
            case PAGE_PERSONAL:
                resId = R.string.personality;
                break;
            default:
                return super.getPageTitle(position);
        }
        return setFont(ctx, MAIN_FONT, ctx.getString(resId));
    }

    @Override
    public int getCount() {
        return 6;
    }
}
