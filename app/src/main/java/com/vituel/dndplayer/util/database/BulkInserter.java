package com.vituel.dndplayer.util.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * Created by Victor on 06/06/2015.
 */
public class BulkInserter<T> {

    private SQLiteDatabase database;
    private String tableName;
    private String[] columnNames;
    private SQLiteStatement statement;

    public BulkInserter(SQLiteDatabase database, String tableName, String[] columnNames) {
        this.database = database;
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    public void begin() {
        database.beginTransaction();
        String sql = createSql();
        statement = database.compileStatement(sql);
    }

    public void insert(ContentValues values) {
        for (String column : values.keySet()) {
            bindValue(values, column);
        }
        statement.execute();
        statement.clearBindings();
    }

    private void bindValue(ContentValues values, String column) {
        Object value = values.get(column);
//        if (value instanceof Long) {
//            statement.bindLong(?, (Long) value);
//        } else if (value instanceof Double) {
//            statement.bindDouble(?, (Double) value);
//        } else if (value instanceof String) {
//            statement.bindString(?, (String) value);
//        } else {
//            throw new InvalidParameterException(value.getClass().getSimpleName());
//        }
    }

    public void markAsSuccessful() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    private String createSql() {
        String columnsStr = Joiner.on(',').join(columnNames);
        String placehodersStr = Strings.repeat("?,", columnNames.length);
        placehodersStr = placehodersStr.substring(0, placehodersStr.length() - 1);
        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnsStr, placehodersStr);
    }

}
