package com.vituel.dndplayer.activity.edit_char;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.util.ActivityUtil;

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

        EditText divinity = findView(R.id.divinityValue);
        divinity.setText(data.getDivinity());

        Spinner gender = findView(R.id.genderValue);
        setSpinnerSelection(gender, data.getGender());

        EditText age = findView(R.id.ageValue);
        age.setText("" + data.getAge());

        EditText height = findView(R.id.heightValue);
        height.setText("" + data.getHeight());

        EditText weight = findView(R.id.weightValue);
        weight.setText("" + data.getWeight());

        EditText player = findView(R.id.playerValue);
        player.setText(data.getPlayer());

        EditText dm = findView(R.id.dmValue);
        dm.setText(data.getDungeonMaster());

        EditText campaign = findView(R.id.campaignValue);
        campaign.setText(data.getCampaign());

        EditText creation = findView(R.id.creationValue);
        creation.setText(data.getCreationDate());

    }

    @Override
    public void onSaveToModel() {
        Spinner tendencyMoral = findView(R.id.tendencyValue, R.id.moral);
        data.setTendencyMoral(tendencyMoral.getSelectedItem().toString());

        Spinner tendencyLoyalty = findView(R.id.tendencyValue, R.id.loyalty);
        data.setTendencyLoyality(tendencyLoyalty.getSelectedItem().toString());

        EditText divinity = findView(R.id.divinityValue);
        data.setDivinity(divinity.getText().toString());

        Spinner gender = findView(R.id.genderValue);
        data.setGender(gender.getSelectedItem().toString());

        EditText age = findView(R.id.ageValue);
        data.setAge(Integer.valueOf(age.getText().toString()));

        EditText height = findView(R.id.heightValue);
        data.setHeight(Double.valueOf(height.getText().toString()));

        EditText weight = findView(R.id.weightValue);
        data.setWeight(Double.valueOf(weight.getText().toString()));

        EditText player = findView(R.id.playerValue);
        data.setPlayer(player.getText().toString());

        EditText dm = findView(R.id.dmValue);
        data.setDungeonMaster(dm.getText().toString());

        EditText campaign = findView(R.id.campaignValue);
        data.setCampaign(campaign.getText().toString());

        EditText creation = findView(R.id.creationValue);
        data.setCreationDate(creation.getText().toString());

    }

    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }

}
