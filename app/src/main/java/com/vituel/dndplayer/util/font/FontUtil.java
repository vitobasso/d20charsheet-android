package com.vituel.dndplayer.util.font;

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

import com.vituel.dndplayer.util.gui.RecursiveViewCaller;

/**
 * Created by Victor on 27/03/14.
 */
public class FontUtil {

    Context ctx;
    public final static String CELESTIA_ANTIQUA = "fonts/Celestia_Antiqua.ttf";
    public final static String PTERRA_DACTYL = "fonts/Pterra-dactyl.ttf";
    public final static String FUTURA_OBLIQUE = "fonts/Futura Oblique.ttf";
    public final static String FUTURA_LIGHT = "fonts/Futura Light.ttf";
    public final static String FUTURA_LIGHTER = "fonts/Futura Lighter.ttf";
    public final static String FUTURA_BOLD = "fonts/Futura Bold.ttf";
    public final static String FUTURA_EXTRA_BOLD = "fonts/Futura Extra Bold.ttf";
    public final static String BOWLBYONE_REGULAR = "fonts/BowlbyOne-Regular.ttf";

    public final static String MAIN_FONT = FUTURA_OBLIQUE;
    public final static String BOLD_FONT = FUTURA_BOLD;
    public final static String BOLDER_FONT = FUTURA_EXTRA_BOLD;

    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    public FontUtil(Context ctx) {
        this.ctx = ctx;
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

    public static void setFontRecursively(Context ctx, View view, String fontPath){
        final Typeface font = new FontUtil(ctx).getFont(fontPath);
        RecursiveViewCaller<TextView> fontChange = new RecursiveViewCaller<TextView>(TextView.class){
            @Override
            protected void leafCall(TextView v, Object... params) {
                v.setTypeface(font);
            }
        };
        fontChange.recursiveCall(view);
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
