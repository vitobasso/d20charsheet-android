package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.dao.entity.ConditionDao;
import com.vituel.dndplayer.model.effect.Condition;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class ActiveConditionDao extends AbstractAssociationDao<Condition> {

    public static final String TABLE = "active_condition";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_CONDITION_ID = "condition_id";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_CONDITION_ID + " integer not null"
            + ");";

    private ConditionDao conditionDao = new ConditionDao(context, database);

    public ActiveConditionDao(Context context) {
        super(context);
    }

    public ActiveConditionDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    protected String tableName() {
        return TABLE;
    }

    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_CHAR_ID,
                COLUMN_CONDITION_ID
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected ContentValues toContentValues(long charId, Condition condiiton) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONDITION_ID, condiiton.getId());
        values.put(COLUMN_CHAR_ID, charId);
        return values;
    }

    @Override
    public Condition fromCursor(Cursor cursor) {
        return conditionDao.findById(cursor.getLong(2));
    }

}
