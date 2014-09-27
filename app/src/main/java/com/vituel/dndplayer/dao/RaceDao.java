package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Race;

import static com.vituel.dndplayer.model.AbstractEffect.Type.RACE;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class RaceDao extends AbstractEntityDao<Race> {

    public static final String TABLE = "race";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null"
            + ");";

    public RaceDao(Context context) {
        super(context);
    }

    public RaceDao(Context context, SQLiteDatabase database) {
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
                COLUMN_NAME
        };
    }

    @Override
    public void save(Race entity) {

        //basic data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        long id = insertOrUpdate(values, entity.getId());

        //magic bonuses
        ModifierDao effectData = new ModifierDao(context);
        effectData.removeAllForEffect(RACE, id);
        effectData.save(entity.getModifiers(), RACE, id);

        entity.setId(id);
    }

    @Override
    protected Race fromCursor(Cursor cursor) {

        //basic fields
        Race result = new Race();
        result.setId(cursor.getLong(0));
        result.setName(cursor.getString(1));

        //modifiers
        ModifierDao effectData = new ModifierDao(context);
        result.setModifiers(effectData.listAll(RACE, result.getId()));

        //traits
        TraitLinkDao traitLinkDao = new TraitLinkDao(context, database);
        result.setTraits(traitLinkDao.findByRace(result.getId()));

        return result;
    }
}
