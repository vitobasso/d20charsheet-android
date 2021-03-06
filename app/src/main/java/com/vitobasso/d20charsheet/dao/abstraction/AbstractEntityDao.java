package com.vitobasso.d20charsheet.dao.abstraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.util.database.BulkInserter;

import java.text.MessageFormat;
import java.util.Collection;

import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

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
        Cursor cursor = cursor(COLUMN_ID + " = " + id);
        return firstResult(cursor);
    }

    public final T findBriefById(long id) {
        Cursor cursor = cursor(COLUMN_ID + " = " + id);
        return firstResultBrief(cursor);
    }

    public final T findByName(String name) {
        String query = String.format("%s=\'%s\'", COLUMN_NAME, name);
        Cursor cursor = cursor(query);
        return firstResult(cursor);
    }

    public final Cursor filterByNameCursor(String name) {
        String query = MessageFormat.format("{0} like ''%{1}%''", COLUMN_NAME, name);
        return cursor(query);
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

    private long insertOrUpdate(ContentValues values, long id) {
        if (id == 0) {
            values.remove(COLUMN_ID);
            id = database.insert(tableName(), "_id", values);
        } else {
            database.update(tableName(), values, COLUMN_ID + " = " + id, null);
        }
        return id;
    }

    public final void remove(T entity) {
        removeForQuery(COLUMN_ID + " = " + entity.getId());
        postRemove(entity);
    }

    @Override
    protected String orderBy() {
        return COLUMN_NAME;
    }

    protected abstract ContentValues toContentValues(T entity);

    protected void postSave(T entity) {}

    protected void postRemove(T entity) {}

    public BulkInserter<T> createBulkInserter() {
        return new BulkInserter<T>(database, getTable()) {
            public void insert(T entity) {
                ContentValues values = toContentValues(entity);
                values.put(COLUMN_ID, entity.getId());
                insert(values);
                postSave(entity); //TODO inserting outside bulk (no transaction, no prepared statement)
            }
        };
    }

}
