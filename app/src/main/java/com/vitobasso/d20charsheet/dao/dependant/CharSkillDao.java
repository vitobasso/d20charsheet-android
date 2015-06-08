package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.SkillDao;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.character.CharSkill;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;

/**
 * Created by Victor on 06/03/14.
 */
public class CharSkillDao extends AbstractAssociationDao<CharSkill> {

    private static final String COLUMN_CHAR_ID = "char_id";
    private static final String COLUMN_SKILL_ID = "skill_id";
    private static final String COLUMN_GRAD = "grad";

    public static final Table TABLE = new Table("char_skill")
            .colNotNull(COLUMN_CHAR_ID, INTEGER)
            .colNotNull(COLUMN_SKILL_ID, INTEGER)
            .colNotNull(COLUMN_GRAD, INTEGER);

    public CharSkillDao(Context context) {
        super(context);
    }

    public CharSkillDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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
        long skillId = getLong(cursor, COLUMN_SKILL_ID);

        SkillDao skillDao = new SkillDao(context, database);
        Skill skill = skillDao.findById(skillId);

        CharSkill charSkill = new CharSkill(skill);
        charSkill.setScore(getInt(cursor, COLUMN_GRAD));

        return charSkill;
    }

}
