package com.vituel.dndplayer.activity.select;

import android.content.Intent;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectEffectActivity;
import com.vituel.dndplayer.activity.edit.EditItemActivity;
import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.model.EquipSlot;
import com.vituel.dndplayer.model.Item;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_TYPE;

/**
 * Created by Victor on 17/03/14.
 */
public class SelectItemActivity extends AbstractSelectEffectActivity<Item> {

    private EquipSlot slot;

    @Override
    protected AbstractEntityDao<Item> getDataSource() {
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
    protected List<Item> onQueryDB(AbstractEntityDao<Item> dataSource) {
        if (slot != null) {
            return ((ItemDao) dataSource).findBySlot(slot.getSlotType());
        } else {
            return dataSource.listAll();
        }
    }

    @Override
    protected void onPreFinish(Intent resultIntent) {
        resultIntent.putExtra(EXTRA_TYPE, slot);
    }

}
