package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.TempEffectDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.io.parser.csv.TempEffectParser;
import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class TempEffectFactory extends EffectEntityToolFactory<TempEffect> {

    public TempEffectFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<TempEffect> createDao() {
        return new TempEffectDao(ctx);
    }

    @Override
    protected AbstractEntityParser<TempEffect> createParser(File csvFile, ModifierParser modifierParser) {
        return new TempEffectParser(ctx, csvFile, modifierParser);
    }

    @Override
    protected String getCsvFileName() {
        return "temp_effects.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.effects;
    }

}
