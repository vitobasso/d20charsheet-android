package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.character.CharSkill;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 06/03/14.
 */
public class CharSkillDao extends AbstractAssociationDao<CharSkill> {

    public static final String TABLE = "char_skill";

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_SKILL_ID = "skill_id";
    private static final String COLUMN_GRAD = "grad";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_CHAR_ID + " integer not null, "
            + COLUMN_SKILL_ID + " integer not null, "
            + COLUMN_GRAD + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_SKILL_ID + ") REFERENCES " + SkillDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    public CharSkillDao(Context context) {
        super(context);
    }

    public CharSkillDao(Context context, SQLiteDatabase database) {
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
                COLUMN_SKILL_ID,
                COLUMN_GRAD
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected ContentValues toContentValues(long charId, CharSkill skill) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_SKILL_ID, skill.getSkill().getId());
        values.put(COLUMN_GRAD, skill.getScore());
        return values;
    }

    @Override
    public CharSkill fromCursor(Cursor cursor) {
        long id = cursor.getLong(2);

        SkillDao skillDao = new SkillDao(context, database);
        Skill skill = skillDao.findById(id);

        CharSkill charSkill = new CharSkill(skill);
        charSkill.setScore(cursor.getInt(3));

        return charSkill;
    }

}
