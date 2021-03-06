package com.vitobasso.d20charsheet.activity.summary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.entity.CharDao;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.character.DamageTaken;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;
import com.vitobasso.d20charsheet.util.font.FontUtil;
import com.vitobasso.d20charsheet.util.gui.GuiInflater;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;

/**
 * Created by Victor on 26/04/14.
 */
public class HpGuiInflater implements GuiInflater {

    private SummaryActivity activity;
    private CharSummary charSummary;

    public HpGuiInflater(SummaryActivity activity, CharSummary charSummary) {
        this.activity = activity;
        this.charSummary = charSummary;
    }

    public void inflate(final ViewGroup parentView) {
        ViewGroup root = ActivityUtil.inflate(activity, null, R.layout.summary_breakdown_hp);
        root.setId(R.id.damage);
        final DamageTaken dmg = charSummary.getBase().getDamageTaken();

        inflateBreakdownItems(root, dmg);
        populateDamageUI(root, dmg);

        parentView.addView(root);
    }

    private void inflateBreakdownItems(ViewGroup root, DamageTaken dmg) {
        BreakdownDialogInflater bdi = new BreakdownDialogInflater(activity, charSummary, ModifierTarget.HP, null);

        ViewGroup listGroup = findView(root, R.id.list);
        listGroup.removeAllViews();
        if (dmg.getTempHp() != 0) {
            bdi.appendRow(listGroup, dmg.getTempHp(), getString(R.string.hp_temp_long), bdi.green);
        }
        if (dmg.getLethal() != 0) {
            bdi.appendRow(listGroup, dmg.getLethal(), getString(R.string.damage), bdi.red);
        }
        if (dmg.getNonlethal() != 0) {
            bdi.appendRow(listGroup, dmg.getNonlethal(), getString(R.string.dmg_nonlethal), bdi.red);
        }

        int visibility = dmg.getTempHp() != 0 || dmg.getLethal() != 0 || dmg.getNonlethal() != 0 ? VISIBLE : GONE;
        View titleView = findView(root, R.id.title);
        titleView.setVisibility(visibility);

        new FontUtil(activity).setFontRecursively(root);
    }

    private void populateDamageUI(final ViewGroup root, final DamageTaken dmg) {

        final NumberPicker picker = findView(root, R.id.dmgPicker);
        picker.setMinValue(1);
        picker.setMaxValue(99);
        picker.setWrapSelectorWheel(false);

        Button subtLethal = findView(root, R.id.subtLethal);
        subtLethal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                dmg.setLethal(Math.max(0, dmg.getLethal() - value));
                apply(root, dmg);
            }
        });

        Button subtNonlethal = findView(root, R.id.subtNonlethal);
        subtNonlethal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                dmg.setNonlethal(Math.max(0, dmg.getNonlethal() - value));
                apply(root, dmg);
            }
        });

        Button subtTemp = findView(root, R.id.subtTempHp);
        subtTemp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                dmg.setTempHp(Math.max(0, dmg.getTempHp() - value));
                apply(root, dmg);
            }
        });

        Button addLethal = findView(root, R.id.addLethal);
        addLethal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                dmg.setLethal(dmg.getLethal() + value);
                apply(root, dmg);
            }
        });

        Button addNonlethal = findView(root, R.id.addNonlethal);
        addNonlethal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                dmg.setNonlethal(dmg.getNonlethal() + value);
                apply(root, dmg);
            }
        });

        Button addTemp = findView(root, R.id.addTempHp);
        addTemp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = picker.getValue();
                dmg.setTempHp(dmg.getTempHp() + value);
                apply(root, dmg);
            }
        });
    }

    private void apply(ViewGroup root, DamageTaken dmg) {

        //refresh dialog UI
        inflateBreakdownItems(root, dmg);

        //update DB
        CharDao charDao = new CharDao(activity);
        charDao.save(charSummary.getBase());
        charDao.close();

        //refresh activity UI
        activity.refreshUI();
    }

    private String getString(int resId) {
        return activity.getResources().getString(resId);
    }

}
