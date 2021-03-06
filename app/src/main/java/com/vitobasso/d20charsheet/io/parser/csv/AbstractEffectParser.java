package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFormatException;
import com.vitobasso.d20charsheet.io.parser.exception.ParseNullValueException;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;

import java.io.File;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by Victor on 18/04/2015.
 */
public abstract class AbstractEffectParser<T extends AbstractEntity & EffectSource> extends AbstractEntityParser<T> {

    public static final String EFFECT_COL_PATTERN = "effect[ _]?\\d+";

    private ModifierParser modifierParser;

    protected AbstractEffectParser(Context ctx, File file, ModifierParser modifierParser) {
        super(ctx, file);
        this.modifierParser = modifierParser;
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
