package com.vituel.dndplayer.util.database;

import android.os.AsyncTask;

import com.vituel.dndplayer.dao.abstraction.AbstractDao;

/**
 * Created by Victor on 10/05/2015.
 */
public abstract class DBTask<T extends AbstractDao> extends AsyncTask<Void, Void, Void> {

    private final T dao;

    public DBTask(T dao) {
        this.dao = dao;
    }

    @Override
    protected final Void doInBackground(Void... params) {
        synchronized (Object.class) {
            doInBackgroud(dao);
        }
        dao.close();
        return null;
    }

    protected abstract void doInBackgroud(T dao);

    @Override
    protected final void onPostExecute(Void aVoid) {
        onPostExecute();
    }

    protected abstract void onPostExecute();

}
