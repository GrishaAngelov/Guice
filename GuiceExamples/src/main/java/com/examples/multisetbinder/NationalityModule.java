package com.examples.multisetbinder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Citizen> citizenSetBinder = Multibinder.newSetBinder(binder(), Citizen.class);
        citizenSetBinder.addBinding().to(Bulgarian.class);
        citizenSetBinder.addBinding().to(German.class);
        citizenSetBinder.addBinding().to(Bulgarian.class);

    }
}


