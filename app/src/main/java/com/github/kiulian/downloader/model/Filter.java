package com.github.kiulian.downloader.model;

import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/Filter.class */
public interface Filter<T> {

    /* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/Filter$_CC.class */
    public final /* synthetic */ class _CC {
        /* JADX WARN: Multi-variable type inference failed */
        public static List $default$select(Filter filter, List list) {
            LinkedList linkedList = new LinkedList();
            for (Object obj : list) {
                if (filter.test(obj)) {
                    linkedList.add(obj);
                }
            }
            return linkedList;
        }
    }

    List<T> select(List<T> list);

    boolean test(T t);
}
