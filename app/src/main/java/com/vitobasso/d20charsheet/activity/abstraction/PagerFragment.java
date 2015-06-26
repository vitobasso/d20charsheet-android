package com.vitobasso.d20charsheet.activity.abstraction;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitobasso.d20charsheet.util.font.FontUtil;
import com.vitobasso.d20charsheet.util.i18n.EnumI18n;

/**
 * Created by Victor on 28/02/14.
 */
public abstract class PagerFragment<T, A extends Activity & PagerActivity<T>> extends Fragment {

    protected A activity;
    protected T data;
    protected ViewGroup root;
    protected EnumI18n i18n;
    protected FontUtil fontUtil;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (A) activity;
        this.i18n = new EnumI18n(activity);
        this.fontUtil = new FontUtil(activity);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(getLayoutResourceId(), container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        refresh();
    }

    public final void refresh() {
        data = activity.getData();
        onPopulate();
        onSetFont();
    }

    public final boolean isReadyToPopulate(){
        return root != null;
    }

    protected abstract int getLayoutResourceId();

    protected abstract void onPopulate();

    protected void onSetFont(){
        fontUtil.setFontRecursively(root);
    }

    public boolean onValidate(){
        return true;
    }

    public void onSave() {}

}
