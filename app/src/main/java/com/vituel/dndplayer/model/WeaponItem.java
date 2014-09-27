package com.vituel.dndplayer.model;

import static com.vituel.dndplayer.model.AbstractEffect.Type.EQUIP_MAGIC;
import static com.vituel.dndplayer.model.Item.ItemType.WEAPON;
import static com.vituel.dndplayer.model.SlotType.HELD;

/**
 * Created by Victor on 19/03/14.
 */
public class WeaponItem extends Item {

    private WeaponProperties weaponProperties;

    public WeaponItem() {
        super(EQUIP_MAGIC, null, HELD, WEAPON);
        this.setWeaponProperties(new WeaponProperties());
    }

    public WeaponProperties getWeaponProperties() {
        return weaponProperties;
    }

    public void setWeaponProperties(WeaponProperties weaponProperties) {
        this.weaponProperties = weaponProperties;
    }
}
