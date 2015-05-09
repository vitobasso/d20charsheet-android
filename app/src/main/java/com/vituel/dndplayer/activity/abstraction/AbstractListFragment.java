package com.vituel.dndplayer.activity.abstraction;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vituel.dndplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;

/**
 * Fragment with:
 * - Contextual action bar w/ remove button.
 * - Click listener on item (remembers clicked item index)
 *
 * Created by Victor on 07/04/2015.
 */
public abstract class AbstractListFragment<T, A extends Activity & PagerActivity<T>, E> extends PagerFragment<T, A> {

    protected List<E> listData;
    protected ListView listView;
    protected int clickedIndex = -1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.list;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_SELECTED, clickedIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            clickedIndex = savedInstanceState.getInt(EXTRA_SELECTED, -1);
        }
    }

    @Override
    protected void onPopulate() {
        this.listData = getListData();
        this.listView = (ListView) root;
        listView.setAdapter(createAdapter());
        listView.setOnItemClickListener(new ClickListener());
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ContextualActionBarListener());
    }

    protected abstract ListAdapter createAdapter();

    protected abstract List<E> getListData();

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            clickedIndex = position;
            onClickRow(listData.get(position));
        }
    }

    protected void onClickRow(E element){}


    private class ContextualActionBarListener implements AbsListView.MultiChoiceModeListener {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate the menu for the CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.remove, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove:
                    removeSelected();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            // Here you can do something when items are selected/de-selected,
            // such as update the title in the CAB
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

    private void removeSelected() {
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        List<E> toRemove = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                toRemove.add(listData.get(i));
            }
        }
        for (E element : toRemove) {
            listData.remove(element);
        }
        refresh();
    }


}
