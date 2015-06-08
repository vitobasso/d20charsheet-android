package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.rulebook.Edition;
import com.vitobasso.d20charsheet.model.rulebook.RuleSystem;

import java.io.File;

/**
 * Created by Victor on 12/04/2015.
 */
public class EditionParser extends AbstractEntityParser<Edition> {

    public EditionParser(Context ctx, File file) {
        super(ctx, file);
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
