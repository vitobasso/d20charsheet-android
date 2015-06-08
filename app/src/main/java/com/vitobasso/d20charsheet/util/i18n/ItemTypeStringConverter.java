package com.vitobasso.d20charsheet.util.i18n;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.item.Item;

/**
 * Created by Victor on 29/09/2014.
 */
public class ItemTypeStringConverter extends AbstractEnumStringConverter<Item.ItemType> {

    public ItemTypeStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(Item.ItemType type) {
        switch (type) {
            case WEAPON:
                return findResource(R.string.weapon);
            case PROTECTIVE:
                return findResource(R.string.shield);
            default:
                return super.toString(type);
        }
    }

}
