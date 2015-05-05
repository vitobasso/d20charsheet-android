package com.vituel.dndplayer.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.model.effect.ModifierType;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class ModifierDao extends AbstractAssociationDao<Modifier> {

    public static final String TABLE = "modifier";

    private static final String COLUMN_EFFECT_ID = "effect_id";
    private static final String COLUMN_TARGET = "target";
    private static final String COLUMN_TARGET_VARIATION = "variation"; //specific skills are treated as variations of the "SKILL" target (because skills are dynamic)
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type"; //enhancement, luck, armor, etc
    private static final String COLUMN_CONDITION_NAME = "cond_name";
    private static final String COLUMN_CONDITION_PREDICATE = "cond_predicate";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_EFFECT_ID + " integer not null, "
            + COLUMN_TARGET + " text not null, "
            + COLUMN_TARGET_VARIATION + " text, "
            + COLUMN_AMOUNT + " text not null, "
            + COLUMN_TYPE + " text, "
            + COLUMN_CONDITION_NAME + " text, "
            + COLUMN_CONDITION_PREDICATE + " text"
            + ");";


    public ModifierDao(Context context) {
        super(context);
    }

    public ModifierDao(Context context, SQLiteDatabase database) {
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
                COLUMN_EFFECT_ID,
                COLUMN_TARGET,
                COLUMN_TARGET_VARIATION,
                COLUMN_AMOUNT,
                COLUMN_TYPE,
                COLUMN_CONDITION_NAME,
                COLUMN_CONDITION_PREDICATE
        };
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
            values.put(COLUMN_CONDITION_NAME, entity.getCondition().getName());
            values.put(COLUMN_CONDITION_PREDICATE, entity.getCondition().getPredicate().toString());
        }

        return values;
    }

    @Override
    public Modifier fromCursor(Cursor cursor) {

        Modifier e = new Modifier();
        e.setId(cursor.getLong(0));
        e.setTarget(ModifierTarget.valueOf(cursor.getString(2)));
        e.setVariation(cursor.getString(3));
        e.setAmount(new DiceRoll(cursor.getString(4)));
        String typeStr = cursor.getString(5);
        if (typeStr != null) {
            e.setType(ModifierType.valueOf(typeStr));
        }

        //condition
        Condition cond = new Condition();
        cond.setName(cursor.getString(6));
        String predStr = cursor.getString(7);
        if(predStr != null) {
            cond.setPredicate(Condition.Predicate.valueOf(predStr));
        }
        if(cond.getName() != null && cond.getPredicate() != null) {
            e.setCondition(cond);
        }

        return e;
    }

}
