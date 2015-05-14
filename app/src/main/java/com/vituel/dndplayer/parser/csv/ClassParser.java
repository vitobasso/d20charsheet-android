package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassParser extends AbstractEntityParser<Clazz> {

    public ClassParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Clazz parse(String[] split, Clazz result) throws ParseFieldException {
        result.setAttackProg(readEnum(Clazz.AttackProgression.class, split, "bab"));
        result.setFortitudeProg(readEnum(Clazz.ResistProgression.class, split, "fort"));
        result.setReflexProg(readEnum(Clazz.ResistProgression.class, split, "refl"));
        result.setWillProg(readEnum(Clazz.ResistProgression.class, split, "will"));
        result.setBook(readRulebook(split, "rulebook_id"));
        return result;
    }

    @Override
    protected Clazz newInstance(String[] split) {
        return new Clazz();
    }

}
