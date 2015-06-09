package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.importer.RulesImporter;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.TempEffect;

import java.io.File;

/**
 * Created by Victor on 19/04/2015.
 */
public class TempEffectParser extends AbstractEffectParser<TempEffect> {

    public TempEffectParser(Context ctx, File file, RulesImporter.ParserCache loadingCache) {
        super(ctx, file, loadingCache);
    }

    @Override
    protected TempEffect parse(String[] line, TempEffect result) throws ParseFieldException {
        result.setBook(readRulebook(line));
        result.setEffect(readEffect(line, result));
        return result;
    }

    @Override
    protected TempEffect newInstance(String[] split) throws ParseFieldException {
        return new TempEffect();
    }

}
