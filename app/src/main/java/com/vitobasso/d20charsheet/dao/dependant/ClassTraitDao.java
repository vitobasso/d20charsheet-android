package com.vitobasso.d20charsheet.dao.dependant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractAssociationDao;
import com.vitobasso.d20charsheet.dao.entity.EffectDao;
import com.vitobasso.d20charsheet.model.ClassTrait;
import com.vitobasso.d20charsheet.model.Clazz;
import com.vitobasso.d20charsheet.util.database.BulkInserter;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 06/03/14.
 */
public class ClassTraitDao extends AbstractAssociationDao<ClassTrait> {

    private static final String COLUMN_CLASS_ID = "class_id";
    private static final String COLUMN_LEVEL = "level"; //for class traits

    public static final Table TABLE = new Table("class_trait")
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
        clazz.setId(getInt(cursor, COLUMN_CLASS_ID));
        classTrait.setClazz(clazz);

        return classTrait;
    }

    public BulkInserter<ClassTrait> createBulkInserter() {
        return new BulkInserter<ClassTrait>(database, getTable()) {
            public void insert(ClassTrait entity) {
                long parentId = entity.getClazz().getId();
                ContentValues values = toContentValues(parentId, entity);
                values.put(COLUMN_ID, entity.getId());
                insert(values);
            }
        };
    }

}
