package com.vituel.dndplayer.activity.summary;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.ActiveConditionDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.util.database.DBTask;
import com.vituel.dndplayer.util.gui.GroupedListAdapter;
import com.vituel.dndplayer.util.i18n.ConditionPredicateStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.vituel.dndplayer.model.effect.Condition.Predicate;

/**
 * Created by Victor on 09/05/2015.
 */
public class ConditionAdapter extends GroupedListAdapter<Condition, Predicate> implements ListView.OnItemClickListener {

    private final SummaryActivity activity;
    private final CharBase charBase;
    private final Set<Condition> activeConditions;
    private final ConditionPredicateStringConverter predicateI18n;

    public ConditionAdapter(SummaryActivity activity) {
        super(activity, activity.getData().getReferencedConditions(),
                R.layout.condition_item_row, R.layout.condition_header_row, R.id.name, R.id.name);
        this.activity = activity;
        this.charBase = activity.getData().getBase();
        this.activeConditions = charBase.getActiveConditions();
        this.predicateI18n = new ConditionPredicateStringConverter(activity);
    }

    @Override
    protected Predicate getGroup(Condition item) {
        return item.getPredicate();
    }

    @Override
    protected boolean isChecked(Condition condition) {
        return activeConditions.contains(condition);
    }

    @Override
    protected String itemToString(Condition item) {
        return item.getName();
    }

    @Override
    protected String groupToString(Predicate group) {
        return predicateI18n.toString(group).toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = getItem(position);
        if (item instanceof Condition) {
            Condition clickedCondition = (Condition) item;
            toggleActive(clickedCondition);
        }
    }

    private void toggleActive(Condition condition) {
        if (activeConditions.contains(condition)) {
            activeConditions.remove(condition);
        } else {
            deactivateAllWithPredicate(condition.getPredicate());
            activeConditions.add(condition);
        }

        new UpdateTask().execute();
    }

    private void deactivateAllWithPredicate(Predicate predicate) {
        List<Condition> conditionsToRemove = new ArrayList<>();
        for (Condition activeCondition : activeConditions) {
            if (activeCondition.getPredicate() == predicate) {
                conditionsToRemove.add(activeCondition);
            }
        }
        for (Condition condition : conditionsToRemove) {
            activeConditions.remove(condition);
        }
    }

    private class UpdateTask extends DBTask<ActiveConditionDao> {

        public UpdateTask() {
            super(new ActiveConditionDao(activity));
        }

        @Override
        protected void doInBackgroud(ActiveConditionDao dao) {
            dao.saveOverwrite(charBase.getId(), activeConditions);
        }

        @Override
        protected void onPostExecute() {
            activity.refreshUI();
        }
    }

}
