package com.vitobasso.d20charsheet.activity.abstraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.util.ReflectionUtil;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_EDITED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_SELECTED;
import static com.vitobasso.d20charsheet.util.font.FontUtil.BOLD_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.MAIN_FONT;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setActionbarTitle;
import static com.vitobasso.d20charsheet.util.font.FontUtil.setFontRecursively;

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_save:

                if (!validate()) {
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
                return false;
        }
    }

    protected abstract int getLayout();

    protected abstract void populate();

    protected abstract T save();

    protected boolean validate() {
        return true;
    }

}
