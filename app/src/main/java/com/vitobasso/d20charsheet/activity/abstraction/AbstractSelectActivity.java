package com.vitobasso.d20charsheet.activity.abstraction;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;
import static com.vitobasso.d20charsheet.util.font.FontUtil.BOLD_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.MAIN_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setActionbarTitle;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 17/03/14.
 */
public abstract class AbstractSelectActivity<T extends AbstractEntity> extends Activity {

    protected ListView listView;
    protected SearchView searchView;

    protected Activity activity = this;
    private LoaderObserver loaderObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item);

        onPrePopulate();

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchListener());

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(createAdapter());
        listView.setOnItemClickListener(new ClickListener());

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
                finish(selected);
            } finally {
                dao.close();
            }
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

    private void finish(T element) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED, element);
        onPreFinish(resultIntent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    protected void refreshUI() {
        getLoaderManager().restartLoader(0, null, loaderObserver);
    }

    protected abstract AbstractEntityDao<T> createDataSource();

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
