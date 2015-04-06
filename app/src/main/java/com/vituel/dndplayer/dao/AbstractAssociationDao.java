package com.vituel.dndplayer.dao;

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

    protected abstract String elementColumn();

    public List<T> findByParent(long parentId) {
        assert parentId != 0;
        Cursor cursor = database.query(tableName(), allColumns(), query(parentId), null, null, null, null);
        return cursorToList(cursor);
    }

    public void saveOverwrite(long parentId, Collection<T> list) {
        removeAllForParent(parentId);
        for (T obj : list) {
            save(parentId, obj);
        }
    }

    public abstract void save(long parentId, T entity);

    protected void insert(ContentValues values) {
        database.insert(tableName(), null, values);
    }

    protected void removeAllForParent(long parentId) {
        database.delete(tableName(), query(parentId), null);
    }

    private String query(long parentId) {
        return String.format("%s=%d", parentColumn(), parentId);
    }

    private String query(long parentId, long elementId) {
        return String.format("%s=%d and %s=%d", parentColumn(), parentId, elementColumn(), elementId);
    }

}
