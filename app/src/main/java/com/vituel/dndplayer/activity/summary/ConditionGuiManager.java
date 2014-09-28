package com.vituel.dndplayer.activity.summary;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.ActiveConditionDao;
import com.vituel.dndplayer.model.Character;
import com.vituel.dndplayer.model.Condition;
import com.vituel.dndplayer.util.JavaUtil;
import com.vituel.dndplayer.util.ModifierStringConverter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.vituel.dndplayer.model.Condition.Predicate;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 22/04/14.
 */
public class ConditionGuiManager {

    SummaryActivity activity;
    ModifierStringConverter modConv;

    Character character;

    public ConditionGuiManager(SummaryActivity activity) {
        this.activity = activity;
        this.modConv = new ModifierStringConverter(activity);
    }

    public void populate(Character character) {
        this.character = character;
        final int cols = 3;
        Set<Condition> activeConds = character.getBase().getActiveConditions();
        Map<Predicate, Set<Condition>> referencedConds = organizeConditions(character.getReferencedConditions());
        ViewGroup root = findView(activity, R.id.conditions);
        ViewGroup list = findView(root, R.id.list);

        //title
        View titleView = findView(root, R.id.draggable);
        titleView.setOnTouchListener(new TouchListener(root));
        titleView.setClickable(true);

        //predicate groups
        list.removeAllViews();
        ViewGroup rowView = null;
        for (Predicate pred : referencedConds.keySet()) {
            Set<Condition> conds = referencedConds.get(pred);
            ViewGroup predGroup = inflate(activity, list, R.layout.cond_group);

            TextView predView = findView(predGroup, R.id.pred);
            predView.setText(modConv.getConditionPredicate(pred));

            //conditions
            for (int i = 0; i < conds.size(); i++) {
                Condition cond = JavaUtil.findByIndex(conds, i);

                int col = i % cols;
                if (col == 0) {
                    rowView = inflate(activity, predGroup, R.layout.three_texts_row);
                }

                assert rowView != null;
                TextView condView = (TextView) rowView.getChildAt(col);
                assert condView != null;
                condView.setText(cond.getName());
                condView.setAlpha(activeConds.contains(cond) ? 1 : .3f);
                condView.setOnClickListener(new ClickListener(activeConds, cond));
            }
        }

        setFontRecursively(activity, root, MAIN_FONT);
    }

    private Map<Predicate, Set<Condition>> organizeConditions(Set<Condition> conditions) {
        Map<Predicate, Set<Condition>> map = new TreeMap<>();
        for (Condition cond : conditions) {
            Predicate pred = cond.getPredicate();
            Set<Condition> set = map.get(pred);
            if (set == null) {
                set = new HashSet<>();
                map.put(pred, set);
            }
            set.add(cond);
        }
        return map;
    }

    public void setVisibility(boolean visible){
        ViewGroup root = findView(activity, R.id.conditions);
        int visibility = visible ? View.VISIBLE : View.GONE;
        root.setVisibility(visibility);
    }

    public void show(){
        ViewGroup root = findView(activity, R.id.conditions);
        root.setVisibility(View.VISIBLE);
    }

    private class ClickListener implements View.OnClickListener {

        Set<Condition> activeConds;
        Condition clickedCond;

        private ClickListener(Set<Condition> activeConds, Condition clickedCond) {
            this.activeConds = activeConds;
            this.clickedCond = clickedCond;
        }

        @Override
        public void onClick(View v) {

            //toggle active
            if (activeConds.contains(clickedCond)) {
                activeConds.remove(clickedCond);
            } else {
                activeConds.add(clickedCond);
            }

            //update conditions on DB
            ActiveConditionDao condDao = new ActiveConditionDao(activity);
            condDao.save(activeConds, character.getBase().getId());
            condDao.close();

            activity.refreshUI();
        }

    }

    private class TouchListener implements View.OnTouchListener {

        View panelView;
        ViewGroup.LayoutParams params;
        int maxHeight;
        int minHeight;

        int initHeight;
        float initPos;
        boolean moved;

        private TouchListener(View panelView) {
            this.panelView = panelView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (params == null) {
                //init
                params = panelView.getLayoutParams();

                float padding = activity.getResources().getDimension(R.dimen.standard_padding);
                minHeight = Math.round(v.getHeight() + 2 * padding);

                View list = panelView.findViewById(R.id.list);
                maxHeight = minHeight + list.getHeight();
            }

            switch (event.getActionMasked()) {
                case ACTION_DOWN:
                    //init drag
                    initHeight = panelView.getHeight();
                    initPos = event.getRawY();
                    moved = false;
                    break;

                case ACTION_MOVE:
                    //do drag
                    float dPos = initPos - event.getRawY();
                    int newHeight = Math.round(initHeight + dPos);
                    newHeight = Math.max(minHeight, newHeight);
                    newHeight = Math.min(maxHeight, newHeight);
                    params.height = newHeight;
                    panelView.requestLayout();
                    if (newHeight != initHeight) {
                        moved = true;
                    }
                    break;

                case ACTION_UP:
                    //toggle show/hide
//                    if (!moved) {
//                        params.height = panelView.getHeight() == minHeight ? maxHeight : minHeight;
//                        panelView.requestLayout();
//                    }
//                    break;
            }

            return false;
        }
    }

}
