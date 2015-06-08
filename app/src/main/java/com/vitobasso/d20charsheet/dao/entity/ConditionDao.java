package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 12/04/2015.
 */
public class ConditionDao extends AbstractEntityDao<Condition> {

    private static String COLUMN_PREDICATE = "predicate";
    private static String COLUMN_PARENT_ID = "parent_id";

    public static final Table TABLE = new Table("condition")
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_PREDICATE, TEXT)
            .col(COLUMN_PARENT_ID, INTEGER);

    public ConditionDao(Context context) {
        super(context);
    }

    public ConditionDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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
        condition.setId(getLong(cursor, COLUMN_ID));
        condition.setName(getString(cursor, COLUMN_NAME));
        condition.setPredicate(Condition.Predicate.valueOf(getString(cursor, COLUMN_PREDICATE)));

        Condition parent = findById(getInt(cursor, COLUMN_PARENT_ID));
        condition.setParent(parent);
        return condition;
    }

}
