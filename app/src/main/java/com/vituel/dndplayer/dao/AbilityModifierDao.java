package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vituel.dndplayer.model.AbilityModifier;
import com.vituel.dndplayer.model.ModifierSource;
import com.vituel.dndplayer.model.ModifierTarget;

import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class AbilityModifierDao extends AbstractEntityDao<AbilityModifier> {

    public static final String TABLE = "ability_modifier";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_ABILITY = "ability";
    private static final String COLUMN_MULTIPLIER = "multiplier";
    private static final String COLUMN_TARGET = "target";
    private static final String COLUMN_TARGET_VARIATION = "variation"; //specific skills are treated as variations of the "SKILL" target (because skills are dynamic)

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_ABILITY + " text not null, "
            + COLUMN_MULTIPLIER + " integer not null, "
            + COLUMN_TARGET + " text not null, "
            + COLUMN_TARGET_VARIATION + " text"
            + ");";


    public AbilityModifierDao(Context context) {
        super(context);
    }

    public AbilityModifierDao(Context context, SQLiteDatabase database) {
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
                COLUMN_CHAR_ID,
                COLUMN_ABILITY,
                COLUMN_MULTIPLIER,
                COLUMN_TARGET,
                COLUMN_TARGET_VARIATION
        };
    }

    public List<AbilityModifier> save(List<AbilityModifier> modifiers, long effectId) {
        for (AbilityModifier modifier : modifiers) {
            if (modifier == null || modifier.getTarget() == null || modifier.getAbility() == null) {
                Log.w(getClass().getSimpleName(), "Empty modifier ignored.");
                continue;
            }

            save(modifier, effectId);
        }
        return modifiers;
    }

    public AbilityModifier save(AbilityModifier entity, long charId) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_ABILITY, entity.getAbility().toString());
        values.put(COLUMN_MULTIPLIER, entity.getMultiplier().toString());
        values.put(COLUMN_TARGET, entity.getTarget().toString());
        values.put(COLUMN_TARGET_VARIATION, entity.getVariation());

        long id = database.insert(tableName(), null, values);
        entity.setId(id);
        return entity;
    }

    @Override
    protected AbilityModifier fromCursor(Cursor cursor) {

        AbilityModifier e = new AbilityModifier();
        e.setId(cursor.getLong(0));
        e.setAbility(ModifierSource.valueOf(cursor.getString(2)));
        e.setMultiplier(AbilityModifier.Multiplier.valueOf(cursor.getString(3)));
        e.setTarget(ModifierTarget.valueOf(cursor.getString(4)));
        e.setVariation(cursor.getString(5));

        return e;
    }

    public List<AbilityModifier> listAllForChar(long charId) {
        return listForQuery(query(charId));
    }

    public void removeAllForChar(long charId) {
        removeForQuery(query(charId));
    }

    private String query(long effectId) {
        return String.format("%s=%d", COLUMN_CHAR_ID, effectId);
    }

    @Override
    public void save(AbilityModifier entity) {
        throw new UnsupportedOperationException("Use the one with sourceType and sourceId instead.");
    }

}
