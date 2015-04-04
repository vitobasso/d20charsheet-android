package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.TempEffect;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class TempEffectDao extends AbstractEntityDao<TempEffect> {

    public static final String TABLE = "temp_effect";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EFFECT_ID + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context, database);
    private CharTempEffectDao charTempDao = new CharTempEffectDao(context, database);;

    public TempEffectDao(Context context) {
        super(context);
    }

    protected TempEffectDao(Context context, SQLiteDatabase database) {
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
                COLUMN_NAME,
                COLUMN_EFFECT_ID
        };
    }

    @Override
    public void save(TempEffect entity) {

        //basic data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());

        //effect
        Effect effect = entity.getEffect();
        effectDao.save(effect);
        values.put(COLUMN_EFFECT_ID, effect.getId());

        long id = insertOrUpdate(values, entity.getId());
        entity.setId(id);
    }

    @Override
    protected TempEffect fromCursor(Cursor cursor) {

        //basic fields
        TempEffect result = new TempEffect();
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));

        //effect
        Effect effect = effectDao.findById(cursor.getLong(2));
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        return result;
    }

    @Override
    public void remove(TempEffect e) {
        super.remove(e);
        charTempDao.removeAllForEffect(e.getId());
    }
}
