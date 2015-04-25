package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vituel.dndplayer.parser.exception.ParseEntityException;
import com.vituel.dndplayer.parser.exception.ParseEnumException;
import com.vituel.dndplayer.parser.exception.ParseFieldException;
import com.vituel.dndplayer.parser.exception.ParseNullValueException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by Victor on 26/03/14.
 */
public abstract class AbstractCsvParser<T> extends AbstractParser implements Closeable {

    private Context ctx;
    private String filePath;

    private BiMap<Integer, String> headers;
    private CSVReader reader;
    private int count;
    private String[] nextLine;

    public AbstractCsvParser(Context ctx, String path) {
        this.ctx = ctx;
        filePath = path;
    }

    public boolean hasNext() throws IOException {
        initIfNecessary();
        return nextLine != null;
    }

    public T next() throws IOException, ParseEntityException {
        initIfNecessary();
        try {
            return parseLine(++count, nextLine);
        } finally {
            nextLine = reader.readNext();
        }
    }

    private void initIfNecessary() throws IOException {
        if (reader == null) {
            InputStream in = ctx.getAssets().open(filePath);
            reader = new CSVReader(new InputStreamReader(in), ';', '"');
            headers = readHeaders();
            nextLine = reader.readNext();
        }
    }

    private BiMap<Integer, String> readHeaders() throws IOException {
        String[] split = reader.readNext();
        BiMap<Integer, String> map = HashBiMap.create();
        for (int i = 0; i <split.length; i++) {
            map.put(i, split[i]);
        }
        return map;
    }

    private T parseLine(int lineIndex, String[] line) throws ParseEntityException {
        try {
            return parse(line);
        } catch (ParseFieldException e) {
            String columnName = headers.get(e.getColumnIndex());
            throw new ParseEntityException(lineIndex, line, columnName, filePath, e);
        }
    }

    protected abstract T parse(String[] line) throws ParseFieldException;

    protected Integer getIndex(String column) throws ParseFieldException {
        Integer index = headers.inverse().get(column);
        if (index == null) {
            throw new ParseFieldException(-1, "Column name not found: " + column);
        }
        return index;
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

    protected Double readDouble(String[] split, String column) throws ParseFieldException {
        return readDouble(split, getIndex(column));
    }

    protected Double readDoubleNullable(String[] split, String column) throws ParseFieldException {
        return readDoubleNullable(split, getIndex(column));
    }

    public <E extends Enum<E>> E readEnum(Class<E> type, String[] line, String column) throws ParseFieldException {
        String value = readString(line, column);
        for (E enumValue : type.getEnumConstants()) {
            if (enumValue.name().equals(value)) {
                return enumValue;
            }
        }
        throw new ParseEnumException(getIndex(column), type, value);
    }

    public <E extends Enum<E>> E readEnumNullable(Class<E> type, String[] line, String column) throws ParseFieldException {
        try {
            return readEnum(type, line, column);
        } catch (ParseNullValueException e) {
            return null;
        }
    }

    public boolean readBoolean(String[] line, String column) throws ParseFieldException {
        String str = readStringNullable(line, column);
        return str != null && !str.isEmpty();
    }

    public int getCount() {
        return count;
    }

    public String getFilePath() {
        return filePath;
    }

    public BiMap<Integer, String> getHeaders() {
        return headers;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }

}
