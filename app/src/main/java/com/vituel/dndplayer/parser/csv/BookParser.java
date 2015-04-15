package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 12/04/2015.
 */
public class BookParser extends AbstractSimpleParser<Book> {

    public BookParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Book parse(String[] split) throws ParseFieldException {
        Book result = new Book();
        result.setId(readInt(split, 0));
        result.setName(readString(split, 2));
        result.setAbbreviation(readString(split, 3));
        result.setYear(readIntNullable(split, 4));

        Edition edition = new Edition();
        edition.setId(readInt(split, 1));
        result.setEdition(edition);

        return result;
    }

}
