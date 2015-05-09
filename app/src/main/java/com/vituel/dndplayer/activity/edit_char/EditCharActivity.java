package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vituel.dndplayer.MemoryCache;
import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerActivity;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.select.SelectBooksActivity;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_CHAR;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_MODE;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_CREATE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 27/02/14.
 */
public class EditCharActivity extends FragmentActivity implements PagerActivity<CharBase> {

    private CharBase base;

    private ViewPager pager;
    private int currentPage;
    private MemoryCache cache; //TODO move to superclass

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager);
        setActionbarTitle(this, BOLD_FONT, getTitle());

        int mode = getIntent().getIntExtra(EXTRA_MODE, REQUEST_CREATE);
        if (mode == REQUEST_EDIT) {
            cache = (MemoryCache) getApplicationContext();
            base = cache.getOpenedChar();
        } else {
            base = createNewCharacter();
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new EditCharPagerAdapter(getSupportFragmentManager(), this));
        pager.setOnPageChangeListener(new PagerListener());

        int page = getIntent().getIntExtra(EXTRA_PAGE, 0);
        pager.setCurrentItem(page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                //copy fields from fragments
                PagerFragment fragment = (PagerFragment) ActivityUtil.findFragment(EditCharActivity.this, pager, currentPage);
                if (fragment.onValidate()) {
                    fragment.onSave();
                }else{
                    return true;
                }

                saveCharacter();

                //send data back
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_CHAR, base);
                setResult(RESULT_OK, resultIntent);
                finish();

                return true;

            case R.id.action_books:

                Intent selectBooksIntent = new Intent(this, SelectBooksActivity.class);
                startActivity(selectBooksIntent);

            default:
                return defaultOnOptionsItemSelected(item, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    }

    @Override
    public CharBase getData() {
        return base;
    }

    private class PagerListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            PagerFragment fragment = (PagerFragment) ActivityUtil.findFragment(EditCharActivity.this, pager, currentPage);
            if (fragment == null || !fragment.isReadyToPopulate()) {
                //should fall here only when being called from onCreate
                currentPage = position;
            } else if (fragment.onValidate()) {
                fragment.onSave();
                currentPage = position;
            } else {
                pager.setCurrentItem(currentPage); //stay in page
            }

        }
    }

}
