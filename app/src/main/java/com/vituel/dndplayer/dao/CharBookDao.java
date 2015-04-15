package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.rulebook.Book;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 12/04/2015.
 */
public class CharBookDao extends AbstractAssociationDao<Book> {

    public static final String TABLE = "char_book";

    private static String COLUMN_CHAR_ID = "char_id";
    private static String COLUMN_BOOK_ID = "book_id";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_BOOK_ID + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_BOOK_ID + ") REFERENCES " + BookDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private BookDao bookDao = new BookDao(context, database);

    public CharBookDao(Context context) {
        super(context);
    }

    protected CharBookDao(Context context, SQLiteDatabase database) {
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
                COLUMN_CHAR_ID,
                COLUMN_BOOK_ID
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected ContentValues toContentValues(long parentId, Book entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, parentId);
        values.put(COLUMN_BOOK_ID, entity.getId());
        return values;
    }

    @Override
    protected Book fromCursor(Cursor cursor) {
        return bookDao.findById(cursor.getInt(2));
    }
}
