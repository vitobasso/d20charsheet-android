package com.vituel.dndplayer.model;

import android.content.Context;

import java.io.Serializable;

/**
 * See DM Guide, p.214
 * <p/>
 * Created by Victor on 29/04/14.
 */
public class EquipSlot implements Serializable {

    private int nameRes;
    private SlotType slotType;
    private Item item;

    public EquipSlot(int nameRes, SlotType slotType) {
        this.setNameRes(nameRes);
        this.setSlotType(slotType);
    }

    public String getName(Context ctx) {
        return ctx.getString(getNameRes());
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public EquipSlot clone() {
        EquipSlot slot = new EquipSlot(getNameRes(), getSlotType());
        slot.setItem(getItem());
        return slot;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getNameRes() {
        return nameRes;
    }

    public void setNameRes(int nameRes) {
        this.nameRes = nameRes;
    }
}
