package com.vitobasso.d20charsheet.util.gui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.vitobasso.d20charsheet.util.font.FontUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Victor on 06/09/14.
 */
public abstract class SimpleExpListAdapter<G, I> extends BaseExpandableListAdapter {

    protected Activity activity;
    protected List<G> groups;
    protected TreeMap<G, List<I>> children;
    protected int groupLayout;
    protected int childLayout;
    protected FontUtil fontUtil;

    /**
     * @param children should not be modified otherwise the "groups" list will get inconsistent.
     */
    public SimpleExpListAdapter(Activity activity, List<G> groups, TreeMap<G, List<I>> children, int groupLayout, int childLayout) {
        this.activity = activity;
        this.children = children;
        this.groups = groups;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.fontUtil = new FontUtil(activity);
    }

    /**
     * @param children should not be modified otherwise the "groups" list will get inconsistent.
     */
    public SimpleExpListAdapter(Activity activity, TreeMap<G, List<I>> children, int groupLayout, int childLayout) {
        this(activity, new ArrayList<>(children.keySet()), children, groupLayout, childLayout);
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(groupLayout, null);
        }

        G group = groups.get(groupPosition);
        populateGroup(group, isExpanded, convertView);

        fontUtil.setFontRecursively(convertView);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(childLayout, null);
        }

        G subgroup = groups.get(groupPosition);
        I child = children.get(subgroup).get(childPosition);
        populateChild(child, convertView);

        fontUtil.setFontRecursively(convertView);
        return convertView;
    }

    protected abstract void populateGroup(G group, boolean isExpanded, View convertView);

    protected abstract void populateChild(I child, View convertView);
}
