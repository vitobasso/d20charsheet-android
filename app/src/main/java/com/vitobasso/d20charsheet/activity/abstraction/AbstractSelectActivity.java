package com.vitobasso.d20charsheet.activity.abstraction;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SearchView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_EDITED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_REQUEST;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CREATE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_EDIT;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_SELECT;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;
import static com.vitobasso.d20charsheet.util.font.FontUtil.BOLD_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.MAIN_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setActionbarTitle;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 17/03/14.
 */
public abstract class AbstractSelectActivity<T extends AbstractEntity> extends Activity {

    //parameter
    protected int request;

    //ui
    protected ListView listView;
    protected SearchView searchView;

    protected Activity activity = this;
    private LoaderObserver loaderObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item);
        request = getIntent().getIntExtra(EXTRA_REQUEST, REQUEST_SELECT);

        onPrePopulate();

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchListener());

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(createAdapter());
        listView.setOnItemClickListener(new ClickListener());
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ContextualActionBarListener());

        loaderObserver = new LoaderObserver();
        getLoaderManager().initLoader(0, null, loaderObserver);

        setActionbarTitle(this, BOLD_FONT, getTitle());
    }

    private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.getFilter().filter(newText);
            return false;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            AbstractEntityDao<T> dao = createDataSource();
            try {
                T selected = dao.fromCursor(cursor);
                if (request != REQUEST_EDIT) {
                    finish(selected);
                } else {
                    goToEdit(selected);
                }
            } finally {
                dao.close();
            }
        }
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
                Intent intent = new Intent(this, getEditActivityClass());
                startActivityForResult(intent, REQUEST_CREATE);
                return true;
            default:
                return false;
        }
    }

    private class ContextualActionBarListener implements AbsListView.MultiChoiceModeListener {

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

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {}

        private boolean finish(ActionMode mode) {
            mode.finish();
            return true;
        }
    }

    protected ListAdapter createAdapter(){
        ResourceCursorAdapter adapter = new ResourceCursorAdapter(this, getRowLayout(), null, false) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                AbstractEntityDao<T> dao = createDataSource(); //doesn't need the db actually, just converting the cursor to entity
                try {
                    T entity = dao.fromCursorBrief(cursor);
                    onPopulateRow(view, entity);
                } finally {
                    dao.close();
                }
                setFontRecursively(activity, view, MAIN_FONT);
            }
        };
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                //TODO should move to Loader to run in background?
                AbstractEntityDao<T> dao = createDataSource();
                return onQueryFiltered(dao, constraint.toString()); //TODO leaking cursor & dao?
            }
        });
        return adapter;
    }

    private class LoaderObserver implements LoaderManager.LoaderCallbacks<Cursor> {

        private AbstractEntityDao<T> dao;

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(activity){
                @Override
                public Cursor loadInBackground() {
                    dao = createDataSource();
                    return onQuery(dao);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(data);
            dao.close();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(null);
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

    private void remove(T element) {
        AbstractEntityDao<T> dataSource = createDataSource();
        try {
            dataSource.remove(element);
        } finally {
            dataSource.close();
        }

        getLoaderManager().restartLoader(0, null, loaderObserver);
    }

    private void save(T created) {
        AbstractEntityDao<T> dataSource = createDataSource();
        try {
            dataSource.save(created);
        } finally {
            dataSource.close();
        }

        getLoaderManager().restartLoader(0, null, loaderObserver);
    }

    private void finish(T element) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED, element);
        onPreFinish(resultIntent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    protected abstract AbstractEntityDao<T> createDataSource();

    protected abstract Class<? extends AbstractEditActivity> getEditActivityClass();

    protected int getRowLayout() {
        return R.layout.simple_row;
    }

    protected void onPopulateRow(View view, T entity) {
        populateTextView(view, R.id.name, entity.getName());
    }

    protected void onPrePopulate() {
    }

    protected void onPreFinish(Intent resultIntent) {
    }

    protected Cursor onQuery(AbstractEntityDao<T> dataSource) {
        return dataSource.listAllCursor();
    }

    protected Cursor onQueryFiltered(AbstractEntityDao<T> dataSource, String filter) {
        return dataSource.filterByNameCursor(filter);
    }

}
