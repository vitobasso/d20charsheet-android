package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class ClassTraitParser extends AbstractDependantParser<ClassTrait, Clazz> {

    private ClassDao classDao;

    public ClassTraitParser(Context ctx, ClassDao classDao) {
        super(ctx);
        this.classDao = classDao;
    }

    @Override
    protected Clazz parseOwner(String line) throws ParseFieldException {
        String split[] = line.split("\t");
        String className = readString(split, 0);
        return classDao.findByName(className);
    }

    @Override
    protected ClassTrait parseDependant(String line) throws ParseFieldException {
        String split[] = line.split("\t");

        String lvlStr = readString(split, 1);
        String traitName = readString(split, 2);
        String overriden = readString(split, 3);

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
