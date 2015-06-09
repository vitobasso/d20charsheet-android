package com.vitobasso.d20charsheet.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.io.importer.DownloadObserver;
import com.vitobasso.d20charsheet.io.importer.RulesDownloadException;
import com.vitobasso.d20charsheet.io.importer.RulesDownloader;

import static com.vitobasso.d20charsheet.io.importer.DownloadObserver.Phase.DOWNLOAD;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.EXTRA_MODE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_CLEAR;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.REQUEST_LOAD;
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
        performTask();
    }

    private void performTask() {
        int mode = getIntent().getIntExtra(EXTRA_MODE, REQUEST_LOAD);
        if (mode == REQUEST_CLEAR) {
            new ClearTask().execute();
        } else {
            new DownloadTask().execute();
        }
    }

    private class DownloadTask extends Task {
        @Override
        protected void performDownloaderAction() {
            downloader.download();
        }
    }

    private class ClearTask extends Task {
        @Override
        protected void performDownloaderAction() {
            downloader.clear();
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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                updateProgressBar(bytesRead, totalBytes);
                updateProgressNumbers(bytesRead, totalBytes);
            }
        });
    }

    private void updateProgressBar(long bytesRead, long totalBytes) {
        progressBar.setMax(100);
        int percentage = (int) (bytesRead * 100 / totalBytes);
        progressBar.setProgress(percentage);
    }

    private void updateProgressNumbers(long bytesRead, long totalBytes) {
        long readKb = bytesRead / 1024;
        long totalKb = totalBytes / 1024;
        String progress;
        if (totalKb > 0) {
            progress = readKb + "/" + totalKb + " kB";
        } else {
            progress = readKb + " kB";
        }
        populateTextView(activity, R.id.progress_numbers, progress);
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


    private abstract class Task extends AsyncTask<Void, Void, Exception> {

        protected RulesDownloader downloader = new RulesDownloader(activity, activity);

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                performDownloaderAction();
                return null;
            } catch (RulesDownloadException e) {
                Log.e(TAG, "Failed to peform rule import task.", e);
                return e;
            }
        }

        protected abstract void performDownloaderAction();

        @Override
        protected void onPostExecute(Exception exception) {
            finishTask(exception);
        }
    }

    private void finishTask(Exception exception) {
        if (exception == null) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_FAILED);
        }
        finish();
    }


}
