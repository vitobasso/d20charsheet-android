package com.vituel.dndplayer.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.ArrayList;
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

    protected abstract String tableName();

    protected abstract String[] allColumns();

    public void close() {
        dbHelper.close();
        database = null;
    }

    public List<T> listAll() {
        Cursor cursor = database.query(tableName(), allColumns(), null, null, null, null, null);
        return cursorToList(cursor);
    }

    protected List<T> listForQuery(String selection) {
        Cursor cursor = database.query(tableName(), allColumns(), selection, null, null, null, null);
        return cursorToList(cursor);
    }

    protected abstract T fromCursor(Cursor cursor);

    protected List<T> cursorToList(Cursor cursor) {
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

    protected void removeForQuery(String selection) {
        database.delete(tableName(), selection, null);
    }

}
