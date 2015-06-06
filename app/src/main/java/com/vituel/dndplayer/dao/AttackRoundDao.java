package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractDao;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.model.character.Attack;
import com.vituel.dndplayer.model.character.AttackRound;

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
            + COLUMN_NAME + " text, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private AttackDao attackDao = new AttackDao(context, database);

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

        attackDao.removeAllForAttackRound(id);
        attackDao.save(round.getAttacks(), id);
    }

    public List<AttackRound> listForChar(long charId) {
        String query = MessageFormat.format("{0}={1}", COLUMN_CHAR_ID, charId);
        return select(query);
    }

    @Override
    public AttackRound fromCursor(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(2);
        assert id > 0;
        List<Attack> attacks = attackDao.findByAttackRound(id);

        AttackRound round = new AttackRound(name);
        round.setId(id);
        round.setAttacks(attacks);

        return round;
    }

    public void remove(long id) {
        attackDao.removeAllForAttackRound(id);

        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_ID, id);
        removeForQuery(removeQuery);
    }

    public void removeAllForChar(long charId) {
        List<AttackRound> rounds = listForChar(charId);
        for (AttackRound round : rounds) {
            attackDao.removeAllForAttackRound(round.getId());
        }

        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_CHAR_ID, charId);
        removeForQuery(removeQuery);
    }

}
