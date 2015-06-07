package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.dependant.CharTempEffectDao;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.util.database.Table;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class TempEffectDao extends AbstractEntityDao<TempEffect> {

    public static final Table TABLE = new Table("temp_effect")
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_EFFECT_ID, INTEGER);

    private EffectDao effectDao = new EffectDao(context, database);

    public TempEffectDao(Context context) {
        super(context);
    }

    public TempEffectDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(TempEffect entity) {
        return effectDao.preSaveEffectSource(entity);
    }

    @Override
    public TempEffect fromCursor(Cursor cursor) {
        return effectDao.loadEffectSource(cursor, new TempEffect(), TABLE);
    }

    @Override
    public void postRemove(TempEffect e) {
        //not created in constructor to avoid cyclic reference
        CharTempEffectDao charTempDao = new CharTempEffectDao(context, database);
        charTempDao.removeAllForChild(e.getId());
    }
}
