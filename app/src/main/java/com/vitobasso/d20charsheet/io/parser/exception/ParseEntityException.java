package com.vitobasso.d20charsheet.io.parser.exception;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Created by Victor on 13/04/2015.
 */
public class ParseEntityException extends ParseException {

    private int lineNumber;
    private String fileName, columnName;
    private String[] line;


    public ParseEntityException(int lineNumber, String[] line, String columnName, String fileName, Throwable throwable) {
        super(buildMessage(lineNumber, line, columnName, fileName), throwable);
    }

    public ParseEntityException(int lineNumber, String[] line, String columnName, String fileName) {
        this(lineNumber, line, columnName, fileName, null);
    }

    private static String buildMessage(int lineNumber, String[] line, String columnName, String fileName) {
        String template = "Failed to parse field {2} for entity {3} at line {0}. Full line: {1}";
        return MessageFormat.format(template, lineNumber, Arrays.toString(line), columnName, fileName);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String[] getLine() {
        return line;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFileName() {
        return fileName;
    }
}
