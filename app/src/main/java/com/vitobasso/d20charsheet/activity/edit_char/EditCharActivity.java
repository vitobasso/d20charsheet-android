package com.vitobasso.d20charsheet.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.MainNavigationActvity;
import com.vitobasso.d20charsheet.activity.abstraction.PagerActivity;
import com.vitobasso.d20charsheet.activity.abstraction.PagerFragment;
import com.vitobasso.d20charsheet.dao.entity.CharDao;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.business.CharBuilder;

import static com.vitobasso.d20charsheet.activity.edit_char.EditCharPagerAdapter.PAGE_BASIC;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_CHAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_MODE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_PAGE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findFragment;
import static com.vitobasso.d20charsheet.util.font.FontUtil.BOLD_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 27/02/14.
 */
public class EditCharActivity extends MainNavigationActvity implements PagerActivity<CharBase> {

    public static enum Mode {
        CREATE, EDIT
    }

    private CharBase base;

    private ViewPager pager;
    private int currentPage;

    @Override
    protected int getContentLayout() {
        return R.layout.pager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionbarTitle(this, BOLD_FONT, getTitle());

        Mode mode = (Mode) getIntent().getSerializableExtra(EXTRA_MODE);
        if (mode == Mode.EDIT) {
            base = cache.getChar();
        } else {
            base = new CharBuilder(this).newChar();
            cache.setNewChar(base);
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new EditCharPagerAdapter(getSupportFragmentManager(), this));
        currentPage = getIntent().getIntExtra(EXTRA_PAGE, PAGE_BASIC);
        pager.setCurrentItem(currentPage);
        pager.setOnPageChangeListener(new PagerListener());
    }

    @Override
    protected void navigateTo(NavigationItem nextActivity) {
        switch (nextActivity) {
            case SUMMARY:
                goToSummary();
                break;
            case BOOKS:
                goToBooks();
                break;
            case OPEN:
                backToBase();
                goToOpenChar();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            default:
                return false;
        }
    }

    private void saveCharacter() {
        //standard attack
        if(base.getAttacks().isEmpty()){
            base.createStandardAttacks();
        }

        //save to database
        CharDao dataSource = new CharDao(this);
        dataSource.save(base);
        dataSource.close();
    }

    @Override
    public CharBase getData() {
        return base;
    }

    private class PagerListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            PagerFragment previousFragment = (PagerFragment) findFragment(EditCharActivity.this, pager, currentPage);
            if (previousFragment.onValidate()) {
                previousFragment.onSave();
                currentPage = position;
            } else {
                pager.setCurrentItem(currentPage); //stay in page
            }

        }
    }

    private void save() {
        //copy fields from fragments
        PagerFragment fragment = (PagerFragment) findFragment(EditCharActivity.this, pager, currentPage);
        if (!fragment.onValidate()) {
            return;
        }

        fragment.onSave();
        saveCharacter();

        //send data back
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_CHAR, base);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
