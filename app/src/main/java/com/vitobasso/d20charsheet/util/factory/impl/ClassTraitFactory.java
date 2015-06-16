package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.dependant.ClassTraitDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ClassTraitParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.model.ClassTrait;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class ClassTraitFactory extends EffectEntityToolFactory<ClassTrait> {

    public ClassTraitFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<ClassTrait> createDao() {
        return new ClassTraitDao(ctx);
    }

    @Override
    protected AbstractEntityParser<ClassTrait> createParser(File csvFile, ModifierParser modifierParser) {
        return new ClassTraitParser(ctx, csvFile, modifierParser);
    }

    @Override
    protected String getCsvFileName() {
        return "class_traits.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.class_traits;
    }

}
