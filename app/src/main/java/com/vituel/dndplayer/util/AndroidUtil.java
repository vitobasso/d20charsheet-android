package com.vituel.dndplayer.util;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Victor on 22/04/14.
 */
public class AndroidUtil {

    public static Point screenSize(Activity ctx) {
        Display display = ctx.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int screenWidth(Activity ctx) {
        return screenSize(ctx).x;
    }

    public static int screenHeight(Activity ctx) {
        return screenSize(ctx).y;
    }

    public static float screenDensity(Activity ctx){
        DisplayMetrics metrics = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

}
