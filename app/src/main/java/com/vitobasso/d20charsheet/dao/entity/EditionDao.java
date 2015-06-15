package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.model.rulebook.Edition;
import com.vitobasso.d20charsheet.model.rulebook.RuleSystem;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 12/04/2015.
 */
public class EditionDao extends AbstractEntityDao<Edition> {


    private static String COLUMN_SYSTEM = "system";

    public static final Table TABLE = new Table("edition")
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_SYSTEM, TEXT);

    public EditionDao(Context context) {
        super(context);
    }

    protected EditionDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(Edition entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_SYSTEM, entity.getSystem().toString());
        return values;
    }

    @Override
    public Edition fromCursor(Cursor cursor) {
        Edition edition = new Edition();
        edition.setId(getInt(cursor, COLUMN_ID));
        edition.setName(getString(cursor, COLUMN_NAME));
        edition.setSystem(RuleSystem.valueOf(getString(cursor, COLUMN_SYSTEM)));
        return edition;
    }
}
