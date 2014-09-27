package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vituel.dndplayer.model.Trait;

import java.util.List;

import static com.vituel.dndplayer.model.AbstractEffect.Type.TRAIT;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class TraitDao extends AbstractEntityDao<Trait> {

    public static final String TABLE = "trait";

    private static final String COLUMN_TYPE = "type";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_TYPE + " text not null"
            + ");";

    public TraitDao(Context context) {
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
                COLUMN_TYPE
        };
    }

    @Override
    public void save(Trait entity) {

        //basic data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_TYPE, entity.getTraitType().toString());
        long id = insertOrUpdate(values, entity.getId());

        //magic bonuses
        ModifierDao effectData = new ModifierDao(context);
        effectData.removeAllForEffect(TRAIT, id);
        effectData.save(entity.getModifiers(), TRAIT, id);

        entity.setId(id);
    }

    @Override
    protected Trait fromCursor(Cursor cursor) {

        //basic fields
        Trait result = new Trait();
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));
        String typeStr = cursor.getString(2);
        if(typeStr != null){
            result.setTraitType(Trait.Type.valueOf(typeStr));
        }

        //modifiers
        ModifierDao effectData = new ModifierDao(context);
        result.setModifiers(effectData.listAll(TRAIT, result.getId()));

        return result;
    }

    public List<Trait> findByType(Trait.Type type){
        String query = String.format("%s=\"%s\"", COLUMN_TYPE, type);
        Cursor cursor = database.query(TABLE, allColumns(), query, null, null, null, null);
        return cursorToList(cursor);
    }
}
