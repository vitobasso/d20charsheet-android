package com.vituel.dndplayer.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vituel.dndplayer.R;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;

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
            onClickLeftDrawerItem(parent, view, position, id);
            drawerLayout.closeDrawer(leftList);
        }
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
        String[] drawerItems = getResources().getStringArray(R.array.main_navigation);
        leftList = findView(drawerLayout, R.id.left_drawer);
        leftList.setAdapter(new ArrayAdapter<>(this, R.layout.simple_row, R.id.name, drawerItems));
        leftList.setOnItemClickListener(new NavigationClickListener());
        selectCurrentActivityInDrawer();
        drawerToggle = new LeftDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    protected abstract void selectCurrentActivityInDrawer();

    protected abstract int getContentLayout();

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

    protected abstract void onClickLeftDrawerItem(AdapterView<?> parent, View view, int position, long id);

    protected <T extends ListAdapter & ListView.OnItemClickListener> void setupRightDrawer(T adapter) {
        setupRightDrawer(adapter, adapter);
    }

    protected void setupRightDrawer(ListAdapter adapter, ListView.OnItemClickListener listener) {
        rightList.setAdapter(adapter);
        rightList.setOnItemClickListener(listener);
        drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED, rightList);
    }

}
