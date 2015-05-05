package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vituel.dndplayer.dao.abstraction.AbstractRuleDao;
import com.vituel.dndplayer.dao.dependant.ClassTraitDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.rulebook.Book;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.Clazz.AttackProgression;
import static com.vituel.dndplayer.model.Clazz.ResistProgression;
import static com.vituel.dndplayer.util.JavaUtil.getAndIfOverflowsCreate;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;


public class ClassDao extends AbstractRuleDao<Clazz> {

    public static final String TABLE = "class";

    private static final String COLUMN_ATTACK = "attack";
    private static final String COLUMN_FORTITUDE = "fortitude";
    private static final String COLUMN_REFLEX = "reflex";
    private static final String COLUMN_WILL = "will";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BOOK_ID+ " integer not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_ATTACK + " text not null, "
            + COLUMN_FORTITUDE + " text not null, "
            + COLUMN_REFLEX + " text not null, "
            + COLUMN_WILL + " text not null"
            + ");";

    private ClassTraitDao classTraitDao = new ClassTraitDao(context, database);;

    public ClassDao(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_BOOK_ID,
                COLUMN_NAME,
                COLUMN_ATTACK,
                COLUMN_FORTITUDE,
                COLUMN_REFLEX,
                COLUMN_WILL
        };
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
        result.setAttackProg(AttackProgression.valueOf(cursor.getString(3)));
        result.setFortitudeProg(ResistProgression.valueOf(cursor.getString(4)));
        result.setReflexProg(ResistProgression.valueOf(cursor.getString(5)));
        result.setWillProg(ResistProgression.valueOf(cursor.getString(6)));

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
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(2));

        // rulebook
        Book book = new Book();
        book.setId(cursor.getInt(1));
        result.setBook(book);

        return result;
    }
}
