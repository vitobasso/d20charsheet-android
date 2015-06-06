package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.RulesImporter;
import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.io.parser.exception.ParseFormatException;
import com.vituel.dndplayer.io.parser.exception.ParseNullValueException;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;

import java.io.File;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by Victor on 18/04/2015.
 */
public abstract class AbstractEffectParser<T extends AbstractEntity & EffectSource> extends AbstractEntityParser<T> {

    public static final String EFFECT_COL_PATTERN = "effect[ _]?\\d+";

    private ModifierParser modifierParser;

    protected AbstractEffectParser(Context ctx, File file, RulesImporter.ParserCache loadingCache) {
        super(ctx, file);
        modifierParser = new ModifierTranslatedParser(loadingCache);
    }

    protected Effect readEffect(String[] line, T source) throws ParseFieldException {
        Effect effect = getOrCreateEffect(source);
        for (String col : getHeaders().values()) {
            if(col.matches(EFFECT_COL_PATTERN)) {
                tryToParseModifier(line, col, effect);
            }
        }
        return effect;
    }

    private void tryToParseModifier(String[] line, String col, Effect effect) throws ParseFieldException {
        String str = readStringNullable(line, col);
        if (!isNullOrEmpty(str)) {
            Modifier modifier = modifierParser.parse(str);
            effect.addModifier(modifier);
        }
    }

    protected void parseModifier(T source, String[] split, String column, ModifierTarget target) throws ParseFieldException {
        Integer value = readInt(split, column);
        addModifier(source, target, value);
    }

    protected void addModifier(T source, ModifierTarget target, int value) throws ParseFormatException, ParseNullValueException {
        Effect effect = getOrCreateEffect(source);
        effect.addModifier(new Modifier(target, value));
    }

    protected void addModifierIfNotNull(T source, ModifierTarget target, Integer value) throws ParseFormatException, ParseNullValueException {
        if (value != null) {
            addModifier(source, target, value);
        }
    }

    private Effect getOrCreateEffect(T source) {
        Effect effect = source.getEffect();
        if (effect == null) {
            effect = new Effect();
            source.setEffect(effect);
        }
        return effect;
    }

}
