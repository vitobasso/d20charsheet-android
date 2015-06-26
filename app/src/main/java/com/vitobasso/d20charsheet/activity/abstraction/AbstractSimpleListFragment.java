package com.vitobasso.d20charsheet.activity.abstraction;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.vitobasso.d20charsheet.R;

import java.util.List;

/**
 * Adds to {@link AbstractListFragment}:
 * - an ArrayAdapter skeleton
 * - finds the row's object
 * - set font
 *
 * Created by Victor on 07/04/2015.
 */
public abstract class AbstractSimpleListFragment<T, A extends Activity & PagerActivity<T>, E> extends AbstractListFragment<T, A, E> {

   @Override
    protected ListAdapter createAdapter() {
        return new Adapter(listData);
    }

    private class Adapter extends ArrayAdapter<E> {

        public Adapter(List<E> objects) {
            super(activity, getRowLayoutResourceId(), getRowTextViewResourceId(), objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            E element = listData.get(position);
            onPopulateRow(view, element);
            fontUtil.setFontRecursively(view);
            return view;
        }
    }

    protected abstract int getRowLayoutResourceId();

    protected int getRowTextViewResourceId() {
        return R.id.name;
    }

    protected abstract void onPopulateRow(View view, E element);

}
