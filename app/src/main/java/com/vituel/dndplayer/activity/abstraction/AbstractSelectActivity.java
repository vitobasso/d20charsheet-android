package com.vituel.dndplayer.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.AbstractEntityDao;
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

    //data
    protected List<T> fullList, filteredList;
    protected String filter;

    //ui
    protected ListView listView;
    protected SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item);
        request = getIntent().getIntExtra(EXTRA_REQUEST, REQUEST_SELECT);

        onPrePopulate();

        listView = (ListView) findViewById(R.id.list);
        searchView = (SearchView) findViewById(R.id.search);

        refresh();

        searchView.setOnQueryTextListener(new SearchListener());
        listView.setOnItemClickListener(new ClickListener());
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ContextualActionBarListener());

        setActionbarTitle(this, BOLD_FONT, getTitle());
    }

    private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            filter = newText;
            updateUI();
            return false;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            T selected = (T) listView.getItemAtPosition(position);

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

                        //save to db
                        AbstractEntityDao<T> dataSource = getDataSource();
                        dataSource.save(created);
                        dataSource.close();

                        refresh();
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
        switch (menuItem.getItemId()) {
            case R.id.action_create:

                //create new character
                Intent intent = new Intent(this, getEditActivityClass());
                startActivityForResult(intent, REQUEST_CREATE);
                return true;

            default:
                return defaultOnOptionsItemSelected(menuItem, this);
        }
    }

    private void refresh() {
        //update memory
        AbstractEntityDao<T> dataSource = getDataSource();
        fullList = onQueryDB(dataSource);
        dataSource.close();

        //update ui
        updateUI();
    }

    private void updateUI() {
        filteredList = filter();
        listView.setAdapter(createAdapter(filteredList));
    }

    private List<T> filter() {
        if (filter == null || filter.isEmpty()) {
            return fullList;
        } else {
            List<T> filtered = new ArrayList<T>();
            for (T element : fullList) {
                if (element.getName().toLowerCase().contains(filter.toLowerCase().trim())) {
                    filtered.add(element);
                }
            }
            return filtered;
        }
    }

    protected abstract AbstractEntityDao<T> getDataSource();

    protected abstract Class<? extends AbstractEditActivity> getEditActivityClass();

    protected void onPrePopulate() {
    }

    protected void onPreFinish(Intent resultIntent) {
    }

    protected List<T> onQueryDB(AbstractEntityDao<T> dataSource) {
        return dataSource.listAll();
    }


    private class ContextualActionBarListener implements AbsListView.MultiChoiceModeListener {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the menu for the CAB
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
                    mode.finish(); // Action picked, so close the CAB
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
            // Here you can perform updates to the CAB due to
            // an invalidate() request
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Here you can make any necessary updates to the activity when
            // the CAB is removed. By default, selected items are deselected/unchecked.
        }
    }

    private void editSelected() {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < checked.size(); i++) {
            if (checked.get(i)) {
                T element = filteredList.get(i);
                edit(element);
                break;
            }
        }
    }

    private void removeSelected() {
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();

        //choose elements for removal
        List<T> toRemove = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                toRemove.add(filteredList.get(i));
            }
        }

        //actually remove elements
        for (T element : toRemove) {
            remove(element);
        }

        updateUI();
    }

    private void select(T element) {
        //send data back to caller activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED, element);
        onPreFinish(resultIntent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void edit(T element) {
        //call edit activity
        Intent intent = new Intent(AbstractSelectActivity.this, getEditActivityClass());
        intent.putExtra(EXTRA_SELECTED, element);
        startActivityForResult(intent, REQUEST_CREATE);
    }

    private void remove(T element) {
        //deleteAll from db
        AbstractEntityDao<T> dataSource = getDataSource();
        dataSource.remove(element);
        dataSource.close();

        //update ui
        fullList.remove(element);
    }

    protected ListAdapter createAdapter(List<T> list){
        return new ArrayAdapter<T>(this, R.layout.simple_row, R.id.name, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                setFontRecursively(AbstractSelectActivity.this, v, MAIN_FONT);
                return v;
            }
        };
    }

}
