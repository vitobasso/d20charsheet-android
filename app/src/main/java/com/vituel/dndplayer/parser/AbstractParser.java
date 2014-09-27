package com.vituel.dndplayer.parser;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 26/03/14.
 */
public abstract class AbstractParser<T> extends ParserUtil {

    Context ctx;

    protected AbstractParser(Context ctx) {
        this.ctx = ctx;
    }

    public List<T> loadFile(String path) {
        List<T> result = new ArrayList<T>();

        try {
            InputStream in = ctx.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("#")) { //comment or header
                    T obj = parse(line);
                    result.add(obj);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected abstract T parse(String line);

}
