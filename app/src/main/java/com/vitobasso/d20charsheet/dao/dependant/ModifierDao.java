package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.ConditionDao;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.model.effect.ModifierType;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;

public class ModifierDao extends AbstractAssociationDao<Modifier> {

    private static final String COLUMN_EFFECT_ID = "effect_id";
    private static final String COLUMN_TARGET = "target";
    private static final String COLUMN_TARGET_VARIATION = "variation"; //specific skills are treated as variations of the "SKILL" target (because skills are dynamic)
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type"; //enhancement, luck, armor, etc
    private static final String COLUMN_CONDITION_ID = "cond_name";

    public static final Table TABLE = new Table("modifier")
            .colNotNull(COLUMN_EFFECT_ID, INTEGER)
            .colNotNull(COLUMN_TARGET, TEXT)
            .col(COLUMN_TARGET_VARIATION, TEXT)
            .colNotNull(COLUMN_AMOUNT, TEXT)
            .col(COLUMN_TYPE, TEXT)
            .col(COLUMN_CONDITION_ID, INTEGER);

    private ConditionDao conditionDao = new ConditionDao(context, database);

    public ModifierDao(Context context) {
        super(context);
    }

    public ModifierDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected String parentColumn() {
        return COLUMN_EFFECT_ID;
    }

    @Override
    protected ContentValues toContentValues(long parentId, Modifier entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EFFECT_ID, parentId);
        values.put(COLUMN_TARGET, entity.getTarget().toString());
        values.put(COLUMN_TARGET_VARIATION, entity.getVariation());
        values.put(COLUMN_AMOUNT, entity.getAmount().toString());
        if (entity.getType() != null) {
            values.put(COLUMN_TYPE, entity.getType().toString());
        }
        if (entity.getCondition() != null) {
            values.put(COLUMN_CONDITION_ID, entity.getCondition().getId());
        }

        return values;
    }

    @Override
    public Modifier fromCursor(Cursor cursor) {

        Modifier result = new Modifier();
        result.setId(getLong(cursor, COLUMN_ID));
        result.setTarget(ModifierTarget.valueOf(getString(cursor, COLUMN_TARGET)));
        result.setVariation(getString(cursor, COLUMN_TARGET_VARIATION));
        result.setAmount(new DiceRoll(getString(cursor, COLUMN_AMOUNT)));
        String typeStr = getString(cursor, COLUMN_TYPE);
        if (typeStr != null) {
            result.setType(ModifierType.valueOf(typeStr));
        }

        //condition
        Condition condition = conditionDao.findById(getLong(cursor, COLUMN_CONDITION_ID));
        result.setCondition(condition);

        return result;
    }

}
