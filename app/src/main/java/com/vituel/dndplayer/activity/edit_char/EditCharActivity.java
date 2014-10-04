package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.abstraction.ParentActivity;
import com.vituel.dndplayer.dao.CharDao;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 27/02/14.
 */
public class EditCharActivity extends FragmentActivity implements ParentActivity<CharBase>{

    private CharBase base;

    private ViewPager pager;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager);
        setActionbarTitle(this, BOLD_FONT, getTitle());

        base = (CharBase) getIntent().getSerializableExtra(EXTRA_EDITED);
        if (base == null) {
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

                //standard attack
                if(base.getAttacks().isEmpty()){
                    base.createStandardAttacks();
                }

                //save to database
                CharDao dataSource = new CharDao(this);
                dataSource.save(base);
                dataSource.close();

                //send data back
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_EDITED, base);
                setResult(RESULT_OK, resultIntent);
                finish();

                return true;

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
        return base;
    }

    @Override
    public CharBase getData() {
        return base;
    }

    private class PagerListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            PagerFragment fragment = (PagerFragment) ActivityUtil.findFragment(EditCharActivity.this, pager, currentPage);
            if (fragment == null) {
                //should fall here only when being called from onCreate
                currentPage = position; //go ahead
            } else if (fragment.onValidate()) {
                fragment.onSave();
                currentPage = position; //go ahead
            } else {
                pager.setCurrentItem(currentPage); //stay in page
            }

        }
    }

}
