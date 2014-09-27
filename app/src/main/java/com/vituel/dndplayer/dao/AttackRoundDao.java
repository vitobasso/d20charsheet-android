package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.AttackRound;

import java.text.MessageFormat;
import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14.
 */
public class AttackRoundDao extends AbstractDao<AttackRound> {

    public static final String TABLE = "attack_round";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_NAME = "attack_name";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_NAME + " text not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    public AttackRoundDao(Context context) {
        super(context);
    }

    public AttackRoundDao(Context context, SQLiteDatabase database) {
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
                COLUMN_NAME
        };
    }

    public void save(List<AttackRound> rounds, long charId) {
        for (AttackRound round : rounds) {
            save(round, charId);
        }
    }

    public void save(AttackRound round, long charId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_NAME, round.getName());

        long id = database.insert(tableName(), null, values);
        round.setId(id);
        assert id > 0;

        AttackDao groupDao = new AttackDao(context, database);
        groupDao.removeAllForAttackRound(id);
        groupDao.save(round.getAttacks(), id);
    }

    public List<AttackRound> findByChar(long charId) {
        String query = MessageFormat.format("{0}={1}", COLUMN_CHAR_ID, charId);
        Cursor cursor = database.query(tableName(), allColumns(), query, null, null, null, null);
        return cursorToList(cursor);
    }

    @Override
    protected AttackRound fromCursor(Cursor cursor) {
        AttackDao groupDao = new AttackDao(context, database);

        int id = cursor.getInt(0);
        String name = cursor.getString(2);
        assert id > 0;
        List<Attack> attacks = groupDao.findByAttackRound(id);

        AttackRound round = new AttackRound(name);
        round.setId(id);
        round.setAttacks(attacks);

        return round;
    }

    public void remove(long id) {
        AttackDao groupDao = new AttackDao(context, database);
        groupDao.removeAllForAttackRound(id);

        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_ID, id);
        database.delete(tableName(), removeQuery, null);
    }

    public void removeAllForChar(long charId) {
        AttackDao groupDao = new AttackDao(context, database);
        List<AttackRound> rounds = findByChar(charId);
        for (AttackRound round : rounds) {
            groupDao.removeAllForAttackRound(round.getId());
        }

        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_CHAR_ID, charId);
        database.delete(tableName(), removeQuery, null);
    }

}
