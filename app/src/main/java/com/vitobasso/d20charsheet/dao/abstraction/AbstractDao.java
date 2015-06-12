package com.vitobasso.d20charsheet.dao.abstraction;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Function;
import com.vitobasso.d20charsheet.util.database.BulkInserter;
import com.vitobasso.d20charsheet.util.database.SQLiteHelper;
import com.vitobasso.d20charsheet.util.database.Table;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractDao<T> {

    protected SQLiteDatabase database;
    protected Context context;
    protected SQLiteHelper dbHelper;

    public AbstractDao(Context context) {
        this.context = context;
        this.dbHelper = new SQLiteHelper(context);
        if (database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    protected AbstractDao(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }

    protected abstract Table getTable();

    protected String tableName() {
        return getTable().getName();
    }

    protected String[] allColumns() { //TODO remove when all daos implement getTable
        return getTable().getColumnNames();
    }

    public final void close() {
        dbHelper.close();
        database = null;
    }

    public Cursor listAllCursor() {
        return cursor(null);
    }

    public final List<T> listAll() {
        Cursor cursor = listAllCursor();
        return list(cursor);
    }

    public final List<T> listAllBrief() {
        Cursor cursor = listAllCursor();
        return listBrief(cursor);
    }

    public Cursor cursor(String selection) {
        return database.query(tableName(), allColumns(), selection, null, null, null, orderBy());
    }

    protected final List<T> select(String selection) {
        Cursor cursor = cursor(selection);
        return list(cursor);
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(database, tableName());
    }

    protected String appendWhereClause(String base, String newClause) {
        StringBuilder str = new StringBuilder();
        if (!isNullOrEmpty(base)) {
            str.append(base);
        }
        if (!isNullOrEmpty(base) && !isNullOrEmpty(newClause)) {
            str.append(" and ");
        }
        if (!isNullOrEmpty(newClause)) {
            str.append(newClause);
        }
        return str.toString();
    }

    protected final List<T> list(Cursor cursor) {
        return listTemplate(cursor, new Function<Cursor, T>() {
            public T apply(Cursor cursor) {
                return fromCursor(cursor);
            }
        });
    }

    protected final List<T> listBrief(Cursor cursor) {
        return listTemplate(cursor, new Function<Cursor, T>() {
            public T apply(Cursor cursor) {
                return fromCursorBrief(cursor);
            }
        });
    }

    protected final List<T> listTemplate(Cursor cursor, Function<Cursor, T> readCursor) {
        try {
            List<T> list = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                T c = readCursor.apply(cursor);
                list.add(c);
                cursor.moveToNext();
            }
            return list;
        } finally {
            cursor.close();
        }
    }

    protected final T firstResult(Cursor cursor) {
        try {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                return fromCursor(cursor);
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    protected final void removeForQuery(String selection) {
        database.delete(tableName(), selection, null);
    }

    protected String orderBy() {
        return null;
    }

    public abstract T fromCursor(Cursor cursor);

    public T fromCursorBrief(Cursor cursor) {
        return fromCursor(cursor);
    }

    public BulkInserter<T> createBulkInserter() {
        throw new UnsupportedOperationException();
    }

    protected String getString(Cursor cursor, String columnName) {
        return cursor.getString(getTable().getIndex(columnName));
    }

    protected int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(getTable().getIndex(columnName));
    }

    protected long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(getTable().getIndex(columnName));
    }

    protected double getDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(getTable().getIndex(columnName));
    }

}
