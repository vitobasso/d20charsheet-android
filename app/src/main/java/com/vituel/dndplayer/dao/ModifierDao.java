package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.vituel.dndplayer.model.AbstractEffect;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Condition;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class ModifierDao extends AbstractEntityDao<Modifier> {

    public static final String TABLE = "modifier";

    private static final String COLUMN_SOURCE_TYPE = "source_type"; //item, trait, temp effect
    private static final String COLUMN_SOURCE_ID = "source_id";
    private static final String COLUMN_TARGET = "target";
    private static final String COLUMN_TARGET_VARIATION = "variation"; //specific skills are treated as variations of the "SKILL" target (because skills are dynamic)
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TYPE = "type"; //enhancement, luck, armor, etc
    private static final String COLUMN_CONDITION_NAME = "cond_name";
    private static final String COLUMN_CONDITION_PREDICATE = "cond_predicate";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SOURCE_TYPE + " text not null, "
            + COLUMN_SOURCE_ID + " integer not null, "
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

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_SOURCE_TYPE,
                COLUMN_SOURCE_ID,
                COLUMN_TARGET,
                COLUMN_TARGET_VARIATION,
                COLUMN_AMOUNT,
                COLUMN_TYPE,
                COLUMN_CONDITION_NAME,
                COLUMN_CONDITION_PREDICATE
        };
    }

    public List<Modifier> save(List<Modifier> modifiers, AbstractEffect.Type sourceType, long sourceId) {
        for (Modifier modifier : modifiers) {
            if (modifier == null || modifier.getTarget() == null || modifier.getAmount() == null) {
                Log.w(getClass().getSimpleName(), "Empty modifier ignored.");
                continue;
            }

            save(modifier, sourceType, sourceId);
        }
        return modifiers;
    }

    public AbstractEntity save(Modifier entity, AbstractEffect.Type sourceType, long sourceId) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE_TYPE, sourceType.toString());
        values.put(COLUMN_SOURCE_ID, sourceId);
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

        long id = database.insert(tableName(), null, values);
        entity.setId(id);
        return entity;
    }

    public List<Modifier> findAll(AbstractEffect.Type sourceType, long sourceId) {
        List<Modifier> list = new ArrayList<Modifier>();

        Cursor cursor = database.query(TABLE, allColumns(), query(sourceType, sourceId), null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    public void removeAllForEffect(AbstractEffect.Type sourceType, long sourceId) {
        database.delete(tableName(), query(sourceType, sourceId), null);
    }

    private String query(AbstractEffect.Type sourceType, long sourceId) {
        return String.format("%s='%s' and %s=%d", COLUMN_SOURCE_TYPE, sourceType, COLUMN_SOURCE_ID, sourceId);
    }

    @Override
    protected Modifier fromCursor(Cursor cursor) {

        Modifier e = new Modifier();
        e.setId(cursor.getLong(0));
        e.setTarget(ModifierTarget.valueOf(cursor.getString(3)));
        e.setVariation(cursor.getString(4));
        e.setAmount(new DiceRoll(cursor.getString(5)));
        String typeStr = cursor.getString(6);
        if (typeStr != null) {
            e.setType(ModifierType.valueOf(typeStr));
        }

        //condition
        Condition cond = new Condition();
        cond.setName(cursor.getString(7));
        String predStr = cursor.getString(8);
        if(predStr != null) {
            cond.setPredicate(Condition.Predicate.valueOf(predStr));
        }
        if(cond.getName() != null && cond.getPredicate() != null) {
            e.setCondition(cond);
        }

        return e;
    }

    @Override
    public void save(Modifier entity) {
        throw new UnsupportedOperationException("Use the one with sourceType and sourceId instead.");
    }

}
