package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.model.effect.Condition;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 12/04/2015.
 */
public class ConditionDao extends AbstractEntityDao<Condition> {

    public static final String TABLE = "condition";

    private static String COLUMN_PREDICATE = "predicate";
    private static String COLUMN_PARENT_ID = "parent_id";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PREDICATE + " text not null, "
            + COLUMN_PARENT_ID + " integer"
            + ");";

    public ConditionDao(Context context) {
        super(context);
    }

    public ConditionDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_PREDICATE,
                COLUMN_PARENT_ID
        };
    }

    @Override
    protected ContentValues toContentValues(Condition entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_PREDICATE, entity.getPredicate().toString());
        Condition parent = entity.getParent();
        Long parentId = parent != null ? parent.getId() : null;
        values.put(COLUMN_PARENT_ID, parentId);
        return values;
    }

    @Override
    public Condition fromCursor(Cursor cursor) {
        Condition condition = new Condition();
        condition.setId(cursor.getInt(0));
        condition.setName(cursor.getString(1));
        condition.setPredicate(Condition.Predicate.valueOf(cursor.getString(2)));

        Condition parent = findById(cursor.getInt(3));
        condition.setParent(parent);
        return condition;
    }

}
