package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vituel.dndplayer.dao.abstraction.AbstractRuleDao;
import com.vituel.dndplayer.dao.dependant.ClassTraitDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.util.database.Table;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.Clazz.AttackProgression;
import static com.vituel.dndplayer.model.Clazz.ResistProgression;
import static com.vituel.dndplayer.util.JavaUtil.getAndIfOverflowsCreate;
import static com.vituel.dndplayer.util.database.ColumnType.INTEGER;
import static com.vituel.dndplayer.util.database.ColumnType.TEXT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;


public class ClassDao extends AbstractRuleDao<Clazz> {

    private static final String COLUMN_ATTACK = "attack";
    private static final String COLUMN_FORTITUDE = "fortitude";
    private static final String COLUMN_REFLEX = "reflex";
    private static final String COLUMN_WILL = "will";

    public static final Table TABLE = new Table("class")
            .colNotNull(COLUMN_BOOK_ID, INTEGER)
            .colNotNull(COLUMN_NAME, TEXT)
            .colNotNull(COLUMN_ATTACK, TEXT)
            .colNotNull(COLUMN_FORTITUDE, TEXT)
            .colNotNull(COLUMN_REFLEX, TEXT)
            .colNotNull(COLUMN_WILL, TEXT);

    private ClassTraitDao classTraitDao = new ClassTraitDao(context, database);;

    public ClassDao(Context context) {
        super(context);
    }

    @Override
    protected Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(Clazz entity) {
        ContentValues values = super.toContentValues(entity);
        values.put(COLUMN_ATTACK, entity.getAttackProg().toString());
        values.put(COLUMN_FORTITUDE, entity.getFortitudeProg().toString());
        values.put(COLUMN_REFLEX, entity.getReflexProg().toString());
        values.put(COLUMN_WILL, entity.getWillProg().toString());
        return values;
    }

    @Override
    public Clazz fromCursor(Cursor cursor) {

        Clazz result = fromCursorBrief(cursor);
        result.setAttackProg(AttackProgression.valueOf(getString(cursor, COLUMN_ATTACK)));
        result.setFortitudeProg(ResistProgression.valueOf(getString(cursor, COLUMN_FORTITUDE)));
        result.setReflexProg(ResistProgression.valueOf(getString(cursor, COLUMN_REFLEX)));
        result.setWillProg(ResistProgression.valueOf(getString(cursor, COLUMN_WILL)));

        //traits
        List<ClassTrait> traits = classTraitDao.findByParent(result.getId());

        //organize traits by level
        result.setTraits(new ArrayList<List<ClassTrait>>());
        for (ClassTrait trait : traits) {
            List<ClassTrait> listOfSpecificLevel = getAndIfOverflowsCreate(result.getTraits(), trait.getLevel() - 1);
            listOfSpecificLevel.add(trait);

            //set source name
            String sourceName = result.getName() + " " + trait.getLevel();
            trait.getEffect().setSourceName(sourceName);
        }

        return result;
    }

    @Override
    public Clazz fromCursorBrief(Cursor cursor) {
        Clazz result = new Clazz();
        result.setId(getLong(cursor, COLUMN_ID));
        result.setName(getString(cursor, COLUMN_NAME));
        setRulebook(result, cursor);
        return result;
    }
}
