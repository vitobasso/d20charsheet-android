package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.ModifierTarget;

/**
 * Created by Victor on 29/09/2014.
 */
public class ModifierTargetStringConverter extends AbstractEnumStringConverter<ModifierTarget> {

    public ModifierTargetStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(ModifierTarget type) {
        switch (type) {
            case HP:
                return findResource(R.string.hp);
            case STR:
                return findResource(R.string.str);
            case DEX:
                return findResource(R.string.dex);
            case CON:
                return findResource(R.string.con);
            case INT:
                return findResource(R.string.int_ability);
            case WIS:
                return findResource(R.string.wis);
            case CHA:
                return findResource(R.string.cha);
            case AC:
                return findResource(R.string.ac);
            case SAVES:
                return findResource(R.string.saves);
            case FORT:
                return findResource(R.string.fort);
            case REFL:
                return findResource(R.string.refl);
            case WILL:
                return findResource(R.string.will);
            case HIT:
                return findResource(R.string.atk_short);
            case DAMAGE:
                return findResource(R.string.damage);
            case DAMAGE_MULT:
                return findResource(R.string.damage_mult);
            case CRIT_RANGE:
                return findResource(R.string.crit_range);
            case CRIT_MULT:
                return findResource(R.string.crit_mult);
            case SKILL:
                return findResource(R.string.skill);
            case SIZE:
                return findResource(R.string.size);
            case SPEED:
                return findResource(R.string.speed);
            case SPEED_MULT:
                return findResource(R.string.speed_mult);
            case INIT:
                return findResource(R.string.init);
            case CONCEAL:
                return findResource(R.string.concealment);
            case DR:
                return findResource(R.string.dr);
            case DR_TYPE:
                return findResource(R.string.dr_type);
            case MR:
                return findResource(R.string.sr);
            case IMMUNE:
                return findResource(R.string.immune);
            case MAX_DEX:
                return findResource(R.string.max_dex);
            default:
                return super.toString(type);
        }
    }

}
