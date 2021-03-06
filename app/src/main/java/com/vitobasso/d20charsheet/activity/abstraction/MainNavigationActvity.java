package com.vitobasso.d20charsheet.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.edit_char.EditCharActivity;
import com.vitobasso.d20charsheet.activity.rules.SelectBooksActivity;
import com.vitobasso.d20charsheet.activity.select.SelectCharActivity;
import com.vitobasso.d20charsheet.activity.summary.SummaryActivity;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;
import com.vitobasso.d20charsheet.util.app.AppGlobals;
import com.vitobasso.d20charsheet.util.app.AppPreferences;

import java.security.InvalidParameterException;

import static com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity.NavigationItem.BOOKS;
import static com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity.NavigationItem.EDIT;
import static com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity.NavigationItem.OPEN;
import static com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity.NavigationItem.SUMMARY;
import static com.vitobasso.d20charsheet.activity.edit_char.EditCharActivity.Mode;
import static com.vitobasso.d20charsheet.activity.edit_char.EditCharActivity.Mode.CREATE;
import static com.vitobasso.d20charsheet.activity.edit_char.EditCharPagerAdapter.PAGE_BASIC;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_MODE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_PAGE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CHAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CREATE;

/**
 * Created by Victor on 09/05/2015.
 */
public abstract class MainNavigationActvity extends DoubleDrawerActivity {

    protected AppGlobals cache;
    protected AppPreferences pref;

    public enum NavigationItem {
        SUMMARY, EDIT, BOOKS, OPEN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cache = (AppGlobals) getApplicationContext();
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

    protected void goToSummary() {
        finish();
        Intent summaryIntent = new Intent(this, SummaryActivity.class);
        summaryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(summaryIntent);
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

    protected void goToCreateChar() {
        Intent intent = new Intent(this, EditCharActivity.class);
        intent.putExtra(EXTRA_MODE, CREATE);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, REQUEST_CREATE);
    }

    protected void goToBooks() {
        Intent booksIntent = new Intent(this, SelectBooksActivity.class);
        startActivityForResult(booksIntent, REQUEST_CHAR);
    }

    protected void goToOpenChar() {
        Intent openIntent = new Intent(this, SelectCharActivity.class);
        startActivityForResult(openIntent, REQUEST_CHAR);
    }

    protected void backToBase() {
        if (cache.isCharOpened()) {
            goToSummary();
        } else {
            goToCreateChar();
        }
    }

    protected void goToEditOrCreateChar() {
        if (cache.isCharOpened()) {
            goToSummary();
            goToEditChar();
        } else {
            goToCreateChar();
        }
    }

    protected abstract void navigateTo(NavigationItem nextActivity);

    // Manages navigation restrictions when creating the first character
    private class LeftDrawerAdapter extends ArrayAdapter<String> {
        public LeftDrawerAdapter(String[] drawerItems) {
            super(MainNavigationActvity.this, R.layout.simple_row, R.id.name, drawerItems);
        }

        @Override
        public boolean isEnabled(int position) {
            if (cache.isCharOpened()) {
                return true; // enable all
            } else if (cache.isCharSet()) {
                return position != SUMMARY.ordinal(); // enable some
            } else {
                return false; // enable none
            }
        }

        @Override
        public boolean areAllItemsEnabled() {
            return cache.isCharOpened();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ActivityUtil.setTextViewEnabled(view, R.id.name, isEnabled(position));
            return view;
        }

    }

}
