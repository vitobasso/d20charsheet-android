package com.vitobasso.d20charsheet.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor on 23/03/14.
 */
public class LangUtil {

    public static <T> List<T> ensureListAtIndex(List<List<T>> lists, int index){
        if(lists.size() <= index){
            int missing = index - lists.size() + 1;
            for(int i=0; i<missing; i++){
                lists.add(new ArrayList<T>());
            }
        }
        return lists.get(index);
    }

    public static boolean equals(Object a, Object b){
        return a == null ? b == null : a.equals(b);
    }

    public static int hash(Object... fields) {
        return Arrays.hashCode(fields);
    }

}
