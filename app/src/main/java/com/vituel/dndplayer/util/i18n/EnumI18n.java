package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.Condition;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.model.SlotType;

/**
 * Created by Victor on 29/09/2014.
 */
public class EnumI18n {

    private Context ctx;

    public EnumI18n(Context ctx) {
        this.ctx = ctx;
    }

    public <T extends Enum> CharSequence get(T value) {
        if (value instanceof Attack.WeaponReferenceType) {
            return new WeaponRefStringConverter(ctx).toString((Attack.WeaponReferenceType) value);
        } else if (value instanceof Condition.Predicate) {
            return new ConditionPredicateStringConverter(ctx).toString((Condition.Predicate) value);
        } else if (value instanceof ModifierType) {
            return new ModifierTypeStringConverter(ctx).toString((ModifierType) value);
        } else if (value instanceof SlotType) {
            return new SlotTypeStringConverter(ctx).toString((SlotType) value);
        } else if (value instanceof ModifierTarget) {
            return new ModifierTargetStringConverter(ctx).toString((ModifierTarget) value);
        } else {
            return null;
        }
    }


}
