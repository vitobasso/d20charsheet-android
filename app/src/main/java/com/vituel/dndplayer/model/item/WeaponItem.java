package com.vituel.dndplayer.model.item;

import static com.vituel.dndplayer.model.item.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.item.SlotType.HELD;

/**
 * Created by Victor on 19/03/14.
 */
public class WeaponItem extends Item {

    private WeaponProperties weaponProperties;

    public WeaponItem() {
        this.setSlotType(HELD);
        this.setItemType(WEAPON);
        this.setWeaponProperties(new WeaponProperties());
    }

    public WeaponProperties getWeaponProperties() {
        return weaponProperties;
    }

    public void setWeaponProperties(WeaponProperties weaponProperties) {
        this.weaponProperties = weaponProperties;
    }
}
