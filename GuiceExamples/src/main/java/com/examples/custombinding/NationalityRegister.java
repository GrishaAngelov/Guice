package com.examples.custombinding;

import com.google.inject.Inject;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityRegister {
    private Citizen citizen;

    @Inject
    public NationalityRegister(@Bul Citizen citizen) {
        this.citizen = citizen;
    }

    public String getNationality(){
        return citizen.getNationality();
    }
}
