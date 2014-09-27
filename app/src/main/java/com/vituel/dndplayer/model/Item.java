package com.vituel.dndplayer.model;

import static com.vituel.dndplayer.model.AbstractEffect.Type.EQUIP_MAGIC;

/**
 * Created by Victor on 25/02/14.
 */
public class Item extends AbstractEffect {

    public SlotType getSlotType() {
        return slotType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public double getWeight() {
        return weight;
    }

    public double getPrice() {
        return price;
    }

    public enum ItemType {
        WEAPON, SHIELD, ARMOR
    }

    private SlotType slotType; //slot where item can be equipped in character
    private ItemType itemType; //if not null, defines specific fields (weapon dmg, armor ac, etc)
    private double weight;
    private double price;

    public Item() {
        super(EQUIP_MAGIC);
    }

    protected Item(Type type, String name, SlotType slotType, ItemType itemType) {
        super(type, name);
        this.setSlotType(slotType);
        this.setItemType(itemType);
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
