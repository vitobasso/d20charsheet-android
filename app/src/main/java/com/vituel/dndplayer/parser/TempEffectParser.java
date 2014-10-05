package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.TempEffect;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class TempEffectParser extends AbstractParser<TempEffect> {

    public TempEffectParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected TempEffect parse(String line) {

        String split[] = line.split("\t");
        TempEffect result = new TempEffect();
        result.setName(read(split, 0));

        ModifierParser modParser = new ModifierParser(result);
        readModifier(modParser, split, 2, result);
        readModifier(modParser, split, 3, result);
        readModifier(modParser, split, 4, result);
        readModifier(modParser, split, 5, result);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, TempEffect result) {
        if (split.length > index) {
            try {
                String str = split[index];
                if (str != null && !str.isEmpty()) {
                    Modifier mod = parser.parse(str);
                    result.addModifier(mod);
                }
            } catch (ParseException e) {
                Log.w(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
