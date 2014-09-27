package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Trait;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class ClassTraitParser extends AbstractDependantParser<Trait, Clazz> {

    public ClassTraitParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Clazz parseOwner(String line) {
        String split[] = line.split("\t");

        String className = read(split, 0);

        ClassDao classDao = new ClassDao(ctx);
        Clazz clazz = classDao.findByName(className);
        classDao.close();

        return clazz;
    }

    @Override
    protected Trait parseDependant(String line) {
        String split[] = line.split("\t");

        String lvlStr = read(split, 1);
        String traitName = read(split, 2);
        String overriden = read(split, 3);

        //basic fields
        ClassTrait result = new ClassTrait();
        result.setName(traitName);
        result.setTraitType(Trait.Type.CLASS);

        //class trait fields
        int lvl = Integer.valueOf(lvlStr);
        result.setLevel(lvl);
        result.setOverridenTraitName(overriden);

        //modifiers
        ModifierParser modParser = new ModifierParser(result);
        readModifier(modParser, split, 4, result);
        readModifier(modParser, split, 5, result);
        readModifier(modParser, split, 6, result);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Trait result) {
        if (split.length > index) {
            try {
                String str = split[index];
                if (str != null && !str.isEmpty()) {
                    Modifier mod = parser.parse(str);
                    result.addModifier(mod);
                }
            } catch (ParseException e) {
                Log.w(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
