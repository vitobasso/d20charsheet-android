package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Critical;
import com.vituel.dndplayer.model.WeaponProperties;
import com.vituel.dndplayer.model.DiceRoll;

import java.text.MessageFormat;

import static com.vituel.dndplayer.model.WeaponProperties.RangeType;
import static com.vituel.dndplayer.model.WeaponProperties.WeightType;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponDao extends AbstractDao<WeaponProperties> {

    public static final String TABLE = "weapon";

    private static final String COLUMN_DAMAGE = "damage";
    private static final String COLUMN_CRIT_RANGE = "crit_range";
    private static final String COLUMN_CRIT_MULT = "crit_mult";
    private static final String COLUMN_STATUS_EFFECT = "status_effect";
    private static final String COLUMN_RANGE = "range";
    private static final String COLUMN_RANGE_TYPE = "range_type";
    private static final String COLUMN_WEIGHT_TYPE = "weight_type";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_DAMAGE + " text not null, "
            + COLUMN_CRIT_RANGE + " integer not null, "
            + COLUMN_CRIT_MULT + " integer not null, "
            + COLUMN_RANGE + " integer not null, "
            + COLUMN_RANGE_TYPE + " text not null, "
            + COLUMN_WEIGHT_TYPE + " text not null, "
            + COLUMN_STATUS_EFFECT + " text"
            + ");";

    public WeaponDao(Context context) {
        super(context);
    }

    public WeaponDao(Context context, SQLiteDatabase database) {
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
                COLUMN_DAMAGE,
                COLUMN_CRIT_RANGE,
                COLUMN_CRIT_MULT,
                COLUMN_RANGE,
                COLUMN_RANGE_TYPE,
                COLUMN_WEIGHT_TYPE,
                COLUMN_STATUS_EFFECT
        };
    }

    public long save(WeaponProperties weapon) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, weapon.getName());
        values.put(COLUMN_DAMAGE, weapon.getDamage().toString());
        values.put(COLUMN_CRIT_RANGE, weapon.getCritical().getRange());
        values.put(COLUMN_CRIT_MULT, weapon.getCritical().getMultiplier());
        values.put(COLUMN_RANGE, weapon.getRange());
        values.put(COLUMN_RANGE_TYPE, weapon.getRangeType().toString());
        values.put(COLUMN_WEIGHT_TYPE, weapon.getWeightType().toString());
        values.put(COLUMN_STATUS_EFFECT, weapon.getStatusEffect());

        long id = database.insert(tableName(), null, values);
        weapon.setId(id);
        return id;
    }

    @Override
    protected WeaponProperties fromCursor(Cursor cursor) {

        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        DiceRoll damage = new DiceRoll(cursor.getString(2));
        int criticalRange = cursor.getInt(3);
        int criticalMultiplier = cursor.getInt(4);
        Critical critical = new Critical(criticalRange, criticalMultiplier);
        int range = cursor.getInt(5);
        RangeType rangeType = RangeType.valueOf(cursor.getString(6));
        WeightType weightType = WeightType.valueOf(cursor.getString(7));
        String statusEffect = cursor.getString(8);

        WeaponProperties weapon = new WeaponProperties(name, damage, critical, range, statusEffect, rangeType, weightType);
        weapon.setId(id);
        return weapon;
    }

    public void remove(long id) {
        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_ID, id);
        database.delete(tableName(), removeQuery, null);
    }

}
