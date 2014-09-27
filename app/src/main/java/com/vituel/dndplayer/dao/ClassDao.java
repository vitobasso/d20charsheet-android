package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Trait;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.model.Clazz.AttackProgression;
import static com.vituel.dndplayer.model.Clazz.ResistProgression;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;
import static com.vituel.dndplayer.util.JavaUtil.getAndIfOverflowsCreate;


public class ClassDao extends AbstractEntityDao<Clazz> {

    public static final String TABLE = "class";

    private static final String COLUMN_ATTACK = "attack";
    private static final String COLUMN_FORTITUDE = "fortitude";
    private static final String COLUMN_REFLEX = "reflex";
    private static final String COLUMN_WILL = "will";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_ATTACK + " text not null, "
            + COLUMN_FORTITUDE + " text not null, "
            + COLUMN_REFLEX + " text not null, "
            + COLUMN_WILL + " text not null"
            + ");";


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
                COLUMN_NAME,
                COLUMN_ATTACK,
                COLUMN_FORTITUDE,
                COLUMN_REFLEX,
                COLUMN_WILL
        };
    }

    @Override
    public void save(Clazz entity) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_ATTACK, entity.getAttackProg().toString());
        values.put(COLUMN_FORTITUDE, entity.getFortitudeProg().toString());
        values.put(COLUMN_REFLEX, entity.getReflexProg().toString());
        values.put(COLUMN_WILL, entity.getWillProg().toString());

        long id = insertOrUpdate(values, entity.getId());
        entity.setId(id);
    }

    @Override
    protected Clazz fromCursor(Cursor cursor) {

        Clazz c = new Clazz();
        c.setId(cursor.getLong(0));
        c.setName(cursor.getString(1));
        c.setAttackProg(AttackProgression.valueOf(cursor.getString(2)));
        c.setFortitudeProg(ResistProgression.valueOf(cursor.getString(3)));
        c.setReflexProg(ResistProgression.valueOf(cursor.getString(4)));
        c.setWillProg(ResistProgression.valueOf(cursor.getString(5)));

        //traits
        TraitLinkDao traitLinkDao = new TraitLinkDao(context, database);
        List<Trait> traits = traitLinkDao.findByClass(c.getId());

        //organize traits by level
        c.setTraits(new ArrayList<List<Trait>>());
        for(Trait trait : traits){
            ClassTrait cTrait = (ClassTrait) trait;
            List<Trait> rightLevelList = getAndIfOverflowsCreate(c.getTraits(), cTrait.getLevel() - 1);
            rightLevelList.add(trait);
        }

        return c;
    }
}
