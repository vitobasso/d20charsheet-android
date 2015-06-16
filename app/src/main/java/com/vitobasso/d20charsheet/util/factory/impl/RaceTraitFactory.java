package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.dependant.RaceTraitDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.io.parser.csv.RaceTraitParser;
import com.vitobasso.d20charsheet.model.RaceTrait;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class RaceTraitFactory extends EffectEntityToolFactory<RaceTrait> {

    public RaceTraitFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<RaceTrait> createDao() {
        return new RaceTraitDao(ctx);
    }

    @Override
    protected AbstractEntityParser<RaceTrait> createParser(File csvFile, ModifierParser modifierParser) {
        return new RaceTraitParser(ctx, csvFile, modifierParser);
    }

    @Override
    protected String getCsvFileName() {
        return "race_traits.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.race_traits;
    }

}
