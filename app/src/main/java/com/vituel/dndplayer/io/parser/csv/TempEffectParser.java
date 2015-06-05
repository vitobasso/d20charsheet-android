package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.RulesImporter;
import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.TempEffect;

import java.io.File;

/**
 * Created by Victor on 19/04/2015.
 */
public class TempEffectParser extends AbstractEffectParser<TempEffect> {

    public TempEffectParser(Context ctx, File file, RulesImporter.Cache loadingCache) {
        super(ctx, file, loadingCache);
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
