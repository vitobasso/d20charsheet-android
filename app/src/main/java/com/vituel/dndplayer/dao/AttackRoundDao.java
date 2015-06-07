package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractDao;
import com.vituel.dndplayer.model.character.Attack;
import com.vituel.dndplayer.model.character.AttackRound;
import com.vituel.dndplayer.util.database.Table;

import java.text.MessageFormat;
import java.util.List;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14.
 */
public class AttackRoundDao extends AbstractDao<AttackRound> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_NAME = "attack_name";

    public static final Table TABLE = new Table("attack_round")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .col(COLUMN_NAME, TEXT);

    private AttackDao attackDao = new AttackDao(context, database);

    public AttackRoundDao(Context context) {
        super(context);
    }

    public AttackRoundDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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

        attackDao.removeAllForAttackRound(id);
        attackDao.save(round.getAttacks(), id);
    }

    public List<AttackRound> listForChar(long charId) {
        String query = MessageFormat.format("{0}={1}", COLUMN_CHAR_ID, charId);
        return select(query);
    }

    @Override
    public AttackRound fromCursor(Cursor cursor) {
        long id = getLong(cursor, COLUMN_ID);
        String name = getString(cursor, COLUMN_NAME);
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
