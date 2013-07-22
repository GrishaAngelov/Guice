package com.examples.multimapbinder;

import com.google.inject.Inject;

import java.util.Map;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityRegister {

    private Map<String, Citizen> citizenMap;

    @Inject
    public NationalityRegister(Map<String, Citizen> citizenMap) {
        this.citizenMap = citizenMap;
    }

    public Map<String,Citizen> getNationalities() {
        return citizenMap;
    }
}
