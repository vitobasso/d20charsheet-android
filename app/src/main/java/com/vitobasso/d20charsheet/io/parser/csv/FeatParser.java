package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.Feat;

import java.io.File;

/**
 * Created by Victor on 19/04/2015.
 */
public class FeatParser extends AbstractEffectParser<Feat> {

    public FeatParser(Context ctx, File file, ModifierParser modifierParser) {
        super(ctx, file, modifierParser);
    }

    @Override
    protected Feat parse(String[] split, Feat result) throws ParseFieldException {
        readEffect(split, result);
        result.setBook(readRulebook(split));
        return result;
    }

    @Override
    protected Feat newInstance(String[] split) {
        return new Feat();
    }

}
