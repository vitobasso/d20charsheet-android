package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.RulesImporter;
import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.Feat;

/**
 * Created by Victor on 19/04/2015.
 */
public class FeatParser extends AbstractEffectParser<Feat> {

    public FeatParser(Context ctx, String filePath, RulesImporter.Cache loadingCache) {
        super(ctx, filePath, loadingCache);
    }

    @Override
    protected Feat parse(String[] split, Feat result) throws ParseFieldException {
        readEffect(split, result);
        result.setBook(readRulebook(split, "rulebook_id"));
        return result;
    }

    @Override
    protected Feat newInstance(String[] split) {
        return new Feat();
    }

}
