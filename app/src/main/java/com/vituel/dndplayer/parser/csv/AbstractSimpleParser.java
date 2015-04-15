package com.vituel.dndplayer.parser.csv;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.parser.exception.ParseEntityException;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 26/03/14.
 */
public abstract class AbstractSimpleParser<T> extends AbstractParser {

    public static final String TAG = AbstractSimpleParser.class.getSimpleName();

    private Context ctx;
    private String filePath;
    private String[] headers;
    private List<ParseEntityException> failures;

    protected AbstractSimpleParser(Context ctx) {
        this.ctx = ctx;
    }

    public List<T> loadFile(String path) {
        filePath = path;
        headers = null;
        failures = new ArrayList<>();
        List<T> result = new ArrayList<>();

        try {
            InputStream in = ctx.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            headers = reader.readLine().split("[\t;]");
            String line = reader.readLine();
            int count = 0;
            while (line != null) {
                try {
                    T obj = parseLine(count, line);
                    result.add(obj);
                } catch (ParseEntityException e) {
                    failures.add(e);
                }
                line = reader.readLine();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logFailures();
        return result;
    }

    private T parseLine(int lineIndex, String line) throws ParseEntityException {
        String split[] = line.split("[\t;]");
        try {
            return parse(split);
        } catch (ParseFieldException e) {
            throw new ParseEntityException(lineIndex, line, headers[e.getColumnIndex()], filePath, e);
        }
    }

    protected abstract T parse(String[] line) throws ParseFieldException;

    private void logFailures() {
        for (ParseEntityException failure : failures) {
            Log.w(TAG, failure.getMessage());
        }
    }

}
