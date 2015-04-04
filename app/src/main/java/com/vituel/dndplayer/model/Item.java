package com.vituel.dndplayer.model;

/**
 * Created by Victor on 25/02/14.
 */
public class Item extends AbstractEntity {

    public enum ItemType {
        WEAPON, SHIELD, ARMOR
    }

    private SlotType slotType; //slot where item can be equipped in character
    private ItemType itemType; //if not null, defines specific fields (weapon dmg, armor ac, etc)
    private Effect effect;
    private double weight;
    private double price;

    public Item() {
    }

    protected Item(String name, SlotType slotType, ItemType itemType) {
        super(name);
        this.setSlotType(slotType);
        this.setItemType(itemType);
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
