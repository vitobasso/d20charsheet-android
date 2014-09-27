package com.vituel.dndplayer.parser;

/**
 * Created by Victor on 05/04/14.
 */
public class ParserUtil {

    protected String read(String[] split, int index) {
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
        return str;
    }

}
