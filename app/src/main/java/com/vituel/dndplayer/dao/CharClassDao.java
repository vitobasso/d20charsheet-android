package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 06/03/14.
 */
public class CharClassDao {

    public static final String TABLE = "character_class";

    private static final String COLUMN_CHAR_ID = "character_id";
    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_LEVEL = "level";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + SQLiteHelper.COLUMN_ID + " integer primary key, "
            + COLUMN_CHAR_ID + " integer, "
            + COLUMN_CLASS_ID + " integer, "
            + COLUMN_LEVEL + " integer, "
            + "FOREIGN KEY(" + COLUMN_CHAR_ID + ") REFERENCES " + CharDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + ClassDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + ")"
            + ");";

    private static final String[] COLUMNS = new String[]{
            SQLiteHelper.COLUMN_ID,
            COLUMN_CHAR_ID,
            COLUMN_CLASS_ID,
            COLUMN_LEVEL
    };

    protected Context context;
    protected SQLiteDatabase database;

    public CharClassDao(Context context, SQLiteDatabase database) {
        this.database = database;
    }

    public void save(long charId, List<ClassLevel> classLevels) {
        assert charId != 0;
        for (ClassLevel classLevel : classLevels) {
            if (classLevel == null) {
                continue;
            }

            //gather values
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHAR_ID, charId);
            values.put(COLUMN_CLASS_ID, classLevel.getClazz().getId());
            values.put(COLUMN_LEVEL, classLevel.getLevel());

            //look for existing character-class row and save/update accordingly
            String query = String.format("%s=%d and %s=%d", COLUMN_CHAR_ID, charId, COLUMN_CLASS_ID, classLevel.getClazz().getId());
            Cursor cursor = database.query(TABLE, COLUMNS, query, null, null, null, null);
            if (cursor.getCount() > 0) {
                database.update(TABLE, values, query, null);
            } else {
                database.insert(TABLE, null, values);
            }
            cursor.close();

        }
    }

    public List<ClassLevel> findByChar(long charId) {
        assert charId != 0;
        List<ClassLevel> classLevels = new ArrayList<>();

        String query = String.format("%s=%d", COLUMN_CHAR_ID, charId);
        Cursor cursor = database.query(TABLE, COLUMNS, query, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ClassLevel classLevel = new ClassLevel();

                //read from cursor
                long classId = cursor.getLong(2);
                classLevel.setLevel(cursor.getInt(3));

                ClassDao classData = new ClassDao(context);
                classLevel.setClazz(classData.findById(classId));

                classLevels.add(classLevel);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return classLevels;
    }

    public void removeAllForChar(long charId) {
        String query = String.format("%s=%d", COLUMN_CHAR_ID, charId);
        database.delete(TABLE, query, null);
    }

}
