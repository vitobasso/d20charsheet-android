package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractRuleDao;
import com.vitobasso.d20charsheet.dao.dependant.ClassTraitDao;
import com.vitobasso.d20charsheet.model.ClassTrait;
import com.vitobasso.d20charsheet.model.Clazz;
import com.vitobasso.d20charsheet.util.database.Table;

import java.util.ArrayList;
import java.util.List;

import static com.vitobasso.d20charsheet.model.Clazz.AttackProgression;
import static com.vitobasso.d20charsheet.model.Clazz.ResistProgression;
import static com.vitobasso.d20charsheet.util.LangUtil.ensureListAtIndex;
import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;


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
        setSourceNameToTraits(result.getName(), traits);
        result.setTraits(organizeTraitsByLevel(traits));

        return result;
    }

    private void setSourceNameToTraits(String className, List<ClassTrait> traits) {
        for (ClassTrait trait : traits) {
            String sourceName = className + " " + trait.getLevel();
            trait.getEffect().setSourceName(sourceName);
        }
    }

    private ArrayList<List<ClassTrait>> organizeTraitsByLevel(List<ClassTrait> traits) {
        ArrayList<List<ClassTrait>> traitsByLevel = new ArrayList<>();
        for (ClassTrait trait : traits) {
            int index = trait.getLevel() - 1;
            List<ClassTrait> listOfSpecificLevel = ensureListAtIndex(traitsByLevel, index);
            listOfSpecificLevel.add(trait);
        }
        return traitsByLevel;
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
