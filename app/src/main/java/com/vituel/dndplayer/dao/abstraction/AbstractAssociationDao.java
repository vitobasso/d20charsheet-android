package com.vituel.dndplayer.dao.abstraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;
import java.util.List;

/**
 * Created by Victor on 06/03/14.
 */
public abstract class AbstractAssociationDao<T> extends AbstractDao<T>{

    public AbstractAssociationDao(Context context) {
        super(context);
    }

    public AbstractAssociationDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    protected abstract String parentColumn();

    public final List<T> findByParent(long parentId) {
        assert parentId != 0;
        Cursor cursor = database.query(tableName(), allColumns(), query(parentId), null, null, null, null);
        return cursorToList(cursor);
    }

    public final void saveOverwrite(long parentId, Collection<T> list) {
        removeAllForParent(parentId);
        for (T obj : list) {
            save(parentId, obj);
        }
    }

    public final void save(long parentId, T entity) {
        ContentValues values = toContentValues(parentId, entity);
        database.insert(tableName(), null, values);
    }

    protected final void removeAllForParent(long parentId) {
        database.delete(tableName(), query(parentId), null);
    }

    private final String query(long parentId) {
        return String.format("%s=%d", parentColumn(), parentId);
    }

    protected abstract ContentValues toContentValues(long parentId, T entity);

}
