package com.examples.multisetbinder;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityRegister {

    private List<Citizen> citizenArrayList;

    @Inject
    public NationalityRegister(Set<Citizen> citizenSet) {
        this.citizenArrayList = new ArrayList<Citizen>(citizenSet);
    }

    public List<Citizen> getCitizenList() {
        return citizenArrayList;
    }
}
