package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassTraitParser extends AbstractCsvParser<ClassTrait> {

    public ClassTraitParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected ClassTrait parse(String[] line) throws ParseFieldException {
        ClassTrait trait = new ClassTrait();
        trait.setId(readInt(line, "id"));
        trait.setName(readString(line, "name"));
        trait.setLevel(readInt(line, "level"));

        Clazz clazz = new Clazz();
        clazz.setId(readInt(line, "class_id"));
        trait.setClazz(clazz);

        return trait;
    }

}
