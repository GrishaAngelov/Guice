package com.examples.aop;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class AgeModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor(Matchers.subclassesOf(StudentRegister.class), Matchers.annotatedWith(Validate.class), new AgeValidatorInterceptor());
    }
}
