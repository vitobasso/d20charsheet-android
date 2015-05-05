package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.ModifierSource;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class SkillDao extends AbstractEntityDao<Skill> {

    public static final String TABLE = "skill";

    private static final String COLUMN_KEY_ABILITY = "key_ability";
    private static final String COLUMN_ARMOR_PENALITY = "armor_penality_applies";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_KEY_ABILITY + " text not null, "
            + COLUMN_ARMOR_PENALITY + " integer not null"
            + ");";

    public SkillDao(Context context) {
        super(context);
    }

    public SkillDao(Context context, SQLiteDatabase database) {
        super(context);
        this.database = database;
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
                COLUMN_KEY_ABILITY,
                COLUMN_ARMOR_PENALITY
        };
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
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        ModifierSource ability = ModifierSource.valueOf(cursor.getString(2));
        boolean armorPenality = cursor.getInt(3) != 0;

        Skill result = new Skill(name);
        result.setId(id);
        result.setKeyAbility(ability);
        result.setArmorPenaltyApplies(armorPenality);

        return result;
    }
}
