package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.AbilityModifier;
import com.vituel.dndplayer.model.ModifierSource;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Multiplier;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class AbilityModifierDao extends AbstractAssociationDao<AbilityModifier> {

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

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected ContentValues toContentValues(long parentId, AbilityModifier entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, parentId);
        values.put(COLUMN_ABILITY, entity.getAbility().toString());
        values.put(COLUMN_MULTIPLIER, entity.getMultiplier().toString());
        values.put(COLUMN_TARGET, entity.getTarget().toString());
        values.put(COLUMN_TARGET_VARIATION, entity.getVariation());
        return values;
    }

    @Override
    protected AbilityModifier fromCursor(Cursor cursor) {

        AbilityModifier e = new AbilityModifier();
        e.setId(cursor.getLong(0));
        e.setAbility(ModifierSource.valueOf(cursor.getString(2)));
        e.setMultiplier(Multiplier.valueOf(cursor.getString(3)));
        e.setTarget(ModifierTarget.valueOf(cursor.getString(4)));
        e.setVariation(cursor.getString(5));

        return e;
    }

}
