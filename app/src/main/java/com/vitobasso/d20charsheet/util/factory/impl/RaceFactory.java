package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.RaceDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.io.parser.csv.RaceParser;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class RaceFactory extends EffectEntityToolFactory<Race> {

    public RaceFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Race> createDao() {
        return new RaceDao(ctx);
    }

    @Override
    protected AbstractEntityParser<Race> createParser(File csvFile, ModifierParser modifierParser) {
        return new RaceParser(ctx, csvFile, modifierParser);
    }

    @Override
    protected String getCsvFileName() {
        return "races.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.races;
    }

}
