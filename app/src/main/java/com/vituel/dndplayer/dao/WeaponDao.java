package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Critical;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.WeaponProperties;

import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponDao extends AbstractEntityDao<WeaponProperties> {

    public static final String TABLE = "weapon";

    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_DAMAGE = "damage";
    private static final String COLUMN_CRIT_RANGE = "crit_range";
    private static final String COLUMN_CRIT_MULT = "crit_mult";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ITEM_ID + " integer, "
            + COLUMN_DAMAGE + " text not null, "
            + COLUMN_CRIT_RANGE + " integer not null, "
            + COLUMN_CRIT_MULT + " integer not null"
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
                COLUMN_ITEM_ID,
                COLUMN_DAMAGE,
                COLUMN_CRIT_RANGE,
                COLUMN_CRIT_MULT
        };
    }

    public void save(WeaponProperties weapon, long itemId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, itemId);
        values.put(COLUMN_DAMAGE, weapon.getDamage().toString());
        values.put(COLUMN_CRIT_RANGE, weapon.getCritical().getRange());
        values.put(COLUMN_CRIT_MULT, weapon.getCritical().getMultiplier());

        long id = insertOrUpdate(values, weapon.getId());
        weapon.setId(id);
    }

    @Override
    protected WeaponProperties fromCursor(Cursor cursor) {

        long id = cursor.getLong(0);
        DiceRoll damage = new DiceRoll(cursor.getString(2));
        int criticalRange = cursor.getInt(3);
        int criticalMultiplier = cursor.getInt(4);
        Critical critical = new Critical(criticalRange, criticalMultiplier);

        WeaponProperties weapon = new WeaponProperties(damage, critical);
        weapon.setId(id);
        return weapon;
    }

    @Override
    public void save(WeaponProperties entity) {
        throw new UnsupportedOperationException();
    }

    public WeaponProperties findByItem(long itemId) {
        String query = String.format("%s=%d", COLUMN_ITEM_ID, itemId);
        List<WeaponProperties> weapons = listForQuery(query);
        if (weapons.size() > 1) {
            throw new IllegalStateException("Multiple WeaponProperties found for Item id=" + itemId);
        } else if (weapons.isEmpty()) {
            return null;
        } else {
            return weapons.get(0);
        }
    }

}
