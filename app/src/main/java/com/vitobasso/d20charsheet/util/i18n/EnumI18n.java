package com.vitobasso.d20charsheet.util.i18n;

import android.content.Context;

import com.vitobasso.d20charsheet.model.character.Attack;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.model.effect.ModifierType;
import com.vitobasso.d20charsheet.model.effect.Multiplier;
import com.vitobasso.d20charsheet.model.item.Item;
import com.vitobasso.d20charsheet.model.item.SlotType;

/**
 * Created by Victor on 29/09/2014.
 */
public class EnumI18n {

    private Context ctx;

    public EnumI18n(Context ctx) {
        this.ctx = ctx;
    }

    public <T extends Enum> CharSequence get(T value) {
        if (value instanceof Attack.WeaponReference) {
            return new WeaponRefStringConverter(ctx).toString((Attack.WeaponReference) value);
        } else if (value instanceof Condition.Predicate) {
            return new ConditionPredicateStringConverter(ctx).toString((Condition.Predicate) value);
        } else if (value instanceof ModifierType) {
            return new ModifierTypeStringConverter(ctx).toString((ModifierType) value);
        } else if (value instanceof SlotType) {
            return new SlotTypeStringConverter(ctx).toString((SlotType) value);
        } else if (value instanceof ModifierTarget) {
            return new ModifierTargetStringConverter(ctx).toString((ModifierTarget) value);
        } else if (value instanceof ModifierSource) {
            return new ModifierSourceStringConverter(ctx).toString((ModifierSource) value);
        } else if (value instanceof Multiplier) {
            return new MultiplierStringConverter(ctx).toString((Multiplier) value);
        } else if (value instanceof Item.ItemType) {
            return new ItemTypeStringConverter(ctx).toString((Item.ItemType) value);
        } else {
            return null;
        }
    }


}
