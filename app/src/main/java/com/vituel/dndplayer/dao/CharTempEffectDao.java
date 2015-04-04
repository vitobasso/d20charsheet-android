package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.CharTempEffect;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.List;
import java.util.Map;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class CharTempEffectDao extends AbstractEntityDao<CharTempEffect> {

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

    public void updateForChar(Map<TempEffect, Boolean> map, long charId){
        removeAllForChar(charId);
        for(TempEffect cond : map.keySet()){
            save(cond, map.get(cond), charId);
        }
    }

    private void save(TempEffect cond, boolean active, long charId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_TEMP_ID, cond.getId());
        values.put(COLUMN_ACTIVE, active);

        database.insert(tableName(), null, values);
    }

    public List<CharTempEffect> listForChar(long charId) {
        return listForQuery(String.format("%s=%d", COLUMN_CHAR_ID, charId));
    }

    public void removeAllForEffect(long effectId) {
        removeForQuery(String.format("%s=%d", COLUMN_TEMP_ID, effectId));
    }

    private void removeAllForChar(long charId) {
        removeForQuery(String.format("%s=%d", COLUMN_CHAR_ID, charId));
    }

    @Override
    protected CharTempEffect fromCursor(Cursor cursor) {

        //basic fields
        CharTempEffect result = new CharTempEffect();
        result.setId(cursor.getLong(0));
        result.setActive(cursor.getInt(3) != 0);

        //temp effect
        TempEffect tempEffect = tempEffectDao.findById(cursor.getLong(2));
        result.setName(tempEffect.getName());
        result.setEffect(tempEffect.getEffect());

        return result;
    }

    @Override
    public void save(CharTempEffect entity) {
        throw new UnsupportedOperationException("Use the one with ActiveConditionals and charId instead.");
    }

}
