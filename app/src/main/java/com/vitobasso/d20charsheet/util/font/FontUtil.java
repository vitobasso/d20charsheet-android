package com.vitobasso.d20charsheet.util.font;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.LruCache;
import android.view.View;
import android.widget.TextView;

import com.vitobasso.d20charsheet.util.gui.RecursiveViewCaller;

/**
 * Created by Victor on 27/03/14.
 */
public class FontUtil {

    private final static String FUTURA_LIGHT = "fonts/FuturaLT-Light.ttf";
    private final static String FUTURA_MEDIUM = "fonts/FuturaLT.ttf";
    private final static String FUTURA_BOOK = "fonts/FuturaLT-Book.ttf";
    private final static String FUTURA_BOLD = "fonts/FuturaLT-Bold.ttf";
    private final static String FUTURA_EXTRA_BOLD = "fonts/FuturaLT-ExtraBold.ttf";

    public final static String MAIN_FONT = FUTURA_MEDIUM;
    public final static String BOLD_FONT = FUTURA_BOLD;

    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    private Context ctx;
    private final Typeface mainFont, boldFont;

    public FontUtil(Context ctx) {
        this.ctx = ctx;
        this.mainFont = getFont(MAIN_FONT);
        this.boldFont = getFont(BOLD_FONT);
    }

    public Typeface getFont(String path) {
        Typeface typeface = sTypefaceCache.get(path);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(ctx.getAssets(), path);
            sTypefaceCache.put(path, typeface);
        }
        return typeface;
    }

    private SpannableString getSpannableString(String fontPath, CharSequence text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new TypefaceSpan(ctx, fontPath), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    public void setFontRecursively(View view){
        RecursiveViewCaller<TextView> fontChange = new RecursiveViewCaller<TextView>(TextView.class){
            @Override
            protected void leafCall(TextView v, Object... params) {
                if (v.getTypeface() == null || !v.getTypeface().isBold()) {
                    v.setTypeface(mainFont);
                } else {
                    v.setTypeface(boldFont);
                }
            }
        };
        fontChange.recursiveCall(view);
    }

    private static SpannableString getSpannableString(int style, int start, int end, CharSequence text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new StyleSpan(style), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private static SpannableString getSpannableString(int style, CharSequence text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new StyleSpan(style), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    public static void setActionbarTitle(Activity activity, String font, CharSequence title) {
        ActionBar bar = activity.getActionBar();
        if (title != null) {
            bar.setTitle(new FontUtil(activity).getSpannableString(font, title));
        }
    }

    public static CharSequence setFont(Context ctx, String font, CharSequence text){
        return new FontUtil(ctx).getSpannableString(font, text);
    }

    public static CharSequence toBold(CharSequence text){
        return FontUtil.getSpannableString(Typeface.BOLD, text);
    }

    public static CharSequence toItalic(CharSequence text){
        return FontUtil.getSpannableString(Typeface.ITALIC, text);
    }

}
