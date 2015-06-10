package com.vitobasso.d20charsheet.io.char_io;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.character.CharBase;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.File;
import java.io.IOException;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.createChooser;

/**
 * Created by Victor on 27/09/2014.
 */
public class CharJsonParser {

    private static String TAG = CharJsonParser.class.getName();

    private Context context;

    public CharJsonParser(Context context) {
        this.context = context;
    }

    public void exportChar(CharBase charBase) {
        try {
            ObjectMapper parser = buildParser();
            String json = parser.writeValueAsString(charBase);
            export(json);

//            String msg = String.format("%s: %s", context.getString(R.string.saved_to), file.getAbsolutePath());
//            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.failed_saving), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to export char.", e);
        }
    }

    private ObjectMapper buildParser() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.REQUIRE_SETTERS_FOR_GETTERS, true);
        mapper.configure(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        return mapper;
    }

    public void export(String json) {
        Intent sendIntent = new Intent(ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(EXTRA_SUBJECT, "Subject");
        sendIntent.putExtra(EXTRA_TEXT, json);
        context.startActivity(createChooser(sendIntent, "Export to"));
    }

    public void importChar() {
        try {
            File file = null; //TODO file chooser

            ObjectMapper parser = buildParser();
            CharBase charBase = parser.readValue(file, CharBase.class);

            //TODO insertOrUpdate into db, find dependencies by name

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
