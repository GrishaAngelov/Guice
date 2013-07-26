package com.examples.custombinding;

import com.google.inject.Inject;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityRegister {
    private Citizen citizen;

    @Inject
    public NationalityRegister(@BulgarianAnnotation Citizen registeredCitizen) {
        this.citizen = registeredCitizen;
    }

    public String getNationality(){
        return citizen.getNationality();
    }
}
