package com.vituel.dndplayer.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractAssociationDao;
import com.vituel.dndplayer.dao.entity.EffectDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.util.database.Table;

import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 06/03/14.
 */
public class ClassTraitDao extends AbstractAssociationDao<ClassTrait> {

    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_LEVEL = "level"; //for class traits

    public static final Table TABLE = new Table("trait_link")
            .col(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_CLASS_ID, INTEGER)
            .col(COLUMN_EFFECT_ID, INTEGER)
            .col(COLUMN_LEVEL, INTEGER);

    private EffectDao effectDao = new EffectDao(context, database);

    public ClassTraitDao(Context context) {
        super(context);
    }

    public ClassTraitDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    protected Table getTable() {
        return TABLE;
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
        return values;
    }

    @Override
    public ClassTrait fromCursor(Cursor cursor) {
        ClassTrait classTrait = effectDao.loadEffectSource(cursor, new ClassTrait(), TABLE);
        classTrait.setLevel(cursor.getInt(4));

        Clazz clazz = new Clazz();
        clazz.setId(cursor.getInt(TABLE.getIndex(COLUMN_CLASS_ID)));
        classTrait.setClazz(clazz);

        return classTrait;
    }

    @Override
    public void insert(ClassTrait entity) {
        ContentValues values = toContentValues(entity.getClazz().getId(), entity);
        values.put(COLUMN_ID, entity.getId());
        long id = database.insertOrThrow(tableName(), "_id", values);
        entity.setId(id);
    }

}
