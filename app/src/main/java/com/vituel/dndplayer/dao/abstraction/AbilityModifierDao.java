package com.vituel.dndplayer.dao.abstraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.effect.AbilityModifier;
import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.model.effect.ModifierTarget;
import com.vituel.dndplayer.model.effect.Multiplier;
import com.vituel.dndplayer.util.database.Table;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class AbilityModifierDao extends AbstractAssociationDao<AbilityModifier> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_ABILITY = "ability";
    private static final String COLUMN_MULTIPLIER = "multiplier";
    private static final String COLUMN_TARGET = "target";
    private static final String COLUMN_TARGET_VARIATION = "variation"; //specific skills are treated as variations of the "SKILL" target (because skills are dynamic)

    public static final Table TABLE = new Table("ability_modifier")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .colNotNull(COLUMN_ABILITY, TEXT)
            .colNotNull(COLUMN_MULTIPLIER, INTEGER)
            .colNotNull(COLUMN_TARGET, TEXT)
            .col(COLUMN_TARGET_VARIATION, TEXT);

    public AbilityModifierDao(Context context) {
        super(context);
    }

    public AbilityModifierDao(Context context, SQLiteDatabase database) {
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
    public AbilityModifier fromCursor(Cursor cursor) {

        AbilityModifier e = new AbilityModifier();
        e.setId(getLong(cursor, COLUMN_ID));
        e.setAbility(ModifierSource.valueOf(getString(cursor, COLUMN_ABILITY)));
        e.setMultiplier(Multiplier.valueOf(getString(cursor, COLUMN_MULTIPLIER)));
        e.setTarget(ModifierTarget.valueOf(getString(cursor, COLUMN_TARGET)));
        e.setVariation(getString(cursor, COLUMN_TARGET_VARIATION));

        return e;
    }

}
