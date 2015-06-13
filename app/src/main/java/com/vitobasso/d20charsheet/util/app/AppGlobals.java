package com.vitobasso.d20charsheet.util.app;

import android.app.Application;

import com.vitobasso.d20charsheet.model.character.CharBase;

/**
 * Created by Victor on 03/05/2015.
 */
public class AppGlobals extends Application {

    public static final String TAG = AppGlobals.class.getSimpleName();

    private boolean isCharOpened;
    private CharBase charBase;

    public void setOpenedChar(CharBase openedChar) {
        this.charBase = openedChar;
        this.isCharOpened = true;
    }

    public void setNewChar(CharBase openedChar) {
        this.charBase = openedChar;
        this.isCharOpened = false;
    }

    public CharBase getChar() {
        return charBase;
    }

    public boolean isCharSet() {
        return charBase != null;
    }

    public boolean isCharOpened() {
        return charBase != null && isCharOpened;
    }

}
