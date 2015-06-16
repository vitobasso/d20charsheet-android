package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.ConditionDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ConditionParser;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class ConditionFactory extends EntityToolFactory<Condition> {

    public ConditionFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Condition> createDao() {
        return new ConditionDao(ctx);
    }

    @Override
    public AbstractEntityParser<Condition> createParser(File csvFile) {
        return new ConditionParser(ctx, csvFile);
    }

    @Override
    protected String getCsvFileName() {
        return "conditions.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.conditionals;
    }

}
