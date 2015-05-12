package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vituel.dndplayer.dao.abstraction.AbstractRuleDao;
import com.vituel.dndplayer.dao.dependant.WeaponDao;
import com.vituel.dndplayer.model.item.Item;
import com.vituel.dndplayer.model.item.SlotType;
import com.vituel.dndplayer.model.item.WeaponItem;
import com.vituel.dndplayer.model.item.WeaponProperties;

import java.text.MessageFormat;
import java.util.List;

import static com.vituel.dndplayer.model.item.Item.ItemType;
import static com.vituel.dndplayer.model.item.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.item.Item.ItemType.valueOf;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;


public class ItemDao extends AbstractRuleDao<Item> {

    public static final String TABLE = "item";

    private static final String COLUMN_SLOT_TYPE = "slot_type"; //held, head, torso, ring, etc
    private static final String COLUMN_ITEM_TYPE = "item_type"; //weapon, shield, armor

    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BOOK_ID + " integer not null, "
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
                COLUMN_BOOK_ID,
                COLUMN_NAME,
                COLUMN_SLOT_TYPE,
                COLUMN_ITEM_TYPE,
                COLUMN_EFFECT_ID
        };
    }

    @Override
    protected ContentValues toContentValues(Item entity) {

        if(entity.getName() == null) {
            Log.w(getClass().getSimpleName(), "Saving item with null name");
        }

        ContentValues values = super.toContentValues(entity);
        values = effectDao.preSaveEffectSource(values, entity);
        if (entity.getSlotType() != null) {
            values.put(COLUMN_SLOT_TYPE, entity.getSlotType().toString());
        }
        if (entity.getItemType() != null) {
            values.put(COLUMN_ITEM_TYPE, entity.getItemType().toString());
        }

        return values;
    }

    @Override
    public void postSave(Item entity) {
        long id = entity.getId();

        //weapon properties
        if (entity.getItemType() == WEAPON) {
            WeaponItem weaponItem = (WeaponItem) entity;
            WeaponProperties weapon = weaponItem.getWeaponProperties();
            if (weapon.getId() == 0) {
                weaponDao.save(id, weapon);
            }
        }
    }

    @Override
    public Item fromCursor(Cursor cursor) {

        //find type
        ItemType itemType;
        try {
            itemType = valueOf(cursor.getString(4));
        } catch (IllegalArgumentException | NullPointerException e) {
            itemType = null;
        }

        //basic fields
        Item item = itemType == WEAPON ? new WeaponItem() : new Item();
        effectDao.loadEffectSource(cursor, item, 0, 2, 5);
        item.setItemType(itemType);
        item.setSlotType(SlotType.valueOf(cursor.getString(3)));
        setRulebook(item, cursor, 1);

        //weapon fields
        if (itemType == WEAPON) {
            WeaponItem w = (WeaponItem) item;
            WeaponProperties weapon = weaponDao.findByItem(item.getId());
            w.setWeaponProperties(weapon);
        }

        return item;
    }

    public List<Item> findBySlot(SlotType slotType) {
        Cursor cursor = findBySlotCursor(slotType);
        return cursorToList(cursor);
    }

    public Cursor findBySlotCursor(SlotType slotType) {
        String query = String.format("%s='%s'", COLUMN_SLOT_TYPE, slotType.toString());
        return selectCursor(query);
    }

    public Cursor filterByNameAndSlotCursor(SlotType slotType, String name) {
        String query = MessageFormat.format("{0} like ''%{1}%'' and {2}=''{3}''", COLUMN_NAME, name, COLUMN_SLOT_TYPE, slotType.toString());
        return selectCursor(query);
    }
}
