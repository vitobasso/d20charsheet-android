package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.model.rulebook.RuleSystem;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 12/04/2015.
 */
public class EditionParser extends AbstractSimpleParser<Edition> {

    public EditionParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Edition parse(String[] line) throws ParseFieldException {
        Edition result = new Edition();
        result.setId(readInt(line, 0));
        result.setName(readString(line, 1));
        result.setSystem(RuleSystem.fromString(readString(line, 2)));
        result.setCore(readInt(line, 3) == 1);

        return result;
    }

}
