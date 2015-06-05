package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;

import java.io.File;

/**
 * Created by Victor on 12/04/2015.
 */
public class BookParser extends AbstractEntityParser<Book> {

    public BookParser(Context ctx, File file) {
        super(ctx, file);
    }

    @Override
    protected Book parse(String[] split, Book result) throws ParseFieldException {
        result.setAbbreviation(readString(split, "abbr"));
        result.setYear(readIntNullable(split, "year"));

        Edition edition = new Edition();
        edition.setId(readInt(split, "dnd_edition_id"));
        result.setEdition(edition);

        return result;
    }

    @Override
    protected Book newInstance(String[] split) {
        return new Book();
    }

}
