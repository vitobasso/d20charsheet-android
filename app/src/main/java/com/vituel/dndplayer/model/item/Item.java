package com.vituel.dndplayer.model.item;

import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.effect.Effect;
import com.vituel.dndplayer.model.effect.EffectSource;

/**
 * Created by Victor on 25/02/14.
 */
public class Item extends AbstractEntity implements EffectSource {

    public enum ItemType {
        WEAPON, SHIELD, ARMOR
    }

    private SlotType slotType; //slot where item can be equipped in character
    private ItemType itemType; //if not null, defines specific fields (weapon dmg, armor ac, etc)
    private Effect effect;
    private Double weight;
    private Double price;

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}