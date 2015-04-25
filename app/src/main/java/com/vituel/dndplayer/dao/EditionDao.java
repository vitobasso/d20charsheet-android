package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.model.rulebook.RuleSystem;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 12/04/2015.
 */
public class EditionDao extends AbstractEntityDao<Edition> {

    public static final String TABLE = "edition";

    private static String COLUMN_SYSTEM = "system";
    private static String COLUMN_CORE = "core";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_SYSTEM + " text not null, "
            + COLUMN_CORE + " integer not null "
            + ");";

    public EditionDao(Context context) {
        super(context);
    }

    protected EditionDao(Context context, SQLiteDatabase database) {
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
                COLUMN_SYSTEM,
                COLUMN_CORE
        };
    }

    @Override
    protected ContentValues toContentValues(Edition entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_SYSTEM, entity.getSystem().toString());
        values.put(COLUMN_CORE, entity.isCore());
        return values;
    }

    @Override
    public Edition fromCursor(Cursor cursor) {
        Edition edition = new Edition();
        edition.setId(cursor.getInt(0));
        edition.setName(cursor.getString(1));
        edition.setSystem(RuleSystem.valueOf(cursor.getString(2)));
        edition.setCore(cursor.getInt(3) != 0);
        return edition;
    }
}
