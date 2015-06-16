package com.vitobasso.d20charsheet.io.importer;

import android.database.SQLException;
import android.util.Log;

import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.exception.ParseEntityException;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.util.database.BulkInserter;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

import java.io.IOException;

/**
 * Created by Victor on 16/06/2015.
 */
public class RulesEntityImporter<T extends AbstractEntity>  {

    public static final String TAG = RulesImporter.class.getSimpleName();
    public static final int BATCH_SIZE = 100;

    private ImporterObserver observer;

    private AbstractEntityParser<T> parser;
    private AbstractDao<T> dao;
    private String title;

    private BulkInserter<T> bulkInserter;

    public RulesEntityImporter(EntityToolFactory<T> factory, ImporterObserver observer) {
        this.observer = observer;
        this.parser = factory.createParser();
        this.dao = factory.createDao();
        this.title = factory.getEntityTitle();
    }

    public void importRules() {
        try {
            runBatchImports();
        } catch (IOException e) {
            Log.e(TAG, "IOException exception when importing to " + dao.getClass().getSimpleName(), e);
        } finally {
            close(dao);
            close(parser);
        }
    }

    private void runBatchImports() throws IOException {
        observer.onBeginFile(title);
        bulkInserter = dao.createBulkInserter();
        while (parser.hasNext()) {
            batchImport();
        }
    }

    private void batchImport() throws IOException {
        try {
            bulkInserter.beginTransaction();
            importEntities();
            bulkInserter.markAsSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Batch import was interrupted.", e);
            throw e;
        } finally {
            bulkInserter.endTransaction();
        }
    }

    private void importEntities() throws IOException {
        for (int i = 0; i < BATCH_SIZE && parser.hasNext(); i++) {
            importEntity();
        }
        observer.onFinishBatch(parser.getCount());
    }

    private void importEntity() {
        try {
            tryImportEntity();
        } catch (ParseEntityException | SQLException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception when importing to " + bulkInserter.getClass().getSimpleName(), e);
        }
    }

    private void tryImportEntity() throws IOException {
        T entity = parser.next();
        bulkInserter.insert(entity);
    }

    private void close(AbstractDao dao) {
        try {
            dao.close();
        } catch (RuntimeException e) {
            Log.e(TAG, "Failed to close dao.", e);
        }
    }

    private void close(AbstractEntityParser parser) {
        try {
            parser.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close parser.", e);
        }
    }

    public AbstractEntityParser<T> getParser() {
        return parser;
    }

}
