package org.googlecode.userapi;

import java.util.List;

/**
 * Created by Ildar Karimov
 * Date: Aug 26, 2009
 */
public class ListWithTotal<T> {
    private List<T> list;
    private long count;

    public ListWithTotal(List<T> list, long count) {
        this.list = list;
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public long getCount() {
        return count;
    }
}
