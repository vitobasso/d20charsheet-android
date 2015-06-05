package com.vituel.dndplayer.io.downloader;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.io.downloader.exception.CreateFileException;
import com.vituel.dndplayer.io.downloader.exception.HttpStatusException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;

import static com.vituel.dndplayer.R.string.cleaning;
import static com.vituel.dndplayer.R.string.downloading;
import static com.vituel.dndplayer.R.string.extracting;
import static com.vituel.dndplayer.R.string.preparing;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FilenameUtils.isExtension;
import static org.rauschig.jarchivelib.ArchiveFormat.TAR;
import static org.rauschig.jarchivelib.CompressionType.GZIP;

/**
 * Created by Victor on 04/06/2015.
 */
public class RulesDownloader {

    public static final String TAG = RulesDownloader.class.getSimpleName();

    public static final String GITHUB_USER = "vitobasso";
    public static final String GITHUB_REPO = "dnd3.5-data";
    public static final String ARCHIVE_URL = "https://api.github.com/repos/" + GITHUB_USER + "/" + GITHUB_REPO + "/tarball/";
    public static final String DIR_NAME = "rules_csv";
    public static final String ARCHIVE_FILE_NAME = "rules.tar.gz";

    private HttpClient client = new DefaultHttpClient();
    private Archiver archiver = ArchiverFactory.createArchiver(TAR, GZIP);
    private Context context;
    private DownloadObserver observer;

    public RulesDownloader(Context context, DownloadObserver observer) {
        this.context = context;
        this.observer = observer;
    }

    public void downloadRules() throws IOException {
        notifyObserver(preparing);
        clearDir();
        notifyObserver(downloading);
        downloadArchive();
        notifyObserver(extracting);
        extract();
        notifyObserver(cleaning);
        moveContentsOutOfExtractedDir();
        deleteAllButCsvFiles();
    }

    private void downloadArchive() throws IOException {
        Log.i(TAG, "Downloading " + ARCHIVE_URL);
        HttpGet request = new HttpGet(ARCHIVE_URL);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode / 100 == 2) {
            HttpEntity entity = response.getEntity();
            File file = getArchiveFile();
            copyInputStreamToFile(entity.getContent(), file);
        } else {
            throw new HttpStatusException(statusCode);
        }
    }

    private void extract() throws IOException {
        File dir = getRulesDir();
        Log.i(TAG, "Extracting rules archive to " + dir.getAbsolutePath());
        archiver.extract(getArchiveFile(), dir);
    }

    private void moveContentsOutOfExtractedDir() throws IOException {
        File extractedDir = findExtractedDir();
        File rulesDir = getRulesDir();
        for (File file : extractedDir.listFiles()) {
            FileUtils.moveToDirectory(file, rulesDir, false);
        }
    }

    private File findExtractedDir() {
        for (File file : getRulesDir().listFiles()) {
            // the name of the root dir inside the archive returned by Github is expected to follow this pattern
            String prefix = GITHUB_USER + "-" + GITHUB_REPO;
            if (file.isDirectory() && file.getName().startsWith(prefix)) {
                return file;
            }
        }
        return null;
    }

    private void deleteAllButCsvFiles() throws IOException {
        for (File file : getRulesDir().listFiles()) {
            if (!isExtension(file.getName(), "csv")) {
                forceDelete(file);
            }
        }
    }

    private void clearDir() throws IOException {
        File dir = getRulesDir();
        if (dir.exists()) {
            forceDelete(dir);
        }
        if (!dir.mkdirs()) {
            throw new CreateFileException(dir.getAbsolutePath());
        }
    }

    private File getArchiveFile() {
        File dir = getRulesDir();
        return new File(dir, ARCHIVE_FILE_NAME);
    }

    private File getRulesDir() {
        File dir = context.getFilesDir();
        return new File(dir, DIR_NAME);
    }

    private void notifyObserver(int resId) {
        observer.onBeginStage(context.getString(resId));
    }

}
