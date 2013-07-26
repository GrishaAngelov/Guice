package com.examples.multimapbinder;

import com.google.inject.Inject;

import java.util.Map;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityRegister {

    private Map<String, Citizen> nationalities;

    @Inject
    public NationalityRegister(Map<String, Citizen> registeredNationalities) {
        this.nationalities = registeredNationalities;
    }

    public Map<String,Citizen> getNationalities() {
        return nationalities;
    }
}
