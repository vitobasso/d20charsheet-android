package com.vituel.dndplayer.util;

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
import com.vituel.dndplayer.model.ModifierType;

/**
 * Created by Victor on 25/04/14.
 */
public class ModifierStringConverter {

    private Context ctx;

    public ModifierStringConverter(Context ctx) {
        this.ctx = ctx;
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
                return getString(R.string.dmg);
            case CRIT_RANGE:
            case CRIT_MULT:
                return getString(R.string.crit);
            case SIZE:
                return getString(R.string.size);
            case MAX_DEX:
                return getString(R.string.max_dex);
            case MR:
                return getString(R.string.sr);
            case DR:
                return getString(R.string.dr);
            case CONCEAL:
                return getString(R.string.concealment);
            case IMMUNE:
                return getString(R.string.immune);
            case SKILL:
                return variation;
            default:
                Log.w(getClass().getSimpleName(), "Couldn't convert target to string: " + target.toString());
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

    private CharSequence getType(ModifierType type) {
        switch (type) {
            case ENHANCEMENT:
                return getString(R.string.enhance);
            case MORALE:
                return getString(R.string.morale);
            case COMPETENCE:
                return getString(R.string.compete);
            case CIRCUMSTANCE:
                return getString(R.string.circums);
            case ARMOR:
                return getString(R.string.armor);
            case SHIELD:
                return getString(R.string.shield);
            case NATURAL_ARMOR:
                return getString(R.string.nat_armor);
            case DEFLECTION:
                return getString(R.string.defl);
            case DODGE:
                return getString(R.string.dodge);
            case SIZE:
                return getString(R.string.size);
            case RACIAL:
                return getString(R.string.racial);
            case LUCK:
                return getString(R.string.luck);
            case ALCHEMICAL:
                return getString(R.string.alchem);
            case INSIGHT:
                return getString(R.string.insight);
            case RESISTANCE:
                return getString(R.string.resist);
            case INHERENT:
                return getString(R.string.inherent);
            case PROFANE:
                return getString(R.string.profane);
            case SACRED:
                return getString(R.string.sacred);
            default:
                return null;
        }
    }

    private CharSequence getCondition(Condition cond) {
        if (cond == null) {
            return null;
        }
        return String.format("%s %s", getConditionPredicate(cond.getPredicate()), cond.getName());
    }

    public CharSequence getConditionPredicate(Condition.Predicate pred) {
        switch (pred) {
            case WHEN:
                return getString(R.string.when);
            case USING:
                return getString(R.string.using);
            case AGAINST:
                return getString(R.string.against);
            case RELATED_TO:
                return getString(R.string.related_to);
            default:
                Log.w(getClass().getSimpleName(), "Couldn't convert condition predicate to string: " + pred.toString());
                return pred.toString();
        }
    }

    private CharSequence getString(int res) {
        return ctx.getResources().getString(res);
    }

}
