package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Trait;
import com.vituel.dndplayer.util.database.SQLiteHelper;
import com.vituel.dndplayer.model.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Victor on 06/03/14.
 */
public class TraitLinkDao {

    public static final String TABLE = "trait_link";

    private static final String COLUMN_TRAIT_ID = "trait_id";
    private static final String COLUMN_ENTITY_TABLE = "entity_table";  //char vs class vs race
    private static final String COLUMN_ENTITY_ID = "entity_id";
    private static final String COLUMN_LEVEL = "level"; //for class traits
    private static final String COLUMN_OVERRIDES = "overrides"; //for class traits

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + SQLiteHelper.COLUMN_ID + " integer primary key, "
            + COLUMN_ENTITY_TABLE + " text not null, "
            + COLUMN_ENTITY_ID + " integer not null, "
            + COLUMN_TRAIT_ID + " integer not null, "
            + COLUMN_LEVEL + " integer, "
            + COLUMN_OVERRIDES + " text, "
            + "FOREIGN KEY(" + COLUMN_TRAIT_ID + ") REFERENCES " + TraitDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_OVERRIDES + ") REFERENCES " + TraitDao.TABLE + "(" + SQLiteHelper.COLUMN_ID + ")"
            + ");";

    private static final String[] COLUMNS = new String[]{
            SQLiteHelper.COLUMN_ID,
            COLUMN_ENTITY_TABLE,
            COLUMN_ENTITY_ID,
            COLUMN_TRAIT_ID,
            COLUMN_LEVEL,
            COLUMN_OVERRIDES
    };

    protected Context context;
    protected SQLiteDatabase database;

    public TraitLinkDao(Context context) {
        this.context = context;
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        if (database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public TraitLinkDao(Context context, SQLiteDatabase database) {
        this.database = database;
    }

    public void saveForChar(long entityId, List<Trait> traits) {
        save(CharDao.TABLE, entityId, traits);
    }

    public void saveForRaces(Map<Trait, Race> map) {
        for (Trait trait : map.keySet()) {
            Race race = map.get(trait);
            save(RaceDao.TABLE, race.getId(), trait);
        }
    }

    public void saveForClasses(Map<Trait, Clazz> map) {
        for (Trait trait : map.keySet()) {
            Clazz clazz = map.get(trait);
            save(ClassDao.TABLE, clazz.getId(), trait);
        }
    }

    private void save(String entityTable, long entityId, List<Trait> traits) {
        for (Trait trait : traits) {
            save(entityTable, entityId, trait);
        }
    }

    private void save(String entityTable, long entityId, Trait trait) {
        assert entityId != 0;

        //gather values
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRAIT_ID, trait.getId());
        values.put(COLUMN_ENTITY_TABLE, entityTable);
        values.put(COLUMN_ENTITY_ID, entityId);

        if (trait instanceof ClassTrait) {
            ClassTrait classTrait = (ClassTrait) trait;
            values.put(COLUMN_LEVEL, classTrait.getLevel());

            if (classTrait.getOverridenTraitName() != null) {
                values.put(COLUMN_OVERRIDES, classTrait.getOverridenTraitName());
            }
        }

        database.insert(TABLE, null, values);
    }

    public List<Trait> findByChar(long charId) {
        return findByEntity(CharDao.TABLE, charId);
    }

    public List<Trait> findByRace(long raceId) {
        return findByEntity(RaceDao.TABLE, raceId);
    }

    public List<Trait> findByClass(long classId) {
        return findByEntity(ClassDao.TABLE, classId);
    }

    private List<Trait> findByEntity(String entityTable, long entityId) {
        assert entityId != 0;
        List<Trait> traits = new ArrayList<>();

        Cursor cursor = database.query(TABLE, COLUMNS, query(entityTable, entityId), null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                //read from cursor
                long traitId = cursor.getLong(3);

                TraitDao traitData = new TraitDao(context);
                Trait trait = traitData.findById(traitId);

                if (entityTable.equals(ClassDao.TABLE)) {
                    ClassTrait classTrait = new ClassTrait(trait);
                    classTrait.setLevel(cursor.getInt(4));

                    String overridenTrait = cursor.getString(5);
                    classTrait.setOverridenTraitName(overridenTrait);

                    trait = classTrait;
                }

                traits.add(trait);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return traits;
    }

    public void removeAllForChar(long charId) {
        removeAllForEntity(CharDao.TABLE, charId);
    }

    public void removeAllForRace(long charId) {
        removeAllForEntity(RaceDao.TABLE, charId);
    }

    public void removeAllForClass(long charId) {
        removeAllForEntity(ClassDao.TABLE, charId);
    }

    private void removeAllForEntity(String entityTable, long entityId) {
        database.delete(TABLE, query(entityTable, entityId), null);
    }

    private String query(String entityTable, long entityId) {
        return String.format("%s='%s' and %s=%d", COLUMN_ENTITY_TABLE, entityTable, COLUMN_ENTITY_ID, entityId);
    }

    public void close() {
        database.close();
    }
}
