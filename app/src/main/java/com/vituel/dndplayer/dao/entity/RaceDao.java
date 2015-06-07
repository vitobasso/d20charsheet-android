package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractRuleDao;
import com.vituel.dndplayer.dao.dependant.RaceTraitDao;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;
import com.vituel.dndplayer.util.database.Table;

import java.util.List;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class RaceDao extends AbstractRuleDao<Race> {

    public static final Table TABLE = new Table("race")
            .colNotNull(COLUMN_BOOK_ID, INTEGER)
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_EFFECT_ID, INTEGER);

    private EffectDao effectDao = new EffectDao(context, database);
    private RaceTraitDao raceTraitDao = new RaceTraitDao(context, database);

    public RaceDao(Context context) {
        super(context);
    }

    protected RaceDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(Race entity) {
        ContentValues values = super.toContentValues(entity);
        return effectDao.preSaveEffectSource(values, entity);
    }

    @Override
    public Race fromCursor(Cursor cursor) {
        Race result = effectDao.loadEffectSource(cursor, new Race(), TABLE);

        //racial traits
        List<RaceTrait> traits = raceTraitDao.findByParent(result.getId());
        result.setTraits(traits);

        return result;
    }

    @Override
    public Race fromCursorBrief(Cursor cursor) {
        Race result = new Race();
        result.setId(getLong(cursor, COLUMN_ID));
        result.setName(getString(cursor, COLUMN_NAME));
        setRulebook(result, cursor);
        return result;
    }

}
