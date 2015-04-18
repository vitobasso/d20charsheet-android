package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.item.Item;
import com.vituel.dndplayer.model.item.SlotType;
import com.vituel.dndplayer.parser.exception.ParseEnumException;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import static com.vituel.dndplayer.model.item.SlotType.ARMS;
import static com.vituel.dndplayer.model.item.SlotType.BODY;
import static com.vituel.dndplayer.model.item.SlotType.FINGER;
import static com.vituel.dndplayer.model.item.SlotType.HANDS;
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
        Item result = new Item();
        result.setId(readInt(split, "id"));
        result.setName(readString(split, "name"));
        result.setSlotType(readSlot(split, "slot"));
        result.setWeight(readDoubleNullable(split, "weight"));
        result.setPrice(readDoubleNullable(split, "price_gp"));
        return result;
    }

    private SlotType readSlot(String[] split, String column) throws ParseFieldException {
        String slotStr = readString(split, column);
        switch (slotStr) {
            case "Body":
            case "Armor":
                return BODY;
            case "held":
            case "Weapon":
            case "Armor or shield":
                return ARMS;
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

}
