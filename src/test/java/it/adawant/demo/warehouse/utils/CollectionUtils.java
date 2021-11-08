package it.adawant.demo.warehouse.utils;

import lombok.val;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    private CollectionUtils() {
    }

    @SafeVarargs
    public static <T> List<T> joinLists(List<T>... lists) {
        val res = new ArrayList<T>();
        for (val list : lists)
            res.addAll(list);
        return res;
    }

    public static <T> Boolean hasSameContent(Collection<T> a, Collection<T> b) {
        return a.size() == b.size() && a.containsAll(b);
    }
}
