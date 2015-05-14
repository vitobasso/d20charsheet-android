package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 12/04/2015.
 */
public class BookParser extends AbstractEntityParser<Book> {

    public BookParser(Context ctx, String filePath) {
        super(ctx, filePath);
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
