package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.FeatDao;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;

/**
 * Created by Victor on 06/03/14.
 */
public class CharFeatDao extends AbstractAssociationDao<Feat> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_FEAT_ID = "feat_id";

    public static final Table TABLE = new Table("char_feat")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .colNotNull(COLUMN_FEAT_ID, INTEGER);

    private FeatDao featDao = new FeatDao(context, database);

    public CharFeatDao(Context context) {
        super(context);
    }

    public CharFeatDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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
        return featDao.findById(getLong(cursor, COLUMN_FEAT_ID));
    }

    public void setIgnoreBookSelection(boolean ignoreActiveBooks) {
        featDao.setIgnoreBookSelection(ignoreActiveBooks);
    }

}
