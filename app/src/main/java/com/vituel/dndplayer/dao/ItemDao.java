package com.vituel.dndplayer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vituel.dndplayer.model.Item;
import com.vituel.dndplayer.model.SlotType;
import com.vituel.dndplayer.model.WeaponItem;

import java.util.List;

import static com.vituel.dndplayer.model.AbstractEffect.Type.EQUIP_MAGIC;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;
import static com.vituel.dndplayer.model.Item.ItemType;
import static com.vituel.dndplayer.model.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.Item.ItemType.valueOf;


public class ItemDao extends AbstractEntityDao<Item> {

    public static final String TABLE = "item";

    private static final String COLUMN_SLOT_TYPE = "slot_type"; //held, head, torso, ring, etc
    private static final String COLUMN_ITEM_TYPE = "item_type"; //weapon, shield, armor
    private static final String COLUMN_WEAPON_ID = "weapon_id"; //for weapons only

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_SLOT_TYPE + " text, "
            + COLUMN_ITEM_TYPE + " text, "
            + COLUMN_WEAPON_ID + " integer"
            + ");";


    public ItemDao(Context context) {
        super(context);
    }

    public ItemDao(Context context, SQLiteDatabase database) {
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
                COLUMN_WEAPON_ID
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

        //weapon properties
        if (entity.getItemType() == WEAPON) {
            WeaponDao weaponDao = new WeaponDao(context, database);
            WeaponItem weapon = (WeaponItem) entity;
            long weaponId = weaponDao.save(weapon.getWeaponProperties());
            values.put(COLUMN_WEAPON_ID, weaponId);
        }

        long id = insertOrUpdate(values, entity.getId());

        //magic bonuses
        ModifierDao effectData = new ModifierDao(context);
        effectData.removeAllForEffect(EQUIP_MAGIC, id);
        effectData.save(entity.getModifiers(), EQUIP_MAGIC, id);

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

        //weapon fields
        if (itemType == WEAPON) {
            WeaponDao weaponDao = new WeaponDao(context, database);
            WeaponItem w = (WeaponItem) item;
            w.setWeaponProperties(weaponDao.findById(cursor.getLong(4)));
        }

        //magic bonuses
        ModifierDao effectData = new ModifierDao(context);
        item.setModifiers(effectData.findAll(EQUIP_MAGIC, item.getId()));

        return item;
    }

    public List<Item> findBySlot(SlotType slotType) {
        String query = String.format("%s='%s'", COLUMN_SLOT_TYPE, slotType.toString());
        Cursor cursor = database.query(TABLE, allColumns(), query, null, null, null, null);
        return cursorToList(cursor);
    }
}
