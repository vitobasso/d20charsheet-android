package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.Critical;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierType;
import com.vituel.dndplayer.model.item.Item;
import com.vituel.dndplayer.model.item.WeaponItem;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;
import com.vituel.dndplayer.util.JavaUtil;

import static com.vituel.dndplayer.model.effect.ModifierTarget.AC;
import static com.vituel.dndplayer.model.effect.ModifierTarget.MAX_DEX;
import static com.vituel.dndplayer.model.item.Item.ItemType.ARMOR;
import static com.vituel.dndplayer.model.item.Item.ItemType.SHIELD;
import static com.vituel.dndplayer.model.item.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.item.SlotType.BODY;
import static com.vituel.dndplayer.model.item.SlotType.HELD;

/**
 * Created by Victor on 26/03/14.
 */
public class ItemParser extends AbstractSimpleParser<Item> {

    private static final String TAG = ItemParser.class.getSimpleName();

    public ItemParser(Context ctx, String filePath) {
        super(ctx, filePath);
    }

    @Override
    protected Item parse(String[] line) {

        //default values
        String name = null;
        Double price = 0.;
        Double weight = 0.;
        String type = null;
        DiceRoll dmg = null;
        Critical crit = null;
        int ac = 0;
        int maxDex = Integer.MAX_VALUE;

        //parse data
        try {
            name = line[0];
            try {
                price = Double.valueOf(line[2]);
            } catch (Throwable e) {
                // price not set
            }
            try {
                weight = Double.valueOf(line[3]);
            } catch (Throwable e) {
                // weight not set
            }
            try {
                type = line[4];
            } catch (Throwable e) {
                // not an equipable item
            }
            String dmgStr = line[7];
            try {
                dmg = new DiceRoll(dmgStr);
            } catch (Throwable e) {
                dmg = new DiceRoll();
                if (dmgStr.isEmpty()) {
                    // dmg not set (not a weapon?)
                }else{
                    Log.w(TAG, "Failed to decode damage: " + dmgStr);
                }
            }
            String critStr = line[8];
            try{
                crit = new Critical(critStr);
            } catch (Throwable e) {
                crit = new Critical();
                if (critStr.isEmpty()) {
                    // crit not set (not a weapon?)
                } else {
                    Log.w(TAG, "Failed to decode critical: " + critStr);
                }
            }
            try {
                ac = Integer.valueOf(line[11]);
            } catch (Throwable e) {
                // ac not set
            }
            try {
                maxDex = Integer.valueOf(line[12]);
            } catch (Throwable e) {
                // maxDex not set
            }
        } catch (Throwable e) {
            // no more columns defined from this point
        }

        //build item
        Item result;
        if (JavaUtil.equals(type, "WEAPON")) {
            WeaponItem w = new WeaponItem();
            w.getWeaponProperties().setName(name);
            w.getWeaponProperties().setDamage(dmg);
            w.getWeaponProperties().setCritical(crit);
            result = w;

            result.setSlotType(HELD);
            result.setItemType(WEAPON);

        } else if (JavaUtil.equals(type, "ARMOR") || JavaUtil.equals(type, "SHIELD")) {
            result = new Item();
            Effect effect = new Effect();
            effect.addModifier(new Modifier(AC, ac, ModifierType.ARMOR));
            if (maxDex < Integer.MAX_VALUE) {
                effect.addModifier(new Modifier(MAX_DEX, maxDex));
            }
            result.setEffect(effect);

            if (JavaUtil.equals(type, "ARMOR")) {
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
