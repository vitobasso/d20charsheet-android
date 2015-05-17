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
import com.vituel.dndplayer.util.i18n.ConditionI18n;
import com.vituel.dndplayer.util.i18n.ConditionPredicateStringConverter;

import java.util.Set;

import static com.vituel.dndplayer.model.effect.Condition.Predicate;

/**
 * Created by Victor on 09/05/2015.
 */
public class ConditionAdapter extends GroupedListAdapter<Condition, Predicate> implements ListView.OnItemClickListener {

    private final SummaryActivity activity;
    private final CharBase charBase;
    private final Set<Condition> activeConditions;
    private final ConditionI18n conditionI18n;
    private final ConditionPredicateStringConverter predicateI18n;

    public ConditionAdapter(SummaryActivity activity) {
        super(activity, activity.getData().getReferencedConditions(),
                R.layout.condition_item_row, R.layout.condition_header_row, R.id.name, R.id.name);
        this.activity = activity;
        this.charBase = activity.getData().getBase();
        this.activeConditions = charBase.getActiveConditions();
        this.conditionI18n = new ConditionI18n(activity);
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
        return conditionI18n.translateName(item).toString();
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
            activeConditions.add(condition);
        }

        new UpdateTask().execute();
    }

    private class UpdateTask extends DBTask<ActiveConditionDao> {

        public UpdateTask() {
            super(new ActiveConditionDao(activity));
        }

        @Override
        protected void doInBackgroud(ActiveConditionDao dao) {
            dao.save(activeConditions, charBase.getId());
        }

        @Override
        protected void onPostExecute() {
            activity.refreshUI();
        }
    }

}
