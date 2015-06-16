package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.EditionDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.EditionParser;
import com.vitobasso.d20charsheet.model.rulebook.Edition;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class EditionFactory extends EntityToolFactory<Edition> {

    public EditionFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Edition> createDao() {
        return new EditionDao(ctx);
    }

    @Override
    public AbstractEntityParser<Edition> createParser(File csvFile) {
        return new EditionParser(ctx, csvFile);
    }

    @Override
    protected String getCsvFileName() {
        return "editions.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.editions;
    }

}
