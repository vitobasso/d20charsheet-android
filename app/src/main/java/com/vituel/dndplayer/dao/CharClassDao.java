package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.ClassLevel;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;

/**
 * Created by Victor on 06/03/14.
 */
public class CharClassDao extends AbstractAssociationDao<ClassLevel> {

    public static final String TABLE = "character_class";

    private static final String COLUMN_CHAR_ID = "character_id";
    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_LEVEL = "level";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_CHAR_ID + " integer, "
            + COLUMN_CLASS_ID + " integer, "
            + COLUMN_LEVEL + " integer, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + ClassDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    public CharClassDao(Context context) {
        super(context);
    }

    public CharClassDao(Context context, SQLiteDatabase database) {
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
                COLUMN_CLASS_ID,
                COLUMN_LEVEL
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CHAR_ID;
    }

    @Override
    protected String elementColumn() {
        return COLUMN_CLASS_ID;
    }

    @Override
    protected ClassLevel fromCursor(Cursor cursor) {
        ClassLevel classLevel = new ClassLevel();

        long classId = cursor.getLong(2);
        classLevel.setLevel(cursor.getInt(3));

        ClassDao classData = new ClassDao(context);
        classLevel.setClazz(classData.findById(classId));

        return classLevel;
    }

    @Override
    public void save(long charId, ClassLevel classLevel) {
        long classId = classLevel.getClazz().getId();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_CLASS_ID, classId);
        values.put(COLUMN_LEVEL, classLevel.getLevel());

        insertOrUpdate(values, charId, classId);
    }

}
