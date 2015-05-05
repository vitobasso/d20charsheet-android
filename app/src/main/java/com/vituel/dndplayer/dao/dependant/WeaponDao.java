package com.vituel.dndplayer.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.model.Critical;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.item.WeaponProperties;

import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponDao extends AbstractAssociationDao<WeaponProperties> {

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

    @Override
    protected String parentColumn() {
        return COLUMN_ITEM_ID;
    }

    @Override
    protected ContentValues toContentValues(long itemId, WeaponProperties weapon) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, itemId);
        values.put(COLUMN_DAMAGE, weapon.getDamage().toString());
        values.put(COLUMN_CRIT_RANGE, weapon.getCritical().getRange());
        values.put(COLUMN_CRIT_MULT, weapon.getCritical().getMultiplier());
        return values;
    }

    @Override
    public WeaponProperties fromCursor(Cursor cursor) {

        long id = cursor.getLong(0);
        DiceRoll damage = new DiceRoll(cursor.getString(2));
        int criticalRange = cursor.getInt(3);
        int criticalMultiplier = cursor.getInt(4);
        Critical critical = new Critical(criticalRange, criticalMultiplier);

        WeaponProperties weapon = new WeaponProperties();
        weapon.setId(id);
        weapon.setDamage(damage);
        weapon.setCritical(critical);
        return weapon;
    }

    public WeaponProperties findByItem(long itemId) {
        List<WeaponProperties> weapons = findByParent(itemId);
        if (weapons.size() > 1) {
            throw new IllegalStateException("Multiple WeaponProperties found for Item id=" + itemId);
        } else if (weapons.isEmpty()) {
            return null;
        } else {
            return weapons.get(0);
        }
    }

}
