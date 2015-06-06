package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractRuleDao;
import com.vituel.dndplayer.model.Feat;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class FeatDao extends AbstractRuleDao<Feat> {

    public static final String TABLE = "feat";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BOOK_ID + " integer not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EFFECT_ID + " integer, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context, database);

    public FeatDao(Context context) {
        super(context);
    }

    public FeatDao(Context context, SQLiteDatabase database) {
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
    protected ContentValues toContentValues(Feat entity) {
        ContentValues values = super.toContentValues(entity);
        return effectDao.preSaveEffectSource(values, entity);
    }

    @Override
    public Feat fromCursor(Cursor cursor) {
        Feat result = effectDao.loadEffectSource(cursor, new Feat(), 0, 2, 3);
        setRulebook(result, cursor, 1);
        return result;
    }

}
