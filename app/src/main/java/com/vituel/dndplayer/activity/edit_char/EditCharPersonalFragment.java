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
    protected int getLayout() {
        return R.layout.edit_char_personal;
    }

    @Override
    protected void onPopulate() {
        CharBase base = activity.base;

        Spinner moral = findView(R.id.moral);
        setSpinnerSelection(moral, base.getTendencyMoral());

        Spinner loyalty = findView(R.id.loyalty);
        setSpinnerSelection(loyalty, base.getTendencyLoyality());

        EditText divinity = findView(R.id.divinityValue);
        divinity.setText(base.getDivinity());

        Spinner gender = findView(R.id.genderValue);
        setSpinnerSelection(gender, base.getGender());

        EditText age = findView(R.id.ageValue);
        age.setText("" + base.getAge());

        EditText height = findView(R.id.heightValue);
        height.setText("" + base.getHeight());

        EditText weight = findView(R.id.weightValue);
        weight.setText("" + base.getWeight());

        EditText player = findView(R.id.playerValue);
        player.setText(base.getPlayer());

        EditText dm = findView(R.id.dmValue);
        dm.setText(base.getDungeonMaster());

        EditText campaign = findView(R.id.campaignValue);
        campaign.setText(base.getCampaign());

        EditText creation = findView(R.id.creationValue);
        creation.setText(base.getCreationDate());

    }

    @Override
    public void onSaveToModel() {
        CharBase base = activity.base;

        Spinner tendencyMoral = findView(R.id.tendencyValue, R.id.moral);
        base.setTendencyMoral(tendencyMoral.getSelectedItem().toString());

        Spinner tendencyLoyalty = findView(R.id.tendencyValue, R.id.loyalty);
        base.setTendencyLoyality(tendencyLoyalty.getSelectedItem().toString());

        EditText divinity = findView(R.id.divinityValue);
        base.setDivinity(divinity.getText().toString());

        Spinner gender = findView(R.id.genderValue);
        base.setGender(gender.getSelectedItem().toString());

        EditText age = findView(R.id.ageValue);
        base.setAge(Integer.valueOf(age.getText().toString()));

        EditText height = findView(R.id.heightValue);
        base.setHeight(Double.valueOf(height.getText().toString()));

        EditText weight = findView(R.id.weightValue);
        base.setWeight(Double.valueOf(weight.getText().toString()));

        EditText player = findView(R.id.playerValue);
        base.setPlayer(player.getText().toString());

        EditText dm = findView(R.id.dmValue);
        base.setDungeonMaster(dm.getText().toString());

        EditText campaign = findView(R.id.campaignValue);
        base.setCampaign(campaign.getText().toString());

        EditText creation = findView(R.id.creationValue);
        base.setCreationDate(creation.getText().toString());

    }

    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }

}
