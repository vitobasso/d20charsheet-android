package com.vituel.dndplayer.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vituel.dndplayer.R;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
import static com.vituel.dndplayer.util.app.ActivityUtil.findView;
import static com.vituel.dndplayer.util.app.ActivityUtil.inflate;

/**
 * Created by Victor on 08/05/2015.
 */
public abstract class DoubleDrawerActivity extends FragmentActivity {

    protected DrawerLayout drawerLayout;
    protected ListView leftList;
    protected ListView rightList;
    protected ActionBarDrawerToggle drawerToggle;

    protected Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        activity = this;
        initDrawerLayout();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conditions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_conditionals);
        item.setVisible(isRightDrawerEnabled());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_conditionals) {
            toggleDrawer(rightList);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectCurrentActivityInDrawer();
    }

    private void initDrawerLayout() {
        drawerLayout = findView(this, R.id.drawer_layout);
        inflate(this, R.id.frame, getContentLayout());

        setupLeftDrawer();

        rightList = findView(drawerLayout, R.id.right_drawer);
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, rightList);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void setupLeftDrawer() {
        ArrayAdapter<String> leftListAdapter = getLeftDrawerAdapter();
        leftList = findView(drawerLayout, R.id.left_drawer);
        leftList.setAdapter(leftListAdapter);
        leftList.setOnItemClickListener(new LeftDrawerClickListener());
        selectCurrentActivityInDrawer();
        drawerToggle = new LeftDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private class LeftDrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onClickLeftDrawerItem(parent, view, position, id);
            drawerLayout.closeDrawer(leftList);
        }
    }

    private class LeftDrawerToggle extends ActionBarDrawerToggle {

        public LeftDrawerToggle() {
            super(activity, drawerLayout, R.drawable.ic_drawer, R.string.open, R.string.close);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                drawerLayout.closeDrawer(rightList);
            }
            return super.onOptionsItemSelected(item);
        }

    }

    protected <T extends ListAdapter & ListView.OnItemClickListener> void enableRightDrawer(T adapter) {
        enableRightDrawer(adapter, adapter);
    }

    protected void enableRightDrawer(ListAdapter adapter, ListView.OnItemClickListener listener) {
        rightList.setAdapter(adapter);
        rightList.setOnItemClickListener(listener);
        drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED, rightList);
        invalidateOptionsMenu();
    }

    protected void disableRightDrawer() {
        rightList.setAdapter(null);
        rightList.setOnItemClickListener(null);
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, rightList);
        invalidateOptionsMenu();
    }

    public boolean isRightDrawerEnabled() {
        return rightList.getAdapter() != null;
    }

    private void toggleDrawer(View drawer) {
        if (drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawer(drawer);
        } else {
            drawerLayout.openDrawer(drawer);
            drawerLayout.closeDrawer(getTheOppositeDrawer(drawer));
        }
    }

    private View getTheOppositeDrawer(View thisDrawer) {
        if (thisDrawer == rightList) {
            return leftList;
        } else if (thisDrawer == leftList) {
            return rightList;
        } else {
            return null;
        }
    }

    protected abstract void selectCurrentActivityInDrawer();

    protected abstract int getContentLayout();

    protected abstract void onClickLeftDrawerItem(AdapterView<?> parent, View view, int position, long id);

    protected abstract ArrayAdapter<String> getLeftDrawerAdapter();

}
