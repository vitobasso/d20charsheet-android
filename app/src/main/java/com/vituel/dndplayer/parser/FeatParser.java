package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.Effect;
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

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        ModifierParser modParser = new ModifierParser();
        readModifier(modParser, split, 1, effect);
        readModifier(modParser, split, 2, effect);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Effect effect) {
        if (split.length > index) {
            try {
                String str = split[index];
                if (str != null && !str.isEmpty()) {
                    Modifier mod = parser.parse(str);
                    effect.addModifier(mod);
                }
            } catch (ParseException e) {
                Log.w(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
