package com.vituel.dndplayer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.vituel.dnd_character_sheet.R;
import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.*;
import static com.vituel.dndplayer.util.font.FontUtil.*;

/**
 * Created by Victor on 17/03/14.
 */
public abstract class AbstractSelectActivity<T extends AbstractEntity> extends Activity {

    //parameter
    protected int request;

    //data
    protected List<T> list;
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
        listView.setOnItemLongClickListener(new LongClickListener());
        listView.setOnItemClickListener(new ClickListener());

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

    private class LongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
            final T selected = (T) listView.getItemAtPosition(pos);

            AlertDialog.Builder builder = new AlertDialog.Builder(AbstractSelectActivity.this);
            builder.setItems(R.array.edit_dialog_items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: //edit
                            edit(selected);
                            break;
                        case 1: //remove
                            remove(selected);
                            dialog.dismiss();
                            break;
                    }
                }
            });

            final Dialog dialog = builder.create();
            dialog.show();
            return false;
        }

    }

    private void select(T selected) {
        //send data back to caller activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED, selected);
        onPreFinish(resultIntent);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void edit(T selected) {
        //call edit activity
        Intent intent = new Intent(AbstractSelectActivity.this, getEditActivityClass());
        intent.putExtra(EXTRA_SELECTED, selected);
        startActivityForResult(intent, REQUEST_CREATE);
    }

    private void remove(T selected) {
        //deleteAll from db
        AbstractEntityDao<T> dataSource = getDataSource();
        dataSource.remove(selected);
        dataSource.close();

        //update ui
        list.remove(selected);
        updateUI();
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
        list = onQueryDB(dataSource);
        dataSource.close();

        //update ui
        updateUI();
    }

    private void updateUI() {
        List<T> filtered = filter();
        display(filtered);
    }

    private List<T> filter() {
        if (filter == null || filter.isEmpty()) {
            return list;
        } else {
            List<T> filtered = new ArrayList<T>();
            for (T cond : list) {
                if (cond.getName().toLowerCase().contains(filter.toLowerCase().trim())) {
                    filtered.add(cond);
                }
            }
            return filtered;
        }
    }

    private void display(List<T> list) {
        listView.setAdapter(new ArrayAdapter<T>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                setFontRecursively(AbstractSelectActivity.this, v, MAIN_FONT);
                return v;
            }
        });
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

}
