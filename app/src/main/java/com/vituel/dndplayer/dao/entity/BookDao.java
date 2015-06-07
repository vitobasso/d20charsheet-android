package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.collect.Lists;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.util.database.Table;

import java.util.List;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 12/04/2015.
 */
public class BookDao extends AbstractEntityDao<Book> {

    private static String COLUMN_EDITION_ID = "edition_id";
    private static String COLUMN_ABBREVIATION = "abbreviation";
    private static String COLUMN_YEAR = "year";

    public static final Table TABLE = new Table("book")
            .colNotNull(COLUMN_EDITION_ID, INTEGER)
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_ABBREVIATION, TEXT)
            .col(COLUMN_YEAR, INTEGER);

    private EditionDao editionDao = new EditionDao(context, database);

    public BookDao(Context context) {
        super(context);
    }

    public BookDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(Book entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_EDITION_ID, entity.getEdition().getId());
        values.put(COLUMN_ABBREVIATION, entity.getAbbreviation());
        values.put(COLUMN_YEAR, entity.getYear());
        return values;
    }

    @Override
    public Book fromCursor(Cursor cursor) {
        Book book = new Book();
        book.setId(getInt(cursor, COLUMN_ID));
        book.setName(getString(cursor, COLUMN_NAME));
        book.setAbbreviation(getString(cursor, COLUMN_ABBREVIATION));
        book.setYear(getInt(cursor, COLUMN_YEAR));

        Edition edition = editionDao.findById(getInt(cursor, COLUMN_EDITION_ID));
        book.setEdition(edition);
        return book;
    }

    public final List<Book> findByEdition(long editionId) {
        String query = String.format("%s=\'%s\'", COLUMN_EDITION_ID, editionId);
        return select(query);
    }

    public static List<Book> getDefaultActiveBooks(Context context) {
        BookDao bookDao = new BookDao(context);
        Book playersHandbook = bookDao.findById(6);
        bookDao.close();
        return Lists.newArrayList(playersHandbook);
    }

}
