package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.parser.LibraryLoader;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 19/04/2015.
 */
public class FeatParser extends AbstractEffectParser<Feat> {

    public FeatParser(Context ctx, String filePath, LibraryLoader.Cache loadingCache) {
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
