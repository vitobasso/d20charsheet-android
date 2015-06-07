package com.vituel.dndplayer.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.dao.entity.TempEffectDao;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.model.character.CharTempEffect;
import com.vituel.dndplayer.util.database.Table;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;


public class CharTempEffectDao extends AbstractAssociationDao<CharTempEffect> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_TEMP_ID = "temp_effect_id";
    private static final String COLUMN_ACTIVE = "active";

    public static final Table TABLE = new Table("active_temp_effect")
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

        return result;
    }


    public final void removeAllForChild(long childId) {
        removeForQuery(String.format("%s=%d", COLUMN_TEMP_ID, childId));
    }

}
