package com.vituel.dndplayer.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.parser.LibraryLoader;
import com.vituel.dndplayer.util.gui.LoaderObserver;

import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;

/**
 * Created by Victor on 21/04/2015.
 */
public class LoadingActivity extends Activity implements LoaderObserver {

    private LoadingActivity activity = this;
    private ProgressBar progressBar;
    private ViewGroup currentRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        progressBar = findView(this, R.id.progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO check if not executing (in case screen went off and on again)
        new Task().execute();
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            LibraryLoader loader = new LibraryLoader(activity, activity);
            progressBar.setMax(loader.getTotalFiles());
            loader.loadDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onStartLoadingFile(final String fileName) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int prog = progressBar.getProgress();
                progressBar.setProgress(prog + 1);
                currentRow = inflate(activity, R.id.list, R.layout.progress_row);
                populateTextView(currentRow, R.id.table, fileName);
            }
        });
    }

    @Override
    public void onFinishLoadingRow(final String name, final int count) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                populateTextView(currentRow, R.id.count, count);
                populateTextView(activity, R.id.name, name);
            }
        });
    }

}
