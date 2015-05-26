package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.dependant.ModifierDao;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;

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
    protected ContentValues toContentValues(Effect entity) {
        return new ContentValues();
    }

    @Override
    public void postSave(Effect entity) {
        long id = entity.getId();

        //magic bonuses
        modifierDao.saveOverwrite(id, entity.getModifiers());

        entity.setId(id);
    }

    @Override
    public Effect fromCursor(Cursor cursor) {

        //basic fields
        Effect result = new Effect();
        result.setId(cursor.getLong(0));

        //modifiers
        result.setModifiers(modifierDao.findByParent(result.getId()));

        return result;
    }

    public ContentValues preSaveEffectSource(EffectSource source) {
        ContentValues values = new ContentValues();
        return preSaveEffectSource(values, source);
    }

    public ContentValues preSaveEffectSource(ContentValues values, EffectSource source) {
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

    @Override
    protected String orderBy() { // TODO needed this because table has no "name" column. shouldn't extend AbstractEntityDao then.
        return null;
    }
}
