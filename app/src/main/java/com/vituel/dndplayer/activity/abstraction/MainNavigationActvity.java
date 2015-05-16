package com.vituel.dndplayer.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vituel.dndplayer.MemoryCache;
import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.select.SelectBooksActivity;
import com.vituel.dndplayer.activity.select.SelectCharActivity;
import com.vituel.dndplayer.activity.summary.SummaryActivity;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.util.AppPreferences;

import java.security.InvalidParameterException;

import static com.vituel.dndplayer.activity.abstraction.MainNavigationActvity.NavigationItem.BOOKS;
import static com.vituel.dndplayer.activity.abstraction.MainNavigationActvity.NavigationItem.EDIT;
import static com.vituel.dndplayer.activity.abstraction.MainNavigationActvity.NavigationItem.OPEN;
import static com.vituel.dndplayer.activity.abstraction.MainNavigationActvity.NavigationItem.SUMMARY;
import static com.vituel.dndplayer.activity.edit_char.EditCharActivity.Mode;
import static com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter.PAGE_BASIC;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_MODE;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CHAR;

/**
 * Created by Victor on 09/05/2015.
 */
public abstract class MainNavigationActvity extends DoubleDrawerActivity {

    protected MemoryCache cache;
    protected AppPreferences pref;

    public enum NavigationItem {
        SUMMARY, EDIT, BOOKS, OPEN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cache = (MemoryCache) getApplicationContext();
        pref = new AppPreferences(this);
        super.onCreate(savedInstanceState); //called after pref so it can be used during onCreate
    }

    @Override
    protected ArrayAdapter<String> getLeftDrawerAdapter() {
        String[] drawerItems = getResources().getStringArray(R.array.main_navigation);
        return new LeftDrawerAdapter(drawerItems);
    }

    @Override
    protected void onClickLeftDrawerItem(AdapterView<?> parent, View view, int position, long id) {
        MainNavigationActvity.NavigationItem nextActivity = NavigationItem.values()[position];
        navigateTo(nextActivity);
    }

    @Override
    protected void selectCurrentActivityInDrawer() {
        NavigationItem currentItem = getNavigationItem(getClass());
        leftList.setItemChecked(currentItem.ordinal(), true);
    }

    private <T extends Activity> NavigationItem getNavigationItem(Class<T> activityClass) {
        if (activityClass.equals(SummaryActivity.class)) {
            return SUMMARY;
        } else if (activityClass.equals(EditCharActivity.class)) {
            return EDIT;
        } else if (activityClass.equals(SelectBooksActivity.class)) {
            return BOOKS;
        } else if (activityClass.equals(SelectCharActivity.class)) {
            return OPEN;
        } else {
            throw new InvalidParameterException(activityClass.getCanonicalName());
        }
    }

    protected void goToEditChar() {
        goToEditChar(PAGE_BASIC);
    }

    protected void goToEditChar(int page) {
        Intent editIntent = new Intent(this, EditCharActivity.class);
        editIntent.putExtra(EXTRA_MODE, Mode.EDIT);
        editIntent.putExtra(EXTRA_PAGE, page);
        startActivityForResult(editIntent, REQUEST_CHAR);
    }

    protected void goToBooks() {
        Intent booksIntent = new Intent(this, SelectBooksActivity.class);
        startActivityForResult(booksIntent, REQUEST_CHAR);
    }

    protected void goToOpenChar() {
        Intent openIntent = new Intent(this, SelectCharActivity.class);
        startActivityForResult(openIntent, REQUEST_CHAR);
    }

    protected abstract void navigateTo(NavigationItem nextActivity);

    // Manages navigation restrictions when there's no character to open
    private class LeftDrawerAdapter extends ArrayAdapter<String> {
        public LeftDrawerAdapter(String[] drawerItems) {
            super(MainNavigationActvity.this, R.layout.simple_row, R.id.name, drawerItems);
        }

        @Override
        public boolean isEnabled(int position) {
            return pref.isLastOpenedCharKnown() || isNavigationAllowedDuringFirstCharCreation(position);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return pref.isLastOpenedCharKnown();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ActivityUtil.setTextViewEnabled(view, R.id.name, isEnabled(position));
            return view;
        }

    }

    private boolean isNavigationAllowedDuringFirstCharCreation(int position) {
        return position == EDIT.ordinal() || position == BOOKS.ordinal();
    }

}
