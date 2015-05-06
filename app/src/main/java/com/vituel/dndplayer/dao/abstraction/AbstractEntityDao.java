package com.vituel.dndplayer.dao.abstraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vituel.dndplayer.model.AbstractEntity;

import java.text.MessageFormat;
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

    public final T findById(long id) {
        Cursor cursor = selectCursor(COLUMN_ID + " = " + id);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            T result = fromCursor(cursor);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }

    public final T findByName(String name) {
        String query = String.format("%s=\'%s\'", COLUMN_NAME, name);
        Cursor cursor = selectCursor(query);
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

    public final Cursor filterByNameCursor(String name) {
        String query = MessageFormat.format("{0} like ''%{1}%''", COLUMN_NAME, name);
        return selectCursor(query);
    }

    public final void save(Collection<T> list) {
        for (T obj : list) {
            save(obj);
        }
    }

    public final void save(T entity) {
        ContentValues values = toContentValues(entity);
        long id = insertOrUpdate(values, entity.getId());
        entity.setId(id);

        postSave(entity);
    }

    private final long insertOrUpdate(ContentValues values, long id) {
        if (id == 0) {
            values.remove(COLUMN_ID);
            id = database.insert(tableName(), "_id", values);
        } else {
            database.update(tableName(), values, COLUMN_ID + " = " + id, null);
        }
        return id;
    }

    @Override
    public final void insert(T entity) {
        ContentValues values = toContentValues(entity);
        values.put(COLUMN_ID, entity.getId());
        long id = database.insertOrThrow(tableName(), "_id", values);
        entity.setId(id);

        postSave(entity);
    }

    public final void remove(T entity) {
        removeForQuery(COLUMN_ID + " = " + entity.getId());
        postRemove(entity);
    }

    protected abstract ContentValues toContentValues(T entity);

    protected void postSave(T entity) {}

    protected void postRemove(T entity) {}

}
