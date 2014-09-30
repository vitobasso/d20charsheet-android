package com.vituel.dndplayer.util.i18n;

import android.content.Context;
import android.util.Log;

/**
 * Created by Victor on 29/09/2014.
 */
public abstract class AbstractEnumStringConverter<T extends Enum> {

    private Context ctx;

    public AbstractEnumStringConverter(Context ctx) {
        this.ctx = ctx;
    }

    public CharSequence toString(T value){
        Log.w(getClass().getSimpleName(), "Couldn't convert to string: " + value.toString());
        return value.toString();
    }

    protected CharSequence findResource(int res) {
        return ctx.getResources().getString(res);
    }

}
