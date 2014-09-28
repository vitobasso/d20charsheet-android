package com.vituel.dndplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 28/02/14.
 */
public abstract class PagerFragment<T, A extends Activity> extends Fragment {

    protected A activity;
    protected ViewGroup root;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (A) activity;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(getLayout(), container, false);
        onPopulate();

        setFontRecursively(activity, root, MAIN_FONT);

        return root;
    }

    public boolean onValidate(){
        return true;
    }

    public void onSaveToModel() {
    }

    public void update(T parameter) {
    }

    protected abstract int getLayout();

    protected abstract void onPopulate();

}
