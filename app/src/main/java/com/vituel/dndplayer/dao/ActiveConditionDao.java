package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Condition;

import java.util.List;
import java.util.Set;

import static com.vituel.dndplayer.model.Condition.Predicate;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;


public class ActiveConditionDao extends AbstractDao<Condition> {

    public static final String TABLE = "active_condition";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_PREDICATE = "predicate";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PREDICATE + " text not null"
            + ");";

    public ActiveConditionDao(Context context) {
        super(context);
    }

    public ActiveConditionDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    protected String tableName() {
        return TABLE;
    }

    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_CHAR_ID,
                COLUMN_NAME,
                COLUMN_PREDICATE
        };
    }

    public void save(long charId, Set<Condition> conds){
        removeAllForChar(charId);
        for(Condition cond : conds){
            save(charId, cond);
        }
    }

    private void save(long charId, Condition cond) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, cond.getName());
        values.put(COLUMN_PREDICATE, cond.getPredicate().toString());
        values.put(COLUMN_CHAR_ID, charId);
        database.insert(tableName(), null, values);
    }

    public List<Condition> findByChar(long charId) {
        Cursor cursor = database.query(tableName(), allColumns(), queryByChar(charId), null, null, null, null);
        return cursorToList(cursor);
    }

    @Override
    protected Condition fromCursor(Cursor cursor) {

        Condition cond = new Condition();
        cond.setName(cursor.getString(2));
        cond.setPredicate(Predicate.valueOf(cursor.getString(3)));

        return cond;
    }

    private void removeAllForChar(long charId) {
        database.delete(tableName(), queryByChar(charId), null);
    }

    private String queryByChar(long charId){
        return String.format("%s=%d", COLUMN_CHAR_ID, charId);
    }

}
