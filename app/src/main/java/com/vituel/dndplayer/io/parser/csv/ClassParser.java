package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.Clazz;

import java.io.File;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassParser extends AbstractEntityParser<Clazz> {

    public ClassParser(Context ctx, File file) {
        super(ctx, file);
    }

    @Override
    protected Clazz parse(String[] split, Clazz result) throws ParseFieldException {
        result.setAttackProg(readEnum(Clazz.AttackProgression.class, split, "bab"));
        result.setFortitudeProg(readEnum(Clazz.ResistProgression.class, split, "fort"));
        result.setReflexProg(readEnum(Clazz.ResistProgression.class, split, "refl"));
        result.setWillProg(readEnum(Clazz.ResistProgression.class, split, "will"));
        result.setBook(readRulebook(split));
        return result;
    }

    @Override
    protected Clazz newInstance(String[] split) {
        return new Clazz();
    }

}
