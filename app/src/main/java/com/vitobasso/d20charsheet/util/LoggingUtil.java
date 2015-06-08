package com.vitobasso.d20charsheet.util;

import android.util.Log;

/**
 * Created by Victor on 06/06/2015.
 */
public class LoggingUtil {

    public static void logTime(String TAG, long startTime, String actionName) {
        long endTime = System.currentTimeMillis();
        float time = (endTime - startTime) / 1000f;
        String msg = String.format("Took %.2f seconds to %s", time, actionName);
        Log.i(TAG, msg);
    }

}
