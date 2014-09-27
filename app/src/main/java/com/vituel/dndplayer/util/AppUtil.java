package com.vituel.dndplayer.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.vituel.dndplayer.parser.InitialDataLoader;

import static android.content.Context.MODE_PRIVATE;
import static com.vituel.dndplayer.util.ActivityUtil.PREF;
import static com.vituel.dndplayer.util.ActivityUtil.PREF_FIRST_RUN;

/**
 * Created by Victor on 26/03/14.
 */
public class AppUtil {

    public static void onLaunch(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PREF, MODE_PRIVATE);

        if (pref.getBoolean(PREF_FIRST_RUN, true)) {
            InitialDataLoader.loadDB(ctx);
        }

        pref.edit()
                .putBoolean(PREF_FIRST_RUN, false)
                .commit();
    }

}
