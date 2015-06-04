package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.model.rulebook.RuleSystem;

/**
 * Created by Victor on 12/04/2015.
 */
public class EditionParser extends AbstractEntityParser<Edition> {

    public EditionParser(Context ctx, String filePath) {
        super(ctx, filePath);
    }

    @Override
    protected Edition parse(String[] line, Edition result) throws ParseFieldException {
        result.setSystem(RuleSystem.fromString(readString(line, "system")));
        result.setCore(readInt(line, "core") == 1);
        return result;
    }

    @Override
    protected Edition newInstance(String[] split) {
        return new Edition();
    }

}
