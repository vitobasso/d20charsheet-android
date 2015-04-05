package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.Effect;
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

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        ModifierParser modParser = new ModifierParser();
        readModifier(modParser, split, 2, effect);
        readModifier(modParser, split, 3, effect);
        readModifier(modParser, split, 4, effect);
        readModifier(modParser, split, 5, effect);

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
