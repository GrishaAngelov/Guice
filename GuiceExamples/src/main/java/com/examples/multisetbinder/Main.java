package com.examples.multisetbinder;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new NationalityModule());
        NationalityRegister nationalityRegister = injector.getInstance(NationalityRegister.class);

        for(Citizen citizen:nationalityRegister.getCitizenList()){
            System.out.println(citizen.getNationality());
        }

    }
}
