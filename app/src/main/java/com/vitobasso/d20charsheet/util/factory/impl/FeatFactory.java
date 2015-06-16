package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.FeatDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.FeatParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class FeatFactory extends EffectEntityToolFactory<Feat> {

    public FeatFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Feat> createDao() {
        return new FeatDao(ctx);
    }

    @Override
    protected AbstractEntityParser<Feat> createParser(File csvFile, ModifierParser modifierParser) {
        return new FeatParser(ctx, csvFile, modifierParser);
    }

    @Override
    protected String getCsvFileName() {
        return "feats.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.feats;
    }

}
