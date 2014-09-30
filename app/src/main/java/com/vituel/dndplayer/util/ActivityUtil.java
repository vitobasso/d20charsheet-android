package com.vituel.dndplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.summary.SummaryActivity;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.util.gui.EnumI18nSpinnerAdapter;
import com.vituel.dndplayer.util.gui.NoSelSpinnerAdapter;

import java.security.InvalidParameterException;

/**
 * Created by Victor on 07/03/14.
 */
public class ActivityUtil {

    public static final int REQUEST_SELECT = 1;
    public static final int REQUEST_EDIT = 2;
    public static final int REQUEST_CREATE = 3;
    public static final int REQUEST_SELECT_RACE = 4;
    public static final int REQUEST_SELECT_CLASS = 5;

    public static final String EXTRA_SELECTED = "SELECTED";
    public static final String EXTRA_EDITED = "EDITED";
    public static final String EXTRA_TYPE = "TYPE";
    public static final String EXTRA_REQUEST = "REQUEST";
    public static final String EXTRA_PAGE = "PAGE";

    public static final String PREF = "dndplayer";
    public static final String PREF_OPENED_CHARACTER = "opened_character";
    public static final String PREF_FIRST_RUN = "first_run";

    public static boolean defaultOnOptionsItemSelected(MenuItem item, Activity activity) {
        switch (item.getItemId()) {
            case R.id.action_cancel:

                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
                return true;

            case R.id.action_summary:

                Intent summaryIntent = new Intent(activity, SummaryActivity.class);
                summaryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(summaryIntent);
                return true;

            default:
                return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(ViewGroup parent, int... ids) {
        for (int i = 0; i < ids.length - 1; i++) {
            parent = (ViewGroup) parent.findViewById(ids[i]);
        }
        return (T) parent.findViewById(ids[ids.length - 1]);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(Activity activity, int... ids) {
        if (ids.length == 1) {
            return (T) activity.findViewById(ids[0]);
        } else {
            ViewGroup parent = (ViewGroup) activity.findViewById(ids[0]);
            return findView(parent, ids);
        }
    }

    public static <T extends View> T findView(Object activityOrViewGroup, int... ids) {
        if (activityOrViewGroup instanceof Activity) {
            return findView((Activity) activityOrViewGroup, ids);
        } else if (activityOrViewGroup instanceof ViewGroup) {
            return findView((ViewGroup) activityOrViewGroup, ids);
        } else {
            throw new InvalidParameterException(activityOrViewGroup.getClass().getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T inflate(Activity activity, ViewGroup parent, int layout) {
        View v = activity.getLayoutInflater().inflate(layout, parent);
        if (parent != null) {
            return (T) parent.getChildAt(parent.getChildCount() - 1);
        } else {
            return (T) v;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T inflateDisatached(Context context, ViewGroup parent, int layout) {
        return (T) ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, parent, false);
    }

    public static Fragment findFragment(FragmentActivity activity, ViewPager pager, int position) {
        FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) pager.getAdapter();
        return activity.getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + pager.getId() + ":"
                        + fragmentPagerAdapter.getItemId(position));
    }

    public static void setSpinnerSelection(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            Object candidate = spinner.getAdapter().getItem(i);
            if (candidate.equals(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    public static void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            Object candidate = spinner.getAdapter().getItem(i);
            if (candidate instanceof String && ((String) candidate).equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    public static <T extends Enum<T>> void populateSpinnerWithEnum(Activity activity, ViewGroup ancestor, int spinnerRes,
                                                                   T[] enumValues, T selection,
                                                                   final AdapterView.OnItemSelectedListener listener) {

        //find spinner
        Spinner spinner;
        if (ancestor != null) {
            spinner = findView(ancestor, spinnerRes);
        } else {
            spinner = findView(activity, spinnerRes);
        }

        //set adapter
        SpinnerAdapter adapter = new EnumI18nSpinnerAdapter(
                activity, android.R.layout.simple_spinner_item, enumValues);
        adapter = new NoSelSpinnerAdapter(activity, adapter);
        spinner.setAdapter(adapter);

        //set selection
        if (selection != null) {
            spinner.setSelection(selection.ordinal() + 1); // +1 to compensate for the "no selection" position
        }

        //set listener
        if (listener != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listener.onItemSelected(parent, view, position - 1, id);  // -1 to compensate for the "no selection" position
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    listener.onNothingSelected(parent);
                }
            });
        }
    }

    public static <T> T readSpinner(Object root, int viewRes) {
        Spinner spinner = findView(root, viewRes);
        return (T) spinner.getSelectedItem();
    }

    public static <T extends TextView> T populateTextView(Object root, int viewRes, Object value) {
        T view = findView(root, viewRes);
        if (view != null && value != null) {
            view.setText(value.toString());
            view.setError(null);
        }
        return view;
    }

    public static <T extends TextView> T populateTextView(Object root, int viewRes, int value) {
        return populateTextView(root, viewRes, "" + value);
    }

    public static String readString(Object root, int viewRes) {
        TextView view = findView(root, viewRes);
        return view.getText().toString().trim();
    }

    public static int readInt(Object root, int viewRes) {
        TextView view = findView(root, viewRes);
        return Integer.valueOf(view.getText().toString().trim());
    }

    public static DiceRoll readDice(Object root, int viewRes) {
        TextView view = findView(root, viewRes);
        return view.getText().length() > 0 ? new DiceRoll(view.getText().toString()) : null;
    }

    public static boolean validateField(Object root, int viewRes) {
        TextView view = findView(root, viewRes);
        String str = view.getText().toString().trim();
        if (str.isEmpty()) {
            String msg = view.getContext().getString(R.string.cant_be_empty);
            view.setError(msg);
            return false;
        } else {
            view.setError(null);
            return true;
        }
    }

}
