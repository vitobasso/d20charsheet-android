package com.vituel.dndplayer.activity.edit_char;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.PagerFragment;
import com.vituel.dndplayer.dao.CharDao;
import com.vituel.dndplayer.model.CharBase;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;

/**
 * Created by Victor on 27/02/14.
 */
public class EditCharActivity extends FragmentActivity {

    CharBase base;

    ViewPager pager;

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
                for (Fragment frag : getSupportFragmentManager().getFragments()) {
                    if (frag instanceof PagerFragment) {
                        PagerFragment pagerFrag = (PagerFragment) frag;
                        if(pagerFrag.onValidate()) {
                            pagerFrag.onSaveToModel();
                        }else{
                            return true; //TODO go to page w/ error.
                            //TODO validate destroyed pages too
                        }
                    }
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

    public void updateFragment(PagerFragment<CharBase, EditCharActivity> fragment){
        fragment.update(base);
    }

}
