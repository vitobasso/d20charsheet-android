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
import com.vitobasso.d20charsheet.io.downloader.RulesDownloadException;
import com.vitobasso.d20charsheet.io.downloader.RulesDownloader;

import static com.vitobasso.d20charsheet.io.downloader.DownloadObserver.Phase.DOWNLOAD;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.RESULT_FAILED;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 04/06/2015.
 */
public class DownloadRulesActivity extends Activity implements DownloadObserver {

    public static final String TAG = DownloadRulesActivity.class.getSimpleName();

    private DownloadRulesActivity activity = this;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_download);
        progressBar = findView(this, R.id.progress);
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
                downloader.download();
                return null;
            } catch (RulesDownloadException e) {
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
    public void onBeginPhase(final Phase phase) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(phase != DOWNLOAD);
                String message = activity.getString(getMessageResource(phase));
                populateTextView(activity, R.id.message, message);
            }
        });
    }

    @Override
    public void onProgress(final long bytesRead, final long totalBytes) {
        final String progress = bytesRead + "/" + totalBytes;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                populateTextView(activity, R.id.progress_numbers, progress);
                progressBar.setMax(100);
                int percentage = (int) (bytesRead * 100 / totalBytes);
                progressBar.setProgress(percentage);
            }
        });
    }

    private int getMessageResource(Phase phase) {
        switch (phase) {
            case CLEAN:
                return R.string.cleaning;
            case DOWNLOAD:
                return R.string.downloading;
            case EXTRACT:
                return R.string.extracting;
            default:
                throw new IllegalStateException();
        }
    }

}
