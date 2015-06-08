package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.RulesImporter;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.ClassTrait;
import com.vitobasso.d20charsheet.model.Clazz;

import java.io.File;

/**
 * Created by Victor on 21/04/2015.
 */
public class ClassTraitParser extends AbstractEffectParser<ClassTrait> {

    public ClassTraitParser(Context ctx, File file, RulesImporter.ParserCache loadingCache) {
        super(ctx, file, loadingCache);
    }

    @Override
    protected ClassTrait parse(String[] line, ClassTrait result) throws ParseFieldException {
        result.setLevel(readInt(line, "level"));
        readEffect(line, result);

        Clazz clazz = new Clazz();
        clazz.setId(readInt(line, "class_id"));
        result.setBook(readRulebook(line));
        result.setClazz(clazz);

        return result;
    }

    @Override
    protected ClassTrait newInstance(String[] split) {
        return new ClassTrait();
    }

}
