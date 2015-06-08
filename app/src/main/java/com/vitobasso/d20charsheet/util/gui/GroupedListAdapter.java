package com.vitobasso.d20charsheet.util.gui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.widget.ListView.CHOICE_MODE_MULTIPLE;
import static android.widget.ListView.CHOICE_MODE_MULTIPLE_MODAL;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 10/05/2015.
 */
public abstract class GroupedListAdapter<T, G> extends BaseAdapter {

    private String TAG = GroupedListAdapter.class.getSimpleName();
    public static final int ITEM_VIEW_TYPE = 0;
    public static final int GROUP_VIEW_TYPE = 1;

    private final LayoutInflater inflater;
    private final List<Row> rows;
    private int itemLayout, groupLayout, itemTextView, groupTextView;

    protected GroupedListAdapter(Context context, Collection<T> items, int itemLayout,
                                 int groupLayout, int itemTextView, int groupTextView) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayout = itemLayout;
        this.groupLayout = groupLayout;
        this.itemTextView = itemTextView;
        this.groupTextView = groupTextView;
        this.rows = createRows(mapByGroup(items));
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        Row row = rows.get(position);
        return row.getObject();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Row row = rows.get(position);
        View view = crateOrRecycleView(row, position, convertView, parent);
        populateTextView(view, row.getTextViewResourceId(), row.toString());
        return view;
    }

    private View crateOrRecycleView(Row row, int position, View convertView, ViewGroup parent) {
        if (convertView != null && convertView.getTag() == row.getViewType()) {
            return convertView;
        } else {
            if (convertView != null) {
                Log.w(TAG, "Adapter received convertView of wrong type. Possibly need to manage list updates"); //TODO remove once fixed
            }
            return createView(row, position, parent);
        }
    }

    private View createView(Row row, int position, ViewGroup parent) {
        View view = inflater.inflate(row.getLayoutResource(), parent, false);
        view.setTag(row.getViewType());
        if (row.getViewType() == GROUP_VIEW_TYPE) {
            disableTouchOnRow(view);
        } else {
            setRowCheckedState(position, (ListView) parent);
        }
        return view;
    }

    private void disableTouchOnRow(View rowView) {
        rowView.setOnClickListener(null);
        rowView.setOnLongClickListener(null);
    }

    private void setRowCheckedState(int position, ListView listView) {
        int choiceMode = listView.getChoiceMode();
        if (choiceMode == CHOICE_MODE_MULTIPLE || choiceMode == CHOICE_MODE_MULTIPLE_MODAL) {
            T item = (T) getItem(position);
            listView.setItemChecked(position, isChecked(item));
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Row row = rows.get(position);
        return row.getViewType();
    }

    private Map<G, List<T>> mapByGroup(Collection<T> items) {
        Map<G, List<T>> map = new TreeMap<>();
        for (T item : items) {
            G pred = getGroup(item);
            List<T> list = map.get(pred);
            if (list == null) {
                list = new ArrayList<>();
                map.put(pred, list);
            }
            list.add(item);
        }
        return map;
    }

    private List<Row> createRows(Map<G, List<T>> map) {
        List<Row> result = new ArrayList<>();
        for (G group : map.keySet()) {
            result.add(new GroupRow(group));
            List<T> conditions = map.get(group);
            for (T item : conditions) {
                result.add(new ItemRow(item));
            }
        }
        return result;
    }

    private abstract class Row {
        abstract Object getObject();
        abstract int getViewType();
        abstract int getLayoutResource();
        abstract int getTextViewResourceId();
    }

    private class ItemRow extends Row {

        T item;

        private ItemRow(T item) {
            this.item = item;
        }

        @Override
        T getObject() {
            return item;
        }

        @Override
        int getViewType() {
            return ITEM_VIEW_TYPE;
        }

        @Override
        int getLayoutResource() {
            return itemLayout;
        }

        @Override
        int getTextViewResourceId() {
            return itemTextView;
        }

        @Override
        public String toString() {
            return itemToString(item);
        }

    }

    private class GroupRow extends Row {

        G group;

        private GroupRow(G group) {
            this.group = group;
        }

        @Override
        G getObject() {
            return group;
        }

        @Override
        int getViewType() {
            return GROUP_VIEW_TYPE;
        }

        @Override
        int getLayoutResource() {
            return groupLayout;
        }

        @Override
        int getTextViewResourceId() {
            return groupTextView;
        }

        @Override
        public String toString() {
            return groupToString(group);
        }
    }

    protected abstract G getGroup(T item);

    protected boolean isChecked(T item) {
        return false;
    }

    protected String itemToString(T item) {
        return item.toString();
    }

    protected String groupToString(G group) {
        return group.toString();
    }
}
