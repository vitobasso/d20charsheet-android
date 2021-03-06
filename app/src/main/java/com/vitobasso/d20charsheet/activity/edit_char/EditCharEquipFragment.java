package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.PagerFragment;
import com.vitobasso.d20charsheet.activity.select.SelectItemActivity;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharEquip;
import com.vitobasso.d20charsheet.model.item.EquipSlot;
import com.vitobasso.d20charsheet.model.item.Item;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_TYPE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_SELECT;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.inflate;

/**
 * Created by Victor on 17/03/14.
 */
public class EditCharEquipFragment extends PagerFragment<CharBase, EditCharActivity> {

    private CharEquip equipment;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.edit_char_equip;
    }

    @Override
    protected void onPopulate() {
        LinearLayout itemsRoot = findView(root, R.id.list);
        itemsRoot.removeAllViews();
        equipment = data.getEquipment();

        List<EquipSlot> list = equipment.listEquip();
        for (EquipSlot equipSlot : list) {
            populateSlot(itemsRoot, equipSlot);
        }
    }

    private void populateSlot(LinearLayout itemsRoot, EquipSlot equipSlot) {
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

    @Override
    public void onSave() {
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
                        EquipSlot selectedSlot = (EquipSlot) data.getSerializableExtra(EXTRA_TYPE);

                        //update memory
                        EquipSlot memSlot = equipment.getByName(selectedSlot.getNameRes()); //TODO can set selectedSlot directlly?
                        memSlot.setItem(item);

                        //update UI
                        refresh();
                        break;
                }
                break;
        }
    }

}