package com.vituel.dndplayer.activity.edit_char;

import android.view.View;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;

import static com.vituel.dndplayer.util.ActivityUtil.populateStaticSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.ActivityUtil.readFloat;
import static com.vituel.dndplayer.util.ActivityUtil.readInt;
import static com.vituel.dndplayer.util.ActivityUtil.readSpinner;
import static com.vituel.dndplayer.util.ActivityUtil.readString;

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

        populateStaticSpinner(activity, null, R.id.moral, data.getTendencyMoral());
        populateStaticSpinner(activity, null, R.id.loyalty, data.getTendencyLoyality());
        populateStaticSpinner(activity, null, R.id.genderValue, data.getGender());
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

        data.setTendencyMoral((String) readSpinner(activity, R.id.moral));
        data.setTendencyLoyality((String) readSpinner(activity, R.id.loyalty));
        data.setGender((String) readSpinner(activity, R.id.genderValue));
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
