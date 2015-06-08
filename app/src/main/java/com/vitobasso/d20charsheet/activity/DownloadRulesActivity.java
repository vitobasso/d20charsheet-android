package com.vitobasso.d20charsheet.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.io.downloader.DownloadObserver;
import com.vitobasso.d20charsheet.io.downloader.RulesDownloader;

import java.io.IOException;

import static com.vitobasso.d20charsheet.util.app.ActivityUtil.RESULT_FAILED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 04/06/2015.
 */
public class DownloadRulesActivity extends Activity implements DownloadObserver {

    public static final String TAG = DownloadRulesActivity.class.getSimpleName();

    private DownloadRulesActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        ProgressBar progressBar = findView(this, R.id.progress);
        progressBar.setIndeterminate(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO check if not executing (in case screen went off and on again)
        new Task().execute();
    }

    private class Task extends AsyncTask<Void, Void, Exception> {

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                RulesDownloader downloader = new RulesDownloader(activity, activity);
                downloader.downloadRules();
                return null;
            } catch (IOException e) {
                Log.e(TAG, "Failed to download rules", e);
                return e;
            }
        }

        @Override
        protected void onPostExecute(Exception exception) {
            if (exception == null) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_FAILED);
            }
            finish();
        }
    }

    @Override
    public void onBeginStage(final String name) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                populateTextView(activity, R.id.name, name);
            }
        });
    }

    @Override
    public void onProgress(int progress) {

    }

}
