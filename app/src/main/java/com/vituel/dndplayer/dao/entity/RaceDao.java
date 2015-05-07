package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractRuleDao;
import com.vituel.dndplayer.dao.dependant.RaceTraitDao;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;

import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class RaceDao extends AbstractRuleDao<Race> {

    public static final String TABLE = "race";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BOOK_ID + " integer not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EFFECT_ID + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context, database);
    private RaceTraitDao raceTraitDao = new RaceTraitDao(context, database);

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
                COLUMN_BOOK_ID,
                COLUMN_NAME,
                COLUMN_EFFECT_ID
        };
    }

    @Override
    protected ContentValues toContentValues(Race entity) {
        ContentValues values = super.toContentValues(entity);
        return effectDao.preSaveEffectSource(values, entity);
    }

    @Override
    public Race fromCursor(Cursor cursor) {
        Race result = effectDao.loadEffectSource(cursor, new Race(), 0, 2, 3);

        //racial traits
        List<RaceTrait> traits = raceTraitDao.findByParent(result.getId());
        result.setTraits(traits);

        return result;
    }

    @Override
    public Race fromCursorBrief(Cursor cursor) {
        Race result = new Race();
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(2));
        setRulebook(result, cursor, 1);
        return result;
    }

}
