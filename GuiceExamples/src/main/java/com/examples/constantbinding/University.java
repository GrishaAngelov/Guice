package com.examples.constantbinding;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class University {
    private String universityName;

    @Inject
    public University(@Named("uni") String universityName) {
        this.universityName = universityName;
    }

    public String getUniversityName() {
        return universityName;
    }
}
