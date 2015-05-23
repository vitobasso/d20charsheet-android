package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.parser.LibraryLoader;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassTraitParser extends AbstractEffectParser<ClassTrait> {

    public ClassTraitParser(Context ctx, String filePath, LibraryLoader.Cache loadingCache) {
        super(ctx, filePath, loadingCache);
    }

    @Override
    protected ClassTrait parse(String[] line, ClassTrait result) throws ParseFieldException {
        result.setLevel(readInt(line, "level"));
        readEffect(line, result);

        Clazz clazz = new Clazz();
        clazz.setId(readInt(line, "class_id"));
        result.setClazz(clazz);

        return result;
    }

    @Override
    protected ClassTrait newInstance(String[] split) {
        return new ClassTrait();
    }

}
