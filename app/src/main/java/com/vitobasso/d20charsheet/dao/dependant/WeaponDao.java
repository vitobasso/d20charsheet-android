package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.model.Critical;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;
import com.vitobasso.d20charsheet.util.database.Table;

import java.security.InvalidParameterException;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponDao extends AbstractDao<WeaponProperties> {

    private static final String COLUMN_DAMAGE = "damage";
    private static final String COLUMN_CRIT_RANGE = "crit_range";
    private static final String COLUMN_CRIT_MULT = "crit_mult";

    public static final Table TABLE = new Table("weapon")
            .colNotNull(COLUMN_DAMAGE, TEXT)
            .colNotNull(COLUMN_CRIT_RANGE, INTEGER)
            .colNotNull(COLUMN_CRIT_MULT, INTEGER);

    public WeaponDao(Context context) {
        super(context);
    }

    public WeaponDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    public void save(WeaponProperties weapon) {
        if (weapon.getId() == 0) {
            throw new InvalidParameterException("A WeaponProperties needs to have the same id as the associated Item");
        }
        ContentValues values = toContentValues(weapon);
        database.insert(tableName(), "_id", values);
    }

    protected ContentValues toContentValues(WeaponProperties weapon) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, weapon.getId());
        values.put(COLUMN_DAMAGE, weapon.getDamage().toString());
        values.put(COLUMN_CRIT_RANGE, weapon.getCritical().getRange());
        values.put(COLUMN_CRIT_MULT, weapon.getCritical().getMultiplier());
        return values;
    }

    public final WeaponProperties findById(long id) {
        Cursor cursor = cursor(COLUMN_ID + " = " + id);
        return firstResult(cursor);
    }

    @Override
    public WeaponProperties fromCursor(Cursor cursor) {

        long id = getLong(cursor, COLUMN_ID);
        DiceRoll damage = new DiceRoll(getString(cursor, COLUMN_DAMAGE));
        int criticalRange = getInt(cursor, COLUMN_CRIT_RANGE);
        int criticalMultiplier = getInt(cursor, COLUMN_CRIT_MULT);
        Critical critical = new Critical(criticalRange, criticalMultiplier);

        WeaponProperties weapon = new WeaponProperties();
        weapon.setId(id);
        weapon.setDamage(damage);
        weapon.setCritical(critical);
        return weapon;
    }

}
