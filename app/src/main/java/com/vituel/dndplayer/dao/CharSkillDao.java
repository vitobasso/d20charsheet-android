package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.CharSkill;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 06/03/14.
 */
public class CharSkillDao {

    public static final String TABLE = "char_skill";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_SKILL_ID = "skill_id";
    private static final String COLUMN_GRAD = "grad";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + SQLiteHelper.COLUMN_ID + " integer primary key, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_SKILL_ID + " integer not null, "
            + COLUMN_GRAD + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_SKILL_ID + ") REFERENCES " + SkillDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + ")"
            + ");";

    private static final String[] COLUMNS = new String[]{
            SQLiteHelper.COLUMN_ID,
            COLUMN_CHAR_ID,
            COLUMN_SKILL_ID,
            COLUMN_GRAD
    };

    protected Context context;
    protected SQLiteDatabase database;
    protected SQLiteHelper dbHelper;

    public CharSkillDao(Context context, SQLiteDatabase database) {
        this.database = database;
    }

    public CharSkillDao(Context context) {
        this.context = context;
        this.dbHelper = new SQLiteHelper(context);
        if (database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
        database = null;
    }

    public void saveForChar(long charId, List<CharSkill> skills) {
        assert charId != 0;
        for (CharSkill charSkill : skills) {

            //gather values
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHAR_ID, charId);
            values.put(COLUMN_SKILL_ID, charSkill.getSkill().getId());
            values.put(COLUMN_GRAD, charSkill.getScore());

            database.insert(TABLE, null, values);
        }
    }

    public List<CharSkill> findByChar(long charId) {
        assert charId != 0;
        List<CharSkill> charSkills = new ArrayList<>();

        String query = String.format("%s=%d", COLUMN_CHAR_ID, charId);
        Cursor cursor = database.query(TABLE, COLUMNS, query, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                //read from cursor
                long id = cursor.getLong(2);

                SkillDao skillDao = new SkillDao(context, database);
                Skill skill = skillDao.findById(id);

                CharSkill charSkill = new CharSkill(skill);
                charSkill.setScore(cursor.getInt(3));

                charSkills.add(charSkill);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return charSkills;
    }

    public void removeAllForChar(long charId) {
        String query = String.format("%s=%d", COLUMN_CHAR_ID, charId);
        database.delete(TABLE, query, null);
    }

}
