package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Feat;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class FeatDao extends AbstractEntityDao<Feat> {

    public static final String TABLE = "feat";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EFFECT_ID + " integer, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context);;

    public FeatDao(Context context) {
        super(context);
    }

    protected FeatDao(Context context, SQLiteDatabase database) {
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
    public void save(Feat entity) {
        ContentValues values = effectDao.preSaveEffectSource(entity);
        long id = insertOrUpdate(values, entity.getId());
        entity.setId(id);
    }

    @Override
    protected Feat fromCursor(Cursor cursor) {
        return effectDao.loadEffectSource(cursor, new Feat(), 0, 1, 2);
    }

}
