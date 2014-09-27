package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.ActiveTempEffect;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.List;
import java.util.Map;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;


public class TempEffectActivityDao extends AbstractEntityDao<ActiveTempEffect> {

    public static final String TABLE = "active_temp_effect";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_EFFECT_ID = "temp_effect_id";
    private static final String COLUMN_ACTIVE = "active";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_EFFECT_ID + " integer not null, "
            + COLUMN_ACTIVE + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + TempEffectDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + ")"
            + ");";


    public TempEffectActivityDao(Context context) {
        super(context);
    }

    public TempEffectActivityDao(Context context, SQLiteDatabase database) {
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
                COLUMN_EFFECT_ID,
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
        values.put(COLUMN_EFFECT_ID, cond.getId());
        values.put(COLUMN_ACTIVE, active);

        database.insert(tableName(), null, values);
    }

    public List<ActiveTempEffect> listForChar(long charId) {
        return listForQuery(String.format("%s=%d", COLUMN_CHAR_ID, charId));
    }

    public void removeAllForEffect(long effectId) {
        removeForQuery(String.format("%s=%d", COLUMN_EFFECT_ID, effectId));
    }

    private void removeAllForChar(long charId) {
        removeForQuery(String.format("%s=%d", COLUMN_CHAR_ID, charId));
    }

    @Override
    protected ActiveTempEffect fromCursor(Cursor cursor) {

        //basic fields
        ActiveTempEffect result = new ActiveTempEffect();
        result.setId(cursor.getLong(0));
        result.setActive(cursor.getInt(3) != 0);

        //effect
        long condId = cursor.getLong(2);
        TempEffectDao condData = new TempEffectDao(context);
        result.setTempEffect(condData.findById(condId));

        return result;
    }

    @Override
    public void save(ActiveTempEffect entity) {
        throw new UnsupportedOperationException("Use the one with ActiveConditionals and charId instead.");
    }

}
