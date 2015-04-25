package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Feat;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 06/03/14.
 */
public class CharFeatDao extends AbstractAssociationDao<Feat> {

    public static final String TABLE = "char_feat";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_FEAT_ID = "feat_id";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_FEAT_ID + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_FEAT_ID + ") REFERENCES " + FeatDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    public CharFeatDao(Context context) {
        super(context);
    }

    public CharFeatDao(Context context, SQLiteDatabase database) {
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
                COLUMN_CHAR_ID,
                COLUMN_FEAT_ID
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected ContentValues toContentValues(long charId, Feat feat) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_FEAT_ID, feat.getId());
        return values;
    }

    @Override
    public Feat fromCursor(Cursor cursor) {
        FeatDao featDao = new FeatDao(context, database);
        return featDao.findById(cursor.getLong(2));
    }

}
