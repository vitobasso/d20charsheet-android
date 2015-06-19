package com.vitobasso.d20charsheet.util.gui;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Victor on 24/04/14.
 */
public abstract class RecursiveViewCaller<T extends View> {

    private Class<T> type;

    protected RecursiveViewCaller(Class<T> type) {
        this.type = type;
    }

    public void recursiveCall(View view, Object... params) {
        if (type.isInstance(view)) {
            leafCall((T) view, params);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                recursiveCall(child, params);
            }
        }
    }

    protected abstract void leafCall(T v, Object... params);

}
