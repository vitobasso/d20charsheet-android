package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.importer.RulesImporter;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.Size;

import java.io.File;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CHA;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CON;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DEX;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.INT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SIZE;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SPEED;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.STR;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.WIS;

/**
 * Created by Victor on 18/04/2015.
 */
public class RaceParser extends AbstractEffectParser<Race> {

    public RaceParser(Context ctx, File file, RulesImporter.ParserCache loadingCache) {
        super(ctx, file, loadingCache);
    }

    @Override
    protected Race parse(String[] split, Race result) throws ParseFieldException {

        result.setBook(readRulebook(split));

        parseModifier(result, split, "str", STR);
        parseModifier(result, split, "dex", DEX);
        parseModifier(result, split, "con", CON);
        parseModifier(result, split, "int", INT);
        parseModifier(result, split, "wis", WIS);
        parseModifier(result, split, "cha", CHA);

        addModifier(result, SIZE, readSize(split, "size").getIndex());

        addModifier(result, SPEED, readSpeed(split, "speed"));

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
