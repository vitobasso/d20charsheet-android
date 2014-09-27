package com.vituel.dndplayer.parser;

import android.content.Context;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Item;
import com.vituel.dndplayer.model.WeaponItem;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.ModifierTarget.AC;
import static com.vituel.dndplayer.model.ModifierTarget.MAX_DEX;
import static com.vituel.dndplayer.util.JavaUtil.equal;
import static com.vituel.dndplayer.model.Item.ItemType.*;
import static com.vituel.dndplayer.model.SlotType.BODY;
import static com.vituel.dndplayer.model.SlotType.HELD;

/**
 * Created by Victor on 26/03/14.
 */
public class ItemParser extends AbstractParser<Item> {

    public ItemParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Item parse(String line) {
        String split[] = line.split("\t");

        //default values
        String name = null;
        int price = 0;
        int weight = 0;
        String type = null;
        DiceRoll dmg = null;
        int ac = 0;
        int maxDex = Integer.MAX_VALUE;

        //parse data
        try {
            name = split[0];
            try {
                price = Integer.valueOf(split[2]);
            } catch (NumberFormatException e) {
                // price not set
            }
            try {
                weight = Integer.valueOf(split[3]);
            } catch (NumberFormatException e) {
                // weight not set
            }
            try {
                type = split[4];
            } catch (IllegalArgumentException e) {
                // not an equipable item
            }
            try {
                dmg = new DiceRoll(split[7]);
            } catch (RuntimeException e) {
                // funny format
            }
            //TODO parse critical
            try {
                ac = Integer.valueOf(split[11]);
            } catch (NumberFormatException e) {
                // ac not set
            }
            try {
                maxDex = Integer.valueOf(split[12]);
            } catch (NumberFormatException e) {
                // maxDex not set
            }
        } catch (IndexOutOfBoundsException e) {
            // no more columns defined from this point
        }

        //build item
        Item result;
        if (equal(type, "WEAPON")) {
            WeaponItem w = new WeaponItem();
            w.getWeaponProperties().setName(name);
            w.getWeaponProperties().setDamage(dmg);
            result = w;

            result.setSlotType(HELD);
            result.setItemType(WEAPON);

        } else if (equal(type, "ARMOR") || equal(type, "SHIELD")) {
            result = new Item();
            List<Modifier> mod = new ArrayList<>();
            mod.add(new Modifier(AC, ac, ModifierType.ARMOR, result));
            if (maxDex < Integer.MAX_VALUE) {
                mod.add(new Modifier(MAX_DEX, maxDex, result));
            }
            result.setModifiers(mod);

            if (equal(type, "ARMOR")) {
                result.setSlotType(BODY);
                result.setItemType(ARMOR);
            } else {
                result.setSlotType(HELD);
                result.setItemType(SHIELD);
            }
        } else {
            result = new Item();
        }

        //common fields
        result.setName(name);
        result.setPrice(price);
        result.setWeight(weight);

        return result;
    }

}
