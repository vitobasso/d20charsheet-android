package com.vituel.dndplayer.io.parser.exception;

import java.text.MessageFormat;

/**
 * Created by Victor on 13/04/2015.
 */
public class ParseFormatException extends ParseFieldException {

    public enum Type {
        STRING, INT, FLOAT
    }

    private Type expectedType;
    private String attributeValue;

    public ParseFormatException(int columnIndex, Type expectedType, String attributeValue) {
        this(columnIndex, expectedType, attributeValue, null);
    }

    public ParseFormatException(int columnIndex, Type expectedType, String attributeValue, Throwable throwable) {
        super(columnIndex, buildMessage(expectedType, attributeValue), throwable);
        this.expectedType = expectedType;
        this.attributeValue = attributeValue;
    }

    private static String buildMessage(Type expectedType, String attributeValue) {
        String template = "Expected {0} but was {1}";
        return MessageFormat.format(template, expectedType, attributeValue);
    }

    public Type getExpectedType() {
        return expectedType;
    }

    public String getAttributeValue() {
        return attributeValue;
    }
}
