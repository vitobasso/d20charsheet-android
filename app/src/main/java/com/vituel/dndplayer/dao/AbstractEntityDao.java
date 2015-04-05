package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vituel.dndplayer.model.AbstractEntity;

import java.util.Collection;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 28/02/14.
 */
public abstract class AbstractEntityDao<T extends AbstractEntity> extends AbstractDao<T> {

    public AbstractEntityDao(Context context) {
        super(context);
    }

    protected AbstractEntityDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    public T findById(long id) {
        Cursor cursor = database.query(tableName(), allColumns(), COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            T result = fromCursor(cursor);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }

    public T findByName(String name) {
        String query = String.format("%s=\'%s\'", COLUMN_NAME, name);
        Cursor cursor = database.query(tableName(), allColumns(), query, null, null, null, null);
        cursor.moveToFirst();
        T result = null;
        if (cursor.getCount() != 0) {
            result = fromCursor(cursor);
        } else {
            String msg = String.format("Couldn't find name '%s' in table '%s'", name, tableName());
            Log.w(getClass().getSimpleName(), msg);
        }
        cursor.close();
        return result;
    }

    protected long insertOrUpdate(ContentValues values, long id) {
        if (id == 0) {
            id = database.insert(tableName(), "_id", values);
        } else {
            database.update(tableName(), values, COLUMN_ID + " = " + id, null);
        }
        return id;
    }

    public void save(Collection<T> list) {
        for (T obj : list) {
            save(obj);
        }
    }

    public abstract void save(T entity);

    public void remove(T c) {
        removeForQuery(COLUMN_ID + " = " + c.getId());
    }

}
