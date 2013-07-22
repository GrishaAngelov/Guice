package com.examples.custombinding;

import com.google.inject.AbstractModule;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Citizen.class).annotatedWith(Bul.class).to(Bulgarian.class);
        bind(Citizen.class).annotatedWith(Ger.class).to(German.class);
    }
}
