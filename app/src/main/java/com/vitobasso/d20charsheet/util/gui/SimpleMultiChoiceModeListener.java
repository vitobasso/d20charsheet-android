package com.vitobasso.d20charsheet.util.gui;

import android.view.ActionMode;
import android.view.Menu;
import android.widget.AbsListView;

/**
 * Created by Victor on 11/06/2015.
 */
public abstract class SimpleMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    protected boolean finish(ActionMode mode) {
        mode.finish();
        return true;
    }

}
