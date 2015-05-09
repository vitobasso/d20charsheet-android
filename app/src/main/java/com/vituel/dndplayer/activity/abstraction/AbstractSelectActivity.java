package com.vituel.dndplayer.activity.abstraction;

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

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_REQUEST;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CREATE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_SELECT;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

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
            T selected = getDataSource().fromCursor(cursor);

            if (request != REQUEST_EDIT) {
                select(selected);
            } else {
                edit(selected);
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
                return defaultOnOptionsItemSelected(menuItem, this);
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
                    mode.finish();
                case R.id.action_remove:
                    removeSelected();
                    mode.finish();
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
    }

    protected ListAdapter createAdapter(){
        ResourceCursorAdapter adapter = new ResourceCursorAdapter(this, getRowLayout(), null, false) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                T entity = getDataSource().fromCursorBrief(cursor);
                onPopulateRow(view, entity);
                setFontRecursively(activity, view, MAIN_FONT);
            }
        };
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                //TODO should move to Loader to run in background?
                return onQueryFiltered(getDataSource(), constraint.toString());
            }

        });
        return adapter;
    }

    private class LoaderObserver implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(activity){
                @Override
                public Cursor loadInBackground() {
                    return onQuery(getDataSource());
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            CursorAdapter adapter = (CursorAdapter) listView.getAdapter();
            adapter.changeCursor(data);
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
        return getDataSource().fromCursor(cursor);
    }


    private void editSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            T element = getElementAt(position);
            edit(element);
            break;
        }
    }

    private void removeSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();

        //mark elements for removal
        List<T> toRemove = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            toRemove.add(getElementAt(position));
        }

        //actually remove elements
        for (T element : toRemove) {
            remove(element);
        }

    }

    private void edit(T element) {
        //call edit activity
        Intent intent = new Intent(activity, getEditActivityClass());
        intent.putExtra(EXTRA_SELECTED, element);
        startActivityForResult(intent, REQUEST_CREATE);
    }

    private void remove(T element) {
        //deleteAll from db
        AbstractEntityDao<T> dataSource = getDataSource();
        dataSource.remove(element);
        dataSource.close();

        getLoaderManager().restartLoader(0, null, loaderObserver);
    }

    private void select(T element) {
        //send data back to caller activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED, element);
        onPreFinish(resultIntent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void save(T created) {
        //save to db
        AbstractEntityDao<T> dataSource = getDataSource();
        dataSource.save(created);
        dataSource.close();

        getLoaderManager().restartLoader(0, null, loaderObserver);
    }

    protected abstract AbstractEntityDao<T> getDataSource();

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
