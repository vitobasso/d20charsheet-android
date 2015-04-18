package com.vituel.dndplayer.parser.csv;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vituel.dndplayer.parser.exception.ParseEntityException;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Created by Victor on 26/03/14.
 */
public abstract class AbstractSimpleParser<T> extends AbstractParser {

    public static final String TAG = AbstractSimpleParser.class.getSimpleName();

    private Context ctx;
    private String filePath;
    private BiMap<Integer, String> headers;
    private List<ParseEntityException> failures;

    protected AbstractSimpleParser(Context ctx, String path) {
        this.ctx = ctx;
        filePath = path;
        headers = null;
        failures = new ArrayList<>();
    }

    public List<T> loadFile() {
        try {
            InputStream in = ctx.getAssets().open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            headers = readHeaders(reader);
            return readLines(reader);
        } catch (IOException e) {
            e.printStackTrace();
            return emptyList();
        } finally {
            logFailures();
        }
    }

    private BiMap<Integer, String> readHeaders(BufferedReader reader) throws IOException {
        String[] split = reader.readLine().split("[\t;]");
        BiMap<Integer, String> map = HashBiMap.create();
        for (int i = 0; i <split.length; i++) {
            map.put(i, split[i]);
        }
        return map;
    }

    private List<T> readLines(BufferedReader reader) throws IOException {
        List<T> result = new ArrayList<>();

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

        return result;
    }

    private T parseLine(int lineIndex, String line) throws ParseEntityException {
        String split[] = line.split("[\t;]");
        try {
            return parse(split);
        } catch (ParseFieldException e) {
            String columnName = headers.get(e.getColumnIndex());
            throw new ParseEntityException(lineIndex, line, columnName, filePath, e);
        }
    }

    protected abstract T parse(String[] line) throws ParseFieldException;

    private void logFailures() {
        for (ParseEntityException failure : failures) {
            Log.w(TAG, failure.getMessage());
        }
    }


    protected String readString(String[] split, String column) throws ParseFieldException {
        return readString(split, getIndex(column));
    }

    protected String readStringNullable(String[] split, String column) throws ParseFieldException {
        return readStringNullable(split, getIndex(column));
    }

    protected Integer readInt(String[] split, String column) throws ParseFieldException {
        return readInt(split, getIndex(column));
    }

    protected Integer readIntNullable(String[] split, String column) throws ParseFieldException {
        return readIntNullable(split, getIndex(column));
    }

    protected Integer getIndex(String column) throws ParseFieldException {
        Integer index = headers.inverse().get(column);
        if (index == null) {
            throw new ParseFieldException(-1, "Column name not found: " + column);
        }
        return index;
    }

}
