package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.dependant.CharTempEffectDao;
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

    public TempEffectDao(Context context) {
        super(context);
    }

    public TempEffectDao(Context context, SQLiteDatabase database) {
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
    protected ContentValues toContentValues(TempEffect entity) {
        return effectDao.preSaveEffectSource(entity);
    }

    @Override
    public TempEffect fromCursor(Cursor cursor) {
        return effectDao.loadEffectSource(cursor, new TempEffect(), 0, 1, 2);
    }

    @Override
    public void postRemove(TempEffect e) {
        //not created in constructor to avoid cyclic reference
        CharTempEffectDao charTempDao = new CharTempEffectDao(context, database);
        charTempDao.removeAllForChild(e.getId());
    }
}
