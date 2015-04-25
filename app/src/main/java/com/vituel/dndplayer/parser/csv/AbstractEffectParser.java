package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.parser.exception.ParseFieldException;
import com.vituel.dndplayer.parser.exception.ParseFormatException;
import com.vituel.dndplayer.parser.exception.ParseNullValueException;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by Victor on 18/04/2015.
 */
public abstract class AbstractEffectParser<T extends EffectSource> extends AbstractCsvParser<T> {

    public static final String EFFECT_COL_PATTERN = "effect \\d+";

    private ModifierParser modifierParser = new ModifierParser();

    protected AbstractEffectParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected abstract T parse(String[] line) throws ParseFieldException;

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

    private Effect getOrCreateEffect(T source) {
        Effect effect = source.getEffect();
        if (effect == null) {
            effect = new Effect();
            source.setEffect(effect);
        }
        return effect;
    }

}
