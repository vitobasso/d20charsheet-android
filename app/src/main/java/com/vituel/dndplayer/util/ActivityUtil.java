package com.vituel.dndplayer.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.util.gui.CustomFontSpinnerAdapter;
import com.vituel.dndplayer.util.gui.EnumI18nSpinnerAdapter;
import com.vituel.dndplayer.util.gui.NoSelectionSpinnerAdapter;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Victor on 07/03/14.
 * TODO separate gui and navigation utils
 */
public class ActivityUtil {

    //TODO replace by enum
    public static final int REQUEST_SELECT = 1;
    public static final int REQUEST_EDIT = 2;
    public static final int REQUEST_CREATE = 3;
    public static final int REQUEST_SELECT_RACE = 4;
    public static final int REQUEST_SELECT_CLASS = 5;
    public static final int REQUEST_LOAD = 6;
    public static final int REQUEST_CHAR = 7;

    public static final String EXTRA_CHAR = "CHAR";
    public static final String EXTRA_MODE = "MODE";
    public static final String EXTRA_SELECTED = "SELECTED";
    public static final String EXTRA_EDITED = "EDITED";
    public static final String EXTRA_TYPE = "TYPE";
    public static final String EXTRA_REQUEST = "REQUEST";
    public static final String EXTRA_PAGE = "PAGE";

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
    public static <T extends View> T inflate(Activity activity, int parentId, int layout) {
        ViewGroup parent = findView(activity, parentId);
        return inflate(activity, parent, layout);
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


    public static <T extends Enum<T>> void populateSpinnerWithEnum(Activity activity, int spinnerRes,
                                                                   T[] enumValues, T selection) {
        populateSpinnerWithEnum(activity, null, spinnerRes, enumValues, selection, null);
    }

    public static <T extends Enum<T>> void populateSpinnerWithEnum(Activity activity, ViewGroup ancestor, int spinnerRes,
                                                                   T[] enumValues, T selection) {
        populateSpinnerWithEnum(activity, ancestor, spinnerRes, enumValues, selection, null);
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
        adapter = new NoSelectionSpinnerAdapter(activity, adapter);
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

    public static Spinner populateStaticSpinner(Activity activity, ViewGroup ancestor, int spinnerRes, String selection){

        //find spinner
        Spinner spinner;
        if (ancestor != null) {
            spinner = findView(ancestor, spinnerRes);
        } else {
            spinner = findView(activity, spinnerRes);
        }

        //setup
        CustomFontSpinnerAdapter adapter = new CustomFontSpinnerAdapter(activity, spinner.getAdapter());
        spinner.setAdapter(adapter);
        setSpinnerSelection(spinner, selection);

        return spinner;
    }

    public static <T> T readSpinner(Object root, int... viewRes) {
        Spinner spinner = findView(root, viewRes);
        return (T) spinner.getSelectedItem();
    }

    public static CheckBox populateCheckBox(Object root, int viewRes, Boolean checked, CompoundButton.OnCheckedChangeListener checkListener) {
        CheckBox view = findView(root, viewRes);
        view.setOnCheckedChangeListener(null);
        if (view != null && checked != null) {
            view.setChecked(checked);
        }
        view.setOnCheckedChangeListener(checkListener);
        return view;
    }

    public static <T extends TextView> T populateTextView(Object root, int viewRes, Object value) {
        T view = findView(root, viewRes);
        if (view != null && value != null) {
            String str = internationalize(value.toString(), view.getContext());
            view.setText(str);
            view.setError(null);
        }
        return view;
    }


    public static <T extends TextView> T populateTextView(Object root, int viewRes, int value) {
        return populateTextView(root, viewRes, "" + value);
    }

    public static <T extends TextView> T populateTextViewOrHide(Object root, int viewRes, Object value) {
        if (value == null || value.toString().isEmpty()) {
            T view = findView(root, viewRes);
            view.setVisibility(View.GONE);
            return view;
        } else {
            return populateTextView(root, viewRes, value);
        }
    }

    public static String readString(Object root, int... viewRes) {
        TextView view = findView(root, viewRes);
        return view.getText().toString().trim();
    }

    public static int readInt(Object root, int... viewRes) {
        TextView view = findView(root, viewRes);
        return Integer.valueOf(view.getText().toString().trim());
    }

    public static float readFloat(Object root, int... viewRes) {
        TextView view = findView(root, viewRes);
        return Float.valueOf(view.getText().toString().trim());
    }

    public static DiceRoll readDice(Object root, int... viewRes) {
        TextView view = findView(root, viewRes);
        return view.getText().length() > 0 ? new DiceRoll(view.getText().toString()) : null;
    }

    public static boolean validateText(Object root, int... viewRes) {
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

    public static boolean validateTextInt(Object root, int viewRes, int min, int max) {
        TextView view = findView(root, viewRes);
        String str = view.getText().toString().trim();
        try {
            Integer value = Integer.valueOf(str);
            if (value >= min && value <= max) {
                view.setError(null);
                return true;
            } else {
                return setIntValidationError(view, min, max);
            }
        } catch (NumberFormatException e) {
            return setIntValidationError(view, min, max);
        }
    }

    private static boolean setIntValidationError(TextView view, int min, int max) {
        String msg = MessageFormat.format("Expected a number between {0} and {1}", min, max);
        view.setError(msg);
        return false;
    }

    public static boolean validateSpinner(Object root, int... viewRes) {
        Spinner spinner = findView(root, viewRes);
        Object selection = spinner.getSelectedItem();
        if (selection == null) {
            String msg = spinner.getContext().getString(R.string.cant_be_empty);
            TextView view = (TextView) spinner.getSelectedView();
            view.setError(msg);
            return false;
        } else {
            TextView view = (TextView) spinner.getSelectedView();
            view.setError(null);
            return true;
        }
    }

    /**
     * To be called before setting to GUI text containing "$resource_id".
     * Ideally this would be called in central "populate" methods to avoid missing any strings.
     * For now there's a populate method only for EditText views though, in some cases it will need
     * to be called "manually" before setting text to GUI.
     */
    public static String internationalize(String input, Context context){
        Pattern pattern = Pattern.compile("\\$(\\w+)");
        Matcher matcher = pattern.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while(matcher.find()){
            String resourceName = matcher.group(1);
            String originalString = matcher.group();
            String replacement = getStringResource(context, resourceName, originalString);
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static String getStringResource(Context context, String resourceName, String defaultString) {
        int resId = getStringResourceId(context, resourceName);
        return resId > 0 ? context.getString(resId) : defaultString;
    }

    public static int getStringResourceId(Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "string", context.getPackageName());
    }

    public static void setTextViewEnabled(Object root, int viewRes, boolean enabled) {
        TextView textView = findView(root, viewRes);
        int colorId = enabled ? android.R.color.black : android.R.color.darker_gray;
        Context context = getContextFromActivityOrView(root);
        textView.setTextColor(context.getResources().getColor(colorId));
    }

    private static Context getContextFromActivityOrView(Object activityOrView) {
        if (activityOrView instanceof Context) {
            return (Context) activityOrView;
        } else {
            View view = (View) activityOrView;
            return view.getContext();
        }
    }

}
