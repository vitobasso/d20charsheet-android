package com.vituel.dndplayer.activity.select;

import android.content.Intent;
import android.database.Cursor;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectEffectActivity;
import com.vituel.dndplayer.activity.edit.EditItemActivity;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.entity.ItemDao;
import com.vituel.dndplayer.model.item.EquipSlot;
import com.vituel.dndplayer.model.item.Item;

import static com.vituel.dndplayer.util.app.ActivityUtil.EXTRA_TYPE;

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
