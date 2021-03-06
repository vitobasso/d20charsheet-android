package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.EffectDao;
import com.vitobasso.d20charsheet.model.RaceTrait;
import com.vitobasso.d20charsheet.util.database.BulkInserter;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 06/03/14.
 */
public class RaceTraitDao extends AbstractAssociationDao<RaceTrait> {

    private static final String COLUMN_RACE_ID = "race_id";

    public static final Table TABLE = new Table("race_trait")
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_RACE_ID, INTEGER)
            .col(COLUMN_EFFECT_ID, INTEGER);

    private EffectDao effectDao = new EffectDao(context, database);;

    public RaceTraitDao(Context context) {
        super(context);
    }

    public RaceTraitDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected String parentColumn() {
        return COLUMN_RACE_ID;
    }

    @Override
    protected ContentValues toContentValues(long parentId, RaceTrait trait) {
        ContentValues values = effectDao.preSaveEffectSource(trait);
        values.put(COLUMN_RACE_ID, parentId);
        return values;
    }

    @Override
    public RaceTrait fromCursor(Cursor cursor) {
        return effectDao.loadEffectSource(cursor, new RaceTrait(), TABLE);
    }

    public BulkInserter<RaceTrait> createBulkInserter() {
        return new BulkInserter<RaceTrait>(database, getTable()) {
            public void insert(RaceTrait entity) {
                long parentId = entity.getRace().getId();
                ContentValues values = toContentValues(parentId, entity);
                values.put(COLUMN_ID, entity.getId());
                insert(values);
            }
        };
    }

}
