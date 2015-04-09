package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.CharTempEffect;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.util.database.SQLiteHelper;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class CharTempEffectDao extends AbstractAssociationDao<CharTempEffect> {

    public static final String TABLE = "active_temp_effect";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_TEMP_ID = "temp_effect_id";
    private static final String COLUMN_ACTIVE = "active";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_TEMP_ID + " integer not null, "
            + COLUMN_ACTIVE + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_TEMP_ID + ") REFERENCES " + TempEffectDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + ")"
            + ");";

    private TempEffectDao tempEffectDao = new TempEffectDao(context, database);

    public CharTempEffectDao(Context context) {
        super(context);
    }

    public CharTempEffectDao(Context context, SQLiteDatabase database) {
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
                COLUMN_CHAR_ID,
                COLUMN_TEMP_ID,
                COLUMN_ACTIVE
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected String elementColumn() {
        return COLUMN_TEMP_ID;
    }

    @Override
    public void save(long charId, CharTempEffect tempEffect) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_TEMP_ID, tempEffect.getTempEffect().getId());
        values.put(COLUMN_ACTIVE, tempEffect.isActive());

        database.insert(tableName(), null, values);
    }

    @Override
    protected CharTempEffect fromCursor(Cursor cursor) {

        //basic fields
        CharTempEffect result = new CharTempEffect();
        result.setActive(cursor.getInt(3) != 0);

        //temp effect
        TempEffect tempEffect = tempEffectDao.findById(cursor.getLong(2));
        result.setTempEffect(tempEffect);

        return result;
    }

}
