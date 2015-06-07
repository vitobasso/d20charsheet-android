package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.google.common.base.Charsets;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vituel.dndplayer.io.parser.exception.ParseEntityException;
import com.vituel.dndplayer.io.parser.exception.ParseEnumException;
import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.io.parser.exception.ParseNullValueException;
import com.vituel.dndplayer.model.Critical;
import com.vituel.dndplayer.model.DiceRoll;
import com.vituel.dndplayer.model.rulebook.Book;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by Victor on 26/03/14.
 */
public abstract class AbstractCsvParser<T> extends AbstractParser implements Closeable {

    private Context ctx; //TODO not used? remove?
    private File file;

    private BiMap<Integer, String> headers;
    private CSVReader reader;
    private int count;
    private String[] nextLine;

    public AbstractCsvParser(Context ctx, File file) {
        this.ctx = ctx;
        this.file = file;
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
            InputStream in = new FileInputStream(file);
            reader = new CSVReader(new InputStreamReader(in, Charsets.UTF_8), ';', '"');
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
            throw new ParseEntityException(lineIndex, line, columnName, file.getAbsolutePath(), e);
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

    public DiceRoll readRoll(String[] line, String column) throws ParseFieldException {
        String str = readString(line, column);
        return new DiceRoll(str);
    }

    public DiceRoll readRollNullable(String[] line, String column) throws ParseFieldException {
        try {
            return readRoll(line, column);
        } catch (ParseNullValueException e) {
            return null;
        }
    }

    public Critical readCritical(String[] line, String column) throws ParseFieldException {
        String str = readString(line, column);
        return new Critical(str);
    }

    public Critical readCriticalNullable(String[] line, String column) throws ParseFieldException {
        try {
            return readCritical(line, column);
        } catch (ParseNullValueException e) {
            return null;
        }
    }

    public boolean readBoolean(String[] line, String column) throws ParseFieldException {
        String str = readStringNullable(line, column);
        return str != null && !str.isEmpty();
    }

    protected Book readRulebook(String[] line, String column) throws ParseFieldException {
        Book book = new Book();
        book.setId(readInt(line, column));
        return book;
    }

    public int getCount() {
        return count;
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
