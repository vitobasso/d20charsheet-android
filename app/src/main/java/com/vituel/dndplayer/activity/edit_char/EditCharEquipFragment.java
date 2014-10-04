package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.SelectItemActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.CharEquip;
import com.vituel.dndplayer.model.EquipSlot;
import com.vituel.dndplayer.model.Item;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_TYPE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 17/03/14.
 */
public class EditCharEquipFragment extends PagerFragment<CharBase, EditCharActivity> {

    private CharEquip equipment = new CharEquip();
    private LinearLayout itemsRoot;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.edit_char_equip;
    }

    @Override
    protected void onPopulate() {
        itemsRoot = findView(root, R.id.list);
        equipment = data.getEquipment().clone();
        populate(equipment);
    }

    private void populate(CharEquip equipment) {

        List<EquipSlot> list = equipment.listEquip();
        for (EquipSlot equipSlot : list) {

            LinearLayout row = inflate(activity, itemsRoot, R.layout.edit_char_equip_row);
            TextView slotView = findView(row, R.id.slot);
            TextView itemView = findView(row, R.id.item);

            slotView.setText(equipSlot.getName(activity));

            Item item = equipSlot.getItem();
            if (item != null) {
                itemView.setText(item.toString());
            }
            itemView.setOnClickListener(new ItemListener(equipSlot));
        }

    }

    @Override
    public void onSaveToModel() {
        data.setEquipment(equipment);
    }

    private class ItemListener implements View.OnClickListener {

        EquipSlot slot;

        private ItemListener(EquipSlot slot) {
            this.slot = slot;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, SelectItemActivity.class);
            intent.putExtra(EXTRA_TYPE, slot);
            startActivityForResult(intent, REQUEST_SELECT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT:
                switch (resultCode) {
                    case RESULT_OK:

                        //get selected item
                        Item item = (Item) data.getSerializableExtra(EXTRA_SELECTED);
                        EquipSlot slot = (EquipSlot) data.getSerializableExtra(EXTRA_TYPE);

                        //update memory
                        EquipSlot memSlot = equipment.getByName(slot.getNameRes());
                        memSlot.setItem(item);

                        //update UI
                        itemsRoot.removeAllViews();
                        populate(equipment);
                        setFontRecursively(activity, root, MAIN_FONT);
                        break;
                }
                break;
        }
    }

}