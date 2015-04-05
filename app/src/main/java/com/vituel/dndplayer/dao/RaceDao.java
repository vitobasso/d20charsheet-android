package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;

import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class RaceDao extends AbstractEntityDao<Race> {

    public static final String TABLE = "race";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EFFECT_ID + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context, database);
    private RaceTraitDao raceTraitDao = new RaceTraitDao(context, database);;

    public RaceDao(Context context) {
        super(context);
    }

    protected RaceDao(Context context, SQLiteDatabase database) {
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
    public void save(Race entity) {
        ContentValues values = effectDao.preSaveEffectSource(entity);
        long id = insertOrUpdate(values, entity.getId());
        entity.setId(id);
    }

    @Override
    protected Race fromCursor(Cursor cursor) {
        Race result = effectDao.loadEffectSource(cursor, new Race(), 0, 1, 2);

        //racial traits
        List<RaceTrait> traits = raceTraitDao.findByParent(result.getId());
        result.setTraits(traits);

        return result;
    }
}
