package com.vitobasso.d20charsheet.util.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;

/**
 * Created by Victor on 06/06/2015.
 */
public abstract class BulkInserter<T> {

    private SQLiteDatabase database;
    private Table table;
    private SQLiteStatement statement;

    public BulkInserter(SQLiteDatabase database, Table table) {
        this.database = database;
        this.table = table;
    }

    public void begin() {
        database.beginTransaction();
        String sql = createSql();
        statement = database.compileStatement(sql);
    }

    public abstract void insert(T entity);

    public void insert(ContentValues values) {
        for (String column : values.keySet()) {
            int index = table.getIndex(column);
            Object value = values.get(column);
            bindValue(index + 1, value);
        }
        statement.execute();
        statement.clearBindings();
    }

    private void bindValue(int index, Object value) {
        if (value == null) {
            statement.bindNull(index);
        } else if (value instanceof String) {
            statement.bindString(index, (String) value);
        } else if (value instanceof Long || value instanceof Integer) {
            statement.bindLong(index, ((Number) value).longValue());
        } else if (value instanceof Double || value instanceof Float) {
            statement.bindDouble(index, ((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            statement.bindLong(index, ((Boolean) value) ? 1 : 0);
        } else {
            throw new InvalidParameterException(value.getClass().getSimpleName());
        }
    }

    public void markAsSuccessful() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    private String createSql() {
        String columnsStr = Joiner.on(',').join(table.getColumnNames());
        String placehodersStr = Strings.repeat("?,", table.countColumns());
        placehodersStr = StringUtils.removeEnd(placehodersStr, ",");
        return String.format("INSERT INTO %s (%s) VALUES (%s)", table.getName(), columnsStr, placehodersStr);
    }

}
