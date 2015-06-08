package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class SkillDao extends AbstractEntityDao<Skill> {

    private static final String COLUMN_KEY_ABILITY = "key_ability";
    private static final String COLUMN_ARMOR_PENALITY = "armor_penality_applies";

    public static final Table TABLE = new Table("skill")
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_KEY_ABILITY, TEXT)
            .colNotNull(COLUMN_ARMOR_PENALITY, INTEGER);

    public SkillDao(Context context) {
        super(context);
    }

    public SkillDao(Context context, SQLiteDatabase database) {
        super(context);
        this.database = database;
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(Skill entity) {
        //basic data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_KEY_ABILITY, entity.getKeyAbility().toString());
        values.put(COLUMN_ARMOR_PENALITY, entity.isArmorPenaltyApplies());
        return values;
    }

    @Override
    public Skill fromCursor(Cursor cursor) {

        //basic fields
        long id = getLong(cursor, COLUMN_ID);
        String name = getString(cursor, COLUMN_NAME);
        ModifierSource ability = ModifierSource.valueOf(getString(cursor, COLUMN_KEY_ABILITY));
        boolean armorPenality = getInt(cursor, COLUMN_ARMOR_PENALITY) != 0;

        Skill result = new Skill(name);
        result.setId(id);
        result.setKeyAbility(ability);
        result.setArmorPenaltyApplies(armorPenality);

        return result;
    }
}
