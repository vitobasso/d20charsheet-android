package com.vituel.dndplayer.io.char_io;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.character.CharBase;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import static com.vituel.dndplayer.util.ActivityUtil.internationalize;

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
            File dir = context.getExternalFilesDir(null); //TODO file chooser
            String fileName = charBase.getDescription();
            fileName = internationalize(fileName, context);
            File file = new File(dir, fileName);

            ObjectMapper parser = buildParser();
            parser.writeValue(file, charBase);

            String msg = MessageFormat.format("{0}: {1}", context.getString(R.string.saved_to), file.getAbsolutePath());
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.failed_saving), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to exportChar char to file.", e);
        }
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

    private ObjectMapper buildParser() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.REQUIRE_SETTERS_FOR_GETTERS, true);
        mapper.configure(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
        return mapper;
    }

}