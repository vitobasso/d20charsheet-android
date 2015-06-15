package com.vitobasso.d20charsheet.io.char_io;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.business.CharUtil;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_STREAM;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.createChooser;

/**
 * Created by Victor on 27/09/2014.
 */
public class CharExporterImporter {

    public static final String MIME_TYPE = "application/octet-stream";
    public static final String DIR_NAME = "chars_json";
    public static final int REQUEST_IMPORT = 1;
    public static final int REQUEST_EXPORT = 2;

    private Activity activity;

    public CharExporterImporter(Activity activity) {
        this.activity = activity;
    }

    public void exportChar(CharBase charBase) {
        try {
            ObjectMapper parser = buildParser();
            String json = parser.writeValueAsString(charBase);
            File file = getFile(charBase);
            FileUtils.write(file, json);
            openExportDialog(file);
        } catch (IOException e) {
            throw new ExportCharException("Failed to export char");
        }
    }

    public void importChar() {
        Intent intent = new Intent(ACTION_GET_CONTENT);
        intent.setType(MIME_TYPE);
        activity.startActivityForResult(intent, REQUEST_IMPORT);
    }

    public CharBase handleImportResponse(Intent intent) {
        try {
            InputStream input = activity.getContentResolver().openInputStream(intent.getData());
            ObjectMapper parser = buildParser();
            return parser.readValue(input, CharBase.class);
        } catch (IOException e) {
            throw new ImportCharException("Failed to import char", e);
        }
    }

    private ObjectMapper buildParser() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.REQUIRE_SETTERS_FOR_GETTERS, true);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper;
    }

    private File getFile(CharBase charBase) {
        File jsonDir = getJsonDir();
        String fileName = getFileName(charBase);
        return new File(jsonDir, fileName);
    }

    private File getJsonDir() {
        File appDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        return new File(appDir, DIR_NAME);
    }

    private String getFileName(CharBase charBase) {
        CharUtil charUtil = new CharUtil(activity, charBase);
        return charUtil.getDescription() + ".json";
    }

    private void openExportDialog(File jsonFile) {
        Intent intent = new Intent(ACTION_SEND);
        intent.setType(MIME_TYPE);
        intent.putExtra(EXTRA_SUBJECT, getSubject());
        intent.putExtra(EXTRA_STREAM, Uri.fromFile(jsonFile));
        String msg = activity.getString(R.string.export_via);
        activity.startActivityForResult(createChooser(intent, msg), REQUEST_EXPORT);
    }

    private String getSubject() {
        return activity.getString(R.string.export_subject);
    }

}
