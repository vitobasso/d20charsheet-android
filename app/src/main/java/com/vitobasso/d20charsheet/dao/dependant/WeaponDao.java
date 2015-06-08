package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.model.Critical;
import com.vitobasso.d20charsheet.model.DiceRoll;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;
import com.vitobasso.d20charsheet.util.database.Table;

import java.util.List;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14.
 */
public class WeaponDao extends AbstractAssociationDao<WeaponProperties> {

    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_DAMAGE = "damage";
    private static final String COLUMN_CRIT_RANGE = "crit_range";
    private static final String COLUMN_CRIT_MULT = "crit_mult";

    public static final Table TABLE = new Table("weapon")
            .col(COLUMN_ITEM_ID, INTEGER)
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
