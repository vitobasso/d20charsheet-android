package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractDao;
import com.vituel.dndplayer.model.character.Attack;
import com.vituel.dndplayer.util.database.Table;

import java.text.MessageFormat;
import java.util.List;

import static com.vituel.dndplayer.model.character.Attack.WeaponReference.valueOf;
import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;

/**
 * Created by Victor on 01/05/14
 */
public class AttackDao extends AbstractDao<Attack> {

    private static final String COLUMN_ATTACK_ROUND_ID = "attack_round_id";
    private static final String COLUMN_PENALTY = "penalty";
    private static final String COLUMN_WEAPON_REF_TYPE = "weapon_reference_type"; //mainhand, offhand

    public static final Table TABLE = new Table("attack")
            .colNotNull(COLUMN_ATTACK_ROUND_ID, INTEGER)
            .colNotNull(COLUMN_PENALTY, TEXT)
            .colNotNull(COLUMN_WEAPON_REF_TYPE, TEXT);

    public AttackDao(Context context) {
        super(context);
    }

    public AttackDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
    }

    public List<Attack> findByAttackRound(long roundId) {
        String query = MessageFormat.format("{0}={1}", COLUMN_ATTACK_ROUND_ID, roundId);
        return select(query);
    }

    public void save(List<Attack> groups, long roundId) {
        for (Attack attack : groups) {
            save(attack, roundId);
        }
    }

    private void save(Attack attack, long roundId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTACK_ROUND_ID, roundId);
        values.put(COLUMN_PENALTY, attack.getAttackBonus());
        values.put(COLUMN_WEAPON_REF_TYPE, attack.getWeaponReference().toString());

        long id = database.insert(tableName(), null, values);
        attack.setId(id);
    }

    @Override
    public Attack fromCursor(Cursor cursor) {

        int penalty = getInt(cursor, COLUMN_PENALTY);
        Attack.WeaponReference refType = valueOf(getString(cursor, COLUMN_WEAPON_REF_TYPE));

        return new Attack(penalty, refType);
    }

    public void removeAllForAttackRound(long roundId) {
        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_ATTACK_ROUND_ID, roundId);
        removeForQuery(removeQuery);
    }

}
