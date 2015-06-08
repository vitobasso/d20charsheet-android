package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.MainNavigationActvity;
import com.vituel.dndplayer.activity.abstraction.PagerActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.dao.dependant.CharBookDao;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.model.character.CharBase;

import static com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter.PAGE_BASIC;
import static com.vituel.dndplayer.util.app.ActivityUtil.EXTRA_CHAR;
import static com.vituel.dndplayer.util.app.ActivityUtil.EXTRA_MODE;
import static com.vituel.dndplayer.util.app.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.app.ActivityUtil.findFragment;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

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
            base = cache.getOpenedChar();
        } else {
            cache.reset();
            base = createNewCharacter();
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
        getMenuInflater().inflate(R.menu.edit, menu);
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

    private CharBase createNewCharacter() {
        CharBase base = new CharBase();
        base.setHitPoints(1);
        base.setStrength(10);
        base.setDexterity(10);
        base.setConstitution(10);
        base.setIntelligence(10);
        base.setWisdom(10);
        base.setCharisma(10);
        base.setGender("M");
        base.setTendencyLoyality("NEUTRAL");
        base.setTendencyMoral("NEUTRAL");
        base.setStandardAbilityMods();
        return base;
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

        //save books
        CharBookDao charBookDao = new CharBookDao(this);
        charBookDao.saveOverwrite(base.getId(), cache.getActiveRulebooks());
        charBookDao.close();

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
