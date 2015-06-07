package com.vituel.dndplayer.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.dao.entity.ClassDao;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.util.database.Table;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;

/**
 * Created by Victor on 06/03/14.
 */
public class CharClassDao extends AbstractAssociationDao<ClassLevel> {

    private static final String COLUMN_CHAR_ID = "character_id";
    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_LEVEL = "level";

    public static final Table TABLE = new Table("character_class")
            .col(COLUMN_CHAR_ID, INTEGER)
            .col(COLUMN_CLASS_ID, INTEGER)
            .col(COLUMN_LEVEL, INTEGER);

    private ClassDao classDao = new ClassDao(context);

    public CharClassDao(Context context) {
        super(context);
    }

    public CharClassDao(Context context, SQLiteDatabase database) {
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
    protected ContentValues toContentValues(long charId, ClassLevel classLevel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAR_ID, charId);
        values.put(COLUMN_CLASS_ID, classLevel.getClazz().getId());
        values.put(COLUMN_LEVEL, classLevel.getLevel());
        return values;
    }

    @Override
    public ClassLevel fromCursor(Cursor cursor) {
        ClassLevel classLevel = new ClassLevel();
        classLevel.setLevel(getInt(cursor, COLUMN_LEVEL));
        long classId = getLong(cursor, COLUMN_CLASS_ID);
        Clazz clazz = classDao.findById(classId);
        classLevel.setClazz(clazz);
        return classLevel;
    }

    public void setIgnoreBookSelection(boolean ignoreActiveBooks) {
        classDao.setIgnoreBookSelection(ignoreActiveBooks);
    }

}
