package com.vitobasso.d20charsheet.activity.rules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;
import com.vitobasso.d20charsheet.util.app.AppPreferences;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_LOAD;

/**
 * Created by Victor on 09/06/2015.
 */
public class RulesDownloadDialog {

    private Activity activity;
    private AppPreferences pref;

    private AlertDialog.Builder builder;
    private EditText editText;
    private AlertDialog dialog;

    public RulesDownloadDialog(Activity activity) {
        this.activity = activity;
        this.pref = new AppPreferences(activity);
        this.builder = new AlertDialog.Builder(activity);
    }

    public void showDialog() {
        setupButtons(builder);
        setupTitle();
        setupEditText(builder);

        dialog = builder.create();
        dialog.show();
        overrideOkButtonListener(dialog); // makes possible to keep the dialog open (if text is not valid) after pressing ok
    }

    private void setupButtons(AlertDialog.Builder builder) {
        builder.setPositiveButton(activity.getString(R.string.import_), null); //to be overriden after dialog.show()
        builder.setNegativeButton(activity.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void setupTitle() {
        String title = activity.getString(R.string.import_rules);
        builder.setTitle(title);
    }

    private void setupEditText(AlertDialog.Builder builder) {
        editText = ActivityUtil.inflateDetached(activity, null, R.layout.import_download_dialog);
        String currentUrl = pref.getImportRulesUrl();
        editText.setText(currentUrl);
        builder.setView(editText);
    }

    private void overrideOkButtonListener(AlertDialog dialog) {
        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickConfirm();
            }
        });
    }

    private void onClickConfirm() {
        String text = editText.getText().toString();
        if (validateRulesUrl(text)) {
            goToImportWithNewUrl(text);
        } else {
            showInvalidUrlMessage();
        }
    }

    private boolean validateRulesUrl(String text) {
        try {
            new URL(text).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }

    private void goToImportWithNewUrl(String text) {
        pref.setImportRuleUrl(text);
        goToImport();
        dialog.dismiss();
    }

    private void goToImport() {
        Intent intent = new Intent(activity, ImportRulesActivity.class);
        activity.startActivityForResult(intent, REQUEST_LOAD);
    }

    private void showInvalidUrlMessage() {
        String message = activity.getString(R.string.import_dialog_invalid_url);
        editText.setError(message);
    }

}
