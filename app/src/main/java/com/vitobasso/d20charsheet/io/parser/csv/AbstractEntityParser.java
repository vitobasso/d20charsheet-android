package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.exception.ParseException;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.rulebook.Book;

import java.io.File;
import java.util.Locale;

/**
 * Created by Victor on 13/05/2015.
 */
public abstract class AbstractEntityParser<T extends AbstractEntity> extends AbstractCsvParser<T> {

    protected final String HEADER_ID = "id";
    protected final String HEADER_NAME_DEFAULT = "name";
    protected final String HEADER_NAME = getNameHeaderForLocale();
    protected final String HEADER_BOOK = "rulebook_id";

    public AbstractEntityParser(Context ctx, File file) {
        super(ctx, file);
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
        try {
            return readString(line, HEADER_NAME);
        } catch (ParseException e) {
            return readString(line, HEADER_NAME_DEFAULT);
        }
    }

    protected Book readRulebook(String[] line) throws ParseFieldException {
        return readRulebook(line, HEADER_BOOK);
    }

    protected abstract T parse(String[] line, T result) throws ParseFieldException;

    protected abstract T newInstance(String[] split) throws ParseFieldException;

}
