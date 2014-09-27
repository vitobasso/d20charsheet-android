package com.vituel.dndplayer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Victor on 23/03/14.
 */
public class JavaUtil {

    public static <T> T findByIndex(Set<T> set, int index) {
        int count = 0;
        for (T item : set) {
            if (count++ == index) {
                return item;
            }
        }
        return null;
    }

    public static <T> List<T> getAndIfOverflowsCreate(List<List<T>> list, int index){
        if(list.size() <= index){
            int missing = index - list.size() + 1;
            for(int i=0; i<missing; i++){
                list.add(new ArrayList<T>());
            }
        }
        return list.get(index);
    }

    public static boolean equal(Object a, Object b){
        return a == null ? b == null : a.equals(b);
    }

}
