package com.vitobasso.d20charsheet.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor on 23/03/14.
 */
public class LangUtil {

    public static <T> List<T> getAndIfOverflowsCreate(List<List<T>> list, int index){
        if(list.size() <= index){
            int missing = index - list.size() + 1;
            for(int i=0; i<missing; i++){
                list.add(new ArrayList<T>());
            }
        }
        return list.get(index);
    }

    public static boolean equals(Object a, Object b){
        return a == null ? b == null : a.equals(b);
    }

    public static int hash(Object... fields) {
        return Arrays.hashCode(fields);
    }

}
