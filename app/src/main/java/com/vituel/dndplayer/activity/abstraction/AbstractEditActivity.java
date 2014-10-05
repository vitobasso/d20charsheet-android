package com.vituel.dndplayer.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.util.ReflectionUtil;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_SELECTED;
import static com.vituel.dndplayer.util.ActivityUtil.defaultOnOptionsItemSelected;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setActionbarTitle;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

public abstract class AbstractEditActivity<T extends AbstractEntity> extends Activity {

    protected T entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        entity = (T) getIntent().getSerializableExtra(EXTRA_SELECTED);
        if (entity == null) {
            entity = ReflectionUtil.createTemplateInstance(this, 0);
        }

        populate();

        setFontRecursively(this, findViewById(android.R.id.content), MAIN_FONT);
        setActionbarTitle(this, BOLD_FONT, getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        getMenuInflater().inflate(R.menu.back_to_summary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                if(!validate()){
                    return true;
                }

                //save fields from fragments
                T entity = save();

                //send data back
                Intent intent = new Intent();
                intent.putExtra(EXTRA_EDITED, entity);
                setResult(RESULT_OK, intent);
                finish();

                return true;

            default:
                return defaultOnOptionsItemSelected(item, this);
        }
    }

    protected abstract int getLayout();

    protected abstract void populate();

    protected abstract T save();

    protected boolean validate() {
        return true;
    }

}