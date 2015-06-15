package com.vitobasso.d20charsheet.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    public static void removeNulls(Collection collection) {
        collection.removeAll(Collections.singleton(null));
    }

}
