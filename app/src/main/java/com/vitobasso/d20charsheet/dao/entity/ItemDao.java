package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractRuleDao;
import com.vitobasso.d20charsheet.dao.dependant.WeaponDao;
import com.vitobasso.d20charsheet.model.item.Item;
import com.vitobasso.d20charsheet.model.item.SlotType;
import com.vitobasso.d20charsheet.model.item.WeaponItem;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;
import com.vitobasso.d20charsheet.util.database.Table;

import java.text.MessageFormat;

import static com.vitobasso.d20charsheet.model.item.Item.ItemType;
import static com.vitobasso.d20charsheet.model.item.Item.ItemType.WEAPON;
import static com.vitobasso.d20charsheet.model.item.Item.ItemType.valueOf;
import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_EFFECT_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;


public class ItemDao extends AbstractRuleDao<Item> {

    private static final String COLUMN_SLOT_TYPE = "slot_type"; //held, head, torso, ring, etc
    private static final String COLUMN_ITEM_TYPE = "item_type"; //weapon, shield, armor

    public static final Table TABLE = new Table("item")
            .colNotNull(COLUMN_BOOK_ID, INTEGER)
            .colNotNull(COLUMN_NAME, TEXT)
            .col(COLUMN_SLOT_TYPE, TEXT)
            .col(COLUMN_ITEM_TYPE, TEXT)
            .col(COLUMN_EFFECT_ID, INTEGER);

    private WeaponDao weaponDao = new WeaponDao(context, database);
    private EffectDao effectDao = new EffectDao(context, database);

    public ItemDao(Context context) {
        super(context);
    }

    protected ItemDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Table getTable() {
        return TABLE;
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
            itemType = valueOf(getString(cursor, COLUMN_ITEM_TYPE));
        } catch (IllegalArgumentException | NullPointerException e) {
            itemType = null;
        }

        //basic fields
        Item item = itemType == WEAPON ? new WeaponItem() : new Item();
        effectDao.loadEffectSource(cursor, item, TABLE);
        item.setItemType(itemType);
        item.setSlotType(SlotType.valueOf(getString(cursor, COLUMN_SLOT_TYPE)));
        setRulebook(item, cursor);

        //weapon fields
        if (itemType == WEAPON) {
            WeaponItem w = (WeaponItem) item;
            WeaponProperties weapon = weaponDao.findByItem(item.getId());
            weapon.setName(w.getName());
            w.setWeaponProperties(weapon);
        }

        return item;
    }

    public Cursor findBySlotCursor(SlotType slotType) {
        String query = String.format("%s='%s'", COLUMN_SLOT_TYPE, slotType.toString());
        return cursor(query);
    }

    public Cursor filterByNameAndSlotCursor(SlotType slotType, String name) {
        String query = MessageFormat.format("{0} like ''%{1}%'' and {2}=''{3}''", COLUMN_NAME, name, COLUMN_SLOT_TYPE, slotType.toString());
        return cursor(query);
    }
}
