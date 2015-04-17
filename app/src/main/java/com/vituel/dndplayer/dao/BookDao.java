package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;

import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 12/04/2015.
 */
public class BookDao extends AbstractEntityDao<Book> {

    public static final String TABLE = "book";

    private static String COLUMN_EDITION_ID = "edition_id";
    private static String COLUMN_ABBREVIATION = "abbreviation";
    private static String COLUMN_YEAR = "year";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EDITION_ID + " integer not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_ABBREVIATION + " text not null, "
            + COLUMN_YEAR + " integer "
            + ");";

    private EditionDao editionDao = new EditionDao(context, database);

    public BookDao(Context context) {
        super(context);
    }

    protected BookDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_EDITION_ID,
                COLUMN_NAME,
                COLUMN_ABBREVIATION,
                COLUMN_YEAR
        };
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
    protected Book fromCursor(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getInt(0));
        book.setName(cursor.getString(2));
        book.setAbbreviation(cursor.getString(3));
        book.setYear(cursor.getInt(4));

        Edition edition = editionDao.findById(cursor.getInt(1));
        book.setEdition(edition);
        return book;
    }

    public final List<Book> findByEdition(long editionId) {
        String query = String.format("%s=\'%s\'", COLUMN_EDITION_ID, editionId);
        return listForQuery(query);
    }

}
