package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassParser extends AbstractCsvParser<Clazz> {

    public ClassParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Clazz parse(String[] line) throws ParseFieldException {
        Clazz clazz = new Clazz();
        clazz.setId(readInt(line, "id"));
        clazz.setName(readString(line, "name"));
        clazz.setAttackProg(readEnum(Clazz.AttackProgression.class, line, "bab"));
        clazz.setFortitudeProg(readEnum(Clazz.ResistProgression.class, line, "fort"));
        clazz.setReflexProg(readEnum(Clazz.ResistProgression.class, line, "refl"));
        clazz.setWillProg(readEnum(Clazz.ResistProgression.class, line, "will"));
        clazz.setBook(readRulebook(line, "rulebook_id"));
        return clazz;
    }

}
