package com.vituel.dndplayer.dao;

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

    public AbstractEntityDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    public T findByName(String name) {
        String query = String.format("%s=\"%s\"", COLUMN_NAME, name);
        Cursor cursor = database.query(tableName(), allColumns(), query, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            T result = fromCursor(cursor);
            cursor.close();
            return result;
        } else {
            cursor.close();
            String msg = String.format("Couldn't find name '%s' in table '%s'", name, tableName());
            Log.w(getClass().getSimpleName(), msg);
            return null;
        }
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
