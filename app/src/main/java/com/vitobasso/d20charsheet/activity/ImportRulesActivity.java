package com.vitobasso.d20charsheet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.io.importer.ImporterObserver;
import com.vitobasso.d20charsheet.io.importer.RulesImporter;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_MODE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CLEAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_LOAD;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.inflate;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 21/04/2015.
 */
public class ImportRulesActivity extends Activity implements ImporterObserver {

    private ImportRulesActivity activity = this;
    private ProgressBar progressBar;
    private ViewGroup currentRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_parse);
        progressBar = findView(this, R.id.progress);
        goToDownload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOAD:
                switch (resultCode) {
                    case RESULT_OK:
                        populateTextView(activity, R.id.message, getString(R.string.importing));
                        new Task().execute();
                        break;
                    default:
                        showErrorMessage();
                }
                break;
            case REQUEST_CLEAR:
                setResult(RESULT_OK);
                finish();
        }
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            importRules();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            goToClearDir();
        }

    }

    private void importRules() {
        RulesImporter loader = new RulesImporter(activity, activity);
        progressBar.setMax(loader.getTotalFiles());
        loader.importCsvs();
    }

    @Override
    public void onBeginFile(final String fileName) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int prog = progressBar.getProgress();
                progressBar.setProgress(prog + 1);
                currentRow = inflate(activity, R.id.list, R.layout.import_parse_row);
                populateTextView(currentRow, R.id.table, fileName);
            }
        });
    }

    @Override
    public void onFinishBatch(final int count) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                populateTextView(currentRow, R.id.count, count);
            }
        });
    }

    private void goToDownload() {
        Intent intent = new Intent(this, DownloadRulesActivity.class);
        intent.putExtra(EXTRA_MODE, REQUEST_LOAD);
        startActivityForResult(intent, REQUEST_LOAD);
    }

    private void goToClearDir() {
        Intent intent = new Intent(this, DownloadRulesActivity.class);
        intent.putExtra(EXTRA_MODE, REQUEST_CLEAR);
        startActivityForResult(intent, REQUEST_CLEAR);
    }

    private void showErrorMessage() {
        String message = activity.getString(R.string.failed_rules_download);
        populateTextView(activity, R.id.message, message);
    }

}
