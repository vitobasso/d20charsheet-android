package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class FeatParser extends AbstractSimpleParser<Feat> {

    public FeatParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Feat parse(String[] line) {
        Feat result = new Feat();
        result.setName(line[0]);

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        ModifierParser modParser = new ModifierParser();
        readModifier(modParser, line, 1, effect);
        readModifier(modParser, line, 2, effect);

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
