package com.vituel.dndplayer.dao.abstraction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractDao<T> {

    protected static SQLiteDatabase database;
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

    public final void close() {
        dbHelper.close();
        database = null;
    }

    public Cursor listAllCursor() {
        return database.query(tableName(), allColumns(), null, null, null, null, orderBy());
    }

    public final List<T> listAll() {
        Cursor cursor = listAllCursor();
        return cursorToList(cursor);
    }

    public Cursor selectCursor(String selection) {
        return database.query(tableName(), allColumns(), selection, null, null, null, orderBy());
    }

    protected final List<T> select(String selection) {
        Cursor cursor = selectCursor(selection);
        return cursorToList(cursor);
    }

    protected final List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T c = fromCursor(cursor);
            list.add(c);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    protected final void removeForQuery(String selection) {
        database.delete(tableName(), selection, null);
    }

    public void insert(Collection<T> list) {
        for (T obj : list) {
            insert(obj);
        }
    }

    protected String orderBy() {
        return null;
    }

    public void insert(T entity) {
        throw new UnsupportedOperationException();
    }

    protected abstract String tableName();

    protected abstract String[] allColumns();

    public abstract T fromCursor(Cursor cursor);

    public T fromCursorBrief(Cursor cursor) {
        return fromCursor(cursor);
    }

}
