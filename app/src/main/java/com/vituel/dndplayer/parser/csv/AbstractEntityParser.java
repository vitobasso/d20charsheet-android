package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.util.Locale;

/**
 * Created by Victor on 13/05/2015.
 */
public abstract class AbstractEntityParser<T extends AbstractEntity> extends AbstractCsvParser<T> {

    protected final String HEADER_ID = "id";
    protected final String HEADER_NAME_DEFAULT = "name";
    protected final String HEADER_NAME = getNameHeaderForLocale();

    public AbstractEntityParser(Context ctx, String path) {
        super(ctx, path);
    }

    public String getNameHeaderForLocale() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "pt":
                return "name_pt";
            default:
                return HEADER_NAME_DEFAULT; // english
        }
    }

    @Override
    protected final T parse(String[] line) throws ParseFieldException {
        T result = newInstance(line);
        result.setId(readInt(line, HEADER_ID));
        result.setName(readName(line));

        return parse(line, result);
    }

    private String readName(String[] line) throws ParseFieldException {
        String name = readStringNullable(line, HEADER_NAME);
        if (name == null) {
            name = readString(line, HEADER_NAME_DEFAULT);
        }
        return name;
    }

    protected abstract T parse(String[] line, T result) throws ParseFieldException;

    protected abstract T newInstance(String[] split) throws ParseFieldException;
}
