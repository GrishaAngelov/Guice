package com.examples.singleton.lazy;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class RegisterModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PersonRegister.class).to(StudentRegister.class).in(Singleton.class);
    }
}
