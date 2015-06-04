package com.vituel.dndplayer.io.parser.exception;

import java.text.MessageFormat;

/**
 * Created by Victor on 13/04/2015.
 */
public class ParseEnumException extends ParseFieldException {

    private Class<? extends Enum> expectedType;
    private String attributeValue;

    public ParseEnumException(int columnIndex, Class<? extends Enum> expectedType, String attributeValue) {
        this(columnIndex, expectedType, attributeValue, null);
    }

    public ParseEnumException(int columnIndex, Class<? extends Enum> expectedType, String attributeValue, Throwable throwable) {
        super(columnIndex, buildMessage(expectedType, attributeValue), throwable);
        this.expectedType = expectedType;
        this.attributeValue = attributeValue;
    }

    private static String buildMessage(Class<? extends Enum> expectedType, String attributeValue) {
        String template = "Expected {0} but was {1}";
        return MessageFormat.format(template, expectedType, attributeValue);
    }

    public Class<? extends Enum> getExpectedType() {
        return expectedType;
    }

    public String getAttributeValue() {
        return attributeValue;
    }
}
