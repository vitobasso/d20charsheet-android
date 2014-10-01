package com.vituel.dndplayer.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.Item;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.ModifierType;
import com.vituel.dndplayer.model.SlotType;
import com.vituel.dndplayer.model.WeaponItem;
import com.vituel.dndplayer.model.WeaponProperties;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vituel.dndplayer.model.Item.ItemType;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateSpinnerWithEnum;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readDice;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;
import static com.vituel.dndplayer.util.ActivityUtil.validateSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.validateText;

public class EditItemActivity extends AbstractEditActivity<Item> {

    private static final int NUM_EFFECTS = 5;

    @Override
    protected int getLayout() {
        return R.layout.edit_item;
    }

    @Override
    protected void populate() {

        //basic fields
        populateTextView(this, R.id.name, entity.getName());
        populateSpinnerWithEnum(this, null, R.id.slot, SlotType.values(), entity.getSlotType(), new SlotSelectionListener());
        populateSpinnerWithEnum(this, null, R.id.itemType, ItemType.values(), entity.getItemType(), new ItemTypeSelectionListener());

        //weapon fields
        if (entity instanceof WeaponItem && entity.getItemType() == ItemType.WEAPON) {
            WeaponItem weapon = (WeaponItem) entity;
            WeaponProperties wp = weapon.getWeaponProperties();

            View weaponFields = findViewById(R.id.weapon_fields);
            weaponFields.setVisibility(VISIBLE);

            populateTextView(this, R.id.damage, wp.getDamage());
            populateTextView(this, R.id.crit_range, wp.getCritical().getRange());
            populateTextView(this, R.id.crit_mult, wp.getCritical().getMultiplier());
        }

        //modifiers
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        List<Modifier> modifiers = entity.getModifiers();
        for (Modifier modifier : modifiers) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);

            populateSpinnerWithEnum(this, group, R.id.target, ModifierTarget.values(), modifier.getTarget(), null);
            populateSpinnerWithEnum(this, group, R.id.type, ModifierType.values(), modifier.getType(), null);
            populateTextView(group, R.id.amount, modifier.getAmount());

        }

        //remaining empty modifiers
        for (int i = modifiers.size(); i < NUM_EFFECTS; i++) {
            ViewGroup group = inflate(this, effectsRoot, R.layout.edit_modifier);
            populateSpinnerWithEnum(this, group, R.id.target, ModifierTarget.values(), null, null);
            populateSpinnerWithEnum(this, group, R.id.type, ModifierType.values(), null, null);
        }
    }

    @Override
    protected boolean validate() {
        boolean allValid = validateText(this, R.id.name);
        allValid &= validateSpinner(this, R.id.slot);

        SlotType slotType = readSpinner(this, R.id.slot);
        if (slotType == SlotType.HELD) {
            allValid &= validateSpinner(this, R.id.itemType);
        }

        ItemType itemType = readSpinner(this, R.id.itemType);
        if (itemType == ItemType.WEAPON) {
            allValid &= validateText(this, R.id.damage);
            allValid &= validateText(this, R.id.crit_range);
            allValid &= validateText(this, R.id.crit_mult);
        }

        return allValid;
    }

    @Override
    protected Item save() {

        //item type
        ItemType itemType = readSpinner(this, R.id.itemType);
        if (itemType == ItemType.WEAPON) {
            long id = entity.getId();
            entity = new WeaponItem();
            entity.setId(id);
        }

        //basic fields
        entity.setName(readString(this, R.id.name));
        entity.setItemType(itemType);
        entity.setSlotType((SlotType)readSpinner(this, R.id.slot));

        //weapon fields
        if (entity.getItemType() == ItemType.WEAPON) {
            WeaponItem weapon = (WeaponItem) entity;
            WeaponProperties wp = weapon.getWeaponProperties();

            String dmgStr = readString(this, R.id.damage);
            wp.setDamage(new DiceRoll(dmgStr));
            wp.getCritical().setRange(readInt(this, R.id.crit_range));
            wp.getCritical().setMultiplier(readInt(this, R.id.crit_mult));
        }

        //modifiers
        entity.getModifiers().clear();
        ViewGroup effectsRoot = findView(this, R.id.effectsList);
        for (int i = 0; i < effectsRoot.getChildCount(); i++) {
            ViewGroup group = (ViewGroup) effectsRoot.getChildAt(i);
            ModifierTarget target = readSpinner(group, R.id.target);
            DiceRoll amount = readDice(group, R.id.amount);
            ModifierType type = readSpinner(group, R.id.type);

            if (target != null && amount != null) {
                Modifier modifier = new Modifier(target, amount, type, entity);
                entity.getModifiers().add(modifier);
            }
        }

        //save to db
        ItemDao dataSource = new ItemDao(this);
        dataSource.save(entity);
        dataSource.close();

        return entity;
    }

    private class SlotSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            View label = findView(EditItemActivity.this, R.id.itemTypeLabel);
            Spinner spinner = findView(EditItemActivity.this, R.id.itemType);
            if (position >= 0 && SlotType.values()[position] == SlotType.HELD) {
                label.setVisibility(VISIBLE);
                spinner.setVisibility(VISIBLE);
            } else {
                label.setVisibility(GONE);
                spinner.setVisibility(GONE);
                //TODO hide weapon fields
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class ItemTypeSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            View weaponFields = findView(EditItemActivity.this, R.id.weapon_fields);
            if (position >= 0 && ItemType.values()[position] == ItemType.WEAPON) {
                weaponFields.setVisibility(VISIBLE);
            } else {
                weaponFields.setVisibility(GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

}
