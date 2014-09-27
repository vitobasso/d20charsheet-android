package com.vituel.dndplayer.parser;

import android.content.Context;
import com.vituel.dndplayer.model.Clazz;

import static com.vituel.dndplayer.model.Clazz.AttackProgression;
import static com.vituel.dndplayer.model.Clazz.ResistProgression;

/**
 * Created by Victor on 26/03/14.
 */
public class ClassParser extends AbstractParser<Clazz> {

    public ClassParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Clazz parse(String line) {
        String split[] = line.split("\t");
        Clazz result = new Clazz();
        result.setName(split[0]);
        result.setAttackProg(AttackProgression.valueOf(split[1]));
        result.setFortitudeProg(ResistProgression.valueOf(split[2]));
        result.setReflexProg(ResistProgression.valueOf(split[3]));
        result.setWillProg(ResistProgression.valueOf(split[4]));
        return result;
    }

}
