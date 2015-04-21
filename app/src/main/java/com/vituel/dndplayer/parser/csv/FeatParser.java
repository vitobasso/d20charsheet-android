package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 19/04/2015.
 */
public class FeatParser extends AbstractEffectParser<Feat> {

    public FeatParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Feat parse(String[] line) throws ParseFieldException {
        Feat result = new Feat();
        result.setId(readInt(line, "id"));
        result.setName(readString(line, "name"));
        return result;
    }

}
