package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vituel.dndplayer.model.TempEffect;

import static com.vituel.dndplayer.model.AbstractEffect.Type.TEMPORARY;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;


public class TempEffectDao extends AbstractEntityDao<TempEffect> {

    public static final String TABLE = "temp_effect";

    private static final String COLUMN_TYPE = "type";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TYPE + " text"
            + ");";


    public TempEffectDao(Context context) {
        super(context);
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
                COLUMN_TYPE
        };
    }

    @Override
    public void save(TempEffect entity) {

        //basic data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_TYPE, entity.getTempEffectType().toString());
        long id = insertOrUpdate(values, entity.getId());

        //modifiers
        ModifierDao effectData = new ModifierDao(context);
        effectData.removeAllForEffect(entity.getType(), id);
        effectData.save(entity.getModifiers(), TEMPORARY, id);

        entity.setId(id);
    }

    @Override
    protected TempEffect fromCursor(Cursor cursor) {

        //basic fields
        TempEffect result = new TempEffect();
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        result.setTempEffectType(TempEffect.Type.valueOf(cursor.getString(2)));

        //modifiers
        ModifierDao effectData = new ModifierDao(context);
        result.setModifiers(effectData.listAll(TEMPORARY, result.getId()));

        return result;
    }

    @Override
    public void remove(TempEffect e) {
        super.remove(e);

        TempEffectActivityDao activeDao = new TempEffectActivityDao(context, database);
        activeDao.removeAllForEffect(e.getId());
    }
}
