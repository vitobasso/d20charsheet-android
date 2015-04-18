package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class TempEffectParser extends AbstractSimpleParser<TempEffect> {

    public TempEffectParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected TempEffect parse(String[] line) throws ParseFieldException {

        TempEffect result = new TempEffect();
        result.setName(readString(line, 0));

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        ModifierParser modParser = new ModifierParser();
        readModifier(modParser, line, 2, effect);
        readModifier(modParser, line, 3, effect);
        readModifier(modParser, line, 4, effect);
        readModifier(modParser, line, 5, effect);

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
