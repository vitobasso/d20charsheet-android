package com.vitobasso.d20charsheet.util.factory;

import android.content.Context;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.io.importer.RulesDownloader;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.model.AbstractEntity;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public abstract class EntityToolFactory<T extends AbstractEntity> {

    private File dir;
    protected Context ctx;

    public EntityToolFactory(Context ctx) {
        this.ctx = ctx;
        this.dir = RulesDownloader.getRulesDir(ctx);
    }

    public String getEntityTitle() {
        return ctx.getString(getNameResourceId());
    }

    public AbstractEntityParser<T> createParser() {
        File csvFile = getRulesCsvFile(getCsvFileName());
        return createParser(csvFile);
    }

    protected File getRulesCsvFile(String fileName) {
        return new File(dir, fileName);
    }

    public abstract AbstractDao<T> createDao();

    protected abstract AbstractEntityParser<T> createParser(File csvFile);

    protected abstract String getCsvFileName();

    public abstract int getNameResourceId();

}
