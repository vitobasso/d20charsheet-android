package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Modifier;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class ClassTraitParser extends AbstractDependantParser<ClassTrait, Clazz> {

    private ClassDao classDao = new ClassDao(ctx);

    public ClassTraitParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Clazz parseOwner(String line) {
        String split[] = line.split("\t");

        String className = read(split, 0);

        Clazz clazz = classDao.findByName(className);
        classDao.close();

        return clazz;
    }

    @Override
    protected ClassTrait parseDependant(String line) {
        String split[] = line.split("\t");

        String lvlStr = read(split, 1);
        String traitName = read(split, 2);
        String overriden = read(split, 3);

        //basic fields
        ClassTrait result = new ClassTrait();
        result.setName(traitName);

        //class trait fields
        int lvl = Integer.valueOf(lvlStr);
        result.setLevel(lvl);
        result.setOverridenTraitName(overriden);

        //effect
        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        //modifiers
        ModifierParser modParser = new ModifierParser();
        readModifier(modParser, split, 4, effect);
        readModifier(modParser, split, 5, effect);
        readModifier(modParser, split, 6, effect);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Effect effect) {
        if (split.length > index) {
            try {
                String str = split[index];
                if (str != null && !str.isEmpty()) {
                    Modifier mod = parser.parse(str);
                    effect.addModifier(mod);
                }
            } catch (ParseException e) {
                Log.w(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
