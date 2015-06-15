package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.TempEffectDao;
import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.model.character.CharTempEffect;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;


public class CharTempEffectDao extends AbstractAssociationDao<CharTempEffect> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_TEMP_ID = "temp_effect_id";
    private static final String COLUMN_ACTIVE = "active";

    public static final Table TABLE = new Table("char_temp_effect")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .colNotNull(COLUMN_TEMP_ID, INTEGER)
            .colNotNull(COLUMN_ACTIVE, INTEGER);

    private TempEffectDao tempEffectDao = new TempEffectDao(context, database);

    public CharTempEffectDao(Context context) {
        super(context);
    }

    public CharTempEffectDao(Context context, SQLiteDatabase database) {
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
    protected ContentValues toContentValues(long charId, CharTempEffect tempEffect) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_TEMP_ID, tempEffect.getTempEffect().getId());
        values.put(COLUMN_ACTIVE, tempEffect.isActive());
        return values;
    }

    @Override
    public CharTempEffect fromCursor(Cursor cursor) {

        //basic fields
        CharTempEffect result = new CharTempEffect();
        result.setActive(getInt(cursor, COLUMN_ACTIVE) != 0);

        //temp effect
        TempEffect tempEffect = tempEffectDao.findById(cursor.getLong(2));
        result.setTempEffect(tempEffect);

        if (!validate(result)) { //to avoid null pointer when char points to temp effect that was deleted
            result = null;
        }
        return result;
    }

    private boolean validate(CharTempEffect result) { //
        return result.getTempEffect() != null;
    }

    public final void removeAllForChild(long childId) {
        removeForQuery(String.format("%s=%d", COLUMN_TEMP_ID, childId));
    }

}
