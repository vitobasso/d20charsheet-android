package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.dependant.ModifierDao;
import com.vitobasso.d20charsheet.model.effect.Effect;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.util.database.Table;

import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

/**
 * Created by Victor on 30/03/14.
 */
public class EffectDao extends AbstractEntityDao<Effect> {

    public static final Table TABLE = new Table("effect");

    private ModifierDao modifierDao = new ModifierDao(context, database);

    public EffectDao(Context context) {
        super(context);
    }

    public EffectDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    protected Table getTable() {
        return TABLE;
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
        result.setId(getLong(cursor, COLUMN_ID));

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
            save(effect); //TODO when called from bulk insert, inserts outside bulk (no transaction, no prepared statement)
            values.put(COLUMN_EFFECT_ID, effect.getId());
        }

        return values;
    }

    public <T extends EffectSource> T loadEffectSource(Cursor cursor, T newEntity, Table table) {
        int idCol = table.getIndex(COLUMN_ID);
        int nameCol = table.getIndex(COLUMN_NAME);
        int effectCol = table.getIndex(COLUMN_EFFECT_ID);
        return loadEffectSource(cursor, newEntity, idCol, nameCol, effectCol);
    }

    public <T extends EffectSource> T loadEffectSource(Cursor cursor, T newEntity, int idCol, int nameCol, int effectCol) {
    //TODO make private when all daos have Table

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
