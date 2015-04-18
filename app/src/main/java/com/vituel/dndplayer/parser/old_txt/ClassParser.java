package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;

import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;

import static com.vituel.dndplayer.model.Clazz.AttackProgression;
import static com.vituel.dndplayer.model.Clazz.ResistProgression;

/**
 * Created by Victor on 26/03/14.
 */
public class ClassParser extends AbstractSimpleParser<Clazz> {

    public ClassParser(Context ctx, String filePath) {
        super(ctx, filePath);
    }

    @Override
    protected Clazz parse(String[] line) {
        Clazz result = new Clazz();
        result.setName(line[0]);
        result.setAttackProg(AttackProgression.valueOf(line[1]));
        result.setFortitudeProg(ResistProgression.valueOf(line[2]));
        result.setReflexProg(ResistProgression.valueOf(line[3]));
        result.setWillProg(ResistProgression.valueOf(line[4]));
        return result;
    }

}
