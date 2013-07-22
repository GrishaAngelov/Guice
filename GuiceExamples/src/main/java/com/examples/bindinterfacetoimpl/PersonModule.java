package com.examples.bindinterfacetoimpl;

import com.google.inject.AbstractModule;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class PersonModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Person.class).to(Student.class);
    }
}
