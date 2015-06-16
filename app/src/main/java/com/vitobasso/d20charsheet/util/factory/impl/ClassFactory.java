package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.ClassDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ClassParser;
import com.vitobasso.d20charsheet.model.Clazz;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class ClassFactory extends EntityToolFactory<Clazz> {

    public ClassFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Clazz> createDao() {
        return new ClassDao(ctx);
    }

    @Override
    protected AbstractEntityParser<Clazz> createParser(File csvFile) {
        return new ClassParser(ctx, csvFile);
    }

    @Override
    protected String getCsvFileName() {
        return "classes.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.classes;
    }

}
