package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.EffectSource;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class EffectDao extends AbstractEntityDao<Effect> {

    public static final String TABLE = "effect";

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement"
            + ");";

    private ModifierDao modifierDao = new ModifierDao(context, database);

    public EffectDao(Context context) {
        super(context);
    }

    public EffectDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID
        };
    }

    @Override
    public void save(Effect entity) {

        //basic data
        ContentValues values = new ContentValues();
        long id = insertOrUpdate(values, entity.getId());

        //magic bonuses
        modifierDao.removeAllForEffect(id);
        modifierDao.save(entity.getModifiers(), id);

        entity.setId(id);
    }

    @Override
    protected Effect fromCursor(Cursor cursor) {

        //basic fields
        Effect result = new Effect();
        result.setId(cursor.getLong(0));

        //modifiers
        result.setModifiers(modifierDao.listAllForEffect(result.getId()));

        return result;
    }

    public ContentValues preSaveEffectSource(EffectSource source) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, source.getName());

        Effect effect = source.getEffect();
        if (effect != null) {
            save(effect);
            values.put(COLUMN_EFFECT_ID, effect.getId());
        }

        return values;
    }

    public <T extends EffectSource> T loadEffectSource(Cursor cursor, T newEntity, int idCol, int nameCol, int effectCol) {

        //basic fields
        newEntity.setId(cursor.getLong(idCol));
        newEntity.setName(cursor.getString(nameCol));

        //effect
        Effect effect = findById(cursor.getLong(effectCol));
        if (effect != null) {
            effect.setSourceName(newEntity.getName());
            newEntity.setEffect(effect);
        }

        return newEntity;
    }

}
