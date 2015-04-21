package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassParser extends AbstractSimpleParser<Clazz> {

    public ClassParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Clazz parse(String[] line) throws ParseFieldException {
        Clazz clazz = new Clazz();
        clazz.setId(readInt(line, "id"));
        clazz.setName(readString(line, "name"));
        clazz.setAttackProg(readEnumNullable(Clazz.AttackProgression.class, line, "bab"));
        clazz.setFortitudeProg(readEnumNullable(Clazz.ResistProgression.class, line, "fort"));
        clazz.setReflexProg(readEnumNullable(Clazz.ResistProgression.class, line, "refl"));
        clazz.setWillProg(readEnumNullable(Clazz.ResistProgression.class, line, "will"));
        return clazz;
    }

}
