package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Item;
import com.vituel.dndplayer.model.SlotType;
import com.vituel.dndplayer.model.WeaponItem;
import com.vituel.dndplayer.model.WeaponProperties;

import java.util.List;

import static com.vituel.dndplayer.model.Item.ItemType;
import static com.vituel.dndplayer.model.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.Item.ItemType.valueOf;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;


public class ItemDao extends AbstractEntityDao<Item> {

    public static final String TABLE = "item";

    private static final String COLUMN_SLOT_TYPE = "slot_type"; //held, head, torso, ring, etc
    private static final String COLUMN_ITEM_TYPE = "item_type"; //weapon, shield, armor

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_SLOT_TYPE + " text, "
            + COLUMN_ITEM_TYPE + " text, "
            + COLUMN_EFFECT_ID + " integer, "
            + "FOREIGN KEY(" + COLUMN_EFFECT_ID + ") REFERENCES " + EffectDao.TABLE + "(" + COLUMN_ID + ")"
            + ");";

    private WeaponDao weaponDao = new WeaponDao(context, database);
    private EffectDao effectDao = new EffectDao(context, database);

    public ItemDao(Context context) {
        super(context);
    }

    protected ItemDao(Context context, SQLiteDatabase database) {
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
                COLUMN_NAME,
                COLUMN_SLOT_TYPE,
                COLUMN_ITEM_TYPE,
                COLUMN_EFFECT_ID
        };
    }

    @Override
    public void save(Item entity) {

        if(entity.getName() == null) {
            Log.e(getClass().getSimpleName(), "null");
        }

        //basic data
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        if (entity.getSlotType() != null) {
            values.put(COLUMN_SLOT_TYPE, entity.getSlotType().toString());
        }
        if (entity.getItemType() != null) {
            values.put(COLUMN_ITEM_TYPE, entity.getItemType().toString());
        }

        //effects
        Effect effect = entity.getEffect();
        effectDao.save(effect);
        values.put(COLUMN_EFFECT_ID, effect.getId());

        long id = insertOrUpdate(values, entity.getId());

        //weapon properties
        if (entity.getItemType() == WEAPON) {
            WeaponItem weaponItem = (WeaponItem) entity;
            WeaponProperties weapon = weaponItem.getWeaponProperties();
            if (weapon.getId() == 0) {
                weaponDao.save(weapon);
            }
        }

        entity.setId(id);
    }

    @Override
    protected Item fromCursor(Cursor cursor) {

        //find type
        ItemType itemType;
        try {
            itemType = valueOf(cursor.getString(3));
        } catch (IllegalArgumentException | NullPointerException e) {
            itemType = null;
        }

        //basic fields
        Item item = itemType == WEAPON ? new WeaponItem() : new Item();
        item.setId(cursor.getLong(0));
        item.setName(cursor.getString(1));
        item.setItemType(itemType);
        item.setSlotType(SlotType.valueOf(cursor.getString(2)));

        //effect
        Effect effect = effectDao.findById(cursor.getLong(4));
        effect.setSourceName(item.getName());
        item.setEffect(effect);

        //weapon fields
        if (itemType == WEAPON) {
            WeaponItem w = (WeaponItem) item;
            WeaponProperties weapon = weaponDao.findByItem(item.getId());
            w.setWeaponProperties(weapon);
        }

        return item;
    }

    public List<Item> findBySlot(SlotType slotType) {
        String query = String.format("%s='%s'", COLUMN_SLOT_TYPE, slotType.toString());
        Cursor cursor = database.query(TABLE, allColumns(), query, null, null, null, null);
        return cursorToList(cursor);
    }
}
