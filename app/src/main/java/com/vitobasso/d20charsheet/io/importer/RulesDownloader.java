package com.vitobasso.d20charsheet.io.importer;

import android.content.Context;
import android.util.Log;

import com.vitobasso.d20charsheet.util.LoggingUtil;
import com.vitobasso.d20charsheet.util.app.AppPreferences;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.vitobasso.d20charsheet.io.importer.DownloadObserver.Phase.CLEAN;
import static com.vitobasso.d20charsheet.io.importer.DownloadObserver.Phase.DOWNLOAD;
import static com.vitobasso.d20charsheet.io.importer.DownloadObserver.Phase.EXTRACT;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.forceDelete;
import static org.rauschig.jarchivelib.ArchiveFormat.TAR;
import static org.rauschig.jarchivelib.CompressionType.GZIP;

/**
 * Created by Victor on 04/06/2015.
 */
public class RulesDownloader {

    public static final String TAG = RulesDownloader.class.getSimpleName();

    public static final String DIR_NAME = "rules_csv";
    public static final String ARCHIVE_FILE_NAME = "rules.tar.gz";

    private HttpClient client = HttpClientBuilder.create().build();
    private Archiver archiver = ArchiverFactory.createArchiver(TAR, GZIP);
    private Context context;
    private DownloadObserver observer;
    private String rulesUrl;

    public RulesDownloader(Context context, DownloadObserver observer) {
        this.context = context;
        this.observer = observer;
        this.rulesUrl = new AppPreferences(context).getImportRulesUrl();
    }

    public void download() {
        long startTime = System.currentTimeMillis(); //TODO use AOP instead?
        downloadAndExtract();
        LoggingUtil.logTime(TAG, startTime, "download rules");
    }

    private void downloadAndExtract() {
        try {
            clearDir();
            downloadArchive();
            extract();
        } catch (IOException e) {
            throw new RulesDownloadException(e);
        }
    }

    private void downloadArchive() throws IOException {
        observer.onBeginPhase(DOWNLOAD);
        Log.i(TAG, "Downloading from " + rulesUrl);
        HttpGet request = new HttpGet(rulesUrl);
        HttpResponse response = client.execute(request);
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() / 100 == 2) {
            handleResponse(response.getEntity());
        } else {
            throw new RulesDownloadException("Request failed with status: " + status);
        }
    }

    private void handleResponse(HttpEntity entity) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = entity.getContent();
            out = FileUtils.openOutputStream(getArchiveFile());
            long totalBytes = entity.getContentLength();
            copyStreamToFile(in, out, totalBytes);
        } catch (IOException e) {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            throw new RulesDownloadException("Failed to copy from stream.", e);
        }
    }

    private void copyStreamToFile(InputStream in, FileOutputStream out, long totalBytes) throws IOException {
        byte[] buffer = new byte[4*1024];
        long totalBytesRead = 0;
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            observer.onProgress(totalBytesRead, totalBytes);
        }
    }

    private void extract() throws IOException {
        observer.onBeginPhase(EXTRACT);
        File dir = getRulesDir();
        Log.i(TAG, "Extracting rules archive to " + dir.getAbsolutePath());
        archiver.extract(getArchiveFile(), dir);
        moveContentsOutOfExtractedDir();
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
            if (file.isDirectory() && containsCsvFiles(file)) {
                return file;
            }
        }
        return null;
    }

    private boolean containsCsvFiles(File dir) {
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".csv")) {
                return true;
            }
        }
        return false;
    }

    private void clearDir() throws IOException {
        observer.onBeginPhase(CLEAN);
        File dir = getRulesDir();
        if (dir.exists()) {
            forceDelete(dir);
        }
        if (!dir.mkdirs()) {
            throw new RulesDownloadException("Filed to create file: " + dir.getAbsolutePath());
        }
    }

    public void clear() {
        deleteQuietly(getRulesDir());
    }

    private File getArchiveFile() {
        File dir = getRulesDir();
        return new File(dir, ARCHIVE_FILE_NAME);
    }

    private File getRulesDir() {
        return RulesDownloader.getRulesDir(context);
    }

    public static File getRulesDir(Context context) {
        File dir = context.getFilesDir();
        return new File(dir, DIR_NAME);
    }

}
