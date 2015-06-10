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
    private SQLiteStatement insertStmt;

    public BulkInserter(SQLiteDatabase database, Table table) {
        this.database = database;
        this.table = table;
        this.insertStmt = database.compileStatement(createInsertSql());
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public abstract void insert(T entity);

    public void insert(ContentValues values) {
        for (String column : values.keySet()) {
            int index = table.getIndex(column);
            Object value = values.get(column);
            bindValue(index + 1, value);
        }
        insertStmt.executeInsert();
        insertStmt.clearBindings();
    }

    private void bindValue(int index, Object value) {
        if (value == null) {
            insertStmt.bindNull(index);
        } else if (value instanceof String) {
            insertStmt.bindString(index, (String) value);
        } else if (value instanceof Long || value instanceof Integer) {
            insertStmt.bindLong(index, ((Number) value).longValue());
        } else if (value instanceof Double || value instanceof Float) {
            insertStmt.bindDouble(index, ((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            insertStmt.bindLong(index, ((Boolean) value) ? 1 : 0);
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

    private String createInsertSql() {
        String columnsStr = Joiner.on(',').join(table.getColumnNames());
        String placeholdersStr = Strings.repeat("?,", table.countColumns());
        placeholdersStr = StringUtils.removeEnd(placeholdersStr, ",");
        return String.format("INSERT OR REPLACE INTO %s (%s) VALUES (%s)", table.getName(), columnsStr, placeholdersStr);
    }

}
