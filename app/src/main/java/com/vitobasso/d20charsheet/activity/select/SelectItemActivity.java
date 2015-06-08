package com.vitobasso.d20charsheet.activity.select;

import android.content.Intent;
import android.database.Cursor;

import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditActivity;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractSelectEffectActivity;
import com.vitobasso.d20charsheet.activity.edit.EditItemActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.entity.ItemDao;
import com.vitobasso.d20charsheet.model.item.EquipSlot;
import com.vitobasso.d20charsheet.model.item.Item;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_TYPE;

/**
 * Created by Victor on 17/03/14.
 */
public class SelectItemActivity extends AbstractSelectEffectActivity<Item> {

    private EquipSlot slot;

    @Override
    protected AbstractEntityDao<Item> createDataSource() {
        return new ItemDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditItemActivity.class;
    }

    @Override
    protected void onPrePopulate() {
        slot = (EquipSlot) getIntent().getSerializableExtra(EXTRA_TYPE);
    }

    @Override
    protected Cursor onQuery(AbstractEntityDao<Item> dataSource) {
        if (slot == null) {
            return super.onQuery(dataSource);
        } else {
            return ((ItemDao) dataSource).findBySlotCursor(slot.getSlotType());
        }
    }

    @Override
    protected Cursor onQueryFiltered(AbstractEntityDao<Item> dataSource, String filter) {
        if (slot == null) {
            return super.onQueryFiltered(dataSource, filter);
        } else {
            return ((ItemDao) dataSource).filterByNameAndSlotCursor(slot.getSlotType(), filter);
        }
    }

    @Override
    protected void onPreFinish(Intent resultIntent) {
        resultIntent.putExtra(EXTRA_TYPE, slot);
    }

}
