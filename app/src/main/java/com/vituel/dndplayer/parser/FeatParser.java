package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Modifier;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class FeatParser extends AbstractParser<Feat> {

    public FeatParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Feat parse(String line) {

        String split[] = line.split("\t");
        Feat result = new Feat();
        result.setName(split[0]);
        result.setTraitType(Feat.Type.FEAT);

        ModifierParser modParser = new ModifierParser(result);
        readModifier(modParser, split, 1, result);
        readModifier(modParser, split, 2, result);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Feat result) {
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
