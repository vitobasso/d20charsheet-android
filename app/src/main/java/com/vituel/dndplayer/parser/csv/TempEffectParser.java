package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 19/04/2015.
 */
public class TempEffectParser extends AbstractEffectParser<TempEffect> {

    public TempEffectParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected TempEffect parse(String[] line, TempEffect result) throws ParseFieldException {
        result.setEffect(readEffect(line, result));
        return result;
    }

    @Override
    protected TempEffect newInstance(String[] split) throws ParseFieldException {
        return new TempEffect();
    }

}
