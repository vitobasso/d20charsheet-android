package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.model.rulebook.RuleSystem;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 12/04/2015.
 */
public class EditionParser extends AbstractSimpleParser<Edition> {

    public EditionParser(Context ctx, String filePath) {
        super(ctx, filePath);
    }

    @Override
    protected Edition parse(String[] line) throws ParseFieldException {
        Edition result = new Edition();
        result.setId(readInt(line, "id"));
        result.setName(readString(line, "name"));
        result.setSystem(RuleSystem.fromString(readString(line, "system")));
        result.setCore(readInt(line, "core") == 1);

        return result;
    }

}
