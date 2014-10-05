package com.vituel.dndplayer.activity.edit_char;

import android.view.View;
import android.widget.Spinner;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;

import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readFloat;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.readString;
import static com.vituel.dndplayer.util.ActivityUtil.setSpinnerSelection;

/**
 * Created by Victor on 28/02/14.
 */
public class EditCharPersonalFragment extends PagerFragment<CharBase, EditCharActivity> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.edit_char_personal;
    }

    @Override
    protected void onPopulate() {

        Spinner moral = findView(R.id.moral);
        setSpinnerSelection(moral, data.getTendencyMoral());

        Spinner loyalty = findView(R.id.loyalty);
        setSpinnerSelection(loyalty, data.getTendencyLoyality());

        Spinner gender = findView(R.id.genderValue);
        setSpinnerSelection(gender, data.getGender());

        populateTextView(root, R.id.divinityValue, data.getDivinity());
        populateTextView(root, R.id.ageValue, data.getAge());
        populateTextView(root, R.id.heightValue, data.getHeight());
        populateTextView(root, R.id.weightValue, data.getWeight());
        populateTextView(root, R.id.playerValue, data.getPlayer());
        populateTextView(root, R.id.dmValue, data.getDungeonMaster());
        populateTextView(root, R.id.campaignValue, data.getCampaign());
        populateTextView(root, R.id.creationValue, data.getCreationDate());
    }

    @Override
    public void onSave() {
        Spinner tendencyMoral = findView(R.id.tendencyValue, R.id.moral);
        data.setTendencyMoral(tendencyMoral.getSelectedItem().toString());

        Spinner tendencyLoyalty = findView(R.id.tendencyValue, R.id.loyalty);
        data.setTendencyLoyality(tendencyLoyalty.getSelectedItem().toString());

        Spinner gender = findView(R.id.genderValue);
        data.setGender(gender.getSelectedItem().toString());

        data.setDivinity(readString(root, R.id.divinityValue));
        data.setAge(readInt(root, R.id.ageValue));
        data.setHeight(readFloat(root, R.id.heightValue));
        data.setWeight(readFloat(root, R.id.weightValue));
        data.setPlayer(readString(root, R.id.playerValue));
        data.setDungeonMaster(readString(root, R.id.dmValue));
        data.setCampaign(readString(root, R.id.campaignValue));
        data.setCreationDate(readString(root, R.id.creationValue));
    }

    //TODO move to PageFragment
    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }

}
