package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.ClassTrait;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 06/03/14.
 */
public class ClassTraitDao extends AbstractAssociationDao<ClassTrait> {

    public static final String TABLE = "trait_link";

    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_LEVEL = "level"; //for class traits
    private static final String COLUMN_OVERRIDES = "overrides"; //for class traits

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text, "
            + COLUMN_CLASS_ID + " integer not null, "
            + COLUMN_EFFECT_ID + " integer, "
            + COLUMN_LEVEL + " integer, "
            + COLUMN_OVERRIDES + " text, "
            + "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + ClassDao.TABLE + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private EffectDao effectDao = new EffectDao(context, database);

    public ClassTraitDao(Context context) {
        super(context);
    }

    protected ClassTraitDao(Context context, SQLiteDatabase database) {
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
                COLUMN_NAME,
                COLUMN_CLASS_ID,
                COLUMN_EFFECT_ID,
                COLUMN_LEVEL,
                COLUMN_OVERRIDES
        };
    }

    @Override
    protected String parentColumn() {
        return COLUMN_CLASS_ID;
    }

    @Override
    protected ContentValues toContentValues(long classId, ClassTrait trait) {
        ContentValues values = effectDao.preSaveEffectSource(trait);
        values.put(COLUMN_CLASS_ID, classId);
        values.put(COLUMN_LEVEL, trait.getLevel());
        if (trait.getOverridenTraitName() != null) {
            values.put(COLUMN_OVERRIDES, trait.getOverridenTraitName());
        }
        return values;
    }

    @Override
    protected ClassTrait fromCursor(Cursor cursor) {
        ClassTrait classTrait = effectDao.loadEffectSource(cursor, new ClassTrait(), 0, 1, 3);
        classTrait.setLevel(cursor.getInt(4));
        classTrait.setOverridenTraitName(cursor.getString(5));
        return classTrait;
    }

}
