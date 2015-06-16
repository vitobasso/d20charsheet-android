package com.vitobasso.d20charsheet.util.factory.impl;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.entity.ItemDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ItemParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.model.item.Item;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public class ItemFactory extends EffectEntityToolFactory<Item> {

    public ItemFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public AbstractDao<Item> createDao() {
        return new ItemDao(ctx);
    }

    @Override
    protected AbstractEntityParser<Item> createParser(File csvFile, ModifierParser modifierParser) {
        return new ItemParser(ctx, csvFile, modifierParser);
    }

    @Override
    protected String getCsvFileName() {
        return "items.csv";
    }

    @Override
    public int getNameResourceId() {
        return R.string.items;
    }

}
