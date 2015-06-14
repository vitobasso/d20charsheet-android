package com.vitobasso.d20charsheet.activity.abstraction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.CursorAdapter;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.util.gui.SimpleMultiChoiceModeListener;

import java.util.ArrayList;
import java.util.List;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_EDITED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CREATE;

/**
 * Created by Victor on 17/03/14.
 */
public abstract class AbstractSelectEditActivity<T extends AbstractEntity> extends AbstractSelectActivity<T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ContextualActionBarListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CREATE:
                switch (resultCode) {
                    case RESULT_OK:
                        T created = (T) data.getSerializableExtra(EXTRA_EDITED);
                        save(created);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        switch (menuItem.getItemId()) {
            case R.id.action_create:
                goToCreate();
                return true;
            default:
                return false;
        }
    }

    private class ContextualActionBarListener extends SimpleMultiChoiceModeListener {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.list_selection, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    editSelected();
                    return finish(mode);
                case R.id.action_remove:
                    removeSelected();
                    return finish(mode);
                default:
                    return false;
            }
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            MenuItem item = mode.getMenu().findItem(R.id.action_edit);
            item.setVisible(listView.getCheckedItemCount() == 1);
            invalidateOptionsMenu();
        }

    }


    private T getElementAt(int position) {
        CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
        Cursor cursor = (Cursor) adapter.getItem(position);
        AbstractEntityDao<T> dao = createDataSource();
        try {
            return dao.fromCursor(cursor);
        } finally {
            dao.close();
        }
    }

    private List<T> getAllSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        List<T> toRemove = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            toRemove.add(getElementAt(position));
        }
        return toRemove;
    }

    private T getFirstSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        int position = checked.keyAt(0);
        return getElementAt(position);
    }

    private void editSelected() {
        T element = getFirstSelected();
        goToEdit(element);
    }

    private void removeSelected() {
        for (T element : getAllSelected()) {
            remove(element);
        }
    }

    private void goToEdit(T element) {
        Intent intent = new Intent(activity, getEditActivityClass());
        intent.putExtra(EXTRA_SELECTED, element);
        startActivityForResult(intent, REQUEST_CREATE);
    }

    private void goToCreate() {
        Intent intent = new Intent(this, getEditActivityClass());
        startActivityForResult(intent, REQUEST_CREATE);
    }

    private void remove(T element) {
        AbstractEntityDao<T> dataSource = createDataSource();
        try {
            dataSource.remove(element);
        } finally {
            dataSource.close();
        }

        refreshUI();
    }

    private void save(T created) {
        AbstractEntityDao<T> dataSource = createDataSource();
        try {
            dataSource.save(created);
        } finally {
            dataSource.close();
        }

        refreshUI();
    }

    protected abstract Class<? extends AbstractEditActivity> getEditActivityClass();

}
