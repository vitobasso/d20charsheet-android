package com.vituel.dndplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vituel.dndplayer.MemoryCache;
import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.activity.select.SelectBooksActivity;
import com.vituel.dndplayer.activity.select.SelectCharActivity;
import com.vituel.dndplayer.activity.summary.SummaryActivity;

import java.security.InvalidParameterException;

import static com.vituel.dndplayer.activity.MainNavigationActivity.NavigationItem.BOOKS;
import static com.vituel.dndplayer.activity.MainNavigationActivity.NavigationItem.EDIT;
import static com.vituel.dndplayer.activity.MainNavigationActivity.NavigationItem.OPEN;
import static com.vituel.dndplayer.activity.MainNavigationActivity.NavigationItem.SUMMARY;
import static com.vituel.dndplayer.activity.MainNavigationActivity.NavigationItem.values;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_MODE;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.PREF;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;

/**
 * Created by Victor on 08/05/2015.
 */
public abstract class MainNavigationActivity extends FragmentActivity {

    protected Activity activity;
    protected MemoryCache cache;
    protected SharedPreferences pref;

    private DrawerLayout drawerLayout;
    private ListView listView;
    private ActionBarDrawerToggle drawerToggle;

    public enum NavigationItem {
        SUMMARY, EDIT, BOOKS, OPEN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        activity = this;
        cache = (MemoryCache) getApplicationContext();
        pref = getSharedPreferences(PREF, MODE_PRIVATE);
        initDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectCurrentActivityInDrawer();
    }

    private class NavigationClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NavigationItem nextActivity = values()[position];
            navigateTo(nextActivity);
            drawerLayout.closeDrawer(listView);
        }
    }

    private void initDrawer() {
        drawerLayout = findView(this, R.id.drawer_layout);
        inflate(this, R.id.frame, getContentLayout());

        String[] drawerItems = getResources().getStringArray(R.array.main_navigation);
        listView = findView(drawerLayout, R.id.drawer);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.simple_row, R.id.name, drawerItems));
        listView.setOnItemClickListener(new NavigationClickListener());
        selectCurrentActivityInDrawer();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.open, R.string.close);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void selectCurrentActivityInDrawer() {
        NavigationItem currentItem = getNavigationItem(getClass());
        listView.setItemChecked(currentItem.ordinal(), true);
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

    protected void navigateToEditChar() {
        Intent editIntent = new Intent(this, EditCharActivity.class);
        editIntent.putExtra(EXTRA_MODE, REQUEST_EDIT);
        editIntent.putExtra(EXTRA_PAGE, EditCharPagerAdapter.PAGE_BASIC); //TODO change according to page in summary
        startActivityForResult(editIntent, REQUEST_CHAR);
    }

    protected void navigateToBooks() {
        Intent booksIntent = new Intent(this, SelectBooksActivity.class);
        startActivityForResult(booksIntent, REQUEST_CHAR);
    }

    protected void navigateToOpenChar() {
        Intent openIntent = new Intent(this, SelectCharActivity.class);
        startActivityForResult(openIntent, REQUEST_CHAR);
    }

    protected abstract int getContentLayout();

    protected abstract void navigateTo(NavigationItem nextActivity);

}
