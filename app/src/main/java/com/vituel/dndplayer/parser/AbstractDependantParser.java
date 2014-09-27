package com.vituel.dndplayer.parser;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 26/03/14.
 */
public abstract class AbstractDependantParser<T, U> extends ParserUtil{

    Context ctx;

    protected AbstractDependantParser(Context ctx) {
        this.ctx = ctx;
    }

    public Map<T, U> loadFile(String path) {
        Map<T, U> result = new HashMap<>();

        try {
            InputStream in = ctx.getAssets().open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("#")) { //comment or header
                    T dependant = parseDependant(line);
                    U owner = parseOwner(line);
                    if(owner == null){
                        throw new RuntimeException("Owner entity not found. Line: " + line);
                    }
                    result.put(dependant, owner);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected abstract T parseDependant(String line);

    protected abstract U parseOwner(String line);

}
