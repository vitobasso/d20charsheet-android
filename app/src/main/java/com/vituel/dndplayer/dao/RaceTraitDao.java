package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Effect;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 06/03/14.
 */
public class RaceTraitDao extends AbstractAssociationDao<Effect> {

    public static final String TABLE = "race_trait";

    private static final String COLUMN_RACE_ID = "race_id";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_RACE_ID + " integer not null, "
            + COLUMN_EFFECT_ID + " integer, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_RACE_ID + ") REFERENCES " + RaceDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context, database);;

    public RaceTraitDao(Context context) {
        super(context);
    }

    protected RaceTraitDao(Context context, SQLiteDatabase database) {
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
                COLUMN_RACE_ID,
                COLUMN_EFFECT_ID
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_RACE_ID;
    }

    @Override
    protected String elementColumn() {
        return COLUMN_EFFECT_ID;
    }

    @Override
    public void save(long parentId, Effect effect) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, effect.getName());
        values.put(COLUMN_RACE_ID, parentId);

        //effect
        effectDao.save(effect);
        values.put(COLUMN_EFFECT_ID, effect.getId());

        insertOrUpdate(values, parentId, effect.getId());
    }

    @Override
    protected Effect fromCursor(Cursor cursor) {
        //TODO name not used
        return effectDao.findById(cursor.getLong(3)); // source name set in RaceDao
    }

}
