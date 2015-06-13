package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.model.rulebook.Book;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;

/**
 * Created by Victor on 12/04/2015.
 */
public class CharBookDao extends AbstractAssociationDao<Book> {

    private static String COLUMN_CHAR_ID = "char_id";
    private static String COLUMN_BOOK_ID = "book_id";

    public static final Table TABLE = new Table("char_book")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .colNotNull(COLUMN_BOOK_ID, INTEGER);

    private BookDao bookDao = new BookDao(context, database);

    public CharBookDao(Context context) {
        super(context);
    }

    public CharBookDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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
    public Book fromCursor(Cursor cursor) {
        return bookDao.findById(getInt(cursor, COLUMN_BOOK_ID));
    }

}
