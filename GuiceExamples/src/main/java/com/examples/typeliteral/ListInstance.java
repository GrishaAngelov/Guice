package com.examples.typeliteral;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class ListInstance {
    private List<String> list;

    @Inject
    public ListInstance(List<String> list) {
        this.list = list;
    }

    public boolean isArrayListInstance() {
        return list instanceof ArrayList;
    }
}
