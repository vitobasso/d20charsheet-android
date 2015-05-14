package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.Size;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import static com.vituel.dndplayer.model.effect.ModifierTarget.CHA;
import static com.vituel.dndplayer.model.effect.ModifierTarget.CON;
import static com.vituel.dndplayer.model.effect.ModifierTarget.DEX;
import static com.vituel.dndplayer.model.effect.ModifierTarget.INT;
import static com.vituel.dndplayer.model.effect.ModifierTarget.SIZE;
import static com.vituel.dndplayer.model.effect.ModifierTarget.STR;
import static com.vituel.dndplayer.model.effect.ModifierTarget.WIS;

/**
 * Created by Victor on 18/04/2015.
 */
public class RaceParser extends AbstractEffectParser<Race> {

    public RaceParser(Context ctx, String filePath) {
        super(ctx, filePath);
    }

    @Override
    protected Race parse(String[] split, Race result) throws ParseFieldException {

        result.setBook(readRulebook(split, "rulebook_id"));

        parseModifier(result, split, "str", STR);
        parseModifier(result, split, "dex", DEX);
        parseModifier(result, split, "con", CON);
        parseModifier(result, split, "int", INT);
        parseModifier(result, split, "wis", WIS);
        parseModifier(result, split, "cha", CHA);

        addModifier(result, SIZE, readSize(split, "size").getIndex());

        readSpeed(split, "speed");

        return result;
    }

    @Override
    protected Race newInstance(String[] split) throws ParseFieldException {
        return new Race();
    }

    private Size readSize(String[] split, String column) throws ParseFieldException {
        String sizeStr = readString(split, column);
        switch (sizeStr) {
            case "Small":
                return Size.SMALL;
            case "Medium":
                return Size.MEDIUM;
            case "Large":
                return Size.LARGE;
            default:
                throw new ParseFieldException(getIndex(column), "Unknown size: " + sizeStr);
        }
    }

    private int readSpeed(String[] split, String column) throws ParseFieldException {
        int speedInFeet = readInt(split, column);
        if (speedInFeet % 5 != 0) {
            throw new ParseFieldException(getIndex(column), "Speed value in feet not a multiple of 5: " + speedInFeet);
        }
        return speedInFeet / 5;
    }

}
