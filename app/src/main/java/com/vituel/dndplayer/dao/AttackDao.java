package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.character.Attack;

import java.text.MessageFormat;
import java.util.List;

import static com.vituel.dndplayer.model.character.Attack.WeaponReference.valueOf;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 01/05/14
 */
public class AttackDao extends AbstractDao<Attack> {

    public static final String TABLE = "attack";

    private static final String COLUMN_ATTACK_ROUND_ID = "attack_round_id";
    private static final String COLUMN_PENALTY = "penalty";
    private static final String COLUMN_WEAPON_REF_TYPE = "weapon_reference_type"; //mainhand, offhand

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ATTACK_ROUND_ID + " integer not null, "
            + COLUMN_PENALTY + " text not null, "
            + COLUMN_WEAPON_REF_TYPE + " text not null, "
            + "FOREIGN KEY(" + COLUMN_ATTACK_ROUND_ID + ") REFERENCES " + AttackRoundDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    public AttackDao(Context context) {
        super(context);
    }

    public AttackDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ATTACK_ROUND_ID,
                COLUMN_PENALTY,
                COLUMN_WEAPON_REF_TYPE
        };
    }

    public List<Attack> findByAttackRound(long roundId) {
        String query = MessageFormat.format("{0}={1}", COLUMN_ATTACK_ROUND_ID, roundId);
        Cursor cursor = database.query(tableName(), allColumns(), query, null, null, null, null);
        return cursorToList(cursor);
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
    protected Attack fromCursor(Cursor cursor) {

        int penalty = cursor.getInt(1);
        Attack.WeaponReference refType = valueOf(cursor.getString(2));

        return new Attack(penalty, refType);
    }

    public void removeAllForAttackRound(long roundId) {
        String removeQuery = MessageFormat.format("{0}={1}", COLUMN_ATTACK_ROUND_ID, roundId);
        removeForQuery(removeQuery);
    }

}
