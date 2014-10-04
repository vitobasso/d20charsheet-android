package com.vituel.dndplayer.util.gui;

import android.app.Activity;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Victor on 06/09/14.
 */
public abstract class SingleColExpListAdapter<G, I> extends BaseExpandableListAdapter {


    protected Activity activity;
    protected List<G> groups;
    protected TreeMap<G, List<I>> children;
    protected int groupLayout;
    protected int childLayout;

    /**
     * @param children should not be modified otherwise the "groups" list will get inconsistent.
     */
    public SingleColExpListAdapter(Activity activity, TreeMap<G, List<I>> children, int groupLayout, int childLayout) {
        this.activity = activity;
        this.children = children;
        this.groups = new ArrayList<>(children.keySet());
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
    }

    @Override
    public int getGroupCount() {
        return children.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        G group = (G) getGroup(groupPosition);
        return children.get(group).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        G group = (G) getGroup(groupPosition);
        return children.get(group).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
