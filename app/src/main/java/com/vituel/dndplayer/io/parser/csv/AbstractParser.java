package com.vituel.dndplayer.io.parser.csv;

import com.vituel.dndplayer.io.parser.exception.ParseFormatException;
import com.vituel.dndplayer.io.parser.exception.ParseNullValueException;

/**
 * Created by Victor on 05/04/14.
 */
public abstract class AbstractParser {

    protected String readString(String[] split, int index) throws ParseNullValueException {
        String str = null;
        if (split.length > index) {
            str = split[index];
        }
        if (str != null) {
            str = str.trim();
            if (str.isEmpty()) {
                str = null;
            }
        }
        if (str == null) {
            throw new ParseNullValueException(index);
        }
        return str;
    }

    protected String readStringNullable(String[] split, int index) {
        try {
            return readString(split, index);
        } catch (ParseNullValueException e) {
            return null;
        }
    }

    protected Integer readInt(String[] split, int index) throws ParseNullValueException, ParseFormatException {
        String str = readString(split, index);
        if (str != null) {
            try {
                return Integer.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParseFormatException(index, ParseFormatException.Type.INT, str, e);
            }
        } else {
            throw new ParseNullValueException(index);
        }
    }

    protected Integer readIntNullable(String[] split, int index) throws ParseFormatException {
        try {
            return readInt(split, index);
        } catch (ParseNullValueException e) {
            return null;
        }
    }

    protected Double readDouble(String[] split, int index) throws ParseNullValueException, ParseFormatException {
        String str = readString(split, index);
        if (str != null) {
            try {
                return Double.valueOf(str);
            } catch (NumberFormatException e) {
                throw new ParseFormatException(index, ParseFormatException.Type.FLOAT, str, e);
            }
        } else {
            throw new ParseNullValueException(index);
        }
    }

    protected Double readDoubleNullable(String[] split, int index) throws ParseFormatException {
        try {
            return readDouble(split, index);
        } catch (ParseNullValueException e) {
            return null;
        }
    }

}
