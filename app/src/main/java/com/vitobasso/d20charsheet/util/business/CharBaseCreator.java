package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.model.character.CharBase;

/**
 * Created by Victor on 13/06/2015.
 */
public class CharBaseCreator {

    private Context context;

    public CharBaseCreator(Context context) {
        this.context = context;
    }

    public CharBase createNew() {
        CharBase base = new CharBase();
        base.setHitPoints(1);
        base.setStrength(10);
        base.setDexterity(10);
        base.setConstitution(10);
        base.setIntelligence(10);
        base.setWisdom(10);
        base.setCharisma(10);
        base.setGender("M");
        base.setTendencyLoyality("NEUTRAL");
        base.setTendencyMoral("NEUTRAL");
        base.setStandardAbilityMods();
        base.setActiveBooks(BookDao.getDefaultActiveBooks(context));
        return base;
    }

}
