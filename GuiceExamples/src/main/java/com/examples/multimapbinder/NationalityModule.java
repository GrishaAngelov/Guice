package com.examples.multimapbinder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class NationalityModule extends AbstractModule {
    @Override
    protected void configure() {
        MapBinder<String,Citizen> citizenMapBinder = MapBinder.newMapBinder(binder(),String.class,Citizen.class);
        citizenMapBinder.addBinding("bg").to(Bulgarian.class);
        citizenMapBinder.addBinding("ger").to(German.class);
    }
}


