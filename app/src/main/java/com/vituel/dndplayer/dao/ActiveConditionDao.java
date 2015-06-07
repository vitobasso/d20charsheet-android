package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.dao.entity.ConditionDao;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.util.database.Table;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;


public class ActiveConditionDao extends AbstractAssociationDao<Condition> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_CONDITION_ID = "condition_id";

    public static final Table TABLE = new Table("active_condition")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .colNotNull(COLUMN_CONDITION_ID, INTEGER);

    private ConditionDao conditionDao = new ConditionDao(context, database);

    public ActiveConditionDao(Context context) {
        super(context);
    }

    public ActiveConditionDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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
        return conditionDao.findById(getLong(cursor, COLUMN_CONDITION_ID));
    }

}
