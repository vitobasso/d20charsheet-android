package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.parser.exception.ParseFieldException;
import com.vituel.dndplayer.parser.exception.ParseFormatException;
import com.vituel.dndplayer.parser.exception.ParseNullValueException;

/**
 * Created by Victor on 18/04/2015.
 */
public abstract class AbstractEffectParser<T extends EffectSource> extends AbstractSimpleParser<T> {

    protected AbstractEffectParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected abstract T parse(String[] line) throws ParseFieldException;

    protected void parseModifier(T source, String[] split, String column, ModifierTarget target) throws ParseFieldException {
        Integer value = readInt(split, column);
        addModifier(source, target, value);
    }

    protected void addModifier(T source, ModifierTarget target, int value) throws ParseFormatException, ParseNullValueException {
        Effect effect = getEffect(source);
        effect.addModifier(new Modifier(target, value));
    }

    private Effect getEffect(T source) {
        Effect effect = source.getEffect();
        if (effect == null) {
            effect = new Effect();
            source.setEffect(effect);
        }
        return effect;
    }

}
