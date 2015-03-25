package com.vituel.dndplayer.util.i18n;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.Condition;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;

/**
 * Created by Victor on 25/04/14.
 */
public class ModifierStringConverter {

    private Context ctx;
    private EnumI18n i18n;

    public ModifierStringConverter(Context ctx) {
        this.ctx = ctx;
        this.i18n = new EnumI18n(ctx);
    }

    public CharSequence getShortString(Modifier mod) {
        CharSequence target = getTargetShort(mod.getTarget(), mod.getVariation());
        CharSequence amount = getAmount(mod.getAmount(), mod.getTarget());
        CharSequence condition = getCondition(mod.getCondition());

        StringBuilder str = new StringBuilder();
        str.append(amount);
        str.append(" ");
        str.append(target);

        if (mod.getCondition() != null) {
            int start = str.length() + 1;

            str.append(" ");
            str.append(condition);

            SpannableString span = new SpannableString(str);
            StyleSpan style = new StyleSpan(Typeface.ITALIC);
            span.setSpan(style, start, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }

        return str;
    }

    public CharSequence getTargetShort(ModifierTarget target, String variation) {
        switch (target) {
            case HP:
                return getString(R.string.hp);
            case STR:
                return getString(R.string.str);
            case DEX:
                return getString(R.string.dex);
            case CON:
                return getString(R.string.con);
            case INT:
                return getString(R.string.int_ability);
            case WIS:
                return getString(R.string.wis);
            case CHA:
                return getString(R.string.cha);
            case AC:
                return getString(R.string.ac);
            case MAX_DEX:
                return getString(R.string.max_dex);
            case MR:
                return getString(R.string.sr);
            case DR:
                return getString(R.string.dr);
            case SAVES:
            case FORT:
            case REFL:
            case WILL:
            case SPEED:
            case SPEED_MULT:
            case INIT:
            case HIT:
            case DAMAGE:
            case DAMAGE_MULT:
            case CRIT_RANGE:
            case CRIT_MULT:
            case SIZE:
            case CONCEAL:
            case IMMUNE:
                return getTargetLong(target, variation);
            case SKILL:
                return variation;
            default:
                Log.w(getClass().getSimpleName(), "Couldn't convert string: " + target.toString());
                return target.toString();
        }
    }

    public CharSequence getTargetLong(ModifierTarget target, String variation) {
        switch (target) {
            case HP:
                return getString(R.string.hit_points);
            case STR:
                return getString(R.string.strength);
            case DEX:
                return getString(R.string.dexterity);
            case CON:
                return getString(R.string.constitution);
            case INT:
                return getString(R.string.intelligence);
            case WIS:
                return getString(R.string.wisdom);
            case CHA:
                return getString(R.string.char_class);
            case AC:
                return getString(R.string.armor_class);
            case SAVES:
                return getString(R.string.saves);
            case FORT:
                return getString(R.string.fort);
            case REFL:
                return getString(R.string.refl);
            case WILL:
                return getString(R.string.will);
            case SPEED:
            case SPEED_MULT:
                return getString(R.string.speed);
            case INIT:
                return getString(R.string.init);
            case HIT:
                return getString(R.string.atk_short);
            case DAMAGE:
            case DAMAGE_MULT:
                return getString(R.string.damage);
            case CRIT_RANGE:
            case CRIT_MULT:
                return getString(R.string.critical);
            case SIZE:
                return getString(R.string.size);
            case MAX_DEX:
                return getString(R.string.max_dexterity);
            case MR:
                return getString(R.string.spell_resistance);
            case DR:
                return getString(R.string.damage_reduction);
            case CONCEAL:
                return getString(R.string.conceal);
            case IMMUNE:
                return getString(R.string.immune);
            case SKILL:
                return variation;
            default:
                Log.w(getClass().getSimpleName(), "Couldn't convert string: " + target.toString());
                return target.toString();
        }
    }

    private CharSequence getAmount(DiceRoll amount, ModifierTarget target) {
        String str = amount.toString();
        switch (target) {
            case SPEED_MULT:
            case DAMAGE_MULT:
            case CRIT_MULT:
                if (str.charAt(0) == '-') {
                    str = str.substring(1, str.length());
                }
                str = "×" + str;
                break;
            case MAX_DEX:
                if (str.charAt(0) == '-') {
                    str = str.substring(1, str.length());
                }
                break;
            default:
                if (str.charAt(0) != '-') {
                    str = "+" + str;
                }
        }
        return str;
    }

    private CharSequence getCondition(Condition cond) {
        if (cond == null) {
            return null;
        }
        return String.format("%s %s", i18n.get(cond.getPredicate()), cond.getName());
    }

    private CharSequence getString(int res) {
        return ctx.getResources().getString(res);
    }

}
