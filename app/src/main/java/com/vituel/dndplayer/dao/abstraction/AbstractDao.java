package com.vituel.dndplayer.dao.abstraction;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collection;
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
        try {
            List<T> list = new ArrayList<>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                T c = fromCursor(cursor);
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
