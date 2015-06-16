package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.exception.ParseEnumException;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.item.Item;
import com.vitobasso.d20charsheet.model.item.SlotType;
import com.vitobasso.d20charsheet.model.item.WeaponItem;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;

import java.io.File;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.AC;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.MAX_DEX;
import static com.vitobasso.d20charsheet.model.item.Item.ItemType;
import static com.vitobasso.d20charsheet.model.item.Item.ItemType.PROTECTIVE;
import static com.vitobasso.d20charsheet.model.item.Item.ItemType.WEAPON;
import static com.vitobasso.d20charsheet.model.item.SlotType.BODY;
import static com.vitobasso.d20charsheet.model.item.SlotType.FINGER;
import static com.vitobasso.d20charsheet.model.item.SlotType.HANDS;
import static com.vitobasso.d20charsheet.model.item.SlotType.HELD;
import static com.vitobasso.d20charsheet.model.item.SlotType.NECK;
import static com.vitobasso.d20charsheet.model.item.SlotType.WAIST;

/**
 * Created by Victor on 18/04/2015.
 */
public class ItemParser extends AbstractEffectParser<Item> {

    public ItemParser(Context ctx, File file, ModifierParser modifierParser) {
        super(ctx, file, modifierParser);
    }

    @Override
    protected Item parse(String[] split, Item result) throws ParseFieldException {
        result.setSlotType(readSlot(split, "slot"));
        result.setWeight(readDoubleNullable(split, "weight"));
        result.setPrice(readDoubleNullable(split, "price_gp"));
        result.setBook(readRulebook(split));

        ItemType type = readType(split, "slot");
        if (type == WEAPON) {
            addWeaponFields(split, (WeaponItem) result);
        } else if (type == PROTECTIVE) {
            addArmorShieldProperties(split, result);
        }

        return result;
    }

    @Override
    protected Item newInstance(String[] split) throws ParseFieldException {
        ItemType type = readType(split, "slot");
        return type == WEAPON ? new WeaponItem() : new Item();
    }

    private void addWeaponFields(String[] split, WeaponItem result) throws ParseFieldException {
        WeaponProperties weaponProp = new WeaponProperties();
        weaponProp.setDamage(readRoll(split, "damage"));
        weaponProp.setCritical(readCritical(split, "critical"));
        result.setWeaponProperties(weaponProp);
    }

    private void addArmorShieldProperties(String[] split, Item result) throws ParseFieldException {
        addModifierIfNotNull(result, MAX_DEX, readIntNullable(split, "max_dex"));
        addModifierIfNotNull(result, AC, readIntNullable(split, "ac"));
    }

    private SlotType readSlot(String[] split, String column) throws ParseFieldException {
        String slotStr = readString(split, column);
        switch (slotStr) {
            case "held":
            case "Weapon":
            case "Armor or shield":
            case "WEAPON":
            case "SHIELD":
                return HELD;
            case "Body":
            case "Armor":
            case "ARMOR":
                return BODY;
            case "Ring":
                return FINGER;
            case "Throat":
                return NECK;
            case "Waist":
                return WAIST;
            case "Hands":
                return HANDS;
            default :
                throw new ParseEnumException(getIndex(column), SlotType.class, slotStr);
        }
    }

    private ItemType readType(String[] split, String column) throws ParseFieldException {
        String slotStr = readString(split, column);
        switch (slotStr) {
            case "Body":
            case "Armor":
            case "ARMOR":
            case "Armor or shield":
            case "SHIELD":
                return PROTECTIVE;
            case "Weapon":
            case "WEAPON":
                return WEAPON;
            default :
                return null;
        }
    }

}
