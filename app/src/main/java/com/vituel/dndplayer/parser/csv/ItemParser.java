package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.item.Item;
import com.vituel.dndplayer.model.item.SlotType;
import com.vituel.dndplayer.model.item.WeaponItem;
import com.vituel.dndplayer.model.item.WeaponProperties;
import com.vituel.dndplayer.parser.exception.ParseEnumException;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import static com.vituel.dndplayer.model.effect.ModifierTarget.AC;
import static com.vituel.dndplayer.model.effect.ModifierTarget.MAX_DEX;
import static com.vituel.dndplayer.model.item.Item.ItemType;
import static com.vituel.dndplayer.model.item.Item.ItemType.PROTECTIVE;
import static com.vituel.dndplayer.model.item.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.item.SlotType.BODY;
import static com.vituel.dndplayer.model.item.SlotType.FINGER;
import static com.vituel.dndplayer.model.item.SlotType.HANDS;
import static com.vituel.dndplayer.model.item.SlotType.HELD;
import static com.vituel.dndplayer.model.item.SlotType.NECK;
import static com.vituel.dndplayer.model.item.SlotType.WAIST;

/**
 * Created by Victor on 18/04/2015.
 */
public class ItemParser extends AbstractEffectParser<Item> {

    public ItemParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Item parse(String[] split) throws ParseFieldException {
        ItemType type = readType(split, "slot");
        Item result = type == WEAPON ? new WeaponItem() : new Item();

        result.setId(readInt(split, "id"));
        result.setName(readString(split, "name"));
        result.setSlotType(readSlot(split, "slot"));
        result.setWeight(readDoubleNullable(split, "weight"));
        result.setPrice(readDoubleNullable(split, "price_gp"));
        result.setBook(readRulebook(split, "rulebook_id"));

        if (type == WEAPON) {
            addWeaponFields(split, (WeaponItem) result);
        } else if (type == PROTECTIVE) {
            addArmorShieldProperties(split, result);
        }

        return result;
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
