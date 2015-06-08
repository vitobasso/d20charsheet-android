package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractRuleDao;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class FeatDao extends AbstractRuleDao<Feat> {

    public static final Table TABLE = new Table("feat")
            .colNotNull(COLUMN_BOOK_ID, INTEGER)
            .colNotNull(COLUMN_NAME, TEXT)
            .col(COLUMN_EFFECT_ID, INTEGER);

    private EffectDao effectDao = new EffectDao(context, database);

    public FeatDao(Context context) {
        super(context);
    }

    public FeatDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(Feat entity) {
        ContentValues values = super.toContentValues(entity);
        return effectDao.preSaveEffectSource(values, entity);
    }

    @Override
    public Feat fromCursor(Cursor cursor) {
        Feat result = effectDao.loadEffectSource(cursor, new Feat(), TABLE);
        setRulebook(result, cursor);
        return result;
    }

}
