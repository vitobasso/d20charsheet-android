package com.vitobasso.d20charsheet.model.item;

import android.content.Context;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * See DM Guide, p.214
 * <p/>
 * Created by Victor on 29/04/14.
 */
public class EquipSlot implements Serializable {

    @JsonIgnore
    private int nameRes;
    private SlotType slotType;
    private Item item;

    // used by jackson
    public EquipSlot() {
    }

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

    @Override
    public String toString() {
        return slotType + " " + item;
    }
}
