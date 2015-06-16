package com.vitobasso.d20charsheet.util.factory;

import android.content.Context;

import com.vitobasso.d20charsheet.io.importer.ImportContext;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierParser;
import com.vitobasso.d20charsheet.io.parser.csv.ModifierTranslatedParser;
import com.vitobasso.d20charsheet.model.AbstractEntity;

import java.io.File;

/**
 * Created by Victor on 15/06/2015.
 */
public abstract class EffectEntityToolFactory<T extends AbstractEntity> extends EntityToolFactory<T>{

    private ImportContext importContext;

    public EffectEntityToolFactory(Context ctx) {
        super(ctx);
    }

    public void setImportContext(ImportContext importContext) {
        this.importContext = importContext;
    }

    @Override
    public AbstractEntityParser<T> createParser(File csvFile) {
        if (importContext == null) {
            throw new IllegalStateException("Parser cache not set");
        }
        ModifierParser modifierParser = new ModifierTranslatedParser(importContext);
        return createParser(csvFile, modifierParser);
    }

    protected abstract AbstractEntityParser<T> createParser(File csvFile, ModifierParser modifierParser);

}
